package com.ctfww.module.assignment.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.entity.RouteTodayAssignment;
import com.ctfww.module.assignment.entity.RouteTodayAssignmentDao;

import java.util.List;

public class RouteTodayAssignmentDBHelper {
    private final static String TAG = "RouteTodayAssignmentDBHelper";

    public static boolean add(RouteTodayAssignmentDao dao, RouteTodayAssignment info) {
        try {
            dao.insert(info);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    public static void update(RouteTodayAssignmentDao dao, RouteTodayAssignment info) {
        try {
            dao.update(info);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    public static RouteTodayAssignment get(RouteTodayAssignmentDao dao, String groupId, String routeId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteTodayAssignmentDao.Properties.GroupId.eq(groupId), RouteTodayAssignmentDao.Properties.RouteId.eq(routeId), RouteTodayAssignmentDao.Properties.UserId.eq(userId))).unique();
    }

    public static List<RouteTodayAssignment> getList(RouteTodayAssignmentDao dao, String groupId) {
        return dao.queryBuilder().where(RouteTodayAssignmentDao.Properties.GroupId.eq(groupId)).list();
    }

    public static List<RouteTodayAssignment> getList(RouteTodayAssignmentDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteTodayAssignmentDao.Properties.GroupId.eq(groupId), RouteTodayAssignmentDao.Properties.UserId.notEq(userId))).list();
    }

    public static void delete(RouteTodayAssignmentDao dao, RouteTodayAssignment info) {
        dao.delete(info);
    }

    public static void clear(RouteTodayAssignmentDao dao) {
        dao.deleteAll();
    }
}
