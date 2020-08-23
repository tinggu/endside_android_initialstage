package com.ctfww.module.user.datahelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.GeneralRsp;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.im.UdpHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;
import com.google.gson.reflect.TypeToken;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.user.constant.UserSPConstant;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.UserGroupInfo;
import com.ctfww.module.user.entity.UserInfo;
import com.ctfww.module.user.storage.sp.SPConstant;

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

    public void deleteAccount(final String userId, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().deleteAccount(userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void loginBySMS(String mobile, String sms, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().loginBySmsCode(mobile, sms, new ICloudCallback() {
                    @Override
                    public void onSuccess(String data) {
                        UserInfo userInfo = GsonUtils.fromJson(data, UserInfo.class);
                        callback.onSuccess(userInfo);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        callback.onError(code);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
                    }
                });

    }

    public void loginByPassword(String mobile, String password, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().loginByPassword(mobile, password, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                UserInfo userInfo = GsonUtils.fromJson(data, UserInfo.class);
                callback.onSuccess(userInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void sendSmsNum(String mobile, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().sendSms(mobile, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    callback.onSuccess(jsonObject.getString("data"));
                }
                catch (JSONException e) {
                    callback.onError(NetworkConst.ERR_CODE_INVALIDE_DATA);
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synUserInfoToCloud(UserInfo userInfo, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synUserInfoToCloud(userInfo, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synUserInfoFromCloud(String userId, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synUserInfoFromCloud(userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                UserInfo userInfo = GsonUtils.fromJson(data, UserInfo.class);
                callback.onSuccess(userInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synGroupInfoToCloud(CargoToCloud<GroupInfo> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synGroupInfoToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synGroupInfoFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synGroupInfoFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<GroupInfo>>() {}.getType();
                List<GroupInfo> groupInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(groupInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synUserGroupInfoToCloud(CargoToCloud<UserGroupInfo> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synUserGroupInfoToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synUserGroupInfoFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synUserGroupInfoFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<UserGroupInfo>>() {}.getType();
                List<UserGroupInfo> groupInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(groupInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synInviteInfoToCloud(CargoToCloud<GroupInviteInfo> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synInviteInfoToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synInviteInfoFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synInviteInfoFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<GroupInviteInfo>>() {}.getType();
                List<GroupInviteInfo> inviteInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(inviteInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synNoticeInfoToCloud(CargoToCloud<NoticeInfo> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synNoticeInfoToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synNoticeInfoFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synNoticeInfoFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<NoticeInfo>>() {}.getType();
                List<NoticeInfo> noticeInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(noticeInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synNoticeReadStatusToCloud(CargoToCloud<NoticeReadStatus> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synNoticeReadStatusToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synNoticeReadStatusFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synNoticeReadStatusFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<NoticeReadStatus>>() {}.getType();
                List<NoticeReadStatus> noticeReadStatusList = GsonUtils.fromJson(data, type);
                callback.onSuccess(noticeReadStatusList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synGroupUserInfoToCloud(CargoToCloud<GroupUserInfo> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synGroupUserInfoToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });

    }

    public void synGroupUserInfoFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synGroupUserInfoFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<GroupUserInfo>>() {}.getType();
                List<GroupUserInfo> userInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(userInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
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
                    callback.onSuccess(url);
                }
                catch (JSONException e) {
                    callback.onError(NetworkConst.ERR_CODE_INVALIDE_DATA);
                }


            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
                }
            }
        });
    }
}
