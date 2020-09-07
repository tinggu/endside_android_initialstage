package com.ctfww.module.assignment.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.assignment.entity.DaoMaster;
import com.ctfww.module.assignment.entity.DaoSession;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.DeskAssignmentDao;
import com.ctfww.module.assignment.entity.RouteAssignment;
import com.ctfww.module.assignment.entity.RouteAssignmentDao;

import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private DeskAssignmentDao deskAssignmentDao;
    private RouteAssignmentDao routeAssignmentDao;

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

        deskAssignmentDao = daoSession.getDeskAssignmentDao();
        routeAssignmentDao = daoSession.getRouteAssignmentDao();
    }

    // 1. 与任务有关

    // 用于app增加任务
    public boolean addDeskAssignment(DeskAssignment deskAssignment) {
        return DeskAssignmentDBHelper.add(deskAssignmentDao, deskAssignment);
    }

    // 用于app对任务信息的修改（包括删除：将status置为“delete”状态）
    public void updateDeskAssignment(DeskAssignment deskAssignment) {
        DeskAssignmentDBHelper.update(deskAssignmentDao, deskAssignment);
    }

    // 获取需要同步上云的任务
    public List<DeskAssignment> getNoSynDeskAssignmentList() {
        return DeskAssignmentDBHelper.getNoSynList(deskAssignmentDao);
    }

    // 用于app查看某个任务详细信息
    // 用于app确实是否存在该任务
    public DeskAssignment getDeskAssignment(String groupId, int deskId, String userId) {
        return DeskAssignmentDBHelper.get(deskAssignmentDao, groupId, deskId, userId);
    }

    public DeskAssignment getDeskAssignment(String groupId, int deskId, String routeId, String userId, String weekDay) {
        return DeskAssignmentDBHelper.get(deskAssignmentDao, groupId, deskId, routeId, userId, weekDay);
    }

    // 获取所有任务（任务列表，但不包括是删除状态的）
    public List<DeskAssignment> getDeskAssignmentList(String groupId) {
        return DeskAssignmentDBHelper.getList(deskAssignmentDao, groupId);
    }

    public List<DeskAssignment> getDeskAssignmentList(String groupId, String userId) {
        return DeskAssignmentDBHelper.getList(deskAssignmentDao, groupId, userId);
    }

    public List<DeskAssignment> getWeekDayDeskAssignmentList(String groupId, String weekDay) {
        return DeskAssignmentDBHelper.getWeekDayList(deskAssignmentDao, groupId, weekDay);
    }

    public List<DeskAssignment> getWeekDayDeskAssignmentList(String groupId, String userId, String weekDay) {
        return DeskAssignmentDBHelper.getWeekDayList(deskAssignmentDao, groupId, userId, weekDay);
    }

    // 2. 与任务有关

    // 用于app增加任务
    public boolean addRouteAssignment(RouteAssignment routeAssignment) {
        return RouteAssignmentDBHelper.add(routeAssignmentDao, routeAssignment);
    }

    // 用于app对任务信息的修改（包括删除：将status置为“delete”状态）
    public void updateRouteAssignment(RouteAssignment routeAssignment) {
        RouteAssignmentDBHelper.update(routeAssignmentDao, routeAssignment);
    }

    // 获取需要同步上云的任务
    public List<RouteAssignment> getNoSynRouteAssignmentList() {
        return RouteAssignmentDBHelper.getNoSynList(routeAssignmentDao);
    }

    // 用于app查看某个任务详细信息
    // 用于app确实是否存在该任务
    public RouteAssignment getRouteAssignment(String groupId, String routeId, String userId) {
        return RouteAssignmentDBHelper.get(routeAssignmentDao, groupId, routeId, userId);
    }

    public RouteAssignment getRouteAssignment(String groupId, String routeId, String userId, String weekDay) {
        return RouteAssignmentDBHelper.get(routeAssignmentDao, groupId, routeId, userId, weekDay);
    }

    // 获取所有任务（任务列表，但不包括是删除状态的）
    public List<RouteAssignment> getRouteAssignmentList(String groupId) {
        return RouteAssignmentDBHelper.getList(routeAssignmentDao, groupId);
    }

    public List<RouteAssignment> getRouteAssignmentList(String groupId, String userId) {
        return RouteAssignmentDBHelper.getList(routeAssignmentDao, groupId, userId);
    }

    public List<RouteAssignment> getWeekDayRouteAssignmentList(String groupId, String weekDay) {
        return RouteAssignmentDBHelper.getWeekDayList(routeAssignmentDao, groupId, weekDay);
    }

    public List<RouteAssignment> getWeekDayRouteAssignmentList(String groupId, String userId, String weekDay) {
        return RouteAssignmentDBHelper.getWeekDayList(routeAssignmentDao, groupId, userId, weekDay);
    }
}
