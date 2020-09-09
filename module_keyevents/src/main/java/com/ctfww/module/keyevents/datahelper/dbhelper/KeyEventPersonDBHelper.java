package com.ctfww.module.keyevents.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keyevents.Entity.KeyEventPerson;
import com.ctfww.module.keyevents.Entity.KeyEventPersonDao;

import java.util.List;

public class KeyEventPersonDBHelper {
    private static final String TAG = "KeyEventPersonDBHelper";

    public static boolean insert(KeyEventPersonDao dao, KeyEventPerson keyEventPerson) {
        try {
            dao.insert(keyEventPerson);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public static boolean update(KeyEventPersonDao dao, KeyEventPerson keyEventPerson) {
        try {
            dao.update(keyEventPerson);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public static KeyEventPerson get(KeyEventPersonDao dao, String eventId) {
        return dao.queryBuilder().where(KeyEventPersonDao.Properties.EventId.eq(eventId)).unique();
    }

    public static List<KeyEventPerson> getList(KeyEventPersonDao dao, String groupId) {
        return dao.queryBuilder().where(KeyEventPersonDao.Properties.GroupId.eq(groupId)).list();
    }

    public static List<KeyEventPerson> getList(KeyEventPersonDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.GroupId.eq(userId))).list();
    }

    public static List<KeyEventPerson> getNoSynList(KeyEventPersonDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.SynTag.eq("new"), KeyEventPersonDao.Properties.SynTag.eq("modify"))).list();
    }

    public static List<KeyEventPerson> getCanSnatchList(KeyEventPersonDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), dao.queryBuilder().or(KeyEventPersonDao.Properties.Status.eq("create"), KeyEventPersonDao.Properties.Status.eq("free")))).list();
    }

    public static List<KeyEventPerson> getNoEndList(KeyEventPersonDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.Status.notEq("end"))).list();
    }

    public static List<KeyEventPerson> getDoingList(KeyEventPersonDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.UserId.eq(userId), dao.queryBuilder().or(KeyEventPersonDao.Properties.Status.eq("accepted"), KeyEventPersonDao.Properties.Status.eq("snatch")))).list();
    }

    public static long getDoingCount(KeyEventPersonDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), dao.queryBuilder().or(KeyEventPersonDao.Properties.Status.eq("received"), KeyEventPersonDao.Properties.Status.eq("accepted"), KeyEventPersonDao.Properties.Status.eq("snatch")))).buildCount().count();
    }

    public static long getNoEndCount(KeyEventPersonDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.Status.notEq("end"))).buildCount().count();
    }

    public static long getDoingCount(KeyEventPersonDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.UserId.eq(userId), dao.queryBuilder().or(KeyEventPersonDao.Properties.Status.eq("received"), KeyEventPersonDao.Properties.Status.eq("accepted"), KeyEventPersonDao.Properties.Status.eq("snatch")))).buildCount().count();
    }

    public static long getEndCount(KeyEventPersonDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.Status.eq("end"))).buildCount().count();
    }

    public static long getEndCount(KeyEventPersonDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.UserId.eq(userId), KeyEventPersonDao.Properties.Status.eq("end"))).buildCount().count();
    }

    public static long getEndCount(KeyEventPersonDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.Status.eq("end"), KeyEventPersonDao.Properties.TimeStamp.ge(startTime), KeyEventPersonDao.Properties.TimeStamp.le(endTime))).buildCount().count();
    }

    public static long getEndCount(KeyEventPersonDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.UserId.eq(userId), KeyEventPersonDao.Properties.Status.eq("end"), KeyEventPersonDao.Properties.TimeStamp.ge(startTime), KeyEventPersonDao.Properties.TimeStamp.le(endTime))).buildCount().count();
    }

    public static List<KeyEventPerson> getEndList(KeyEventPersonDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.Status.eq("end"), KeyEventPersonDao.Properties.TimeStamp.ge(startTime), KeyEventPersonDao.Properties.TimeStamp.le(endTime))).list();
    }

    public static List<KeyEventPerson> getEndList(KeyEventPersonDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeyEventPersonDao.Properties.GroupId.eq(groupId), KeyEventPersonDao.Properties.UserId.eq(userId), KeyEventPersonDao.Properties.Status.eq("end"), KeyEventPersonDao.Properties.TimeStamp.ge(startTime), KeyEventPersonDao.Properties.TimeStamp.le(endTime))).list();
    }
}
