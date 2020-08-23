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
                    synKeepWatchDeskToCloud();
                    synKeepWatchSigninToCloud();
                    synKeepWatchAssignmentToCloud();
                    synKeepWatchRouteSummaryToCloud();
                    synKeepWatchRouteDeskToCloud();
                }
                catch (Exception e) {
                    LogUtils.i("com.ctfww.module.keepwatch.Airship", "e.getMessage() = " + e.getMessage());
                }
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    // 1. 签到点的同步

    // 同步签到点上云
    public void synKeepWatchDeskToCloud() {
        KeepWatchDeskAirship.synToCloud();
    }

    // 从云上同步签到点
    public void synKeepWatchDeskFromCloud() {
        KeepWatchDeskAirship.synFromCloud();
    }

    // 2. 签到路线相关

    // 将路线数据同步上云
    public void synKeepWatchRouteSummaryToCloud() {
        KeepWatchRouteSummaryAirship.synToCloud();
    }

    // 从云上同步签到线路
    public void synKeepWatchRouteSummaryFromCloud() {
        KeepWatchRouteSummaryAirship.synFromCloud();
    }

    // 3. 签到路线点相关

    // 将路线签到点数据同步上云
    public void synKeepWatchRouteDeskToCloud() {
        KeepWatchRouteSummaryAirship.synToCloud();
    }

    // 从云上同步路线签到点
    public void synKeepWatchRouteDeskFromCloud() {
        KeepWatchRouteSummaryAirship.synFromCloud();
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

    // 20. 同步任务

    // 同步任务上云
    public void synKeepWatchAssignmentToCloud() {
        KeepWatchAssignmentAirship.synToCloud();
    }

    // 从云上同步任务
    public void synKeepWatchAssignmentFromCloud() {
        KeepWatchAssignmentAirship.synFromCloud();
    }

    // 30. 统计相关

    // 从云上同步动态
    public void synKeepWatchPersonTrendsFromCloud() {
        KeepWatchPersonTrendsAirship.synFromCloud();
    }

    // 从云上同步排行
    public void synKeepWatchRankingFromCloud() {
        KeepWatchRankingAirship.synFromCloud();
    }

    // 从云上同步签到点签到统计
    public void synKeepWatchAssignmentFinishStatusFromCloud() {
        KeepWatchAssignmentFinishStatusAirship.synFromCloud();
    }
}
