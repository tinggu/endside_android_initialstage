package com.ctfww.module.assignment.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.entity.DeskTodayAssignment;
import com.ctfww.module.assignment.entity.DeskTodayAssignmentDao;

import java.util.List;

public class DeskTodayAssignmentDBHelper {
    private final static String TAG = "DeskTodayAssignmentDBHelper";

    public static boolean add(DeskTodayAssignmentDao dao, DeskTodayAssignment info) {
        try {
            dao.insert(info);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    public static void update(DeskTodayAssignmentDao dao, DeskTodayAssignment info) {
        try {
            dao.update(info);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    public static DeskTodayAssignment get(DeskTodayAssignmentDao dao, String groupId, int deskId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(DeskTodayAssignmentDao.Properties.GroupId.eq(groupId), DeskTodayAssignmentDao.Properties.DeskId.eq(deskId), DeskTodayAssignmentDao.Properties.UserId.eq(userId))).unique();
    }

    public static List<DeskTodayAssignment> getList(DeskTodayAssignmentDao dao, String groupId) {
        return dao.queryBuilder().where(DeskTodayAssignmentDao.Properties.GroupId.eq(groupId)).list();
    }

    public static List<DeskTodayAssignment> getList(DeskTodayAssignmentDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(DeskTodayAssignmentDao.Properties.GroupId.eq(groupId), DeskTodayAssignmentDao.Properties.UserId.notEq(userId))).list();
    }

    public static void delete(DeskTodayAssignmentDao dao, DeskTodayAssignment info) {
        dao.delete(info);
    }

    public static void clear(DeskTodayAssignmentDao dao) {
        dao.deleteAll();
    }
}
