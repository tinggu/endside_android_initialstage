package com.ctfww.module.user.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        final String key = "group_user_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, 0);
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString("user_open_id");
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synGroupUserInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<GroupUserInfo> userList = (List<GroupUserInfo>)obj;

                if (!userList.isEmpty()) {
                    if (updateByCloud(userList)) {
                        EventBus.getDefault().post("finish_group_user_syn");
                    }
                }

                SPStaticUtils.put(key, condition.getEndTime());
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
            user.setSynTag("cloud");
            if (DBHelper.getInstance().addGroupUser(user)) {
                ret = true;
                continue;
            }

            GroupUserInfo localUser = DBHelper.getInstance().getGroupUser(user.getGroupId(), user.getUserId());
            if (localUser.getTimeStamp() < user.getTimeStamp()) {
                user.setSynTag("cloud");
                DBHelper.getInstance().updateGroupUser(user);
                ret = true;
            }
        }

        return ret;
    }
}
