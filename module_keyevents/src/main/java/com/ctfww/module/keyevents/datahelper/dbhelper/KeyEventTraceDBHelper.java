package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.Entity.KeyEventTraceDao;

import java.util.List;

public class KeyEventTraceDBHelper {
    private static final String TAG = "KeyEventTraceDBHelper";

    public static boolean add(KeyEventTraceDao dao, KeyEventTrace keyEventTrace) {
        try {
            dao.insert(keyEventTrace);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public static boolean addOrReplace(KeyEventTraceDao dao, KeyEventTrace keyEventTrace) {
        try {
            dao.insertOrReplace(keyEventTrace);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public static boolean update(KeyEventTraceDao dao, KeyEventTrace keyEventTrace) {
        try {
            dao.update(keyEventTrace);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public static KeyEventTrace get(KeyEventTraceDao dao, String eventId, long timeStamp) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventTraceDao.Properties.EventId.eq(eventId), KeyEventTraceDao.Properties.TimeStamp.eq(timeStamp))).unique();
    }

    public static List<KeyEventTrace> getListForKeyEvent(KeyEventTraceDao dao, String eventId) {
        return dao.queryBuilder().where(KeyEventTraceDao.Properties.EventId.eq(eventId)).list();
    }

    public static List<KeyEventTrace> getListForGroup(KeyEventTraceDao dao, String groupId) {
        return dao.queryBuilder()
                .where(KeyEventTraceDao.Properties.GroupId.eq(groupId))
                .orderDesc(KeyEventTraceDao.Properties.TimeStamp)
                .list();
    }

    public static List<KeyEventTrace> getNoSynList(KeyEventTraceDao dao) {
        return dao.queryBuilder().where(KeyEventTraceDao.Properties.SynTag.eq("new")).list();
    }

    public static List<KeyEventTrace> getCreateList(KeyEventTraceDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(KeyEventTraceDao.Properties.Status.notEq("create"),
                                KeyEventTraceDao.Properties.GroupId.eq(groupId),
                                KeyEventTraceDao.Properties.TimeStamp.ge(startTime),
                                KeyEventTraceDao.Properties.TimeStamp.le(endTime)))
                .list();
    }

    public static List<KeyEventTrace> getCreateList(KeyEventTraceDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(KeyEventTraceDao.Properties.Status.notEq("create"),
                                KeyEventTraceDao.Properties.GroupId.eq(groupId),
                                KeyEventTraceDao.Properties.UserId.eq(userId),
                                KeyEventTraceDao.Properties.TimeStamp.ge(startTime),
                                KeyEventTraceDao.Properties.TimeStamp.le(endTime)))
                .list();
    }

    public static long getCreateCount(KeyEventTraceDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(KeyEventTraceDao.Properties.Status.notEq("create"),
                                KeyEventTraceDao.Properties.GroupId.eq(groupId),
                                KeyEventTraceDao.Properties.TimeStamp.ge(startTime),
                                KeyEventTraceDao.Properties.TimeStamp.le(endTime)))
                .buildCount()
                .count();
    }

    public static long getCreateCount(KeyEventTraceDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(KeyEventTraceDao.Properties.Status.notEq("create"),
                                KeyEventTraceDao.Properties.GroupId.eq(groupId),
                                KeyEventTraceDao.Properties.UserId.eq(userId),
                                KeyEventTraceDao.Properties.TimeStamp.ge(startTime),
                                KeyEventTraceDao.Properties.TimeStamp.le(endTime)))
                .buildCount()
                .count();
    }

    public static List<KeyEventTrace> getEndList(KeyEventTraceDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(KeyEventTraceDao.Properties.Status.notEq("end"),
                                KeyEventTraceDao.Properties.GroupId.eq(groupId),
                                KeyEventTraceDao.Properties.TimeStamp.ge(startTime),
                                KeyEventTraceDao.Properties.TimeStamp.le(endTime)))
                .list();
    }

    public static List<KeyEventTrace> getEndList(KeyEventTraceDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(KeyEventTraceDao.Properties.Status.notEq("end"),
                                KeyEventTraceDao.Properties.GroupId.eq(groupId),
                                KeyEventTraceDao.Properties.UserId.eq(userId),
                                KeyEventTraceDao.Properties.TimeStamp.ge(startTime),
                                KeyEventTraceDao.Properties.TimeStamp.le(endTime)))
                .list();
    }
}
