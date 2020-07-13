package com.guoliang.module.keyevents.datahelper;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.guoliang.module.keyevents.Entity.DaoMaster;
import com.guoliang.module.keyevents.Entity.DaoSession;
import com.guoliang.module.keyevents.Entity.KeyEvent;
import com.guoliang.module.keyevents.Entity.KeyEventDao;
import com.guoliang.module.keyevents.Entity.KeyEventReportRepaired;
import com.guoliang.module.keyevents.Entity.KeyEventReportRepairedDao;

import java.util.List;

public class DBHelper {
    private KeyEventDao keyEventDao;
    private KeyEventReportRepairedDao keyEventReportRepairedDao;

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
        keyEventReportRepairedDao = daoSession.getKeyEventReportRepairedDao();
    }


    /**
     * 插入关键事件信息
     * @param keyEvent 关键事件
     */
    public boolean addKeyEvent(KeyEvent keyEvent) {
        try {
            keyEventDao.insert(keyEvent);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 删除关键事件
     * @param keyEvent 关键事件
     */
    public void deleteKeyEvent(KeyEvent keyEvent) {
        keyEventDao.delete(keyEvent);
    }

    /**
     * 更新关键事件
     * @param keyEvent 关键事件
     */
    public void updateKeyEvent(KeyEvent keyEvent) {
        keyEventDao.update(keyEvent);
    }

    /**
     * 根据eventId查询KeyEvent
     * @param eventId 事件ID
     * @return 对应KeyEvent
     */
    public KeyEvent queryKeyEventById(String eventId) {
        List<KeyEvent> keyEvents = keyEventDao.queryBuilder().where(KeyEventDao.Properties.EventId.eq(eventId)).list();
        if (keyEvents == null) {
            return null;
        }

        return keyEvents.get(0);
    }


    /**
     * 查询关键事件表中的所有项
     * @return 所有关键事件
     */
    public List<KeyEvent> queryAllKeyEvent() {
        return keyEventDao.queryBuilder().list();
    }

    /**
     * 查询某一时间之后的事件
     * @return 所有关键事件
     */
    public List<KeyEvent> queryAllKeyEvent(long utcTime) {
        return keyEventDao.queryBuilder().where(KeyEventDao.Properties.TimeStamp.gt(utcTime)).list();
    }


    /**
     * 删除所有关键事件
     */
    public void deleteAllKeyEvent() {
        keyEventDao.deleteAll();
    }

    public List<KeyEvent> getAllKeyEvent(String userId, long startTime, long endTime) {
        return keyEventDao.queryBuilder().where(keyEventDao.queryBuilder().and(KeyEventDao.Properties.UserId.eq(userId), KeyEventDao.Properties.TimeStamp.between(startTime, endTime))).orderDesc(KeyEventDao.Properties.TimeStamp).orderDesc().list();
    }

    public List<KeyEvent> getAllNoSynKeyEvent(String userId) {
        return keyEventDao.queryBuilder().where(keyEventDao.queryBuilder().and(KeyEventDao.Properties.UserId.eq(userId), KeyEventDao.Properties.SynTag.eq("new"))).orderDesc(KeyEventDao.Properties.TimeStamp).orderDesc().list();
    }

    public boolean addReportRepaired(KeyEventReportRepaired keyEventReportRepaired) {
        try {
            keyEventReportRepairedDao.insert(keyEventReportRepaired);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public List<KeyEventReportRepaired> getNoSynReportRepaired() {
        return keyEventReportRepairedDao.queryBuilder().where(KeyEventReportRepairedDao.Properties.SynTag.eq("new")).list();
    }

    public void deleteReportRepaired(String eventId, long timaStamp) {
        KeyEventReportRepaired keyEventReportRepaired = new KeyEventReportRepaired();
        keyEventReportRepaired.setEventId(eventId);
        keyEventReportRepaired.setTimeStamp(timaStamp);
        keyEventReportRepaired.combineId();
        keyEventReportRepairedDao.delete(keyEventReportRepaired);
    }

    public void deleteReportRepaired(KeyEventReportRepaired keyEventReportRepaired) {
        keyEventReportRepairedDao.delete(keyEventReportRepaired);
    }
}
