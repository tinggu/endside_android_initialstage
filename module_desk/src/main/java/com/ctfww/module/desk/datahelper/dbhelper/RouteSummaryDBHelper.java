package com.ctfww.module.desk.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.desk.entity.RouteSummaryDao;

import java.util.List;

public class RouteSummaryDBHelper {
    private final static String TAG = "RouteSummaryDBHelper";

    // 增加线路概要信息
    public static void add(RouteSummaryDao dao, RouteSummary routeSummary) {
        try {
            dao.insertOrReplace(routeSummary);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    // 修改线路概要信息
    public static void update(RouteSummaryDao dao, RouteSummary routeSummary) {
        try {
            dao.update(routeSummary);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    // 获取未同步的线路概要信息
    public static List<RouteSummary> getNoSynList(RouteSummaryDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(RouteSummaryDao.Properties.SynTag.eq("new"), RouteSummaryDao.Properties.SynTag.eq("modify"))).list();
    }

    // 获取路线概要信息
    public static RouteSummary get(RouteSummaryDao dao, String routeId) {
        return dao.queryBuilder().where(RouteSummaryDao.Properties.RouteId.eq(routeId)).unique();
    }

    // 获取路线概要信息列表
    public static List<RouteSummary> getList(RouteSummaryDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(RouteSummaryDao.Properties.GroupId.eq(groupId), RouteSummaryDao.Properties.Status.notEq("delete"))).list();
    }

    // 删除某条路线
    public static void newRoute(RouteSummaryDao dao, String routeId) {
        RouteSummary routeSummary = dao.queryBuilder().where(RouteSummaryDao.Properties.RouteId.eq(routeId)).unique();
        if (routeSummary == null) {
            return;
        }

        routeSummary.setStatus("new");
        dao.update(routeSummary);
    }

    // 删除某条路线
    public static void deleteRoute(RouteSummaryDao dao, String routeId, boolean isDirect) {
        RouteSummary routeSummary = dao.queryBuilder().where(RouteSummaryDao.Properties.RouteId.eq(routeId)).unique();
        if (routeSummary == null) {
            return;
        }

        if (isDirect) {
            dao.delete(routeSummary);
        }
        else {
            routeSummary.setStatus("delete");
            routeSummary.setTimeStamp(System.currentTimeMillis());
            dao.update(routeSummary);
        }
    }
}
