package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignmentDao;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrendsDao;

import java.util.List;

public class KeepWatchPersonTrendsDBHelper {
    private final static String TAG = "KeepWatchPersonTrendsDBHelper";

    // 用于app增加动态
    public static boolean add(KeepWatchPersonTrendsDao dao, KeepWatchPersonTrends personTrends) {
        try {
            dao.insertOrReplace(personTrends);
            return true;
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "add fail: e = " + e.getMessage());
            return false;
        }
    }

    // 用于app对动态的修改
    public static void update(KeepWatchPersonTrendsDao dao, KeepWatchPersonTrends personTrends) {
        try {
            dao.update(personTrends);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取所有动态
    public static List<KeepWatchPersonTrends> getList(KeepWatchPersonTrendsDao dao, String groupId) {
        return dao.queryBuilder().where(KeepWatchPersonTrendsDao.Properties.GroupId.eq(groupId)).list();
    }

    // 删除所有动态
    public static void delete(KeepWatchPersonTrendsDao dao) {
        dao.deleteAll();
    }
}
