package com.ctfww.module.keepwatch.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.KeepWatchRanking;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDesk;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchAssignmentFinishStatusAirship {
    private final static String TAG = "KeepWatchAssignmentFinishStatusAirship";

    // 从云上同步签到点签到统计
    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String key = "keepwatch_assignment_finish_status_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synTodayKeepWatchAssignmetnFinishStatusFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchStatisticsByDesk> statisticsByDeskList = (List<KeepWatchStatisticsByDesk>)obj;

                if (!statisticsByDeskList.isEmpty()) {
                    updateByCloud(statisticsByDeskList);
                    EventBus.getDefault().post("finish_assignment_finish_status_syn");
                }

                SPStaticUtils.put(key, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static void updateByCloud(List<KeepWatchStatisticsByDesk> statisticsByDeskList) {
        for (int i = 0; i < statisticsByDeskList.size(); ++i) {
            KeepWatchStatisticsByDesk statisticsByDesk = statisticsByDeskList.get(i);
            DBHelper.getInstance().addKeepWatchStatisticsByDesk(statisticsByDesk);
        }
    }
}
