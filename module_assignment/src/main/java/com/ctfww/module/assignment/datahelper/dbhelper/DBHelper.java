package com.ctfww.module.assignment.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.assignment.entity.DaoMaster;
import com.ctfww.module.assignment.entity.DaoSession;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.DeskAssignmentDao;
import com.ctfww.module.assignment.entity.DeskTodayAssignment;
import com.ctfww.module.assignment.entity.DeskTodayAssignmentDao;
import com.ctfww.module.assignment.entity.RouteAssignment;
import com.ctfww.module.assignment.entity.RouteAssignmentDao;
import com.ctfww.module.assignment.entity.RouteTodayAssignment;
import com.ctfww.module.assignment.entity.RouteTodayAssignmentDao;

import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private DeskAssignmentDao deskAssignmentDao;
    private RouteAssignmentDao routeAssignmentDao;
    private DeskTodayAssignmentDao deskTodayAssignmentDao;
    private RouteTodayAssignmentDao routeTodayAssignmentDao;

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
        deskTodayAssignmentDao = daoSession.getDeskTodayAssignmentDao();
        routeTodayAssignmentDao = daoSession.getRouteTodayAssignmentDao();
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

    public List<DeskAssignment> getWeekDayDeskAssignmentList(String weekDay) {
        return DeskAssignmentDBHelper.getWeekDayList(deskAssignmentDao, weekDay);
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

    public List<RouteAssignment> getWeekDayRouteAssignmentList(String weekDay) {
        return RouteAssignmentDBHelper.getWeekDayList(routeAssignmentDao, weekDay);
    }

    public List<RouteAssignment> getWeekDayRouteAssignmentList(String groupId, String weekDay) {
        return RouteAssignmentDBHelper.getWeekDayList(routeAssignmentDao, groupId, weekDay);
    }

    public List<RouteAssignment> getWeekDayRouteAssignmentList(String groupId, String userId, String weekDay) {
        return RouteAssignmentDBHelper.getWeekDayList(routeAssignmentDao, groupId, userId, weekDay);
    }

    // 3. 与任务有关

    public boolean addDeskTodayAssignment(DeskTodayAssignment info) {
        return DeskTodayAssignmentDBHelper.add(deskTodayAssignmentDao, info);
    }

    public void updateDeskTodayAssignment(DeskTodayAssignment info) {
        DeskTodayAssignmentDBHelper.update(deskTodayAssignmentDao, info);
    }

    public void updateDeskTodayAssignment(DeskAssignment info) {
        DeskTodayAssignment deskTodayAssignment = getDeskTodayAssignment(info.getGroupId(), info.getDeskId(), info.getUserId());
        String weekDay = GlobeFun.getTodayWeekDayStr();
        if ("delete".equals(info.getStatus()) || -1 == info.getCircleType().indexOf(weekDay)) {
            if (deskTodayAssignment != null) {
                deleteDeskTodayAssignment(deskTodayAssignment);
                return;
            }
        }

        if (deskTodayAssignment == null) {
            deskTodayAssignment = new DeskTodayAssignment();
            deskTodayAssignment.setAssignmentId(info.getGroupId() + info.getId() + MyDateTimeUtils.getTodayStartTime());
            deskTodayAssignment.setDeskId(info.getDeskId());
            deskTodayAssignment.setGroupId(info.getGroupId());
            deskTodayAssignment.setUserId(info.getUserId());
            deskTodayAssignment.setFrequency(info.getFrequency());
            deskTodayAssignment.setStartTime(info.getStartTime());
            deskTodayAssignment.setEndTime(info.getEndTime());

            DBHelper.getInstance().addDeskTodayAssignment(deskTodayAssignment);
        }
        else {
            deskTodayAssignment.setFrequency(info.getFrequency());
            deskTodayAssignment.setStartTime(info.getStartTime());
            deskTodayAssignment.setEndTime(info.getEndTime());

            DBHelper.getInstance().updateDeskTodayAssignment(deskTodayAssignment);
        }
    }

    public DeskTodayAssignment getDeskTodayAssignment(String groupId, int deskId, String userId) {
        return DeskTodayAssignmentDBHelper.get(deskTodayAssignmentDao, groupId, deskId, userId);
    }

    public List<DeskTodayAssignment> getDeskTodayAssignmentList(String groupId) {
        return DeskTodayAssignmentDBHelper.getList(deskTodayAssignmentDao, groupId);
    }

    public List<DeskTodayAssignment> getDeskTodayAssignmentList(String groupId, String userId) {
        return DeskTodayAssignmentDBHelper.getList(deskTodayAssignmentDao, groupId, userId);
    }

    public void deleteDeskTodayAssignment(DeskTodayAssignment deskTodayAssignment) {
        DeskTodayAssignmentDBHelper.delete(deskTodayAssignmentDao, deskTodayAssignment);
    }

    public void clearDeskTodayAssignment() {
        DeskTodayAssignmentDBHelper.clear(deskTodayAssignmentDao);
    }

    // 4. 与任务有关

    public boolean addRouteTodayAssignment(RouteTodayAssignment info) {
        return RouteTodayAssignmentDBHelper.add(routeTodayAssignmentDao, info);
    }

    public void updateRouteTodayAssignment(RouteTodayAssignment info) {
        RouteTodayAssignmentDBHelper.update(routeTodayAssignmentDao, info);
    }

    public void updateRouteTodayAssignment(RouteAssignment info) {
        RouteTodayAssignment routeTodayAssignment = getRouteTodayAssignment(info.getGroupId(), info.getRouteId(), info.getUserId());
        String weekDay = GlobeFun.getTodayWeekDayStr();
        if ("delete".equals(info.getStatus()) || -1 == info.getCircleType().indexOf(weekDay)) {
            if (routeTodayAssignment != null) {
                deleteRouteTodayAssignment(routeTodayAssignment);
                return;
            }
        }

        if (routeTodayAssignment == null) {
            routeTodayAssignment = new RouteTodayAssignment();
            routeTodayAssignment.setAssignmentId(info.getRouteId() + MyDateTimeUtils.getTodayStartTime());
            routeTodayAssignment.setGroupId(info.getGroupId());
            routeTodayAssignment.setUserId(info.getUserId());
            routeTodayAssignment.setFrequency(info.getFrequency());
            routeTodayAssignment.setStartTime(info.getStartTime());
            routeTodayAssignment.setEndTime(info.getEndTime());

            DBHelper.getInstance().addRouteTodayAssignment(routeTodayAssignment);
        }
        else {
            routeTodayAssignment.setFrequency(info.getFrequency());
            routeTodayAssignment.setStartTime(info.getStartTime());
            routeTodayAssignment.setEndTime(info.getEndTime());

            DBHelper.getInstance().updateRouteTodayAssignment(routeTodayAssignment);
        }
    }

    public RouteTodayAssignment getRouteTodayAssignment(String groupId, String routeId, String userId) {
        return RouteTodayAssignmentDBHelper.get(routeTodayAssignmentDao, groupId, routeId, userId);
    }

    public List<RouteTodayAssignment> getRouteTodayAssignmentList(String groupId) {
        return RouteTodayAssignmentDBHelper.getList(routeTodayAssignmentDao, groupId);
    }

    public List<RouteTodayAssignment> getRouteTodayAssignmentList(String groupId, String userId) {
        return RouteTodayAssignmentDBHelper.getList(routeTodayAssignmentDao, groupId, userId);
    }

    public void deleteRouteTodayAssignment(RouteTodayAssignment routeTodayAssignment) {
        routeTodayAssignmentDao.delete(routeTodayAssignment);
    }

    public void clearRouteTodayAssignment() {
        RouteTodayAssignmentDBHelper.clear(routeTodayAssignmentDao);
    }
}
