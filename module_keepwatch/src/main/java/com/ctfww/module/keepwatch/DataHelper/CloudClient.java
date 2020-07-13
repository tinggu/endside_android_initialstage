package com.ctfww.module.keepwatch.DataHelper;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.bean.QueryConditionBean;
import com.ctfww.commonlib.entity.CloudRspData;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.utils.NetworkUtils;
import com.ctfww.module.keepwatch.bean.KeepWatchAssignmentBean;
import com.ctfww.module.keepwatch.bean.KeepWatchDeskBean;
import com.ctfww.module.keepwatch.bean.KeepWatchSigninBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CloudClient {

    private static final String TAG = "SelfCloudClient";

    private final static int VALIDE_DATA = 80000000;
    private final static String mUrl = "http://39.98.147.77:7001";

    private ICloudMethod mCloudMethod;

    public static CloudClient getInstance() {
        return CloudClient.Inner.INSTANCE;
    }

    private static class Inner {
        private static final CloudClient INSTANCE = new CloudClient();
    }

    private CloudClient(){
    }

    public void setCloudMethod(ICloudMethod method) {
        mCloudMethod = method;
    }

    public void createCloudMethod() {
        mCloudMethod = NetworkUtils.createCloudMethod(ICloudMethod.class, mUrl);
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
                    }
                    else {
                        callback.onError(resultCode, "数据错误！");
                    }
                }else {
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

    public void updateKeepWatchDeskAddition(String groupId,
                String deskName, String deskAddress,
                int deskId, String deskType, String fingerPrint,
                long modifyTimestamp, final ICloudCallback callback) {
        KeepWatchDeskBean keepWatchDeskBean = new KeepWatchDeskBean();
        keepWatchDeskBean.setGroupId(groupId);
        keepWatchDeskBean.setDeskId(deskId);
        keepWatchDeskBean.setDeskName(deskName);
        keepWatchDeskBean.setDeskAddress(deskAddress);
        keepWatchDeskBean.setDeskType(deskType);
        keepWatchDeskBean.setFingerPrint(fingerPrint);
        keepWatchDeskBean.setModifyTimeStamp(modifyTimestamp);
        LogUtils.i(TAG, "modifyDesk: keepWatchDeskBean = " + keepWatchDeskBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.updateKeepWatchDeskAddition(keepWatchDeskBean);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void updateKeepWatchDeskFingerPrint(String groupId, int deskId, long timeStamp, String fingerPrint, final ICloudCallback callback) {
        KeepWatchDeskBean keepWatchDeskBean = new KeepWatchDeskBean();
        keepWatchDeskBean.setGroupId(groupId);
        keepWatchDeskBean.setDeskId(deskId);
        keepWatchDeskBean.setFingerPrint(fingerPrint);
        keepWatchDeskBean.setModifyTimeStamp(timeStamp);
        LogUtils.i(TAG, "updateKeepWatchDeskFingerPrint: keepWatchDeskBean = " + keepWatchDeskBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.updateKeepWatchDeskFingerPrint(keepWatchDeskBean);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void addKeepWatchDesk(String groupId,
                              double lat, double lng, String deskName, String address,
                              int deskId,  String deskType, String fingerPrint,
                              long timeStamp, long modifyTimestamp, final ICloudCallback callback) {
        KeepWatchDeskBean keepWatchDeskBean = new KeepWatchDeskBean();
        keepWatchDeskBean.setGroupId(groupId);

        keepWatchDeskBean.setLat(lat);
        keepWatchDeskBean.setLng(lng);
        keepWatchDeskBean.setDeskName(deskName);
        keepWatchDeskBean.setDeskAddress(address);

        keepWatchDeskBean.setDeskId(deskId);
        keepWatchDeskBean.setDeskType(deskType);
        keepWatchDeskBean.setFingerPrint(fingerPrint);
        keepWatchDeskBean.setCreateTimeStamp(timeStamp);
        keepWatchDeskBean.setModifyTimeStamp(modifyTimestamp);
        keepWatchDeskBean.setStatus("reserve");

        LogUtils.i(TAG, "addDesk: keepWatchDeskBean = " + keepWatchDeskBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.addKeepWatchDesk(keepWatchDeskBean);
        processGeneralRsp(responseBodyCall, callback);
    }

    /****************************new*****************************************/

    public void addKeepWatchSignin(KeepWatchSigninBean keepWatchSigninBean, final ICloudCallback callback) {
        LogUtils.i(TAG, "addKeepWatchSignin: keepWatchSigninBean = " + keepWatchSigninBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.addKeepWatchSignin(keepWatchSigninBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getTodayKeepWatchInfo(String groupId, String userId, final ICloudCallback callback) {
        QueryConditionBean condition = new QueryConditionBean(groupId, userId);
        LogUtils.i(TAG, "getTodayKeepWatchInfo: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getTodayKeepWatchInfo(condition);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void getPersonTrends(String groupId, long startTime, long endTime, int count, final ICloudCallback callback) {
        QueryConditionBean condition = new QueryConditionBean(groupId, "", startTime, endTime);
        condition.setCondition("" + count);
        LogUtils.i(TAG, "getPersonTrends: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getPersonTrends(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchRanking(String groupId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean condition = new QueryConditionBean(groupId, "", startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchRanking: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchRanking(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void addKeepWatchAssignment(KeepWatchAssignmentBean info, final ICloudCallback callback) {
        LogUtils.i(TAG, "addKeepWatchAssignment: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.addKeepWatchAssignment(info);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void deleteKeepWatchAssignment(String groupId, String userId, int deskId, final ICloudCallback callback) {
        KeepWatchAssignmentBean info = new KeepWatchAssignmentBean();
        info.setGroupId(groupId);
        info.setUserId(userId);
        info.setDeskId(deskId);
        LogUtils.i(TAG, "deleteKeepWatchAssignment: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.deleteKeepWatchAssignment(info);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void transferKeepWatchAssignment(String groupId, String userId, String toUserId, final ICloudCallback callback) {
        QueryConditionBean condition = new QueryConditionBean();
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        condition.setCondition(toUserId);
        LogUtils.i(TAG, "transferKeepWatchAssignment: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.transferKeepWatchAssignment(condition);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void takeBackKeepWatchAssignment(String groupId, String userId, final ICloudCallback callback) {
        QueryConditionBean condition = new QueryConditionBean();
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        LogUtils.i(TAG, "takeBackKeepWatchAssignment: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.takeBackKeepWatchAssignment(condition);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void addKeepWatchDesk(KeepWatchDeskBean info, final ICloudCallback callback) {
        LogUtils.i(TAG, "addKeepWatchDesk: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.addKeepWatchDesk(info);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void deleteKeepWatchDesk(String groupId, int deskId, final ICloudCallback callback) {
        KeepWatchDeskBean info = new KeepWatchDeskBean();
        info.setGroupId(groupId);
        info.setDeskId(deskId);
        LogUtils.i(TAG, "deleteKeepWatchDesk: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.deleteKeepWatchDesk(info);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void updateKeepWatchDeskAddition(KeepWatchDeskBean info, final ICloudCallback callback) {
        LogUtils.i(TAG, "updateKeepWatchDeskAddition: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.updateKeepWatchDeskAddition(info);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void getKeepWatchDesk(String groupId, int deskId, final ICloudCallback callback) {
        KeepWatchDeskBean info = new KeepWatchDeskBean();
        info.setGroupId(groupId);
        info.setDeskId(deskId);
        LogUtils.i(TAG, "getKeepWatchDesk: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchDesk(info);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchGroupSummary(String groupId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getKeepWatchGroupSummary: groupId = " + groupId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchGroupSummary(groupId);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninLeak(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninLeak: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninLeak(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninStatistics(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninList(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninList(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchAssignmentList(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchAssignmentList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchAssignmentList(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchPeriodAssignmentList(String groupId, String userId, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId);
        LogUtils.i(TAG, "getKeepWatchPeriodAssignmentList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchPeriodAssignmentList(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchStatistics(queryConditionBean);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void getKeepWatchAssignmentAndSigninStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchAssignmentAndSigninStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchAssignmentAndSigninStatisticsForDesk(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchStatisticsByPeriodByDayUnit(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchStatisticsByPeriodByDayUnit: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchStatisticsByPeriodByDayUnit(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchLeakStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchLeakStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchLeakStatisticsForDesk(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninStatisticsForDesk(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getHistoryEveryDayStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryConditionBean queryConditionBean = new QueryConditionBean(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getHistoryEveryDayStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getHistoryEveryDayStatistics(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }
}
