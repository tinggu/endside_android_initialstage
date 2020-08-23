package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfoDao;

import java.util.List;

public class KeepWatchSigninDBHelper {
    private final static String TAG = "KeepWatchSigninDBHelper";

    // 存储签到信息，用于签到
    public static boolean add(KeepWatchSigninInfoDao dao, KeepWatchSigninInfo keepWatchSigninInfo) {
        try {
            keepWatchSigninInfo.combineId();
            dao.insert(keepWatchSigninInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    // 获取未同步上云的签到，用于签到信息的同步上云
    public static List<KeepWatchSigninInfo> getNoSyn(KeepWatchSigninInfoDao dao) {
        return dao.queryBuilder().where(KeepWatchSigninInfoDao.Properties.SynTag.eq("new")).list();
    }

    // 用于同步上云后更改同步状态
    public static void update(KeepWatchSigninInfoDao dao, KeepWatchSigninInfo signin) {
        try {
            signin.combineId();
            dao.update(signin);
        }
        catch (SQLiteConstraintException e) {

        }
    }
}
