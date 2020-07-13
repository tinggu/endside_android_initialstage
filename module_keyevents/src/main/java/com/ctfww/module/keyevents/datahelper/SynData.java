package com.ctfww.module.keyevents.datahelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.utils.FileUtils;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventReportRepaired;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SynData {
    private static final String TAG = "SynData";

    public static void startTimedSyn() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                        .daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synDataToCloud();
                synRepairedToCloud();
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    private static void synDataToCloud() {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        List<KeyEvent> keyEventList = DBHelper.getInstance().getAllNoSynKeyEvent(userId);
        if (keyEventList == null || keyEventList.isEmpty()) {
            return;
        }

        for (int i = 0; i < keyEventList.size(); ++i) {
            uploadKeyEvent(keyEventList.get(i));
        }
    }

    private static void synRepairedToCloud() {
        List<KeyEventReportRepaired> keyEventReportRepairedList = DBHelper.getInstance().getNoSynReportRepaired();
        if (keyEventReportRepairedList == null || keyEventReportRepairedList.isEmpty()) {
            return;
        }

        for (int i = 0; i < keyEventReportRepairedList.size(); ++i) {
            synReportRepaired(keyEventReportRepairedList.get(i));
        }
    }

    public static void uploadKeyEvent(KeyEvent keyEvent) {
        if (canUpload(keyEvent)) {
            LogUtils.i(TAG, "uploadKeyEvent: start uploadSelf!");
            uploadSelf(keyEvent);
        }
        else {
            LogUtils.i(TAG, "uploadKeyEvent: has addtion info!");
            uploadPic(keyEvent);
            uploadVoice(keyEvent);
            uploadVideo(keyEvent);
        }
    }

    public static void uploadPic(KeyEvent keyEvent) {
        LogUtils.i(TAG, "uploadPic: start...");
        String picFilePath = keyEvent.getPicPath();
        if (TextUtils.isEmpty(picFilePath)) {
            LogUtils.i(TAG, "uploadPic: no pic");
            return;
        }

        if (FileUtils.isNetworkUrl(picFilePath)) {
            LogUtils.i(TAG, "uploadPic: is url");
            return;
        }

        File file = new File(picFilePath);
        if (!file.exists()) {
            LogUtils.i(TAG, "uploadPic: can't find pic file");
            keyEvent.setPicPath("");
            DBHelper.getInstance().updateKeyEvent(keyEvent);
            if (canUpload(keyEvent)) {
                uploadSelf(keyEvent);
            }
        }
        else {
            NetworkHelper.getInstance().uploadFile(picFilePath, new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    String url = (String)obj;
                    keyEvent.setPicPath(url);
                    DBHelper.getInstance().updateKeyEvent(keyEvent);
                    if (canUpload(keyEvent)) {
                        LogUtils.i(TAG, "uploadPic: start uploadSelf!");
                        uploadSelf(keyEvent);
                    }
                    LogUtils.i(TAG, "uploadPic success!");
                }

                @Override
                public void onError(int code) {
                    LogUtils.i(TAG, "uploadPic fail: code = " + code);
                }
            });
        }
    }

    public static void uploadVoice(KeyEvent keyEvent) {
        LogUtils.i(TAG, "uploadVoice: start...");
        String voiceFilePath = keyEvent.getVoicePath();
        if (TextUtils.isEmpty(voiceFilePath)) {
            LogUtils.i(TAG, "uploadVoice: no voice");
            return;
        }

        if (FileUtils.isNetworkUrl(voiceFilePath)) {
            LogUtils.i(TAG, "uploadVoice: is url");
            return;
        }

        File file = new File(voiceFilePath);
        if (!file.exists()) {
            LogUtils.i(TAG, "uploadVoice: can't find voice file");
            keyEvent.setVoicePath("");
            DBHelper.getInstance().updateKeyEvent(keyEvent);
            if (canUpload(keyEvent)) {
                LogUtils.i(TAG, "uploadVoice: start uploadSelf!");
                uploadSelf(keyEvent);
            }
        }
        else {
            NetworkHelper.getInstance().uploadFile(voiceFilePath, new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    String url = (String)obj;
                    keyEvent.setVoicePath(url);
                    DBHelper.getInstance().updateKeyEvent(keyEvent);
                    if (canUpload(keyEvent)) {
                        LogUtils.i(TAG, "uploadVoice: start uploadSelf!");
                        uploadSelf(keyEvent);
                    }
                    LogUtils.i(TAG, "uploadVoice success!");
                }

                @Override
                public void onError(int code) {
                    LogUtils.i(TAG, "uploadVoice fail: code = " + code);
                }
            });
        }
    }

    public static void uploadVideo(KeyEvent keyEvent) {
        LogUtils.i(TAG, "uploadVideo: start...");
        String videoFilePath = keyEvent.getVideoPath();
        if (TextUtils.isEmpty(videoFilePath)) {
            LogUtils.i(TAG, "uploadVideo: no video");
            return;
        }

        if (FileUtils.isNetworkUrl(videoFilePath)) {
            LogUtils.i(TAG, "uploadVideo: is url");
            return;
        }

        File file = new File(videoFilePath);
        if (!file.exists()) {
            LogUtils.i(TAG, "uploadVideo: can't find video file");
            keyEvent.setVideoPath("");
            DBHelper.getInstance().updateKeyEvent(keyEvent);
            if (canUpload(keyEvent)) {
                uploadSelf(keyEvent);
            }
        }
        else {
            NetworkHelper.getInstance().uploadFile(videoFilePath, new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    String url = (String)obj;
                    keyEvent.setVideoPath(url);
                    DBHelper.getInstance().updateKeyEvent(keyEvent);
                    if (canUpload(keyEvent)) {
                        LogUtils.i(TAG, "uploadVideo: start uploadSelf!");
                        uploadSelf(keyEvent);
                    }
                    LogUtils.i(TAG, "uploadVideo success!");
                }

                @Override
                public void onError(int code) {
                    LogUtils.i(TAG, "uploadVideo fail: code = " + code);
                }
            });
        }
    }

    private static boolean canUpload(KeyEvent keyEvent) {
        String picPath = keyEvent.getPicPath();
        if (!TextUtils.isEmpty(picPath) && !FileUtils.isNetworkUrl(picPath)) {
            return false;
        }

        String voicePath = keyEvent.getVoicePath();
        if (!TextUtils.isEmpty(voicePath) && !FileUtils.isNetworkUrl(voicePath)) {
            return false;
        }

        String videoPath = keyEvent.getVideoPath();
        if (!TextUtils.isEmpty(videoPath) && !FileUtils.isNetworkUrl(videoPath)) {
            return false;
        }

        return true;
    }

    private static void uploadSelf(KeyEvent keyEvent) {
        NetworkHelper.getInstance().addKeyEvent(keyEvent, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                LogUtils.i(TAG, "uploadSelf success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "uploadSelf fail: code = " + code);
            }
        });
    }

    private static void synReportRepaired(KeyEventReportRepaired keyEventReportRepaired) {
        NetworkHelper.getInstance().synReportRepaired(keyEventReportRepaired, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                LogUtils.i(TAG, "synReportRepaired success: keyEventReportRepaired.getEventId() = " + keyEventReportRepaired.getEventId());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synReportRepaired fail: code = " + code);
            }
        });
    }
}
