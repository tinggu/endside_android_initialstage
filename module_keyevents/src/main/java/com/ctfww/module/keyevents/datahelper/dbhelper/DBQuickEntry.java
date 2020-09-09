package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventPerson;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.datahelper.sp.Const;

import java.util.ArrayList;
import java.util.List;

public class DBQuickEntry {
    public static List<KeyEvent> getCanSnatchKeyEventList() {
        List<KeyEvent> keyEventList = new ArrayList<>();
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return keyEventList;
        }
        List<KeyEventPerson> keyEventPersonList = DBHelper.getInstance().getCanSnatchKeyEventPersonList(groupId);
        if (keyEventPersonList.isEmpty()) {
            return keyEventList;
        }

        for (int i = 0; i < keyEventPersonList.size(); ++i) {
            KeyEventPerson keyEventPerson = keyEventPersonList.get(i);
            KeyEvent keyEvent = DBHelper.getInstance().getKeyEvent(keyEventPerson.getEventId());
            if (keyEvent == null) {
                continue;
            }

            keyEventList.add(keyEvent);
        }

        return keyEventList;
    }

    public static long getNoEndKeyEventCount() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        return "admin".equals(role) ? DBHelper.getInstance().getNoEndKeyEventPersonCount(groupId) : DBHelper.getInstance().getDoingKeyEventPersonCount(groupId, userId);
    }

    public static List<KeyEvent> getNoEndKeyEventList() {
        List<KeyEvent> keyEventList = new ArrayList<>();
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return keyEventList;
        }

        List<KeyEventPerson> keyEventPersonList = "admin".equals(role) ? DBHelper.getInstance().getNoEndKeyEventPersonList(groupId) : DBHelper.getInstance().getDoingKeyEventPersonList(groupId, userId);
        if (keyEventPersonList.isEmpty()) {
            return keyEventList;
        }

        for (int i = 0; i < keyEventPersonList.size(); ++i) {
            KeyEventPerson keyEventPerson = keyEventPersonList.get(i);
            KeyEvent keyEvent = DBHelper.getInstance().getKeyEvent(keyEventPerson.getEventId());
            if (keyEvent == null) {
                continue;
            }

            keyEventList.add(keyEvent);
        }

        return keyEventList;
    }

    public static List<KeyEvent> getDoingKeyEventList() {
        List<KeyEvent> keyEventList = new ArrayList<>();
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return keyEventList;
        }

        List<KeyEventPerson> keyEventPersonList = DBHelper.getInstance().getDoingKeyEventPersonList(groupId, userId);
        if (keyEventPersonList.isEmpty()) {
            return keyEventList;
        }

        for (int i = 0; i < keyEventPersonList.size(); ++i) {
            KeyEventPerson keyEventPerson = keyEventPersonList.get(i);
            KeyEvent keyEvent = DBHelper.getInstance().getKeyEvent(keyEventPerson.getEventId());
            if (keyEvent == null) {
                continue;
            }

            keyEventList.add(keyEvent);
        }

        return keyEventList;
    }

    public static List<KeyEvent> getEndKeyEventList(long startTime, long endTime) {
        List<KeyEvent> keyEventList = new ArrayList<>();
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return keyEventList;
        }

        List<KeyEventPerson> keyEventPersonList = "admin".equals(role) ? DBHelper.getInstance().getEndKeyEventPersonList(groupId, startTime, endTime) : DBHelper.getInstance().getEndKeyEventPersonList(groupId, userId, startTime, endTime);
        if (keyEventPersonList.isEmpty()) {
            return keyEventList;
        }

        for (int i = 0; i < keyEventPersonList.size(); ++i) {
            KeyEventPerson keyEventPerson = keyEventPersonList.get(i);
            KeyEvent keyEvent = DBHelper.getInstance().getKeyEvent(keyEventPerson.getEventId());
            if (keyEvent == null) {
                continue;
            }

            keyEventList.add(keyEvent);
        }

        return keyEventList;
    }
}
