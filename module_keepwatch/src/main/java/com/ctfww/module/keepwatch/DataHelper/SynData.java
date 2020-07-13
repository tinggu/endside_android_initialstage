package com.ctfww.module.keepwatch.DataHelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SynData {
    private static final String TAG = SynData.class.getName();

    public static void startTimedSyn() {
        LogUtils.i("com.ctfww.module.keepwatch.SynData", "startTimedSyn...");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                        .daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    synAddDeskToCloud();
                    synModifyDesk();
                    synSigninToCloud();
                }
                catch (Exception e) {
                    LogUtils.i("com.ctfww.module.keepwatch.SynData", "e.getMessage() = " + e.getMessage());
                }
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    public static void synAddDeskToCloud() {
        String role = SPStaticUtils.getString("role");
        if (!"admin".equals(role)) {
            return;
        }

        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        List<KeepWatchDesk> deskList = DBHelper.getInstance().getNoSynAddDesk(groupId);
        if (deskList == null || deskList.isEmpty()) {
            return;
        }

        for (int i = 0; i < deskList.size(); ++i) {
            KeepWatchDesk desk = deskList.get(i);
            NetworkHelper.getInstance().synAddDeskToCloud(desk, new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    LogUtils.i(TAG, "synAddDesk success: desk.getDeskName() = " + desk.getDeskName());
                }

                @Override
                public void onError(int code) {
                    LogUtils.i(TAG, "synAddDesk fail: desk.getDeskName() = " + desk.getDeskName());
                }
            });
        }
    }

    public static void synModifyDesk() {
        String role = SPStaticUtils.getString("role");
        if (!"admin".equals(role)) {
            return;
        }

        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        List<KeepWatchDesk> deskList = DBHelper.getInstance().getNoSynModifyDesk(groupId);
        if (deskList == null || deskList.isEmpty()) {
            return;
        }

        for (int i = 0; i < deskList.size(); ++i) {
            KeepWatchDesk desk = deskList.get(i);
            NetworkHelper.getInstance().synModifyDeskToCloud(desk, new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    LogUtils.i(TAG, "synModifyDesk success: desk.getDeskName() = " + desk.getDeskName());
                }

                @Override
                public void onError(int code) {
                    LogUtils.i(TAG, "synModifyDesk fail: desk.getDeskName() = " + desk.getDeskName());
                }
            });
        }
    }

    public static void synSigninToCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        List<KeepWatchSigninInfo> keepWatchSigninInfoList = DBHelper.getInstance().getNoSynAddSignin(groupId);
        if (keepWatchSigninInfoList == null || keepWatchSigninInfoList.isEmpty()) {
            return;
        }

        for (int i = 0; i < keepWatchSigninInfoList.size(); ++i) {
            KeepWatchSigninInfo keepWatchSigninInfo = keepWatchSigninInfoList.get(i);
            NetworkHelper.getInstance().synKeepWatchSigninToCloud(keepWatchSigninInfo, new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    LogUtils.i(TAG, "synSigninToCloud success: deskId = " + keepWatchSigninInfo.getDeskId());
                }

                @Override
                public void onError(int code) {
                    LogUtils.i(TAG, "synSigninToCloud fail: deskId = " + keepWatchSigninInfo.getDeskId());
                }
            });
        }
    }

    public static void getAllDesk() {
        NetworkHelper.getInstance().getAllDesk(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                EventBus.getDefault().post(new MessageEvent("has_get_all_desk"));
                LogUtils.i(TAG, "getAllDesk suceess!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getAllDesk fail!");
            }
        });
    }
}
