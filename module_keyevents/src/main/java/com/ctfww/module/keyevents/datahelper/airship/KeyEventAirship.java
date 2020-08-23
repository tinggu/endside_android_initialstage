package com.ctfww.module.keyevents.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.utils.FileUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

public class KeyEventAirship {
    private static final String TAG = "KeyEventAirship";

    public static void synToCloud() {
        List<KeyEvent> keyEventList = DBHelper.getInstance().getNoSynKeyEventList();
        if (keyEventList == null || keyEventList.isEmpty()) {
            return;
        }

        CargoToCloud<KeyEvent> cargoToCloud = new CargoToCloud<>(keyEventList);
        NetworkHelper.getInstance().synKeyEventToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < keyEventList.size(); ++i) {
                    KeyEvent keyEvent = keyEventList.get(i);
                    if (keyEvent.isPicSyned() && keyEvent.isVoiceSyned() && keyEvent.isVideoSyned()) {
                        keyEvent.setSynTag("cloud");
                    }
                    else {
                        keyEvent.setSynTag("addition");
                    }

                    DBHelper.getInstance().updateKeyEvent(keyEvent);
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String key = "keyevent_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeyEventFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeyEvent> keyEventList = (List<KeyEvent>)obj;

                if (!keyEventList.isEmpty()) {
                    if (updateByCloud(keyEventList)) {
                        EventBus.getDefault().post("finish_key_event_syn");
                    }
                }

                SPStaticUtils.put(key, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<KeyEvent> keyEventList) {
        boolean ret = false;
        for (int i = 0; i < keyEventList.size(); ++i) {
            KeyEvent keyEvent = keyEventList.get(i);
            KeyEvent localKeyEvent = DBHelper.getInstance().getKeyEvent(keyEvent.getEventId());
            keyEvent.setSynTag("cloud");
            if (localKeyEvent == null) {
                DBHelper.getInstance().addKeyEvent(keyEvent);
                ret = true;
            }
            else {
                if (localKeyEvent.getTimeStamp() < keyEvent.getTimeStamp()) {
                    DBHelper.getInstance().updateKeyEvent(keyEvent);
                    ret = true;
                }
            }
        }

        return ret;
    }

    public static void synAdditionToCloud() {
        List<KeyEvent> keyEventList = DBHelper.getInstance().getNoSynAdditionKeyEventList();
        if (keyEventList == null || keyEventList.isEmpty()) {
            return;
        }

        for (int i = 0; i < keyEventList.size(); ++i) {
            KeyEvent keyEvent = keyEventList.get(i);
            if (keyEvent.isPicSyned() && keyEvent.isVoiceSyned() && keyEvent.isVideoSyned()) {
                keyEvent.setSynTag("cloud");
                DBHelper.getInstance().updateKeyEvent(keyEvent);
            }
            else {
                if (!keyEvent.isPicSyned()) {
                    uploadPic(keyEvent);
                }
                else if (!keyEvent.isVoiceSyned()) {
                    uploadVoice(keyEvent);
                }
                else if (!keyEvent.isVideoSyned()) {
                    uploadVideo(keyEvent);
                }
            }
        }
    }

    private static void uploadPic(KeyEvent keyEvent) {
        File file = new File(keyEvent.getPicPath());
        if (!file.exists()) {
            keyEvent.setPicPath("");
            if (keyEvent.isVoiceSyned() && keyEvent.isVideoSyned() && "addition".equals(keyEvent.getSynTag())) {
                keyEvent.setSynTag("cloud");
            }

            DBHelper.getInstance().updateKeyEvent(keyEvent);
            return;
        }

        NetworkHelper.getInstance().uploadFile(keyEvent.getPicPath(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                String url = (String)obj;
                keyEvent.setPicPath(url);
                if (keyEvent.isVoiceSyned() && keyEvent.isVideoSyned() && "addition".equals(keyEvent.getSynTag())) {
                    keyEvent.setSynTag("cloud");
                }

                DBHelper.getInstance().updateKeyEvent(keyEvent);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "uploadPic fail: code = " + code);
            }
        });
    }

    private static void uploadVoice(KeyEvent keyEvent) {
        File file = new File(keyEvent.getVoicePath());
        if (!file.exists()) {
            keyEvent.setVoicePath("");
            if (keyEvent.isPicSyned() && keyEvent.isVideoSyned() && "addition".equals(keyEvent.getSynTag())) {
                keyEvent.setSynTag("cloud");
            }

            DBHelper.getInstance().updateKeyEvent(keyEvent);
            return;
        }

        NetworkHelper.getInstance().uploadFile(keyEvent.getVoicePath(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                String url = (String)obj;
                keyEvent.setVoicePath(url);
                if (keyEvent.isPicSyned() && keyEvent.isVideoSyned() && "addition".equals(keyEvent.getSynTag())) {
                    keyEvent.setSynTag("cloud");
                }

                DBHelper.getInstance().updateKeyEvent(keyEvent);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "uploadVoice fail: code = " + code);
            }
        });
    }

    private static void uploadVideo(KeyEvent keyEvent) {
        File file = new File(keyEvent.getVideoPath());
        if (!file.exists()) {
            LogUtils.i(TAG, "uploadVideo: can't find video file");
            keyEvent.setVideoPath("");
            if (keyEvent.isPicSyned() && keyEvent.isVoiceSyned() && "addition".equals(keyEvent.getSynTag())) {
                keyEvent.setSynTag("cloud");
            }

            DBHelper.getInstance().updateKeyEvent(keyEvent);
            return;
        }

        NetworkHelper.getInstance().uploadFile(keyEvent.getVideoPath(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                String url = (String)obj;
                keyEvent.setVideoPath(url);
                if (keyEvent.isPicSyned() && keyEvent.isVoiceSyned() && "addition".equals(keyEvent.getSynTag())) {
                    keyEvent.setSynTag("cloud");
                }

                DBHelper.getInstance().updateKeyEvent(keyEvent);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "uploadVideo fail: code = " + code);
            }
        });
    }
}
