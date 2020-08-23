package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignmentDao;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrendsDao;
import com.ctfww.module.keepwatch.entity.KeepWatchRanking;
import com.ctfww.module.keepwatch.entity.KeepWatchRankingDao;

import java.util.List;

public class KeepWatchRankingDBHelper {
    private final static String TAG = "KeepWatchRankingDBHelper";

    // 用于app增加排行
    public static boolean add(KeepWatchRankingDao dao, KeepWatchRanking ranking) {
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
    public static void update(KeepWatchRankingDao dao, KeepWatchRanking ranking) {
        try {
            dao.update(ranking);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取所有排行
    public static List<KeepWatchRanking> getList(KeepWatchRankingDao dao, String groupId) {
        return dao.queryBuilder().where(KeepWatchRankingDao.Properties.GroupId.eq(groupId)).list();
    }

    // 删除所有排行
    public static void delete(KeepWatchRankingDao dao) {
        dao.deleteAll();
    }
}
