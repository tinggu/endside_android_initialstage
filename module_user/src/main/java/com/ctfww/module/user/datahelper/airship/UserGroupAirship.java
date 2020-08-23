package com.ctfww.module.user.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.UserGroupInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class UserGroupAirship {
    private final static String TAG = "UserGroupAirship";

    // 同步群组信息上云
    public static void synToCloud() {
        final List<UserGroupInfo> groupList = DBHelper.getInstance().getNoSynUserGroupList();
        if (groupList.isEmpty()) {
            return;
        }

        CargoToCloud<UserGroupInfo> cargoToCloud = new CargoToCloud<>(groupList);

        NetworkHelper.getInstance().synUserGroupInfoToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < groupList.size(); ++i) {
                    UserGroupInfo groupInfo = groupList.get(i);
                    groupInfo.setSynTag("cloud");
                    DBHelper.getInstance().updateUserGroup(groupInfo);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步群组信息
    public static void synFromCloud() {
        long startTime = SPStaticUtils.getLong("user_group_syn_time_stamp_cloud", CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString("user_open_id");
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synUserGroupInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<UserGroupInfo> groupList = (List<UserGroupInfo>)obj;

                if (!groupList.isEmpty()) {
                    if (updateByCloud(groupList)) {
                        EventBus.getDefault().post("finish_user_group_syn");
                    }
                }

                SPStaticUtils.put("user_group_syn_time_stamp_cloud", condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<UserGroupInfo> groupList) {
        boolean ret = false;
        for (int i = 0; i < groupList.size(); ++i) {
            UserGroupInfo group = groupList.get(i);
            group.setSynTag("cloud");
            if (DBHelper.getInstance().addUserGroup(group)) {
                ret = true;
                continue;
            }

            UserGroupInfo localGroup = DBHelper.getInstance().getUserGroup(group.getGroupId(), group.getUserId());
            if (localGroup.getTimeStamp() < group.getTimeStamp()) {
                group.setSynTag("cloud");
                DBHelper.getInstance().updateUserGroup(group);
                ret = true;
            }
        }

        return ret;
    }
}
