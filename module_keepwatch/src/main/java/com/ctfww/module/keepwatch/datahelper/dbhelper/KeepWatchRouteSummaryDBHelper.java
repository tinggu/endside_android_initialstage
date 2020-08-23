package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummaryDao;

import java.util.List;

public class KeepWatchRouteSummaryDBHelper {
    private final static String TAG = "KeepWatchRouteSummaryDBHelper";

    // 增加线路概要信息
    public static void add(KeepWatchRouteSummaryDao dao, KeepWatchRouteSummary routeSummary) {
        try {
            dao.insert(routeSummary);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    // 修改线路概要信息
    public static void update(KeepWatchRouteSummaryDao dao, KeepWatchRouteSummary routeSummary) {
        try {
            dao.update(routeSummary);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    // 获取未同步的线路概要信息
    public static List<KeepWatchRouteSummary> getNoSynList(KeepWatchRouteSummaryDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(KeepWatchRouteSummaryDao.Properties.SynTag.eq("new"), KeepWatchRouteSummaryDao.Properties.SynTag.eq("modify"))).list();
    }

    // 获取路线概要信息
    public static KeepWatchRouteSummary get(KeepWatchRouteSummaryDao dao, String routeId) {
        return dao.queryBuilder().where(KeepWatchRouteSummaryDao.Properties.RouteId.eq(routeId)).unique();
    }

    // 获取路线概要信息列表
    public static List<KeepWatchRouteSummary> getList(KeepWatchRouteSummaryDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeepWatchRouteSummaryDao.Properties.GroupId.eq(groupId), KeepWatchRouteSummaryDao.Properties.Status.notEq("delete"))).list();
    }

    // 删除某条路线
    public static void newRoute(KeepWatchRouteSummaryDao dao, String routeId) {
        KeepWatchRouteSummary routeSummary = dao.queryBuilder().where(KeepWatchRouteSummaryDao.Properties.RouteId.eq(routeId)).unique();
        if (routeSummary == null) {
            return;
        }

        routeSummary.setStatus("new");
        dao.update(routeSummary);
    }

    // 删除某条路线
    public static void deleteRoute(KeepWatchRouteSummaryDao dao, String routeId, boolean isDirect) {
        KeepWatchRouteSummary routeSummary = dao.queryBuilder().where(KeepWatchRouteSummaryDao.Properties.RouteId.eq(routeId)).unique();
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
