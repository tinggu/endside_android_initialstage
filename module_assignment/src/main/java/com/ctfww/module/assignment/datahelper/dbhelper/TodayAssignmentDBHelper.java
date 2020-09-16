package com.ctfww.module.assignment.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.assignment.entity.TodayAssignmentDao;

import java.util.List;

public class TodayAssignmentDBHelper {
    private final static String TAG = "DeskTodayAssignmentDBHelper";

    public static boolean add(TodayAssignmentDao dao, TodayAssignment info) {
        try {
            dao.insert(info);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    public static void update(TodayAssignmentDao dao, TodayAssignment info) {
        try {
            dao.update(info);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    public static TodayAssignment get(TodayAssignmentDao dao, String groupId, int objectId, String userId, long dayTimeStamp, String type) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.ObjectId.eq(objectId),
                                TodayAssignmentDao.Properties.UserId.eq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp),
                                TodayAssignmentDao.Properties.Type.eq(type)))
                .unique();
    }

    public static List<TodayAssignment> getList(TodayAssignmentDao dao, String groupId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp)))
                .list();
    }

    public static List<TodayAssignment> getList(TodayAssignmentDao dao, String groupId, String userId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp))).list();
    }

    public static List<TodayAssignment> getList(TodayAssignmentDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime)))
                .list();
    }

    public static List<TodayAssignment> getList(TodayAssignmentDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime)
                        )).list();
    }

    public static long getCount(TodayAssignmentDao dao, String groupId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp)))
                .buildCount()
                .count();
    }

    public static long getCount(TodayAssignmentDao dao, String groupId, String userId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp)))
                .buildCount()
                .count();
    }

    public static long getCount(TodayAssignmentDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime)))
                .buildCount()
                .count();
    }

    public static long getCount(TodayAssignmentDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime)
                        ))
                .buildCount()
                .count();
    }

    public static List<TodayAssignment> getFinishList(TodayAssignmentDao dao, String groupId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp),
                                TodayAssignmentDao.Properties.SigninCount.ge(TodayAssignmentDao.Properties.Frequency)))
                .list();
    }

    public static List<TodayAssignment> getFinishList(TodayAssignmentDao dao, String groupId, String userId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp),
                                TodayAssignmentDao.Properties.SigninCount.ge(TodayAssignmentDao.Properties.Frequency))).list();
    }

    public static List<TodayAssignment> getFinishList(TodayAssignmentDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime),
                                TodayAssignmentDao.Properties.SigninCount.ge(TodayAssignmentDao.Properties.Frequency)))
                .list();
    }

    public static List<TodayAssignment> getFinishList(TodayAssignmentDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime),
                                TodayAssignmentDao.Properties.SigninCount.ge(TodayAssignmentDao.Properties.Frequency))).list();
    }

    public static long getFinishCount(TodayAssignmentDao dao, String groupId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp),
                                TodayAssignmentDao.Properties.SigninCount.ge(TodayAssignmentDao.Properties.Frequency)))
                .buildCount()
                .count();
    }

    public static long getFinishCount(TodayAssignmentDao dao, String groupId, String userId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp),
                                TodayAssignmentDao.Properties.SigninCount.ge(TodayAssignmentDao.Properties.Frequency)))
                .buildCount()
                .count();
    }

    public static long getFinishCount(TodayAssignmentDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime),
                                TodayAssignmentDao.Properties.SigninCount.ge(TodayAssignmentDao.Properties.Frequency)))
                .buildCount()
                .count();
    }

    public static long getFinishCount(TodayAssignmentDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime),
                                TodayAssignmentDao.Properties.SigninCount.ge(TodayAssignmentDao.Properties.Frequency)))
                .buildCount()
                .count();
    }

    public static List<TodayAssignment> getLeakList(TodayAssignmentDao dao, String groupId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp),
                                TodayAssignmentDao.Properties.SigninCount.eq(0),
                                TodayAssignmentDao.Properties.Status.notEq("delete")))
                .list();
    }

    public static List<TodayAssignment> getLeakList(TodayAssignmentDao dao, String groupId, String userId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp),
                                TodayAssignmentDao.Properties.SigninCount.ge(0),
                                TodayAssignmentDao.Properties.Status.notEq("delete")))
                .list();
    }

    public static List<TodayAssignment> getLeakList(TodayAssignmentDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime),
                                TodayAssignmentDao.Properties.SigninCount.eq(0),
                                TodayAssignmentDao.Properties.Status.notEq("delete")))
                .list();
    }

    public static List<TodayAssignment> getLeakList(TodayAssignmentDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime),
                                TodayAssignmentDao.Properties.SigninCount.ge(0),
                                TodayAssignmentDao.Properties.Status.notEq("delete")))
                .list();
    }

    public static long getLeakCount(TodayAssignmentDao dao, String groupId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.eq(dayTimeStamp),
                                TodayAssignmentDao.Properties.SigninCount.ge(0),
                                TodayAssignmentDao.Properties.Status.notEq("delete")))
                .buildCount()
                .count();
    }

    public static long getLeakCount(TodayAssignmentDao dao, String groupId, String userId, long dayTimeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(dayTimeStamp),
                                TodayAssignmentDao.Properties.SigninCount.ge(0),
                                TodayAssignmentDao.Properties.Status.notEq("delete")))
                .buildCount()
                .count();
    }

    public static long getLeakCount(TodayAssignmentDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime),
                                TodayAssignmentDao.Properties.SigninCount.ge(0),
                                TodayAssignmentDao.Properties.Status.notEq("delete")))
                .buildCount()
                .count();
    }

    public static long getLeakCount(TodayAssignmentDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(TodayAssignmentDao.Properties.GroupId.eq(groupId),
                                TodayAssignmentDao.Properties.UserId.notEq(userId),
                                TodayAssignmentDao.Properties.DayTimeStamp.ge(startTime),
                                TodayAssignmentDao.Properties.DayTimeStamp.le(endTime),
                                TodayAssignmentDao.Properties.SigninCount.ge(0),
                                TodayAssignmentDao.Properties.Status.notEq("delete")))
                .buildCount()
                .count();
    }
}
