package com.ctfww.module.user.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeInfoDao;

import java.util.List;

public class NoticeDBHelper {
    private static final String TAG ="NoticeDBHelper";
    /**
     * 插入通知
     * @param noticeInfo 通知信息
     */
    public static boolean add(NoticeInfoDao dao, NoticeInfo noticeInfo) {
        try {
            dao.insert(noticeInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 更新通知信息
     * @param noticeInfo 通知信息
     */
    public static void update(NoticeInfoDao dao, NoticeInfo noticeInfo) {
        try {
            dao.update(noticeInfo);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    /**
     * 根据noticeId查询通知
     * @param noticeId 通知ID
     * @return 对应的通知信息
     */
    public static NoticeInfo get(NoticeInfoDao dao, String noticeId) {
        return dao.queryBuilder().where(NoticeInfoDao.Properties.NoticeId.eq(noticeId)).unique();
    }

    public static List<NoticeInfo> getList(NoticeInfoDao dao, String groupId) {
        return dao.queryBuilder().where(dao.queryBuilder().and(NoticeInfoDao.Properties.Flag.eq(-1), NoticeInfoDao.Properties.GroupId.eq(groupId))).list();
    }

    public static List<NoticeInfo> getNoSynList(NoticeInfoDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(NoticeInfoDao.Properties.SynTag.eq("new"), NoticeInfoDao.Properties.SynTag.eq("modify"))).list();
    }
}
