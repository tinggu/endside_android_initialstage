package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.keepwatch.entity.SigninInfo;
import com.ctfww.module.keepwatch.entity.SigninInfoDao;

import java.util.List;

public class SigninDBHelper {
    private final static String TAG = "SigninDBHelper";

    // 存储签到信息，用于签到
    public static boolean add(SigninInfoDao dao, SigninInfo signinInfo) {
        try {
            dao.insert(signinInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    // 获取未同步上云的签到，用于签到信息的同步上云
    public static List<SigninInfo> getNoSynList(SigninInfoDao dao) {
        return dao.queryBuilder().where(SigninInfoDao.Properties.SynTag.eq("new")).list();
    }

    // 用于同步上云后更改同步状态
    public static void update(SigninInfoDao dao, SigninInfo signin) {
        try {
            dao.update(signin);
        }
        catch (SQLiteConstraintException e) {

        }
    }
}
