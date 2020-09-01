package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.keyevents.Entity.KeyEvent;
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
        List<KeyEventTrace> keyEventTraceList = DBHelper.getInstance().getCanSnatcKeyEventhList(groupId);
        if (keyEventTraceList.isEmpty()) {
            return keyEventList;
        }

        for (int i = 0; i < keyEventTraceList.size(); ++i) {
            KeyEventTrace keyEventTrace = keyEventTraceList.get(i);
            KeyEvent keyEvent = DBHelper.getInstance().getKeyEvent(keyEventTrace.getEventId());
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

        List<KeyEventTrace> keyEventTraceList = DBHelper.getInstance().getDoingKeyEventList(groupId, userId);
        if (keyEventTraceList.isEmpty()) {
            return keyEventList;
        }

        for (int i = 0; i < keyEventTraceList.size(); ++i) {
            KeyEventTrace keyEventTrace = keyEventTraceList.get(i);
            KeyEvent keyEvent = DBHelper.getInstance().getKeyEvent(keyEventTrace.getEventId());
            if (keyEvent == null) {
                continue;
            }

            keyEventList.add(keyEvent);
        }

        return keyEventList;
    }
}
