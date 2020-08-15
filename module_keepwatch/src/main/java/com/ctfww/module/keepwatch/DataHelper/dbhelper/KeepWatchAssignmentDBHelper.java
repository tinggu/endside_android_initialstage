package com.ctfww.module.keepwatch.DataHelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignmentDao;

import java.util.List;

public class KeepWatchAssignmentDBHelper {
    private final static String TAG = "KeepWatchAssignmentDBHelper";

    // 用于app增加任务
    public static boolean add(KeepWatchAssignmentDao dao, KeepWatchAssignment assignment) {
        try {
            assignment.combineAssignmentId();
            dao.insert(assignment);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    // 用于app对签到任务的修改（包括删除：将status置为“delete”状态）
    public static void update(KeepWatchAssignmentDao dao, KeepWatchAssignment assignment) {
        try {
            dao.update(assignment);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取需要同步上云的任务
    public static List<KeepWatchAssignment> getNoSynList(KeepWatchAssignmentDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(KeepWatchAssignmentDao.Properties.SynTag.eq("new"), KeepWatchAssignmentDao.Properties.SynTag.eq("modify"))).list();
    }

    // 用于app查看某个任务详细信息
    // 用于app确实是否存在该编号的任务
    public static KeepWatchAssignment get(KeepWatchAssignmentDao dao, String assignmentId) {
        return dao.queryBuilder().where(KeepWatchAssignmentDao.Properties.DeskId.eq(assignmentId)).unique();
    }

    // 获取所有任务（任务列表，但不包括是删除状态的）
    public static List<KeepWatchAssignment> getList(KeepWatchAssignmentDao dao, String groupId, String userId) {
        if (TextUtils.isEmpty(userId)) {
            return dao.queryBuilder().where(dao.queryBuilder().and(KeepWatchAssignmentDao.Properties.GroupId.eq(groupId), KeepWatchAssignmentDao.Properties.Status.notEq("delete"))).list();
        }
        else {
            return dao.queryBuilder().where(dao.queryBuilder().and(KeepWatchAssignmentDao.Properties.GroupId.eq(groupId), KeepWatchAssignmentDao.Properties.Status.notEq("delete"), KeepWatchAssignmentDao.Properties.UserId.eq(userId))).list();
        }
    }

    // 删除任务
    public static void delete(KeepWatchAssignmentDao dao, KeepWatchAssignment assignment) {
        assignment.setStatus("delete");
        assignment.setTimeStamp(System.currentTimeMillis());
        dao.update(assignment);
    }
}
