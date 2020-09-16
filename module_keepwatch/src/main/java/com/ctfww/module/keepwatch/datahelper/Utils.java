package com.ctfww.module.keepwatch.datahelper;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.utils.SynDB;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Utils {
    private final static String TAG = "Utils";

    public static void start(Context ctx){
        // 初始化keepwatch网络化模块
        ICloudMethod keepWatchMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(keepWatchMethod);
//        CloudClient.getInstance().createCloudMethod();

        // 初始化签到点的网络设置
//        com.ctfww.module.

        // 初始化apk升级的网络设置
        com.ctfww.module.upgrade.datahelper.CloudClient.getInstance().createCloudMethod();

        // 初始化keepwatch数据库模块
//        DBHelper.getInstance().init(ctx);
    }

    public static void synData() {
        SynDB.startSyn(new SynDB.ISynHelper() {
            @Override
            public void doThing() {
//                synTracePoint();
            }
        });
    }

    private static boolean mIsFirstToken = false;
    public static void setFirstToken(boolean isFirstToken) {
        mIsFirstToken = isFirstToken;
    }

    public static boolean isFirstToken() {
        return mIsFirstToken;
    }

    public static void startTimeSyn() {
        LogUtils.i(TAG, "startTimedSyn...");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                        .daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    com.ctfww.module.user.datahelper.airship.Airship.getInstance().synToCloud();
                    com.ctfww.module.desk.datahelper.airship.Airship.getInstance().synToCloud();
                    com.ctfww.module.assignment.datahelper.airship.Airship.getInstance().synToCloud();
                    com.ctfww.module.signin.datahelper.airship.Airship.getInstance().synToCloud();
                    com.ctfww.module.keyevents.datahelper.airship.Airship.getInstance().synToCloud();
                }
                catch (Exception e) {
                    LogUtils.i("com.ctfww.module.keepwatch.Airship", "e.getMessage() = " + e.getMessage());
                }
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }
}
