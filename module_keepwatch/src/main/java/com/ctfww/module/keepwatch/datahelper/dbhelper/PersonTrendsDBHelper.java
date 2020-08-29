package com.ctfww.module.keepwatch.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.entity.PersonTrends;
import com.ctfww.module.keepwatch.entity.PersonTrendsDao;

import java.util.List;

public class PersonTrendsDBHelper {
    private final static String TAG = "PersonTrendsDBHelper";

    // 用于app增加动态
    public static boolean add(PersonTrendsDao dao, PersonTrends personTrends) {
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
    public static void update(PersonTrendsDao dao, PersonTrends personTrends) {
        try {
            dao.update(personTrends);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "update fail: e = " + e.getMessage());
        }
    }

    // 获取所有动态
    public static List<PersonTrends> getList(PersonTrendsDao dao, String groupId) {
        return dao.queryBuilder().where(PersonTrendsDao.Properties.GroupId.eq(groupId)).list();
    }

    // 删除所有动态
    public static void delete(PersonTrendsDao dao) {
        dao.deleteAll();
    }
}
