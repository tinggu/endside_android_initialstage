package com.ctfww.module.desk.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.DeskInfoDao;

import java.util.List;

public class DeskDBHelper {
    private final static String TAG = "DeskDBHelper";

    // 用于app增加签到点
    public static boolean add(DeskInfoDao dao, DeskInfo deskInfo) {
        try {
            deskInfo.combineId();
            dao.insert(deskInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    // 用于app对签到点信息的修改（包括删除：将status置为“delete”状态）
    public static void update(DeskInfoDao dao, DeskInfo deskInfo) {
        try {
            deskInfo.combineId();
            dao.update(deskInfo);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取需要同步上云的签到点
    public static List<DeskInfo> getNoSynList(DeskInfoDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(DeskInfoDao.Properties.SynTag.eq("new"), DeskInfoDao.Properties.SynTag.eq("modify"))).list();
    }

    // 用于app查看某个签到点详细信息
    // 用于app确实是否存在该编号的签到点
    public static DeskInfo get(DeskInfoDao dao, String groupId, int deskId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(DeskInfoDao.Properties.GroupId.eq(groupId), DeskInfoDao.Properties.DeskId.eq(deskId))).unique();
    }

    // 获取所有签到点（签到点列表，但不包括是删除状态的）
    public static List<DeskInfo> getList(DeskInfoDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(DeskInfoDao.Properties.GroupId.eq(groupId), DeskInfoDao.Properties.Status.notEq("delete"))).list();
    }

    // 删除签到点
    public static void delete(DeskInfoDao dao, DeskInfo desk) {
        desk.setStatus("delete");
        desk.setTimeStamp(System.currentTimeMillis());
        dao.update(desk);
    }
}
