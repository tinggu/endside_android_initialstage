package com.ctfww.module.keepwatch.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchPersonTrendsAirship {
    private final static String TAG = "KeepWatchPersonTrendsAirship";

    // 从云上同步签到点
    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String key = "keepwatch_person_trends_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synTodayKeepWatchPersonTrendsFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchPersonTrends> personTrendsList = (List<KeepWatchPersonTrends>)obj;

                if (!personTrendsList.isEmpty()) {
                    updateByCloud(personTrendsList);
                    EventBus.getDefault().post("finish_person_trends_syn");
                }

                SPStaticUtils.put(key, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static void updateByCloud(List<KeepWatchPersonTrends> personTrendsList) {
        for (int i = 0; i < personTrendsList.size(); ++i) {
            KeepWatchPersonTrends personTrends = personTrendsList.get(i);
            DBHelper.getInstance().addKeepWatchPersonTrends(personTrends);
        }
    }
}
