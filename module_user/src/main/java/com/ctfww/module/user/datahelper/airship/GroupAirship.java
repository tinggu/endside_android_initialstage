package com.ctfww.module.user.datahelper.airship;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.GroupInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GroupAirship {
    private final static String TAG = "GroupAirship";

    // 同步群组信息上云
    public static void synToCloud() {
        final List<GroupInfo> groupList = DBHelper.getInstance().getNoSynGroupList();
        LogUtils.i(TAG, "synToCloud: groupList.size() = " + groupList.size());
        if (groupList.isEmpty()) {
            return;
        }

        CargoToCloud<GroupInfo> cargoToCloud = new CargoToCloud<>(groupList);

        NetworkHelper.getInstance().synGroupInfoToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < groupList.size(); ++i) {
                    GroupInfo groupInfo = groupList.get(i);
                    groupInfo.setSynTag("cloud");
                    DBHelper.getInstance().updateGroup(groupInfo);
                }

                LogUtils.i(TAG, "synToCloud success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步群组信息
    public static void synFromCloud() {
        long startTime = SPStaticUtils.getLong(Const.GROUP_SYN_TIME_STAMP_CLOUD, 0);
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        LogUtils.i(TAG, "synFromCloud: condition = " + condition.toString());

        NetworkHelper.getInstance().synGroupInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<GroupInfo> groupList = (List<GroupInfo>)obj;
                LogUtils.i(TAG, "synFromCloud: groupList.size() = " + groupList.size());

                if (!groupList.isEmpty()) {
                    if (updateByCloud(groupList)) {
                        EventBus.getDefault().post(Const.FINISH_GROUP_SYN);
                    }
                }

                SPStaticUtils.put(Const.GROUP_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<GroupInfo> groupList) {
        boolean ret = false;
        for (int i = 0; i < groupList.size(); ++i) {
            GroupInfo group = groupList.get(i);
            group.setSynTag("cloud");
            if (DBHelper.getInstance().addGroup(group)) {
                ret = true;
                continue;
            }

            GroupInfo localGroup = DBHelper.getInstance().getGroup(group.getGroupId());
            if (localGroup.getTimeStamp() < group.getTimeStamp()) {
                group.setSynTag("cloud");
                DBHelper.getInstance().updateGroup(group);
                ret = true;
            }
        }

        return ret;
    }
}
