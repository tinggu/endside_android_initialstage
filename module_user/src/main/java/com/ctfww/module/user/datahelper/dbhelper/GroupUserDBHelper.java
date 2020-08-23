package com.ctfww.module.user.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.GroupUserInfoDao;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeInfoDao;

import java.util.List;

public class GroupUserDBHelper {
    private static final String TAG ="GroupUserDBHelper";
    /**
     * 插入成员
     * @param groupUserInfo 通知信息
     */
    public static boolean add(GroupUserInfoDao dao, GroupUserInfo groupUserInfo) {
        try {
            dao.insert(groupUserInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 更新成员信息
     * @param groupUserInfo 成员信息
     */
    public static void update(GroupUserInfoDao dao, GroupUserInfo groupUserInfo) {
        try {
            dao.update(groupUserInfo);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    /**
     * 根据groupId、userId查询成员信息
     * @param groupId 群组ID
     * @Param userId 用户Id
     * @return 对应的成员信息
     */
    public static GroupUserInfo get(GroupUserInfoDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(NoticeInfoDao.Properties.GroupId.eq(groupId), NoticeInfoDao.Properties.UserId.eq(userId))).unique();
    }

    public static List<GroupUserInfo> getNoSynList(GroupUserInfoDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(GroupUserInfoDao.Properties.SynTag.eq("new"), GroupUserInfoDao.Properties.SynTag.eq("modify"))).list();
    }

    public static List<GroupUserInfo> getList(GroupUserInfoDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(GroupUserInfoDao.Properties.GroupId.eq(groupId), GroupUserInfoDao.Properties.SynTag.notEq("delete"))).list();
    }
}
