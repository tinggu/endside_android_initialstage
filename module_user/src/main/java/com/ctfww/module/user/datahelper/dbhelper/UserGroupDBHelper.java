package com.ctfww.module.user.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.user.entity.UserGroupInfo;
import com.ctfww.module.user.entity.UserGroupInfoDao;

import java.util.List;

public class UserGroupDBHelper {
    private static final String TAG ="UserGroupDBHelper";
    /**
     * 插入群组信息
     * @param userGroupInfo 群组信息
     */
    public static boolean add(UserGroupInfoDao dao, UserGroupInfo userGroupInfo) {
        try {
            dao.insert(userGroupInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 更新群组信息
     * @param userGroupInfo 群组信息
     */
    public static void update(UserGroupInfoDao dao, UserGroupInfo userGroupInfo) {
        try {
            dao.update(userGroupInfo);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    /**
     * 根据userID、groupId查询群组信息
     * @param userId 用户ID
     * @return 对应的用户信息
     */
    public static UserGroupInfo get(UserGroupInfoDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(UserGroupInfoDao.Properties.GroupId.eq(groupId), UserGroupInfoDao.Properties.UserId.eq(userId))).unique();
    }

    public static List<UserGroupInfo> getList(UserGroupInfoDao dao, String userId) {
        return dao.queryBuilder().where(UserGroupInfoDao.Properties.UserId.eq(userId)).list();
    }

    public static List<UserGroupInfo> getNoSynList(UserGroupInfoDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(UserGroupInfoDao.Properties.SynTag.eq("new"), UserGroupInfoDao.Properties.SynTag.eq("modify"))).list();
    }
}
