package com.ctfww.module.keepwatch.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.Ranking;
import com.ctfww.module.user.datahelper.sp.Const;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RankingAirship {
    private final static String TAG = "RankingAirship";

    // 从云上同步签到点
    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String key = "keepwatch_ranking_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, MyDateTimeUtils.getTodayStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synTodayKeepWatchRankingFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<Ranking> rankingList = (List<Ranking>)obj;

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

    private static void updateByCloud(List<Ranking> rankingList) {
        for (int i = 0; i < rankingList.size(); ++i) {
            Ranking ranking = rankingList.get(i);
            DBHelper.getInstance().addRanking(ranking);
        }
    }
}
