package com.ctfww.module.keyevents.datahelper;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.entity.CloudRspData;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.bean.KeyEventTraceBean;
import com.ctfww.module.user.datahelper.sp.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CloudClient {

    private static final String TAG = "KeyEventsCloudClient";

    private final static int VALIDE_DATA = 80000000;

    private ICloudMethod mCloudMethod;

    public static CloudClient getInstance() {
        return CloudClient.Inner.INSTANCE;
    }

    private static class Inner {
        private static final CloudClient INSTANCE = new CloudClient();
    }

    private CloudClient() {
    }

    public void setCloudMethod(ICloudMethod method) {
        mCloudMethod = method;
    }

    private int getResultCode(String data) {
        try {
            JSONObject jsonObj = new JSONObject(data);
            return jsonObj.getInt("resultCode");
        }
        catch (JSONException e) {
            return -10;
        }
    }

    private void processRsp(retrofit2.Response<ResponseBody> response, final ICloudCallback callback) {
        LogUtils.i(TAG, "response code = " + response.code());
        if (response.code() == 200) {
            try {
                if (response.body() != null) {
                    String data = response.body().string();
                    LogUtils.i(TAG, "data = " + data);
                    int resultCode = getResultCode(data);
                    LogUtils.i(TAG, "resultCode = " + resultCode);
                    if (resultCode == VALIDE_DATA) {
                        callback.onSuccess(data);
                    } else {
                        callback.onError(resultCode, "数据错误！");
                    }
                } else {
                    callback.onError(-9, "没有返回数据！");
                }
            } catch (IOException e) {
                LogUtils.e(TAG, "e.message = " + e.getMessage());
                callback.onError(-8, "数据异常！");
            }
        }

        if (response.code() != 200 || !response.isSuccessful()) {
            callback.onError(response.code(), response.message());
        }
    }

    private CloudRspData parseRsp(retrofit2.Response<ResponseBody> response) {
        LogUtils.i(TAG, "response code = " + response.code());
        if (response.code() != 200) {
            return new CloudRspData(response.code(), "网络有问题");
        }

        if (!response.isSuccessful()) {
            return new CloudRspData(-100, "网络有问题");
        }

        try {
            if (response.body() != null) {
                String data = response.body().string();
                LogUtils.i(TAG, "data = " + data);
                CloudRspData cloudRspData = new CloudRspData(getResultCode(data), data) ;
                return cloudRspData;
            }else {
                return new CloudRspData(-9, "没有body");
            }
        } catch (IOException e) {
            LogUtils.e(TAG, "e.message = " + e.getMessage());
            return new CloudRspData(-8, "body数据有异常");
        }
    }

    private void processGeneralRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    callback.onSuccess(cloudRspData.getBodyStr());
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    private void processSingleObjRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        callback.onSuccess(jsonObjectData.toString());
                    }
                    catch (JSONException e) {
                        LogUtils.e(TAG, "e.message = " + e.getMessage());
                        callback.onError(-10, "数据格式错误");
                    }
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    private void processListRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        callback.onSuccess(jsonArray.toString());
                    }
                    catch (JSONException e) {
                        LogUtils.e(TAG, "e.message = " + e.getMessage());
                        callback.onError(-10, "数据格式错误");
                    }
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void synKeyEventToCloud(CargoToCloud<KeyEvent> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeyEventToCloud: cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeyEventToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synKeyEventFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeyEventFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeyEventFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synKeyEventTraceToCloud(CargoToCloud<KeyEventTrace> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeyEventTraceToCloud: cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeyEventTraceToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synKeyEventTraceFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeyEventTraceFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeyEventTraceFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void uploadFile(final String filePath, final ICloudCallback callback) {
        File file = new File(filePath);

        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileRQ);

        Call<ResponseBody> responseBodyCall = mCloudMethod.uploadFile(SPStaticUtils.getString(Const.USER_OPEN_ID), part);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                processRsp(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                LogUtils.i(TAG, "uploadFile: t.getMessage()" + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getKeyEventActionList(String eventId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getKeyEventActionList: eventId = " + eventId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeyEventActionList(eventId);
        processListRsp(responseBodyCall, callback);
    }

    public void getNoEndKeyEventCount(String group_id, String user_id, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(group_id, user_id);
        LogUtils.i(TAG, "getNoEndKeyEventCount: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getNoEndKeyEventCount(condition);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void getEndKeyEventList(String group_id, String user_id, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(group_id, user_id, startTime, endTime);
        LogUtils.i(TAG, "getEndKeyEventList: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getEndKeyEventList(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void getNoEndKeyEventList(String group_id, String user_id, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(group_id, user_id);
        LogUtils.i(TAG, "getNoEndKeyEventList: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getNoEndKeyEventList(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void getHistoryEveryDayKeyEventStatistics(String group_id, String user_id, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(group_id, user_id, startTime, endTime);
        LogUtils.i(TAG, "getHistoryEveryDayKeyEventStatistics: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getHistoryEveryDayKeyEventStatistics(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void getEveryOneEndKeyEventStatistics(String group_id, String user_id, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(group_id, user_id, startTime, endTime);
        LogUtils.i(TAG, "getEveryOneEndKeyEventStatistics: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getEveryOneEndKeyEventStatistics(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void getSomeOneDoingKeyEvent(String group_id, String user_id, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(group_id, user_id);
        LogUtils.i(TAG, "getSomeOneDoingKeyEvent: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getSomeOneDoingKeyEvent(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void getCanBeSnatchedKeyEvent(String groupId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getCanBeSnatchedKeyEvent: groupId = " + groupId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getCanBeSnatchedKeyEvent(groupId);
        processListRsp(responseBodyCall, callback);
    }

    public void snatchKeyEvent(String groupId, String userId, String eventId, int deskId, long timeStamp, final ICloudCallback callback) {
        KeyEventTraceBean keyEventTraceBean = new KeyEventTraceBean();
        keyEventTraceBean.setGroupId(groupId);
        keyEventTraceBean.setUserId(userId);
        keyEventTraceBean.setEventId(eventId);
        keyEventTraceBean.setDeskId(deskId);
        keyEventTraceBean.setTimeStamp(timeStamp);
        LogUtils.i(TAG, "snatchKeyEvent: info = " + keyEventTraceBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.snatchKeyEvent(keyEventTraceBean);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void freeKeyEvent(String groupId, String userId, String eventId, int deskId, long timeStamp, final ICloudCallback callback) {
        KeyEventTraceBean keyEventTraceBean = new KeyEventTraceBean();
        keyEventTraceBean.setGroupId(groupId);
        keyEventTraceBean.setUserId(userId);
        keyEventTraceBean.setEventId(eventId);
        keyEventTraceBean.setDeskId(deskId);
        keyEventTraceBean.setTimeStamp(timeStamp);
        LogUtils.i(TAG, "freeKeyEvent: info = " + keyEventTraceBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.freeKeyEvent(keyEventTraceBean);
        processGeneralRsp(responseBodyCall, callback);
    }
}
