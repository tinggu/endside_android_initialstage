package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventDao;

import java.util.List;

public class KeyEventDBHelper {
    private static final String TAG = "KeyEventDBHelper";

    /**
     * 插入关键事件信息
     * @param keyEvent 关键事件
     */
    public static boolean add(KeyEventDao dao, KeyEvent keyEvent) {
        try {
            dao.insert(keyEvent);
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
    public static void delete(KeyEventDao dao, KeyEvent keyEvent) {
        keyEvent.setStatus("delete");
        keyEvent.setTimeStamp(System.currentTimeMillis());
        dao.update(keyEvent);
    }

    /**
     * 更新关键事件
     * @param keyEvent 关键事件
     */
    public static void update(KeyEventDao dao, KeyEvent keyEvent) {
        dao.update(keyEvent);
    }

    /**
     * 根据eventId查询KeyEvent
     * @param eventId 事件ID
     * @return 对应KeyEvent
     */
    public static KeyEvent get(KeyEventDao dao, String eventId) {
        return dao.queryBuilder().where(KeyEventDao.Properties.EventId.eq(eventId)).unique();
    }

    /**
     * 查询关键事件表中的所有项
     * @return 所有关键事件
     */
    public static List<KeyEvent> getList(KeyEventDao dao, String groupId) {
        return dao.queryBuilder().where(KeyEventDao.Properties.GroupId.eq(groupId)).list();
    }

    // 获得没有同步的事件
    public static List<KeyEvent> getNoSynList(KeyEventDao dao) {
        return dao.queryBuilder().where(KeyEventDao.Properties.SynTag.eq("new")).list();
    }

    // 获得有addition没有同步的事件
    public static List<KeyEvent> getNoSynAdditionList(KeyEventDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(KeyEventDao.Properties.SynTag.eq("new"), KeyEventDao.Properties.SynTag.eq("addition"))).list();
    }
}
