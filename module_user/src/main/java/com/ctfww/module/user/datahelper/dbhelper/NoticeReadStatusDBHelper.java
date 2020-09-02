package com.ctfww.module.user.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeInfoDao;
import com.ctfww.module.user.entity.NoticeReadStatus;
import com.ctfww.module.user.entity.NoticeReadStatusDao;

import java.util.List;

public class NoticeReadStatusDBHelper {
    private static final String TAG ="NoticeReadStatusDBHelper";
    /**
     * 插入通知读取状态
     * @param noticeReadStatus 通知读取状态信息
     */
    public static boolean add(NoticeReadStatusDao dao, NoticeReadStatus noticeReadStatus) {
        try {
            dao.insert(noticeReadStatus);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 更新通知读取状态信息
     * @param noticeReadStatus 通知读取状态信息
     */
    public static void update(NoticeReadStatusDao dao, NoticeReadStatus noticeReadStatus) {
        try {
            dao.update(noticeReadStatus);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    /**
     * 根据noticeId、userId查询通知读取状态
     * @param noticeId 通知ID
     * @param userId 成员Id
     * @return 对应的通知读取状态
     */
    public static NoticeReadStatus get(NoticeReadStatusDao dao, String noticeId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(NoticeReadStatusDao.Properties.NoticeId.eq(noticeId), NoticeReadStatusDao.Properties.UserId.eq(userId))).unique();
    }

    public static List<NoticeReadStatus> getList(NoticeReadStatusDao dao, String noticeId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(NoticeReadStatusDao.Properties.Flag.notEq(-1), NoticeReadStatusDao.Properties.NoticeId.eq(noticeId))).list();
    }

    public static List<NoticeReadStatus> getNoSynList(NoticeReadStatusDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(NoticeReadStatusDao.Properties.SynTag.eq("new"), NoticeReadStatusDao.Properties.SynTag.eq("modify"))).list();
    }

    public static long getNoLookOverCount(NoticeReadStatusDao dao, String groupId, String userId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(NoticeReadStatusDao.Properties.GroupId.eq(groupId), NoticeReadStatusDao.Properties.UserId.eq(userId), NoticeReadStatusDao.Properties.Flag.eq(0))).buildCount().count();
    }
}
