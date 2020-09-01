package com.ctfww.module.user.datahelper.airship;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.GroupUserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GroupUserAirship {
    private final static String TAG = "GroupUserAirship";

    // 同步邀请信息上云
    public static void synToCloud() {
        final List<GroupUserInfo> userList = DBHelper.getInstance().getNoSynGroupUserList();
        if (userList.isEmpty()) {
            return;
        }

        CargoToCloud<GroupUserInfo> cargoToCloud = new CargoToCloud<>(userList);

        NetworkHelper.getInstance().synGroupUserInfoToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < userList.size(); ++i) {
                    GroupUserInfo userInfo = userList.get(i);
                    userInfo.setSynTag("cloud");
                    DBHelper.getInstance().updateGroupUser(userInfo);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步通知信息
    public static void synFromCloud() {
        long startTime = SPStaticUtils.getLong(Const.GROUP_USER_SYN_TIME_STAMP_CLOUD, 0);
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        LogUtils.i(TAG, "synFromCloud: condition = " + condition.toString());

        NetworkHelper.getInstance().synGroupUserInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<GroupUserInfo> userList = (List<GroupUserInfo>)obj;
                LogUtils.i(TAG, "synFromCloud: userList.size() = " + userList.size());

                if (!userList.isEmpty()) {
                    if (updateByCloud(userList)) {
                        EventBus.getDefault().post(Const.FINISH_GROUP_USER_SYN);
                    }
                }

                SPStaticUtils.put(Const.GROUP_USER_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<GroupUserInfo> userList) {
        boolean ret = false;
        for (int i = 0; i < userList.size(); ++i) {
            GroupUserInfo user = userList.get(i);
            user.combineId();
            user.setSynTag("cloud");
            LogUtils.i(TAG, "updateByCloud: user = " + user.toString());
            if (DBHelper.getInstance().addGroupUser(user)) {
                ret = true;
                continue;
            }

            GroupUserInfo localUser = DBHelper.getInstance().getGroupUser(user.getGroupId(), user.getUserId());
            if (localUser.getTimeStamp() < user.getTimeStamp()) {
                DBHelper.getInstance().updateGroupUser(user);
                ret = true;
            }
        }

        return ret;
    }
}
