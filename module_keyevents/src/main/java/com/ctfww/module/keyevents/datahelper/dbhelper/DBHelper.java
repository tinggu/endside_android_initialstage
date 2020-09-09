package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.keyevents.Entity.DaoMaster;
import com.ctfww.module.keyevents.Entity.DaoSession;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventDao;
import com.ctfww.module.keyevents.Entity.KeyEventPerson;
import com.ctfww.module.keyevents.Entity.KeyEventPersonDao;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.Entity.KeyEventTraceDao;

import java.util.List;

public class DBHelper {
    private KeyEventDao keyEventDao;
    private KeyEventTraceDao keyEventTraceDao;
    private KeyEventPersonDao keyEventPersonDao;

    private DBHelper() {

    }

    private static class Inner {
        private static final DBHelper INSTANCE = new DBHelper();
    }

    public static DBHelper getInstance() {
        return Inner.INSTANCE;
    }

    public void init(Context ctx) {
        if (ctx == null) {
            return;
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "keyevents");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        keyEventDao = daoSession.getKeyEventDao();
        keyEventTraceDao = daoSession.getKeyEventTraceDao();
        keyEventPersonDao = daoSession.getKeyEventPersonDao();
    }


    // 1. 关键事件相关

    /**
     * 插入关键事件信息
     * @param keyEvent 关键事件
     */
    public boolean addKeyEvent(KeyEvent keyEvent) {
        return KeyEventDBHelper.add(keyEventDao, keyEvent);
    }

    /**
     * 删除关键事件
     * @param keyEvent 关键事件
     */
    public void deleteKeyEvent(KeyEvent keyEvent) {
        KeyEventDBHelper.delete(keyEventDao, keyEvent);
    }

    /**
     * 更新关键事件
     * @param keyEvent 关键事件
     */
    public void updateKeyEvent(KeyEvent keyEvent) {
        KeyEventDBHelper.update(keyEventDao, keyEvent);
    }

    /**
     * 根据eventId查询KeyEvent
     * @param eventId 事件ID
     * @return 对应KeyEvent
     */
    public KeyEvent getKeyEvent(String eventId) {
        return KeyEventDBHelper.get(keyEventDao, eventId);
    }

    /**
     * 查询关键事件表中的所有项
     * @return 所有关键事件
     */
    public List<KeyEvent> getKeyEventList(String groupId) {
        return KeyEventDBHelper.getList(keyEventDao, groupId);
    }

    // 获得需要同步的事件列表
    public List<KeyEvent> getNoSynKeyEventList() {
        return KeyEventDBHelper.getNoSynList(keyEventDao);
    }

    // 获得需要同步的事件列表
    public List<KeyEvent> getNoSynAdditionKeyEventList() {
        return KeyEventDBHelper.getNoSynAdditionList(keyEventDao);
    }

    // 2. 关键事件处理相关

    // 增加事件追踪状态
    public boolean addKeyEventTrace(KeyEventTrace keyEventTrace) {
        KeyEventTrace dbKeyEventTrace = KeyEventTraceDBHelper.get(keyEventTraceDao, keyEventTrace.getEventId());
        if (dbKeyEventTrace == null) {
            return KeyEventTraceDBHelper.add(keyEventTraceDao, keyEventTrace);
        }

        if (dbKeyEventTrace.getTimeStamp() < keyEventTrace.getTimeStamp()) {
            KeyEventTraceDBHelper.update(keyEventTraceDao, keyEventTrace);
            return true;
        }

        return false;
    }

    // 更新事件追踪状态
    public boolean updateKeyEventTrace(KeyEventTrace keyEventTrace) {
        return KeyEventTraceDBHelper.update(keyEventTraceDao, keyEventTrace);
    }

    public KeyEventTrace getKeyEventTrace(String eventId) {
        return KeyEventTraceDBHelper.get(keyEventTraceDao, eventId);
    }

    public List<KeyEventTrace> getNoSynKeyEventTraceList() {
        return KeyEventTraceDBHelper.getNoSynList(keyEventTraceDao);
    }

    public boolean addKeyEventPerson(KeyEventPerson keyEventPerson) {
        return KeyEventPersonDBHelper.insert(keyEventPersonDao, keyEventPerson);
    }

    public boolean updateKeyEventPerson(KeyEventPerson keyEventPerson) {
        return KeyEventPersonDBHelper.update(keyEventPersonDao, keyEventPerson);
    }

    public KeyEventPerson getKeyEventPerson(String eventId) {
        return KeyEventPersonDBHelper.get(keyEventPersonDao, eventId);
    }

    public List<KeyEventPerson> getKeyEventPersonList(String groupId) {
        return KeyEventPersonDBHelper.getList(keyEventPersonDao, groupId);
    }

    public List<KeyEventPerson> getKeyEventPersonList(String groupId, String userId) {
        return KeyEventPersonDBHelper.getList(keyEventPersonDao, groupId, userId);
    }

    public List<KeyEventPerson> getNoSynKeyEventPersonList() {
        return KeyEventPersonDBHelper.getNoSynList(keyEventPersonDao);
    }

    public List<KeyEventPerson> getCanSnatchKeyEventPersonList(String groupId) {
        return KeyEventPersonDBHelper.getCanSnatchList(keyEventPersonDao, groupId);
    }

    public List<KeyEventPerson> getDoingKeyEventPersonList(String groupId, String userId) {
        return KeyEventPersonDBHelper.getDoingList(keyEventPersonDao, groupId, userId);
    }

    public List<KeyEventPerson> getNoEndKeyEventPersonList(String groupId) {
        return KeyEventPersonDBHelper.getNoEndList(keyEventPersonDao, groupId);
    }

    public long getDoingKeyEventPersonCount(String groupId) {
        return KeyEventPersonDBHelper.getDoingCount(keyEventPersonDao, groupId);
    }

    public long getNoEndKeyEventPersonCount(String groupId) {
        return KeyEventPersonDBHelper.getNoEndCount(keyEventPersonDao, groupId);
    }

    public long getDoingKeyEventPersonCount(String groupId, String userId) {
        return KeyEventPersonDBHelper.getDoingCount(keyEventPersonDao, groupId, userId);
    }

    public long getEndKeyEventPersonCount(String groupId) {
        return KeyEventPersonDBHelper.getEndCount(keyEventPersonDao, groupId);
    }

    public long getEndKeyEventPersonCount(String groupId, String userId) {
        return KeyEventPersonDBHelper.getEndCount(keyEventPersonDao, groupId, userId);
    }

    public long getEndKeyEventPersonCount(String groupId, long startTime, long endTime) {
        return KeyEventPersonDBHelper.getEndCount(keyEventPersonDao, groupId, startTime, endTime);
    }

    public long getEndKeyEventPersonCount(String groupId, String userId, long startTime, long endTime) {
        return KeyEventPersonDBHelper.getEndCount(keyEventPersonDao, groupId, userId, startTime, endTime);
    }

    public List<KeyEventPerson> getEndKeyEventPersonList(String groupId, long startTime, long endTime) {
        return KeyEventPersonDBHelper.getEndList(keyEventPersonDao, groupId, startTime, endTime);
    }

    public List<KeyEventPerson> getEndKeyEventPersonList(String groupId, String userId, long startTime, long endTime) {
        return KeyEventPersonDBHelper.getEndList(keyEventPersonDao, groupId, userId, startTime, endTime);
    }
}
