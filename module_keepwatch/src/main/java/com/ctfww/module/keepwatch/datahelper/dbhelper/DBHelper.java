package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.keepwatch.entity.DaoMaster;
import com.ctfww.module.keepwatch.entity.DaoSession;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignmentDao;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchDeskDao;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrendsDao;
import com.ctfww.module.keepwatch.entity.KeepWatchRanking;
import com.ctfww.module.keepwatch.entity.KeepWatchRankingDao;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDeskDao;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummaryDao;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfoDao;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDeskDao;

import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private KeepWatchDeskDao keepWatchDeskDao;
    private KeepWatchSigninInfoDao keepWatchSigninInfoDao;
    private KeepWatchRouteSummaryDao keepWatchRouteSummaryDao;
    private KeepWatchRouteDeskDao keepWatchRouteDeskDao;
    private KeepWatchAssignmentDao keepWatchAssignmentDao;
    private KeepWatchPersonTrendsDao keepWatchPersonTrendsDao;
    private KeepWatchRankingDao keepWatchRankingDao;
    private KeepWatchStatisticsByDeskDao keepWatchStatisticsByDeskDao;

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

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "keepwatch");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        keepWatchDeskDao = daoSession.getKeepWatchDeskDao();
        keepWatchSigninInfoDao = daoSession.getKeepWatchSigninInfoDao();
        keepWatchRouteSummaryDao = daoSession.getKeepWatchRouteSummaryDao();
        keepWatchRouteDeskDao = daoSession.getKeepWatchRouteDeskDao();
        keepWatchAssignmentDao = daoSession.getKeepWatchAssignmentDao();
        keepWatchPersonTrendsDao = daoSession.getKeepWatchPersonTrendsDao();
        keepWatchRankingDao = daoSession.getKeepWatchRankingDao();
        keepWatchStatisticsByDeskDao = daoSession.getKeepWatchStatisticsByDeskDao();
    }

    // 1. 与签到点有关

    // 用于app增加签到点
    public boolean addKeepWatchDesk(KeepWatchDesk keepWatchDesk) {
        return KeepWatchDeskDBHelper.add(keepWatchDeskDao, keepWatchDesk);
    }

    // 用于app对签到点信息的修改（包括删除：将status置为“delete”状态）
    public void updateKeepWatchDesk(KeepWatchDesk keepWatchDesk) {
        KeepWatchDeskDBHelper.update(keepWatchDeskDao, keepWatchDesk);
    }

    // 获取需要同步上云的签到点
    public List<KeepWatchDesk> getNoSynKeepWatchDeskList() {
        return KeepWatchDeskDBHelper.getNoSynList(keepWatchDeskDao);
    }

    // 用于app查看某个签到点详细信息
    // 用于app确实是否存在该编号的签到点
    public KeepWatchDesk getKeepWatchDesk(String groupId, int deskId) {
        return KeepWatchDeskDBHelper.get(keepWatchDeskDao, groupId, deskId);
    }

    // 获取所有签到点（签到点列表，但不包括是删除状态的）
    public List<KeepWatchDesk> getKeepWatchDeskList(String groupId) {
        return KeepWatchDeskDBHelper.getList(keepWatchDeskDao, groupId);
    }

    // 删除签到点
    public void deleteKeepWatchDesk(KeepWatchDesk desk) {
        KeepWatchDeskDBHelper.delete(keepWatchDeskDao, desk);
    }


    // 2. 与签到线路相关

    // 增加线路概要信息
    public void addKeepWatchRouteSummary(KeepWatchRouteSummary routeSummary) {
        KeepWatchRouteSummaryDBHelper.add(keepWatchRouteSummaryDao, routeSummary);
    }

    // 修改线路概要信息
    public void updateKeepWatchRouteSummary(KeepWatchRouteSummary routeSummary) {
        KeepWatchRouteSummaryDBHelper.update(keepWatchRouteSummaryDao, routeSummary);
    }

    // 获取路线概要信息
    public KeepWatchRouteSummary getKeepWatchRouteSummary(String routeId) {
        return KeepWatchRouteSummaryDBHelper.get(keepWatchRouteSummaryDao, routeId);
    }

    // 获取路线概要信息列表
    public List<KeepWatchRouteSummary> getKeepWatchRouteSummaryList(String groupId) {
        return KeepWatchRouteSummaryDBHelper.getList(keepWatchRouteSummaryDao, groupId);
    }

    // 获取未同步的线路概要信息
    public List<KeepWatchRouteSummary> getNoSynKeepWatchRouteSummaryList() {
        return KeepWatchRouteSummaryDBHelper.getNoSynList(keepWatchRouteSummaryDao);
    }

    // 3. 与签到路线点相关

    // 增加线路签到点信息
    public void addKeepWatchRouteDesk(KeepWatchRouteDesk routeDesk) {
        KeepWatchRouteDeskDBHelper.add(keepWatchRouteDeskDao, routeDesk);
    }

    // 修改线路签到点信息
    public void updateKeepWatchRouteDesk(KeepWatchRouteDesk routeDesk) {
        KeepWatchRouteDeskDBHelper.update(keepWatchRouteDeskDao, routeDesk);
    }

    // 获得某线路的签到点
    public KeepWatchRouteDesk getKeepWatchRouteDesk(String routeId, int deskId) {
        return KeepWatchRouteDeskDBHelper.get(keepWatchRouteDeskDao, routeId, deskId);
    }

    // 获得某线路的签到点
    public List<KeepWatchRouteDesk> getKeepWatchRouteDeskInOneRoute(String routeId) {
        return KeepWatchRouteDeskDBHelper.getInOneRoute(keepWatchRouteDeskDao, routeId);
    }

    // 获得未同步的路线签到点
    public List<KeepWatchRouteDesk> getNoSynKeepWatchRouteDeskList() {
        return KeepWatchRouteDeskDBHelper.getNoSynList(keepWatchRouteDeskDao);
    }

    // 删除路线
    public void newKeepWatchRoute(String routeId) {
        KeepWatchRouteSummary routeSummary = KeepWatchRouteSummaryDBHelper.get(keepWatchRouteSummaryDao, routeId);
        if (routeSummary == null) {
            return;
        }

        boolean isDirect = "0".equals(routeSummary.getSynTag()) ? true : false;
        KeepWatchRouteSummaryDBHelper.newRoute(keepWatchRouteSummaryDao, routeId);
        KeepWatchRouteDeskDBHelper.newRoute(keepWatchRouteDeskDao, routeId);
    }

    // 删除路线
    public void deleteKeepWatchRoute(String routeId) {
        KeepWatchRouteSummary routeSummary = KeepWatchRouteSummaryDBHelper.get(keepWatchRouteSummaryDao, routeId);
        if (routeSummary == null) {
            return;
        }

        boolean isDirect = "0".equals(routeSummary.getSynTag()) ? true : false;
        KeepWatchRouteSummaryDBHelper.deleteRoute(keepWatchRouteSummaryDao, routeId, isDirect);
        KeepWatchRouteDeskDBHelper.deleteRoute(keepWatchRouteDeskDao, routeId, isDirect);
    }

    // 10. 与签到有关

    // 存储签到信息，用于签到
    public boolean addKeepWatchSignin(KeepWatchSigninInfo keepWatchSigninInfo) {
        return KeepWatchSigninDBHelper.add(keepWatchSigninInfoDao, keepWatchSigninInfo);
    }

    // 获取未同步上云的签到，用于签到信息的同步上云
    public List<KeepWatchSigninInfo> getNoSynKeepWatchSignin() {
        return KeepWatchSigninDBHelper.getNoSyn(keepWatchSigninInfoDao);
    }

    // 用于同步上云后更改同步状态
    public void updateKeepWatchSignin(KeepWatchSigninInfo signin) {
        KeepWatchSigninDBHelper.update(keepWatchSigninInfoDao, signin);
    }

    // 20. 与任务有关
    // 用于app增加任务
    public boolean addKeepWatchAssignment(KeepWatchAssignment assignment) {
        return KeepWatchAssignmentDBHelper.add(keepWatchAssignmentDao, assignment);
    }

    // 用于app对签到任务的修改（包括删除：将status置为“delete”状态）
    public void updateKeepWatchAssignment(KeepWatchAssignment assignment) {
        KeepWatchAssignmentDBHelper.update(keepWatchAssignmentDao, assignment);
    }

    // 获得某任务信息
    public KeepWatchAssignment getKeepWatchAssignment(String assignmentId) {
        return KeepWatchAssignmentDBHelper.get(keepWatchAssignmentDao, assignmentId);
    }

    // 获得任务列表（不包括删除：将status置为“delete”状态）
    public List<KeepWatchAssignment> getKeepWatchAssignmentList(String groupId, String userId) {
        return KeepWatchAssignmentDBHelper.getList(keepWatchAssignmentDao, groupId, userId);
    }

    // 获得需要同步的任务列表
    public List<KeepWatchAssignment> getNoSynKeepWatchAssignmentList() {
        return KeepWatchAssignmentDBHelper.getNoSynList(keepWatchAssignmentDao);
    }

    // 删除任务
    public void deleteKeepWatchAssignment(KeepWatchAssignment assignment) {
        KeepWatchAssignmentDBHelper.delete(keepWatchAssignmentDao, assignment);
    }

    // 30. 与每日统计相关

    // 用于app增加动态
    public boolean addKeepWatchPersonTrends(KeepWatchPersonTrends personTrends) {
        return KeepWatchPersonTrendsDBHelper.add(keepWatchPersonTrendsDao, personTrends);
    }

    // 用于app对动态的修改
    public void updateKeepWatchPersonTrends(KeepWatchPersonTrends personTrends) {
        KeepWatchPersonTrendsDBHelper.update(keepWatchPersonTrendsDao, personTrends);
    }

    // 获得动态列表
    public List<KeepWatchPersonTrends> getKeepWatchPersonTrends(String groupId) {
        return KeepWatchPersonTrendsDBHelper.getList(keepWatchPersonTrendsDao, groupId);
    }

    // 删除所有动态
    public void deleteKeepWatchPersonTrends() {
        KeepWatchPersonTrendsDBHelper.delete(keepWatchPersonTrendsDao);
    }

    // 用于app增加排行
    public boolean addKeepWatchRanking(KeepWatchRanking ranking) {
        return KeepWatchRankingDBHelper.add(keepWatchRankingDao, ranking);
    }

    // 用于app对动态的修改
    public void updateKeepWatchRanking(KeepWatchRanking ranking) {
        KeepWatchRankingDBHelper.update(keepWatchRankingDao, ranking);
    }

    // 获得排行列表
    public List<KeepWatchRanking> getKeepWatchRanking(String groupId) {
        return KeepWatchRankingDBHelper.getList(keepWatchRankingDao, groupId);
    }

    // 删除所有排行
    public void deleteKeepWatchRanking() {
        KeepWatchRankingDBHelper.delete(keepWatchRankingDao);
    }

    // 用于app增加签到点的签到统计
    public boolean addKeepWatchStatisticsByDesk(KeepWatchStatisticsByDesk statisticsByDesk) {
        return KeepWatchAssignmentFinishStatusDBHelper.add(keepWatchStatisticsByDeskDao, statisticsByDesk);
    }

    // 获得签到点的签到统计列表
    public List<KeepWatchStatisticsByDesk> getKeepWatchStatisticsByDeskList(String groupId) {
        return KeepWatchAssignmentFinishStatusDBHelper.getList(keepWatchStatisticsByDeskDao, groupId);
    }

    // 删除所有签到点签到统计
    public void deleteKeepWatchStatisticsByDesk() {
        KeepWatchAssignmentFinishStatusDBHelper.delete(keepWatchStatisticsByDeskDao);
    }
}
