package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.entity.KeepWatchRanking;
import com.ctfww.module.keepwatch.entity.KeepWatchRankingDao;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDeskDao;

import java.util.List;

public class KeepWatchAssignmentFinishStatusDBHelper {
    private final static String TAG = "KeepWatchAssignmentFinishStatusDBHelper";

    // 用于app增加签到点签到个数统计
    public static boolean add(KeepWatchStatisticsByDeskDao dao, KeepWatchStatisticsByDesk statisticsByDesk) {
        try {
            dao.insertOrReplace(statisticsByDesk);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }


    // 获取所有签到点的签到统计
    public static List<KeepWatchStatisticsByDesk> getList(KeepWatchStatisticsByDeskDao dao, String groupId) {
        return dao.queryBuilder().where(KeepWatchStatisticsByDeskDao.Properties.GroupId.eq(groupId)).list();
    }

    // 删除所有签到点签到统计
    public static void delete(KeepWatchStatisticsByDeskDao dao) {
        dao.deleteAll();
    }
}
