package com.ctfww.module.user.datahelper.airship;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

public class UserAirship {
    private final static String TAG = "UserAirship";

    // 同步用户信息上云
    public static void synToCloud() {
        final List<UserInfo> userList = DBHelper.getInstance().getNoSynUserList();
        if (userList.isEmpty()) {
            return;
        }

        CargoToCloud<UserInfo> cargoToCloud = new CargoToCloud<>(userList);

        NetworkHelper.getInstance().synUserInfoToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < userList.size(); ++i) {
                    UserInfo userInfo = userList.get(i);
                    if (userInfo.isHeadSyned()) {
                        userInfo.setSynTag("cloud");
                    }
                    else {
                        userInfo.setSynTag("addition");
                    }
                    DBHelper.getInstance().updateUser(userInfo);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步用户信息
    public static void synFromCloud() {
        long startTime = SPStaticUtils.getLong(Const.USER_SYN_TIME_STAMP_CLOUD, 0);
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synUserInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<UserInfo> infoList = (List<UserInfo>)obj;
                if (updateByCloud(infoList)) {
                    EventBus.getDefault().post(Const.FINISH_USER_INFO_SYN);
                };

                SPStaticUtils.put(Const.USER_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    public static boolean updateByCloud(List<UserInfo> infoList) {
        boolean ret = false;
        for (int i = 0; i < infoList.size(); ++i) {
            UserInfo info = infoList.get(i);
            info.setSynTag("cloud");
            if (DBHelper.getInstance().addUser(info)) {
                ret = true;
                continue;
            }

            UserInfo localInfo = DBHelper.getInstance().getUser(info.getUserId());
            if (localInfo.getTimeStamp() < info.getTimeStamp()) {
                info.setSynTag("cloud");
                DBHelper.getInstance().updateUser(info);
                ret = true;
            }
        }

        return ret;
    }

    public static void synAdditionToCloud() {
        List<UserInfo> userInfoList = DBHelper.getInstance().getNoSynAdditionUserList();
        if (userInfoList == null || userInfoList.isEmpty()) {
            return;
        }

        for (int i = 0; i < userInfoList.size(); ++i) {
            UserInfo userInfo = userInfoList.get(i);
            if (userInfo.isHeadSyned()) {
                userInfo.setSynTag("cloud");
                DBHelper.getInstance().updateUser(userInfo);
            }
            else {
                uploadHeadUrl(userInfo);
            }
        }
    }

    private static void uploadHeadUrl(final UserInfo userInfo) {
        File file = new File(userInfo.getHeadUrl());
        if (!file.exists()) {
            userInfo.setHeadUrl("");
            if ("addition".equals(userInfo.getSynTag())) {
                userInfo.setSynTag("cloud");
            }

            DBHelper.getInstance().updateUser(userInfo);
            return;
        }

        NetworkHelper.getInstance().uploadFile(userInfo.getHeadUrl(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                String url = (String)obj;
                userInfo.setHeadUrl(url);
                if ("addition".equals(userInfo.getSynTag())) {
                    userInfo.setSynTag("cloud");
                }

                DBHelper.getInstance().updateUser(userInfo);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "uploadHeadUrl fail: code = " + code);
            }
        });
    }
}
