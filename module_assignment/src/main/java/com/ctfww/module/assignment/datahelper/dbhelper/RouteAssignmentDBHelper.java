package com.ctfww.module.assignment.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.entity.RouteAssignment;
import com.ctfww.module.assignment.entity.RouteAssignmentDao;

import java.util.List;

public class RouteAssignmentDBHelper {
    private final static String TAG = "RouteAssignmentDBHelper";

    // 用于app增加任务
    public static boolean add(RouteAssignmentDao dao, RouteAssignment routeAssignment) {
        try {
            dao.insert(routeAssignment);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    // 用于app对任务进行修改（包括删除：将status置为“delete”状态）
    public static void update(RouteAssignmentDao dao, RouteAssignment routeAssignment) {
        try {
            dao.update(routeAssignment);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取需要同步上云的任务信息
    public static List<RouteAssignment> getNoSynList(RouteAssignmentDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(RouteAssignmentDao.Properties.SynTag.eq("new"), RouteAssignmentDao.Properties.SynTag.eq("modify"))).list();
    }

    // 用于app查看某个任务的详细信息
    // 用于app确实是否存在该任务
    public static RouteAssignment get(RouteAssignmentDao dao, String groupId, String routeId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteAssignmentDao.Properties.GroupId.eq(groupId), RouteAssignmentDao.Properties.RouteId.eq(routeId), RouteAssignmentDao.Properties.UserId.eq(userId))).unique();
    }

    public static RouteAssignment get(RouteAssignmentDao dao, String groupId, String routeId, String userId, String weekDay) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteAssignmentDao.Properties.GroupId.eq(groupId), RouteAssignmentDao.Properties.RouteId.eq(routeId), RouteAssignmentDao.Properties.UserId.eq(userId), RouteAssignmentDao.Properties.CircleType.like("%" + weekDay + "%"))).unique();
    }

    public static List<RouteAssignment> getList(RouteAssignmentDao dao) {
        return dao.queryBuilder().where(RouteAssignmentDao.Properties.Status.notEq("delete")).list();
    }

    // 获取所有任务（任务列表，但不包括是删除状态的）
    public static List<RouteAssignment> getList(RouteAssignmentDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteAssignmentDao.Properties.GroupId.eq(groupId), RouteAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<RouteAssignment> getList(RouteAssignmentDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteAssignmentDao.Properties.GroupId.eq(groupId), RouteAssignmentDao.Properties.UserId.notEq(userId), RouteAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<RouteAssignment> getWeekDayList(RouteAssignmentDao dao, String weekDay) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteAssignmentDao.Properties.CircleType.like("%" + weekDay + "%"), RouteAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<RouteAssignment> getWeekDayList(RouteAssignmentDao dao, String groupId, String weekDay) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteAssignmentDao.Properties.GroupId.eq(groupId), RouteAssignmentDao.Properties.CircleType.like("%" + weekDay + "%"), RouteAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<RouteAssignment> getWeekDayList(RouteAssignmentDao dao, String groupId, String userId, String weekDay) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteAssignmentDao.Properties.GroupId.eq(groupId), RouteAssignmentDao.Properties.UserId.notEq(userId), RouteAssignmentDao.Properties.CircleType.like("%" + weekDay + "%"), RouteAssignmentDao.Properties.Status.notEq("delete"))).list();
    }

    // 删除任务
    public static void delete(RouteAssignmentDao dao, RouteAssignment routeAssignment) {
        dao.delete(routeAssignment);
    }
}
