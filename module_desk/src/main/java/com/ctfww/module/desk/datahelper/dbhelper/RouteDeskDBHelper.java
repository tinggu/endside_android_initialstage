package com.ctfww.module.desk.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteDeskDao;

import java.util.List;

public class RouteDeskDBHelper {
    private final static String TAG = "RouteDeskDBHelper";

    // 增加线路签到点信息
    public static void add(RouteDeskDao dao, RouteDesk routeDesk) {
        try {
            dao.insert(routeDesk);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    // 修改线路签到点信息
    public static void update(RouteDeskDao dao, RouteDesk routeDesk) {
        try {
            dao.update(routeDesk);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    // 获得某线路的签到点
    public static List<RouteDesk> getInOneRoute(RouteDeskDao dao, String groupId, int routeId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteDeskDao.Properties.GroupId.eq(groupId), RouteDeskDao.Properties.RouteId.eq(routeId), dao.queryBuilder().or(RouteDeskDao.Properties.SynTag.eq("new"), RouteDeskDao.Properties.SynTag.eq("modify"), RouteDeskDao.Properties.SynTag.eq("cloud")))).orderAsc(RouteDeskDao.Properties.DeskId).orderAsc(RouteDeskDao.Properties.DeskId).list();
    }

    // 获得未同步的路线签到点
    public static List<RouteDesk> getNoSynList(RouteDeskDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(RouteDeskDao.Properties.SynTag.eq("new"), RouteDeskDao.Properties.SynTag.eq("modify"))).list();
    }

    // 获得路线签到点
    public static RouteDesk get(RouteDeskDao dao, String groupId, int routeId, int deskId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteDeskDao.Properties.GroupId.eq(groupId), RouteDeskDao.Properties.RouteId.eq(routeId), RouteDeskDao.Properties.DeskId.eq(deskId))).unique();
    }

    // 获得某路线的签到点
    public static List<RouteDesk> getList(RouteDeskDao dao, String groupId, int routeId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteDeskDao.Properties.GroupId.eq(groupId), RouteDeskDao.Properties.RouteId.eq(routeId), RouteDeskDao.Properties.Status.notEq("delete"))).list();
    }

    // 删除某条路线签到点
    public static void newRoute(RouteDeskDao dao, String groupId, int routeId) {
        List<RouteDesk> routeDeskList = getList(dao, groupId, routeId);
        if (routeDeskList.isEmpty()) {
            return;
        }

        for (int i = 0; i < routeDeskList.size(); ++i) {
            RouteDesk routeDesk = routeDeskList.get(i);
            routeDesk.setStatus("new");
            dao.update(routeDesk);
        }
    }

    // 删除某条路线签到点
    public static void deleteRoute(RouteDeskDao dao, String groupId, int routeId, boolean isDirect) {
        List<RouteDesk> routeDeskList = getList(dao, groupId, routeId);
        if (routeDeskList.isEmpty()) {
            return;
        }

        if (isDirect) {
            dao.queryBuilder().where(dao.queryBuilder().and(RouteDeskDao.Properties.GroupId.eq(groupId), RouteDeskDao.Properties.RouteId.eq(routeId))).buildDelete().executeDeleteWithoutDetachingEntities();
        } else {
            for (int i = 0; i < routeDeskList.size(); ++i) {
                RouteDesk routeDesk = routeDeskList.get(i);
                routeDesk.setStatus("delete");
                routeDesk.setTimeStamp(System.currentTimeMillis());
                dao.update(routeDesk);
            }
        }
    }
}
