package com.ctfww.module.assignment.datahelper.airship;

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
        synAssignmentToCloud();
    }

    public void synFromCloud() {
        synAssignmentFromCloud();
    }

    // 1. 任务的同步

    // 同步任务上云
    public void synAssignmentToCloud() {
        AssignmentAirship.synToCloud();
    }

    // 从云上同步任务
    public void synAssignmentFromCloud() {
        AssignmentAirship.synFromCloud();
    }
}
