package com.guoliang.module.user.datahelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.guoliang.module.user.entity.DaoMaster;
import com.guoliang.module.user.entity.DaoSession;
import com.guoliang.module.user.entity.UserInfo;
import com.guoliang.module.user.entity.UserInfoDao;

import java.util.List;

public class DBHelper {

//    private static final String TAG = DBHelper.class.getSimpleName();

    private UserInfoDao userInfoDao;

    private DBHelper() {

    }

    private static class Inner {
        private static final DBHelper INSTANCE = new DBHelper();
    }

    public static DBHelper getInstance() {
        return Inner.INSTANCE;
    }

    public void init(Context ctx) {
        if (ctx == null) {
            return;
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "user");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        userInfoDao = daoSession.getUserInfoDao();
    }


    /**
     * 插入用户信息
     * @param userInfo 用户信息
     */
   public void addUser(UserInfo userInfo) {
        userInfoDao.insertOrReplace(userInfo);
    }

    /**
     * 删除用户信息
     * @param userInfo 用户信息
     */
    public void deleteUser(UserInfo userInfo) {
        userInfoDao.delete(userInfo);
    }

    /**
     * 更新用户信息
     * @param userInfo 用户信息
     */
    public void updateUser(UserInfo userInfo) {
        userInfoDao.save(userInfo);
    }

    /**
     * 根据userID查询用户
     * @param userId 用户ID
     * @return 对应的用户信息
     */
    public List<UserInfo> queryUserByUserId(String userId) {
        return userInfoDao.queryBuilder().where(UserInfoDao.Properties.UserId.eq(userId)).list();
    }


    /**
     * 查询用户信息表中的所以项
     * @return 所有用户信息
     */
    public List<UserInfo> queryAllUser() {
        return userInfoDao.queryBuilder().list();
    }


    /**
     * 删除用户信息表中所以项
     */
    public void deleteAllUser() {
        userInfoDao.deleteAll();
    }

    /**
     * 创建群
     */
    public void addGroup() {
        userInfoDao.deleteAll();
    }


}

