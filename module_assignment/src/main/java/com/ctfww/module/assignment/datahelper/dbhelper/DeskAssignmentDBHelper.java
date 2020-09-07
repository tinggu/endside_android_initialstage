package com.ctfww.module.assignment.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.DeskAssignmentDao;

import java.util.List;

public class DeskAssignmentDBHelper {
    private final static String TAG = "DeskAssignmentDBHelper";

    // 用于app增加任务
    public static boolean add(DeskAssignmentDao dao, DeskAssignment deskAssignment) {
        try {
            dao.insert(deskAssignment);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    // 用于app对任务进行修改（包括删除：将status置为“delete”状态）
    public static void update(DeskAssignmentDao dao, DeskAssignment deskAssignment) {
        try {
            dao.update(deskAssignment);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取需要同步上云的任务信息
    public static List<DeskAssignment> getNoSynList(DeskAssignmentDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(DeskAssignmentDao.Properties.SynTag.eq("new"), DeskAssignmentDao.Properties.SynTag.eq("modify"))).list();
    }

    // 用于app查看某个任务的详细信息
    // 用于app确实是否存在该任务
    public static DeskAssignment get(DeskAssignmentDao dao, String groupId, int deskId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(DeskAssignmentDao.Properties.GroupId.eq(groupId), DeskAssignmentDao.Properties.DeskId.eq(deskId), DeskAssignmentDao.Properties.UserId.eq(userId))).unique();
    }

    public static DeskAssignment get(DeskAssignmentDao dao, String groupId, int deskId, String routeId, String userId, String weekDay) {
        return dao.queryBuilder().where(dao.queryBuilder().and(DeskAssignmentDao.Properties.GroupId.eq(groupId), DeskAssignmentDao.Properties.DeskId.eq(deskId), DeskAssignmentDao.Properties.UserId.eq(userId), DeskAssignmentDao.Properties.CircleType.like("%" + weekDay + "%"))).unique();
    }

    // 获取所有任务（任务列表，但不包括是删除状态的）
    public static List<DeskAssignment> getList(DeskAssignmentDao dao, String groupId) {
        return dao.queryBuilder().list();
//        return dao.queryBuilder().where(DeskAssignmentDao.Properties.GroupId.eq(groupId)).list();
//        return dao.queryBuilder().where(dao.queryBuilder().and(DeskAssignmentDao.Properties.GroupId.eq(groupId), DeskAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<DeskAssignment> getList(DeskAssignmentDao dao, String groupId, String userId) {
        return dao.queryBuilder().list();
//        return dao.queryBuilder().where(dao.queryBuilder().and(DeskAssignmentDao.Properties.GroupId.eq(groupId), DeskAssignmentDao.Properties.UserId.eq(userId))).list();
//        return dao.queryBuilder().where(dao.queryBuilder().and(DeskAssignmentDao.Properties.GroupId.eq(groupId), DeskAssignmentDao.Properties.UserId.notEq(userId), DeskAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<DeskAssignment> getWeekDayList(DeskAssignmentDao dao, String groupId, String weekDay) {
        return dao.queryBuilder().where(dao.queryBuilder().and(DeskAssignmentDao.Properties.GroupId.eq(groupId), DeskAssignmentDao.Properties.CircleType.like("%" + weekDay + "%"), DeskAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<DeskAssignment> getWeekDayList(DeskAssignmentDao dao, String groupId, String userId, String weekDay) {
        return dao.queryBuilder().where(dao.queryBuilder().and(DeskAssignmentDao.Properties.GroupId.eq(groupId), DeskAssignmentDao.Properties.UserId.notEq(userId), DeskAssignmentDao.Properties.CircleType.like("%" + weekDay + "%"), DeskAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    // 删除任务
    public static void delete(DeskAssignmentDao dao, DeskAssignment info) {
        dao.delete(info);
    }
}
