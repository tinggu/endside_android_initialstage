package com.ctfww.module.signin.datahelper.dbhelper;

import android.content.pm.SigningInfo;
import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.signin.entity.SigninInfo;
import com.ctfww.module.signin.entity.SigninInfoDao;

import java.util.List;

public class SigninInfoDBHelper {
    private final static String TAG = "DeskSigninDBHelper";

    public static boolean add(SigninInfoDao dao, SigninInfo info) {
        try {
            dao.insert(info);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public static SigninInfo get(SigninInfoDao dao, String groupId, int objectId, String userId, long timeStamp) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(SigninInfoDao.Properties.GroupId.eq(groupId),
                                SigninInfoDao.Properties.ObjectId.eq(objectId),
                                SigninInfoDao.Properties.UserId.eq(userId),
                                SigninInfoDao.Properties.TimeStamp.eq(timeStamp)))
                .unique();
    }

    public static List<SigninInfo> getNoSynList(SigninInfoDao dao) {
        return dao.queryBuilder()
                .where(SigninInfoDao.Properties.SynTag.eq("new"))
                .list();
    }

    public static List<SigninInfo> getList(SigninInfoDao dao, String groupId) {
        return dao.queryBuilder()
                .where(SigninInfoDao.Properties.GroupId.eq(groupId))
                .list();
    }

    public static List<SigninInfo> getList(SigninInfoDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(SigninInfoDao.Properties.GroupId.eq(groupId),
                                SigninInfoDao.Properties.TimeStamp.ge(startTime),
                                SigninInfoDao.Properties.TimeStamp.le(endTime)))
                .orderDesc(SigninInfoDao.Properties.TimeStamp)
                .list();
    }

    public static List<SigninInfo> getList(SigninInfoDao dao, String groupId, String userId) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(SigninInfoDao.Properties.GroupId.eq(groupId),
                                SigninInfoDao.Properties.UserId.eq(userId)))
                .list();
    }

    public static List<SigninInfo> getList(SigninInfoDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(SigninInfoDao.Properties.GroupId.eq(groupId),
                                SigninInfoDao.Properties.UserId.eq(userId),
                                SigninInfoDao.Properties.TimeStamp.ge(startTime),
                                SigninInfoDao.Properties.TimeStamp.le(endTime)))
                .list();
    }

    public static List<SigninInfo> getList(SigninInfoDao dao, String groupId, int objectId, String userId, String type, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(SigninInfoDao.Properties.GroupId.eq(groupId),
                                SigninInfoDao.Properties.ObjectId.eq(objectId),
                                SigninInfoDao.Properties.UserId.eq(userId),
                                SigninInfoDao.Properties.Type.eq(type),
                                SigninInfoDao.Properties.TimeStamp.ge(startTime),
                                SigninInfoDao.Properties.TimeStamp.le(endTime)))
                .list();
    }

    public static long getCount(SigninInfoDao dao, String groupId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(SigninInfoDao.Properties.GroupId.eq(groupId),
                                SigninInfoDao.Properties.TimeStamp.ge(startTime),
                                SigninInfoDao.Properties.TimeStamp.le(endTime)))
                .buildCount()
                .count();
    }

    public static long getCount(SigninInfoDao dao, String groupId, String userId, long startTime, long endTime) {
        return dao.queryBuilder()
                .where(dao.queryBuilder()
                        .and(SigninInfoDao.Properties.GroupId.eq(groupId),
                                SigninInfoDao.Properties.UserId.eq(userId),
                                SigninInfoDao.Properties.TimeStamp.ge(startTime),
                                SigninInfoDao.Properties.TimeStamp.le(endTime)))
                .buildCount()
                .count();
    }

    public static void update(SigninInfoDao dao, SigninInfo info) {
        try {
            dao.update(info);
        }
        catch (SQLiteConstraintException e) {

        }
    }
}
