package com.ctfww.module.keepwatch.datahelper.airship;

import com.blankj.utilcode.util.LogUtils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Airship {
    private final static String TAG = "Airship";

    private Airship() {

    }

    private static class Inner {
        private static final Airship INSTANCE = new Airship();
    }

    public static Airship getInstance() {
        return Airship.Inner.INSTANCE;
    }



    public void startTimedSyn() {
        LogUtils.i(TAG, "startTimedSyn...");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                        .daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    synKeepWatchSigninToCloud();
                    com.ctfww.module.user.datahelper.airship.Airship.getInstance().synToCloud();
                    com.ctfww.module.desk.datahelper.airship.Airship.getInstance().synToCloud();
                    com.ctfww.module.keyevents.datahelper.airship.Airship.getInstance().synToCloud();
                }
                catch (Exception e) {
                    LogUtils.i("com.ctfww.module.keepwatch.Airship", "e.getMessage() = " + e.getMessage());
                }
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    // 10. 同步签到信息上云

    // 同步签到信息上云
    public void synKeepWatchSigninToCloud() {
        KeepWatchSigninAirship.synToCloud();
    }

    // 从云上同步签到信息
    public void synKeepWatchSigninFromCloud() {
        KeepWatchSigninAirship.synFromCloud();
    }

    // 30. 统计相关

    // 从云上同步动态
    public void synKeepWatchPersonTrendsFromCloud() {
        PersonTrendsAirship.synFromCloud();
    }

    // 从云上同步排行
    public void synKeepWatchRankingFromCloud() {
        RankingAirship.synFromCloud();
    }
}
