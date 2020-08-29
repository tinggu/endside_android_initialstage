package com.ctfww.module.desk.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.desk.entity.DaoMaster;
import com.ctfww.module.desk.entity.DaoSession;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.DeskInfoDao;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteDeskDao;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.desk.entity.RouteSummaryDao;

import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private DeskInfoDao deskInfoDao;
    private RouteSummaryDao routeSummaryDao;
    private RouteDeskDao routeDeskDao;

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

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "desk");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        deskInfoDao = daoSession.getDeskInfoDao();
        routeSummaryDao = daoSession.getRouteSummaryDao();
        routeDeskDao = daoSession.getRouteDeskDao();
    }

    // 1. 与签到点有关

    // 用于app增加签到点
    public boolean addDesk(DeskInfo deskInfo) {
        return DeskDBHelper.add(deskInfoDao, deskInfo);
    }

    // 用于app对签到点信息的修改（包括删除：将status置为“delete”状态）
    public void updateDesk(DeskInfo deskInfo) {
        DeskDBHelper.update(deskInfoDao, deskInfo);
    }

    // 获取需要同步上云的签到点
    public List<DeskInfo> getNoSynDeskList() {
        return DeskDBHelper.getNoSynList(deskInfoDao);
    }

    // 用于app查看某个签到点详细信息
    // 用于app确实是否存在该编号的签到点
    public DeskInfo getDesk(String groupId, int deskId) {
        return DeskDBHelper.get(deskInfoDao, groupId, deskId);
    }

    // 获取所有签到点（签到点列表，但不包括是删除状态的）
    public List<DeskInfo> getDeskList(String groupId) {
        return DeskDBHelper.getList(deskInfoDao, groupId);
    }

    // 删除签到点
    public void deleteDesk(DeskInfo desk) {
        DeskDBHelper.delete(deskInfoDao, desk);
    }


    // 2. 与签到线路相关

    // 增加线路概要信息
    public void addRouteSummary(RouteSummary routeSummary) {
        RouteSummaryDBHelper.add(routeSummaryDao, routeSummary);
    }

    // 修改线路概要信息
    public void updateRouteSummary(RouteSummary routeSummary) {
        RouteSummaryDBHelper.update(routeSummaryDao, routeSummary);
    }

    // 获取路线概要信息
    public RouteSummary getRouteSummary(String routeId) {
        return RouteSummaryDBHelper.get(routeSummaryDao, routeId);
    }

    // 获取路线概要信息列表
    public List<RouteSummary> getRouteSummaryList(String groupId) {
        return RouteSummaryDBHelper.getList(routeSummaryDao, groupId);
    }

    // 获取未同步的线路概要信息
    public List<RouteSummary> getNoSynRouteSummaryList() {
        return RouteSummaryDBHelper.getNoSynList(routeSummaryDao);
    }

    // 3. 与签到路线点相关

    // 增加线路签到点信息
    public void addRouteDesk(RouteDesk routeDesk) {
        RouteDeskDBHelper.add(routeDeskDao, routeDesk);
    }

    // 修改线路签到点信息
    public void updateRouteDesk(RouteDesk routeDesk) {
        RouteDeskDBHelper.update(routeDeskDao, routeDesk);
    }

    // 获得某线路的签到点
    public RouteDesk getRouteDesk(String routeId, int deskId) {
        return RouteDeskDBHelper.get(routeDeskDao, routeId, deskId);
    }

    // 获得某线路的签到点
    public List<RouteDesk> getRouteDeskInOneRoute(String routeId) {
        return RouteDeskDBHelper.getInOneRoute(routeDeskDao, routeId);
    }

    // 获得未同步的路线签到点
    public List<RouteDesk> getNoSynRouteDeskList() {
        return RouteDeskDBHelper.getNoSynList(routeDeskDao);
    }

    // 确认新建路线
    public void newRoute(String routeId) {
        RouteSummary routeSummary = RouteSummaryDBHelper.get(routeSummaryDao, routeId);
        if (routeSummary == null) {
            return;
        }

        RouteSummaryDBHelper.newRoute(routeSummaryDao, routeId);
        RouteDeskDBHelper.newRoute(routeDeskDao, routeId);
    }

    // 删除路线
    public void deleteKeepWatchRoute(String routeId) {
        RouteSummary routeSummary = RouteSummaryDBHelper.get(routeSummaryDao, routeId);
        if (routeSummary == null) {
            return;
        }

        boolean isDirect = "0".equals(routeSummary.getSynTag()) ? true : false;
        RouteSummaryDBHelper.deleteRoute(routeSummaryDao, routeId, isDirect);
        RouteDeskDBHelper.deleteRoute(routeDeskDao, routeId, isDirect);
    }
}
