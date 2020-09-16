package com.ctfww.module.assignment.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.assignment.entity.AssignmentInfoDao;

import java.util.List;

public class AssignmentInfoDBHelper {
    private final static String TAG = "DeskAssignmentDBHelper";

    // 用于app增加任务
    public static boolean add(AssignmentInfoDao dao, AssignmentInfo info) {
        try {
            dao.insert(info);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    // 用于app对任务进行修改（包括删除：将status置为“delete”状态）
    public static void update(AssignmentInfoDao dao, AssignmentInfo info) {
        try {
            dao.update(info);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取需要同步上云的任务信息
    public static List<AssignmentInfo> getNoSynList(AssignmentInfoDao dao) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                .or(AssignmentInfoDao.Properties.SynTag.eq("new"),
                        AssignmentInfoDao.Properties.SynTag.eq("modify")))
                .list();
    }

    // 用于app查看某个任务的详细信息
    // 用于app确实是否存在该任务
    public static AssignmentInfo get(AssignmentInfoDao dao, String groupId, int objectId, String userId, String type) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(AssignmentInfoDao.Properties.GroupId.eq(groupId),
                                AssignmentInfoDao.Properties.ObjectId.eq(objectId),
                                AssignmentInfoDao.Properties.UserId.eq(userId),
                                AssignmentInfoDao.Properties.Type.eq(type)))
                .unique();
    }

    public static AssignmentInfo get(AssignmentInfoDao dao, String groupId, int objectId, String userId, String weekDay, String type) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(AssignmentInfoDao.Properties.GroupId.eq(groupId),
                                AssignmentInfoDao.Properties.ObjectId.eq(objectId),
                                AssignmentInfoDao.Properties.UserId.eq(userId),
                                AssignmentInfoDao.Properties.CircleType.like("%" + weekDay + "%"),
                                AssignmentInfoDao.Properties.Type.eq(type))).unique();
    }

    public static List<AssignmentInfo> getList(AssignmentInfoDao dao) {
        return dao.queryBuilder()
                .where(AssignmentInfoDao.Properties.Status.notEq("delete"))
                .list();
    }

    // 获取所有任务（任务列表，但不包括是删除状态的）
    public static List<AssignmentInfo> getList(AssignmentInfoDao dao, String groupId) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(AssignmentInfoDao.Properties.GroupId.eq(groupId),
                                AssignmentInfoDao.Properties.Status.notEq("delete")))
                .list();
    }

    public static List<AssignmentInfo> getList(AssignmentInfoDao dao, String groupId, String userId) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(AssignmentInfoDao.Properties.GroupId.eq(groupId),
                                AssignmentInfoDao.Properties.UserId.notEq(userId),
                                AssignmentInfoDao.Properties.Status.notEq("delete")))
                .list();
    }

    public static List<AssignmentInfo> getWeekDayList(AssignmentInfoDao dao, String weekDay) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(AssignmentInfoDao.Properties.CircleType.like("%" + weekDay + "%"),
                                AssignmentInfoDao.Properties.Status.notEq("delete")))
                .list();
    }

    public static List<AssignmentInfo> getWeekDayList(AssignmentInfoDao dao, String groupId, String weekDay) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(AssignmentInfoDao.Properties.GroupId.eq(groupId),
                                AssignmentInfoDao.Properties.CircleType.like("%" + weekDay + "%"),
                                AssignmentInfoDao.Properties.Status.notEq("delete")))
                .list();
    }

    public static List<AssignmentInfo> getWeekDayList(AssignmentInfoDao dao, String groupId, String userId, String weekDay) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(AssignmentInfoDao.Properties.GroupId.eq(groupId),
                                AssignmentInfoDao.Properties.UserId.notEq(userId),
                                AssignmentInfoDao.Properties.CircleType.like("%" + weekDay + "%"),
                                AssignmentInfoDao.Properties.Status.notEq("delete")))
                .list();
    }

    // 删除任务
    public static void delete(AssignmentInfoDao dao, AssignmentInfo info) {
        dao.delete(info);
    }
}
