package com.ctfww.module.keyevents.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.FileUtils;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Airship {
    private static final String TAG = "Airship";

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
                    LogUtils.i("com.ctfww.module.keyevents.Airship", "e.getMessage() = " + e.getMessage());
                }
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    public void synToCloud() {
        synKeyEventToCloud();
        synKeyEventTraceToCloud();
    }

    public void synFromCloud() {
        synKeyEventFromCloud();
        synKeyEventTraceFromCloud();
    }

    public void synKeyEventToCloud() {
        KeyEventAirship.synToCloud();
    }

    public void synKeyEventFromCloud() {
        KeyEventAirship.synFromCloud();
    }

    public void synKeyEventTraceToCloud() {
        KeyEventTraceAirship.synToCloud();
    }

    public void synKeyEventTraceFromCloud() {
        KeyEventTraceAirship.synFromCloud();
    }
}
