package com.ctfww.module.user.datahelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.GeneralRsp;
import com.ctfww.commonlib.im.UdpHelper;
import com.ctfww.module.user.bean.NoticeBean;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;
import com.google.gson.reflect.TypeToken;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.user.bean.GroupInfoBean;
import com.ctfww.module.user.bean.GroupUserBean;
import com.ctfww.module.user.bean.UserGroupBean;
import com.ctfww.module.user.bean.UserInfoBean;
import com.ctfww.module.user.constant.UserSPConstant;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.UserGroupInfo;
import com.ctfww.module.user.entity.UserInfo;
import com.ctfww.module.user.storage.sp.SPConstant;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class NetworkHelper {
    private static final String TAG = "UserNetworkHelper";

    private NetworkHelper() {

    }

    private static class Inner {
        private static final NetworkHelper INSTANCE = new NetworkHelper();
    }

    public static NetworkHelper getInstance() {
        return NetworkHelper.Inner.INSTANCE;
    }

    public void loginBySMS(String mobile, String sms, final IUIDataHelperCallback uiCallBack) {
        CloudClient.getInstance().loginBySmsCode(mobile, sms, new ICloudCallback() {
                    @Override
                    public void onSuccess(String data) {
                        if (TextUtils.isEmpty(data)) {
                            uiCallBack.onError(NetworkConst.ERR_CODE_INVALIDE_DATA);
                            return;
                        }

                        UserInfo userInfo = processSuccessLogin(data);
                        uiCallBack.onSuccess(userInfo);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        uiCallBack.onError(code);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        uiCallBack.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
                    }
                });

    }

    public void loginByPassword(String mobile, String password, final IUIDataHelperCallback uiCallBack) {
        CloudClient.getInstance().loginByPassword(mobile, password, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                if (TextUtils.isEmpty(data)) {
                    uiCallBack.onError(NetworkConst.ERR_CODE_INVALIDE_DATA);
                    return;
                }

                UserInfo userInfo = processSuccessLogin(data);
                uiCallBack.onSuccess(userInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                uiCallBack.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                uiCallBack.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    private UserInfo processSuccessLogin(String data) {
        LogUtils.i(TAG, "data = " + data);
        String userInfoStr = "";
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject userDataJson = jsonObject.getJSONObject("data");
            userInfoStr = userDataJson.toString();
        } catch (JSONException e) {
            LogUtils.e(TAG, "e.message = " + e.getMessage());
        }
        UserInfoBean userInfoBean = GsonUtils.fromJson(userInfoStr, UserInfoBean.class);
        UserInfo userInfo = Util.userInfoBean2UserInfo(userInfoBean);
        LogUtils.i(TAG, "userInfo = " + userInfo.toString());

        userInfo.setSynTag("cloud");
        DBHelper.getInstance().addUser(userInfo);

        SPStaticUtils.put(SPConstant.USER_OPEN_ID, userInfoBean.getUserId());
        SPStaticUtils.put(UserSPConstant.USER_HAD_LOGIN_FLAG, true);

        // 更新拦截设置
//        com.ctfww.commonlib.network.CloudClient.getInstance().init("http://39.98.147.77:8888");
//        ICloudMethod userMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
//        CloudClient.getInstance().setCloudMethod(userMethod);

        return userInfo;
    }

//    private String processSuccessToken(String data) {
//        LogUtils.i(TAG, "data = " + data);
//        if (TextUtils.isEmpty(data)){
//            return "";
//        }
//
//        String token = "";
//        try {
//            JSONObject jsonObjData = new JSONObject(data);
//            token = jsonObjData.getString("data");
//        } catch (JSONException e) {
//            LogUtils.e(TAG, "e.message = " + e.getMessage());
//            return "";
//        }
//
//        LogUtils.i(TAG, "token = " + token);
//        SPStaticUtils.put(SPConstant.USER_ACCESS_TOKEN, token);
//
//        // 更新拦截设置
////        com.ctfww.commonlib.network.CloudClient.getInstance().init("http://39.98.147.77:8888");
////        ICloudMethod userMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
////        CloudClient.getInstance().setCloudMethod(userMethod);
//
//        return token;
//    }

    public void sendSmsNum(String mobile, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().sendSms(mobile, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                String sms = processSuccessSms(data);
                callback.onSuccess(sms);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    private String processSuccessSms(String data) {
        LogUtils.i(TAG, "data = " + data);
        if (TextUtils.isEmpty(data)){
            return "";
        }

        String sms = "";
        try {
            JSONObject jsonObjData = new JSONObject(data);
            sms = jsonObjData.getString("data");
        } catch (JSONException e) {
            LogUtils.e(TAG, "e.message = " + e.getMessage());
            return "";
        }

        LogUtils.i(TAG, "sms = " + sms);

        return sms;
    }

    public void updateUserInfo(UserInfo userInfo, final IUIDataHelperCallback callback) {
        updateUserInfo(userInfo.getNickName(), userInfo.getPassword(), userInfo.getMobile(), userInfo.getEmail(), userInfo.getHeadUrl(), userInfo.getBirthday(), userInfo.getGender(), userInfo.getWechatNum(), userInfo.getBlogNum(), userInfo.getQqNum(), userInfo.getModifyTimestamp(), callback);
    }

    public void updateUserInfo(String nickName, String password, String mobile, String email, String headUrl, String birthday, int gender, String wechatNum, String blogNum, String qqNum, long modifyTimestamp, final IUIDataHelperCallback callback) {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUserId(SPStaticUtils.getString("user_open_id"));
        userInfoBean.setNickName(nickName);
        userInfoBean.setPassword(password);
        userInfoBean.setMobile(mobile);
        userInfoBean.setEmail(email);
        userInfoBean.setHeadUrl(headUrl);
        userInfoBean.setBirthday(birthday);
        userInfoBean.setGender(gender);
        userInfoBean.setWechatNum(wechatNum);
        userInfoBean.setBlogNum(blogNum);
        userInfoBean.setQqNum(qqNum);
        LogUtils.i(TAG, "userInfoBean = " + userInfoBean.toString());

        CloudClient.getInstance().updateUserInfo(userInfoBean, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "updateUserInfo: fail, code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    private void processSuccessUpdateUserInfo(UserInfo userInfo) {
        DBHelper.getInstance().updateUser(userInfo);
        LogUtils.i(TAG, "successUpdateUserInfo = " + userInfo.toString());
    }

    public void updateHeadImg(File imgFile, final IUIDataHelperCallback callback) {

    }

    public void unregister(final String userId, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "unregister,userId = " + userId);
        CloudClient.getInstance().unregister(userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                String sms = processSuccessSms(data);
                callback.onSuccess(sms);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void createGroup(final String groupName, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "createGroup: groupName = " + groupName);
        CloudClient.getInstance().createGroup(groupName, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                GroupInfoBean groupInfoBean = GsonUtils.fromJson(data, GroupInfoBean.class);

                UserGroupInfo userGroupInfo = new UserGroupInfo();
                userGroupInfo.setGroupId(groupInfoBean.getGroupId());
                userGroupInfo.setGroupName(groupInfoBean.getGroupName());
                userGroupInfo.setRole("admin");

                callback.onSuccess(userGroupInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void updateGroupInfo(final String groupId, final String groupName, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "updateGroupInfo: groupId = " + groupId + ", groupName = " + groupName);
        CloudClient.getInstance().updateGroupInfo(groupId, groupName, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                GroupInfoBean groupInfoBean = GsonUtils.fromJson(data, GroupInfoBean.class);

                UserGroupInfo userGroupInfo = new UserGroupInfo();
                userGroupInfo.setGroupId(groupInfoBean.getGroupId());
                userGroupInfo.setGroupName(groupInfoBean.getGroupName());
                callback.onSuccess(userGroupInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void deleteGroup(final String groupId, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "deleteGroup: groupId = " + groupId);
        CloudClient.getInstance().deleteGroup(groupId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                callback.onSuccess(groupId);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void addInvite(final String groupId, final String toMobile, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "addInvite: groupId = " + groupId + ", toMobile = " + toMobile);
        CloudClient.getInstance().addInvite(groupId, toMobile, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                GroupInviteInfo groupInviteInfo = GsonUtils.fromJson(data, GroupInviteInfo.class);
                callback.onSuccess(groupInviteInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "addInvite: error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void updateInviteStatus(final String inviteId, final String status, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "updateInviteStatus: inviteId = " + inviteId + ", status = " + status);
        CloudClient.getInstance().updateInviteStatus(inviteId, status, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                callback.onSuccess(status);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void deleteInvite(final String inviteId, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "deleteInvite: inviteId = " + inviteId);
        CloudClient.getInstance().deleteInvite(inviteId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                callback.onSuccess(inviteId);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getSendInvite(final IUIDataHelperCallback callback) {
        String userId = SPStaticUtils.getString("user_open_id");
        CloudClient.getInstance().getSendInvite(userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                Type type = new TypeToken<List<GroupInviteInfo>>() {}.getType();
                List<GroupInviteInfo> groupInviteInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(groupInviteInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getInvite(final IUIDataHelperCallback callback) {
        String userId = SPStaticUtils.getString("user_open_id");
        CloudClient.getInstance().getInvite(userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                Type type = new TypeToken<List<GroupInviteInfo>>() {}.getType();
                List<GroupInviteInfo> groupInviteInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(groupInviteInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getNewMessageCount(final IUIDataHelperCallback callback) {
        String userId = SPStaticUtils.getString("user_open_id");
        CloudClient.getInstance().getNewMessageCount(userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "getNewMessageCount success: data = " + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int count = jsonObject.getInt("data");
                    callback.onSuccess(count);
                }
                catch (JSONException e) {
                    callback.onError(NetworkConst.ERR_CODE_INVALIDE_DATA);
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getNewMessageCount error: code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "getNewMessageCount failure: errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void withdrawGroup(final String userId, final String groupId, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "withdrawGroup: userId = " + userId);
        CloudClient.getInstance().withdrawGroup(userId, groupId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                callback.onSuccess(groupId);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void updateGroupUserRole(final String userId, final String role, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        LogUtils.i(TAG, "updateGroupUserRole: role = " + role);
        CloudClient.getInstance().updateGroupUserRole(groupId, userId, role, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                callback.onSuccess(role);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getUserGroupInfo(final String userId, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "getUserGroupInfo: userId = " + userId);
        CloudClient.getInstance().getUserGroupInfo(userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                Type type = new TypeToken<List<UserGroupBean>>() {}.getType();
                List<UserGroupBean> userGroupBeanList = GsonUtils.fromJson(data, type);
                List<UserGroupInfo> userGroupInfoList = Util.userGroupBeanList2UserGroupInfoList(userGroupBeanList);
                callback.onSuccess(userGroupInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getGroupUserInfo(final String groupId, final IUIDataHelperCallback callback) {
        LogUtils.i(TAG, "getGroupUserInfo: groupId = " + groupId);
        CloudClient.getInstance().getGroupUserInfo(groupId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                Type type = new TypeToken<List<GroupUserBean>>() {}.getType();
                List<GroupUserBean> groupUserBeanList = GsonUtils.fromJson(data, type);
                List<GroupUserInfo> groupUserInfoList = Util.groupUserBeanList2GroupUserInfoList(groupUserBeanList);
                callback.onSuccess(groupUserInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void uploadFile(final String filePath, final IUIDataHelperCallback callback) {
        if (TextUtils.isEmpty(filePath)) {
            callback.onSuccess("");
            return;
        }

        CloudClient.getInstance().uploadFile(filePath, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                String url = "";
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    url = jsonObject.getString("data");

                }
                catch (JSONException e) {

                }

                callback.onSuccess(url);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "uploadFile fail: code = " + code);
                if (callback != null) {
                    callback.onError(code);
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
                }
            }
        });
    }

    public void addNotice(String tittle, String content, final IUIDataHelperCallback callback) {
        final String groupId = SPStaticUtils.getString("working_group_id");
        final String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return;
        }

        long timeStamp = System.currentTimeMillis();
        CloudClient.getInstance().addNotice(groupId, userId, tittle, content, timeStamp, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                NoticeInfo noticeInfo = GsonUtils.fromJson(data, NoticeInfo.class);
                callback.onSuccess(noticeInfo);

                UdpHelper.getInstance().sendBasicDataToGroup(userId, "notice", groupId);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getNotice(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        CloudClient.getInstance().getNotice(groupId, userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                Type type = new TypeToken<List<NoticeInfo>>() {}.getType();
                List<NoticeInfo> noticeInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(noticeInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void addNoticeReadStatus(String noticeId, int flag, final IUIDataHelperCallback callback) {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        long timeStamp = System.currentTimeMillis();
        CloudClient.getInstance().addNoticeReadStatus(noticeId, userId, flag, timeStamp, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getNoticeReadStatus(String noticeId, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        CloudClient.getInstance().getNoticeReadStatus(groupId, noticeId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                Type type = new TypeToken<List<NoticeReadStatus>>() {}.getType();
                List<NoticeReadStatus> noticeReadStatusList = GsonUtils.fromJson(data, type);
                callback.onSuccess(noticeReadStatusList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getNoLookOverCount(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return;
        }

        CloudClient.getInstance().getNoLookOverCount(groupId, userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "success data = " + data);
                Type type = new TypeToken<GeneralRsp<Integer>>() {}.getType();
                GeneralRsp<Integer> generalRsp = GsonUtils.fromJson(data, type);
                callback.onSuccess(generalRsp.getData());
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "error code = " + code + " msg = " + errorMsg);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "failure errorMsg = " + errorMsg);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
