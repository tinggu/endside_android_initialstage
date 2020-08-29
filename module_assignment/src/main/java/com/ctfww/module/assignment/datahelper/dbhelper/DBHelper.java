package com.ctfww.module.assignment.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.assignment.entity.AssignmentInfoDao;
import com.ctfww.module.assignment.entity.DaoMaster;
import com.ctfww.module.assignment.entity.DaoSession;

import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private AssignmentInfoDao assignmentInfoDao;

    private static class Inner {
        private static final DBHelper INSTANCE = new DBHelper();
    }

    public static DBHelper getInstance() {
        return Inner.INSTANCE;
    }

    public void init(Context ctx) {
        if (ctx == null) {
            return;
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "assignment");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        assignmentInfoDao = daoSession.getAssignmentInfoDao();
    }

    // 1. 与任务有关

    // 用于app增加任务
    public boolean addAssignment(AssignmentInfo assignmentInfo) {
        return AssignmentDBHelper.add(assignmentInfoDao, assignmentInfo);
    }

    // 用于app对任务信息的修改（包括删除：将status置为“delete”状态）
    public void updateAssignment(AssignmentInfo assignmentInfo) {
        AssignmentDBHelper.update(assignmentInfoDao, assignmentInfo);
    }

    // 获取需要同步上云的任务
    public List<AssignmentInfo> getNoSynAssignmentList() {
        return AssignmentDBHelper.getNoSynList(assignmentInfoDao);
    }

    // 用于app查看某个任务详细信息
    // 用于app确实是否存在该任务
    public AssignmentInfo getAssignment(String groupId, int deskId, String routeId, String userId) {
        return AssignmentDBHelper.get(assignmentInfoDao, groupId, deskId, routeId, userId);
    }

    public AssignmentInfo getAssignment(String groupId, int deskId, String routeId, String userId, String weekDay) {
        return AssignmentDBHelper.get(assignmentInfoDao, groupId, deskId, routeId, userId, weekDay);
    }

    // 获取所有任务（任务列表，但不包括是删除状态的）
    public List<AssignmentInfo> getAssignmentList(String groupId) {
        return AssignmentDBHelper.getList(assignmentInfoDao, groupId);
    }

    public List<AssignmentInfo> getAssignmentList(String groupId, String userId) {
        return AssignmentDBHelper.getList(assignmentInfoDao, groupId, userId);
    }

    public List<AssignmentInfo> getWeekDayAssignmentList(String groupId, String weekDay) {
        return AssignmentDBHelper.getWeekDayList(assignmentInfoDao, groupId, weekDay);
    }

    public List<AssignmentInfo> getWeekDayAssignmentList(String groupId, String userId, String weekDay) {
        return AssignmentDBHelper.getWeekDayList(assignmentInfoDao, groupId, userId, weekDay);
    }
}
