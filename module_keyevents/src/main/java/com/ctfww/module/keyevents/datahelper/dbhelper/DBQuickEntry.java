package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.datahelper.sp.Const;

import java.util.ArrayList;
import java.util.List;

public class DBQuickEntry {
    public static List<KeyEvent> getCanSnatchList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<KeyEvent>();
        }

        return DBHelper.getInstance().getCanSnatchList(groupId);
    }

    public static List<KeyEvent> getEndList(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<KeyEvent>();
        }

        return "admin".equals(role) ? DBHelper.getInstance().getEndList(groupId, startTime, endTime) : DBHelper.getInstance().getEndList(groupId, userId, startTime, endTime);
    }

    public static long getEndCount(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        return "admin".equals(role) ? DBHelper.getInstance().getEndCount(groupId, startTime, endTime) : DBHelper.getInstance().getEndCount(groupId, userId, startTime, endTime);
    }

    public static List<KeyEvent> getNotEndList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<KeyEvent>();
        }

        return "admin".equals(role) ? DBHelper.getInstance().getNotEndList(groupId) : DBHelper.getInstance().getNotEndList(groupId, userId);
    }

    public static long getNotEndCount() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }
        return "admin".equals(role) ? DBHelper.getInstance().getNotEndCount(groupId) : DBHelper.getInstance().getNotEndCount(groupId, userId);
    }

    public static List<KeyEvent> getDoingList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<KeyEvent>();
        }

        return DBHelper.getInstance().getNotEndList(groupId, userId);
    }

    public static List<KeyEventTrace> getKeyEventTraceListForGroup(long startTime, long endTime) {
        List<KeyEventTrace> keyEventTraceList = new ArrayList<>();
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return keyEventTraceList;
        }

        return DBHelper.getInstance().getKeyEventTraceListForGroup(groupId);
    }
}
