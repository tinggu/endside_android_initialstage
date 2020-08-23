package com.ctfww.module.user.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.user.entity.UserInfo;
import com.ctfww.module.user.entity.UserInfoDao;

import java.util.List;

public class UserDBHelper {
    private static final String TAG ="UserDBHelper";
    /**
     * 插入用户信息
     * @param userInfo 用户信息
     */
    public static boolean add(UserInfoDao dao, UserInfo userInfo) {
        try {
            dao.insert(userInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 更新用户信息
     * @param userInfo 用户信息
     */
    public static void update(UserInfoDao dao, UserInfo userInfo) {
        try {
            dao.update(userInfo);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    /**
     * 根据userID查询用户
     * @param userId 用户ID
     * @return 对应的用户信息
     */
    public static UserInfo get(UserInfoDao dao, String userId) {
        return dao.queryBuilder().where(UserInfoDao.Properties.UserId.eq(userId)).unique();
    }

    public static UserInfo getNoSyn(UserInfoDao dao, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(UserInfoDao.Properties.UserId.eq(userId), dao.queryBuilder().or(UserInfoDao.Properties.SynTag.eq("new"), UserInfoDao.Properties.SynTag.eq("modify")))).unique();
    }
}
