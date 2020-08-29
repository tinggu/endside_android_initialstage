package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.entity.Ranking;
import com.ctfww.module.keepwatch.entity.RankingDao;

import java.util.List;

public class RankingDBHelper {
    private final static String TAG = "RankingDBHelper";

    // 用于app增加排行
    public static boolean add(RankingDao dao, Ranking ranking) {
        try {
            dao.insertOrReplace(ranking);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    // 用于app对排行的修改
    public static void update(RankingDao dao, Ranking ranking) {
        try {
            dao.update(ranking);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取所有排行
    public static List<Ranking> getList(RankingDao dao, String groupId) {
        return dao.queryBuilder().where(RankingDao.Properties.GroupId.eq(groupId)).list();
    }

    // 删除所有排行
    public static void delete(RankingDao dao) {
        dao.deleteAll();
    }
}
