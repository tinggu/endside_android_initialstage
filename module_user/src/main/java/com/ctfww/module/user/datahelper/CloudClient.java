package com.ctfww.module.user.datahelper;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.entity.CloudRspData;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.user.bean.PasswordLoginBean;
import com.ctfww.module.user.bean.SMSLoginBean;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;
import com.ctfww.module.user.entity.UserInfo;

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

    private static final String TAG = "SelfCloudClient";

    private static final int VALIDE_DATA = 80000000;

    ICloudMethod mCloudMethod;

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


    public void loginByPassword(String phoneNum, String password, final ICloudCallback callback) {
        PasswordLoginBean info = new PasswordLoginBean(phoneNum, password);
        LogUtils.i(TAG, "loginByPassword: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.loginByPassword(info);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void loginBySmsCode(String phoneNum, String smsNum, final ICloudCallback callback) {
        SMSLoginBean info = new SMSLoginBean(phoneNum, smsNum);
        LogUtils.i(TAG, "loginBySmsCode: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.loginBySmsCode(info);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void sendSms(String phoneNum, final ICloudCallback callback) {
        LogUtils.i(TAG, "sendSms: phoneNum = " + phoneNum);
        Call<ResponseBody> responseBodyCall = mCloudMethod.sendSms(phoneNum);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void deleteAccount(String userId, final ICloudCallback callback) {
        LogUtils.i(TAG, "userId = " + userId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.deleteAccount(userId);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void uploadFile(final String filePath, final ICloudCallback callback) {
        File file = new File(filePath);

        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileRQ);

        Call<ResponseBody> responseBodyCall = mCloudMethod.uploadFile(SPStaticUtils.getString(Const.USER_OPEN_ID), part);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synUserInfoToCloud(CargoToCloud<UserInfo> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synUserInfoToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synUserInfoFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synUserInfoFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synGroupInfoToCloud(CargoToCloud<GroupInfo> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synGroupInfoToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synGroupInfoFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synGroupInfoFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synInviteInfoToCloud(CargoToCloud<GroupInviteInfo> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synInviteInfoToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synInviteInfoFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synInviteInfoFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synNoticeInfoToCloud(CargoToCloud<NoticeInfo> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synNoticeInfoToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synNoticeInfoFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synNoticeInfoFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synNoticeReadStatusToCloud(CargoToCloud<NoticeReadStatus> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synNoticeReadStatusToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synNoticeReadStatusFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synNoticeReadStatusFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synGroupUserInfoToCloud(CargoToCloud<GroupUserInfo> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synGroupUserInfoToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synGroupUserInfoFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synGroupUserInfoFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }
}
