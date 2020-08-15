package com.ctfww.module.keepwatch.DataHelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.keepwatch.entity.KeepWatchRouteDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDeskDao;

import java.util.List;

public class KeepWatchRouteDeskDBHelper {
    private final static String TAG = "KeepWatchRouteDeskDBHelper";

    // 增加线路签到点信息
    public static void add(KeepWatchRouteDeskDao dao, KeepWatchRouteDesk routeDesk) {
        try {
            dao.insert(routeDesk);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    // 修改线路签到点信息
    public static void update(KeepWatchRouteDeskDao dao, KeepWatchRouteDesk routeDesk) {
        try {
            dao.update(routeDesk);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    // 获得某线路的签到点
    public static List<KeepWatchRouteDesk> getInOneRoute(KeepWatchRouteDeskDao dao, String routeId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeepWatchRouteDeskDao.Properties.RouteId.eq(routeId), dao.queryBuilder().or(KeepWatchRouteDeskDao.Properties.SynTag.eq("new"), KeepWatchRouteDeskDao.Properties.SynTag.eq("modify"), KeepWatchRouteDeskDao.Properties.SynTag.eq("cloud")))).orderAsc(KeepWatchRouteDeskDao.Properties.DeskId).list();
    }

    // 获得未同步的路线签到点
    public static List<KeepWatchRouteDesk> getNoSynList(KeepWatchRouteDeskDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(KeepWatchRouteDeskDao.Properties.SynTag.eq("new"), KeepWatchRouteDeskDao.Properties.SynTag.eq("modify"))).list();
    }

    // 获得路线签到点
    public static KeepWatchRouteDesk get(KeepWatchRouteDeskDao dao, String routeId, int deskId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeepWatchRouteDeskDao.Properties.RouteId.eq(routeId), KeepWatchRouteDeskDao.Properties.DeskId.eq(deskId))).unique();
    }

    // 获得某路线的签到点
    public static List<KeepWatchRouteDesk> getList(KeepWatchRouteDeskDao dao, String routeId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeepWatchRouteDeskDao.Properties.RouteId.eq(routeId), KeepWatchRouteDeskDao.Properties.Status.notEq("delete"))).list();
    }

    // 删除某条路线签到点
    public static void newRoute(KeepWatchRouteDeskDao dao, String routeId) {
        List<KeepWatchRouteDesk> routeDeskList = getList(dao, routeId);
        if (routeDeskList.isEmpty()) {
            return;
        }

        for (int i = 0; i < routeDeskList.size(); ++i) {
            KeepWatchRouteDesk routeDesk = routeDeskList.get(i);
            routeDesk.setStatus("new");
            dao.update(routeDesk);
        }
    }

    // 删除某条路线签到点
    public static void deleteRoute(KeepWatchRouteDeskDao dao, String routeId, boolean isDirect) {
        List<KeepWatchRouteDesk> routeDeskList = getList(dao, routeId);
        if (routeDeskList.isEmpty()) {
            return;
        }

        if (isDirect) {
            dao.queryBuilder().where(KeepWatchRouteDeskDao.Properties.RouteId.eq(routeId)).buildDelete().executeDeleteWithoutDetachingEntities();
        } else {
            for (int i = 0; i < routeDeskList.size(); ++i) {
                KeepWatchRouteDesk routeDesk = routeDeskList.get(i);
                routeDesk.setStatus("delete");
                routeDesk.setTimeStamp(System.currentTimeMillis());
                dao.update(routeDesk);
            }
        }
    }
}
