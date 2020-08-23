package com.ctfww.module.keepwatch.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;
import com.ctfww.module.keepwatch.entity.KeepWatchRanking;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchRankingAirship {
    private final static String TAG = "KeepWatchRankingAirship";

    // 从云上同步签到点
    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String key = "keepwatch_ranking_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synTodayKeepWatchRankingFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchRanking> rankingList = (List<KeepWatchRanking>)obj;

                if (!rankingList.isEmpty()) {
                    updateByCloud(rankingList);
                    EventBus.getDefault().post("finish_ranking_syn");
                }

                SPStaticUtils.put(key, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static void updateByCloud(List<KeepWatchRanking> rankingList) {
        for (int i = 0; i < rankingList.size(); ++i) {
            KeepWatchRanking ranking = rankingList.get(i);
            DBHelper.getInstance().addKeepWatchRanking(ranking);
        }
    }
}
