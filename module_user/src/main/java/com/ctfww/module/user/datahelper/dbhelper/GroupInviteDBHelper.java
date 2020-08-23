package com.ctfww.module.user.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.GroupInviteInfoDao;

import java.util.List;

public class GroupInviteDBHelper {
    private static final String TAG ="GroupInviteDBHelper";
    /**
     * 插入邀请
     * @param inviteInfo 邀请信息
     */
    public static boolean add(GroupInviteInfoDao dao, GroupInviteInfo inviteInfo) {
        try {
            dao.insert(inviteInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 更新邀请信息
     * @param inviteInfo 通知邀请信息
     */
    public static void update(GroupInviteInfoDao dao, GroupInviteInfo inviteInfo) {
        try {
            dao.update(inviteInfo);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    /**
     * 根据inviteId查询邀请
     * @param inviteId 邀请ID
     * @return 对应的邀请信息
     */
    public static GroupInviteInfo get(GroupInviteInfoDao dao, String inviteId) {
        return dao.queryBuilder().where(GroupInviteInfoDao.Properties.InviteId.eq(inviteId)).unique();
    }

    public static List<GroupInviteInfo> getSendList(GroupInviteInfoDao dao, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(GroupInviteInfoDao.Properties.FromUserId.eq(userId), GroupInviteInfoDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<GroupInviteInfo> getReceivedList(GroupInviteInfoDao dao, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(GroupInviteInfoDao.Properties.ToUserId.eq(userId), GroupInviteInfoDao.Properties.Status.notEq("delete"))).list();
    }

    public static List<GroupInviteInfo> getNoSynList(GroupInviteInfoDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(GroupInviteInfoDao.Properties.SynTag.eq("new"), GroupInviteInfoDao.Properties.SynTag.eq("modify"))).list();
    }
}
