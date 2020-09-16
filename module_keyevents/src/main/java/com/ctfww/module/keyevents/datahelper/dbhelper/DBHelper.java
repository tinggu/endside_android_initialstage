package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.keyevents.Entity.DaoMaster;
import com.ctfww.module.keyevents.Entity.DaoSession;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventDao;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.Entity.KeyEventTraceDao;

import java.util.List;

public class DBHelper {
    private KeyEventDao keyEventDao;
    private KeyEventTraceDao keyEventTraceDao;

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

    public List<KeyEvent> getCanSnatchList(String groupId) {
        return KeyEventDBHelper.getCanSnatchList(keyEventDao, groupId);
    }

    public List<KeyEvent> getEndList(String groupId, long startTime, long endTime) {
        return KeyEventDBHelper.getEndList(keyEventDao, groupId, startTime, endTime);
    }

    public List<KeyEvent> getEndList(String groupId, String userId, long startTime, long endTime) {
        return KeyEventDBHelper.getEndList(keyEventDao, groupId, userId, startTime, endTime);
    }

    public long getEndCount(String groupId, long startTime, long endTime) {
        return KeyEventDBHelper.getEndCount(keyEventDao, groupId, startTime, endTime);
    }

    public long getEndCount(String groupId, String userId, long startTime, long endTime) {
        return KeyEventDBHelper.getEndCount(keyEventDao, groupId, userId, startTime, endTime);
    }

    public List<KeyEvent> getNotEndList(String groupId) {
        return KeyEventDBHelper.getNotEndList(keyEventDao, groupId);
    }

    public List<KeyEvent> getNotEndList(String groupId, String userId) {
        return KeyEventDBHelper.getNotEndList(keyEventDao, groupId, userId);
    }

    public long getNotEndCount(String groupId) {
        return KeyEventDBHelper.getNotEndCount(keyEventDao, groupId);
    }

    public long getNotEndCount(String groupId, String userId) {
        return KeyEventDBHelper.getNotEndCount(keyEventDao, groupId, userId);
    }

    // 2. 关键事件处理相关

    // 增加事件追踪状态
    public boolean addKeyEventTrace(KeyEventTrace keyEventTrace) {
        return KeyEventTraceDBHelper.addOrReplace(keyEventTraceDao, keyEventTrace);
    }

    // 更新事件追踪状态
    public boolean updateKeyEventTrace(KeyEventTrace keyEventTrace) {
        return KeyEventTraceDBHelper.update(keyEventTraceDao, keyEventTrace);
    }

    public KeyEventTrace getKeyEventTrace(String eventId, long timeStamp) {
        return KeyEventTraceDBHelper.get(keyEventTraceDao, eventId, timeStamp);
    }

    public List<KeyEventTrace> getKeyEventTraceListForKeyEvent(String eventId) {
        return KeyEventTraceDBHelper.getListForKeyEvent(keyEventTraceDao, eventId);
    }

    public List<KeyEventTrace> getKeyEventTraceListForGroup(String groupId) {
        return KeyEventTraceDBHelper.getListForGroup(keyEventTraceDao, groupId);
    }

    public List<KeyEventTrace> getNoSynKeyEventTraceList() {
        return KeyEventTraceDBHelper.getNoSynList(keyEventTraceDao);
    }
}
