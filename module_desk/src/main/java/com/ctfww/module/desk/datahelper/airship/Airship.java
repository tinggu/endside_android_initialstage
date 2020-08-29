package com.ctfww.module.desk.datahelper.airship;

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
                    synToCloud();
                }
                catch (Exception e) {
                    LogUtils.i("com.ctfww.module.desk.Airship", "e.getMessage() = " + e.getMessage());
                }
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    public void synToCloud() {
        synDeskToCloud();
        synRouteSummaryToCloud();
        synRouteDeskToCloud();
    }

    public void synFromCloud() {
        synDeskFromCloud();
        synRouteSummaryFromCloud();
        synRouteDeskFromCloud();
    }

    // 1. 签到点的同步

    // 同步签到点上云
    public void synDeskToCloud() {
        DeskAirship.synToCloud();
    }

    // 从云上同步签到点
    public void synDeskFromCloud() {
        DeskAirship.synFromCloud();
    }

    // 2. 签到路线相关

    // 将路线数据同步上云
    public void synRouteSummaryToCloud() {
        RouteSummaryAirship.synToCloud();
    }

    // 从云上同步签到线路
    public void synRouteSummaryFromCloud() {
        RouteSummaryAirship.synFromCloud();
    }

    // 3. 签到路线点相关

    // 将路线签到点数据同步上云
    public void synRouteDeskToCloud() {
        RouteSummaryAirship.synToCloud();
    }

    // 从云上同步路线签到点
    public void synRouteDeskFromCloud() {
        RouteSummaryAirship.synFromCloud();
    }
}
