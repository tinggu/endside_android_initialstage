package com.ctfww.module.keepwatch.DataHelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchDeskDao;

import java.util.List;

public class KeepWatchDeskDBHelper {
    private final static String TAG = "KeepWatchDeskDBHelper";

    // 用于app增加签到点
    public static boolean add(KeepWatchDeskDao dao, KeepWatchDesk keepWatchDesk) {
        try {
            keepWatchDesk.combineId();
            dao.insert(keepWatchDesk);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    // 用于app对签到点信息的修改（包括删除：将status置为“delete”状态）
    public static void update(KeepWatchDeskDao dao, KeepWatchDesk keepWatchDesk) {
        try {
            keepWatchDesk.combineId();
            dao.update(keepWatchDesk);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取需要同步上云的签到点
    public static List<KeepWatchDesk> getNoSynList(KeepWatchDeskDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(KeepWatchDeskDao.Properties.SynTag.eq("new"), KeepWatchDeskDao.Properties.SynTag.eq("modify"))).list();
    }

    // 用于app查看某个签到点详细信息
    // 用于app确实是否存在该编号的签到点
    public static KeepWatchDesk get(KeepWatchDeskDao dao, String groupId, int deskId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeepWatchDeskDao.Properties.GroupId.eq(groupId), KeepWatchDeskDao.Properties.DeskId.eq(deskId))).unique();
    }

    // 获取所有签到点（签到点列表，但不包括是删除状态的）
    public static List<KeepWatchDesk> getList(KeepWatchDeskDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(KeepWatchDeskDao.Properties.GroupId.eq(groupId), KeepWatchDeskDao.Properties.Status.notEq("delete"))).list();
    }

    // 删除签到点
    public static void delete(KeepWatchDeskDao dao, KeepWatchDesk desk) {
        desk.setStatus("delete");
        desk.setTimeStamp(System.currentTimeMillis());
        dao.update(desk);
    }
}
