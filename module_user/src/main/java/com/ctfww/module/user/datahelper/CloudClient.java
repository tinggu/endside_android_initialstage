package com.ctfww.module.user.datahelper;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.bean.QueryConditionBean;
import com.ctfww.commonlib.entity.CloudRspData;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.user.bean.GroupInfoBean;
import com.ctfww.module.user.bean.GroupInviteBean;
import com.ctfww.module.user.bean.NoticeBean;
import com.ctfww.module.user.bean.NoticeReadStatusBean;
import com.ctfww.module.user.bean.PasswordLoginBean;
import com.ctfww.module.user.bean.SMSLoginBean;
import com.ctfww.module.user.bean.User2GroupBean;
import com.ctfww.module.user.bean.UserGroupBean;
import com.ctfww.module.user.bean.UserInfoBean;
import com.ctfww.module.user.entity.NoticeReadStatus;

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

//    public void getAccessTokenByUserId(String userId, final ICloudCallback callback) {
//        String url = "http://39.98.147.77:9090/token/getAccessTokenByUserId";
//        Call<ResponseBody> responseBodyCall = mCloudMethod.getAccessTokenByUserId(url, userId);
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
//                processRsp(response, callback);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                callback.onFailure(t.getMessage());
//            }
//        });
//    }

    public void loginByPassword(String phoneNum, String password, final ICloudCallback callback) {
        Call<ResponseBody> responseBodyCall = mCloudMethod.loginByPassword(new PasswordLoginBean(phoneNum, password));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                processRsp(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void loginBySmsCode(String phoneNum, String smsNum, final ICloudCallback callback) {
        Call<ResponseBody> responseBodyCall = mCloudMethod.loginBySmsCode(new SMSLoginBean(phoneNum, smsNum));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                processRsp(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void sendSms(String phoneNum, final ICloudCallback callback) {
        LogUtils.i(TAG, "phoneNum = " + phoneNum);
        Call<ResponseBody> responseBodyCall = mCloudMethod.sendSms(phoneNum);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                processRsp(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void updateUserInfo(UserInfoBean userInfoBean, final ICloudCallback callback) {
        Call<ResponseBody> responseBodyCall = mCloudMethod.updateUserInfoByUserId(userInfoBean);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                processRsp(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void unregister(String userId, final ICloudCallback callback) {
        LogUtils.i(TAG, "userId = " + userId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.deleteAccount(userId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                processRsp(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void createGroup(String groupName, final ICloudCallback callback) {
        final GroupInfoBean groupInfoBean = new GroupInfoBean();
        groupInfoBean.setGroupName(groupName);
        LogUtils.i(TAG, "createGroup: groupInfoBean = " + groupInfoBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.createGroup(groupInfoBean);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    UserGroupBean userGroupBean = new UserGroupBean();
                    userGroupBean.setGroupId(groupInfoBean.getGroupId());
                    userGroupBean.setGroupName(groupInfoBean.getGroupName());
                    userGroupBean.setRole("admin");

                    callback.onSuccess(GsonUtils.toJson(userGroupBean, UserGroupBean.class));
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

    public void updateGroupInfo(final String groupId, final String groupName, final ICloudCallback callback) {
        GroupInfoBean groupInfoBean = new GroupInfoBean(groupId, groupName);
        LogUtils.i(TAG, "updateGroupInfo: groupInfoBean = " + groupInfoBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.updateGroupInfo(groupInfoBean);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    UserGroupBean userGroupBean = new UserGroupBean();
                    userGroupBean.setGroupId(groupId);
                    userGroupBean.setGroupName(groupName);

                    callback.onSuccess(GsonUtils.toJson(userGroupBean, UserGroupBean.class));
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

    public void deleteGroup(final String groupId, final ICloudCallback callback) {
        LogUtils.i(TAG, "deleteGroup: groupId = " + groupId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.deleteGroup(groupId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    callback.onSuccess(groupId);
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

    public void addInvite(String groupId, String toMobile, final ICloudCallback callback) {
        GroupInviteBean groupInviteBean = new GroupInviteBean();
        groupInviteBean.setGroupId(groupId);
        groupInviteBean.setToMobile(toMobile);
        LogUtils.i(TAG, "addInvite: groupInviteBean = " + groupInviteBean);
        Call<ResponseBody> responseBodyCall = mCloudMethod.addInvite(groupInviteBean);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        callback.onSuccess(jsonData.toString());
                    }
                    catch (JSONException e) {
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

    public void updateInviteStatus(final String inviteId, final String status, final ICloudCallback callback) {
        GroupInviteBean groupInviteBean = new GroupInviteBean(inviteId, status);
        LogUtils.i(TAG, "updateInviteStatus: groupInviteBean = " + groupInviteBean);
        Call<ResponseBody> responseBodyCall = mCloudMethod.updateInviteStatus(groupInviteBean);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    callback.onSuccess(status);
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

    public void deleteInvite(final String inviteId, final ICloudCallback callback) {
        LogUtils.i(TAG, "deleteInvite: inviteId = " + inviteId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.deleteInvite(inviteId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    callback.onSuccess(inviteId);
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

    public void getSendInvite(String userId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getSendInvite: userId = " + userId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getSendInvite(userId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONArray jsonData = jsonObject.getJSONArray("data");
                        callback.onSuccess(jsonData.toString());
                    }
                    catch (JSONException e) {
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

    public void getInvite(String userId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getInvite: userId = " + userId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getInvite(userId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONArray jsonData = jsonObject.getJSONArray("data");
                        callback.onSuccess(jsonData.toString());
                    }
                    catch (JSONException e) {
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

    public void getNewMessageCount(String userId, final ICloudCallback callback) {
        Call<ResponseBody> responseBodyCall = mCloudMethod.getNewMessageCount(userId);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void withdrawGroup(final String userId, final String groupId, final ICloudCallback callback) {
        User2GroupBean user2GroupBean = new User2GroupBean();
        user2GroupBean.setUserId(userId);
        user2GroupBean.setGroupId(groupId);
        LogUtils.i(TAG, "withdrawGroup: user2Group = " + user2GroupBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.withdrawGroup(user2GroupBean);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    callback.onSuccess(userId + ", " + groupId);
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

    public void updateGroupUserRole(final String groupId, final String userId, final String role, final ICloudCallback callback) {
        User2GroupBean user2GroupBean = new User2GroupBean();
        user2GroupBean.setGroupId(groupId);
        user2GroupBean.setRole(role);
        user2GroupBean.setUserId(userId);
        LogUtils.i(TAG, "updateGroupUserRole: user2GroupBean = " + user2GroupBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.updateGroupUserRole(user2GroupBean);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    callback.onSuccess(role);
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

    public void getUserGroupInfo(String userId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getUserGroupInfo: userId = " + userId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getUserGroupInfo(userId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONArray jsonData = jsonObject.getJSONArray("data");
                        callback.onSuccess(jsonData.toString());
                    }
                    catch (JSONException e) {
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

    public void getGroupUserInfo(String groupId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getGroupUserInfo: groupId = " + groupId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getGroupUserInfo(groupId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONArray jsonData = jsonObject.getJSONArray("data");
                        callback.onSuccess(jsonData.toString());
                    }
                    catch (JSONException e) {
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

    public void uploadFile(final String filePath, final ICloudCallback callback) {
        File file = new File(filePath);

        RequestBody fileRQ = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileRQ);

        Call<ResponseBody> responseBodyCall = mCloudMethod.uploadFile(SPStaticUtils.getString("user_open_id"), part);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void addNotice(String groupId, String userId, String tittle, String content, long timeStamp, final ICloudCallback callback) {
        NoticeBean noticeBean = new NoticeBean();
        noticeBean.setGroupId(groupId);
        noticeBean.setUserId(userId);
        noticeBean.setTittle(tittle);
        noticeBean.setContent(content);
        noticeBean.setTimeStamp(timeStamp);
        LogUtils.i(TAG, "addNotice: noticeBean = " + noticeBean);
        Call<ResponseBody> responseBodyCall = mCloudMethod.addNotice(noticeBean);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void getNotice(String groupId, String userId, final ICloudCallback callback) {
        QueryConditionBean condition = new QueryConditionBean(groupId, userId);
        LogUtils.i(TAG, "getNotice: condition = " + condition);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getNotice(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void updateNoticeReadStatus(String noticeId, String userId, int flag, long timeStamp, final ICloudCallback callback) {
        NoticeReadStatusBean info = new NoticeReadStatusBean();
        info.setNoticeId(noticeId);
        info.setUserId(userId);
        info.setFlag(flag);
        info.setTimeStamp(timeStamp);
        LogUtils.i(TAG, "updateNoticeReadStatus: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.updateNoticeReadStatus(info);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void getNoticeReadStatus(String noticeId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getNoticeReadStatus: noticeId = " + noticeId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getNoticeReadStatus(noticeId);
        processListRsp(responseBodyCall, callback);
    }
}
