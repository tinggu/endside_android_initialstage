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
}
