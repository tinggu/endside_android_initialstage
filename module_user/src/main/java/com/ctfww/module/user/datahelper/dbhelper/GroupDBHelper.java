package com.ctfww.module.user.datahelper.dbhelper;

import android.database.sqlite.SQLiteConstraintException;

import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupInfoDao;

import java.util.ArrayList;
import java.util.List;

public class GroupDBHelper {
    private static final String TAG ="GroupDBHelper";
    /**
     * 插入群组信息
     * @param groupInfo 群组信息
     */
    public static boolean add(GroupInfoDao dao, GroupInfo groupInfo) {
        List<GroupInfo> list = new ArrayList<>();
        try {
            dao.insert(groupInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 更新群组信息
     * @param groupInfo 群组信息
     */
    public static void update(GroupInfoDao dao, GroupInfo groupInfo) {
        try {
            dao.update(groupInfo);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    /**
     * 根据groupId查询群组信息
     * @param groupId 群组ID
     * @return 对应的群组信息
     */
    public static GroupInfo get(GroupInfoDao dao, String groupId) {
        return dao.queryBuilder().where(GroupInfoDao.Properties.GroupId.eq(groupId)).unique();
    }

    public static List<GroupInfo> getNoSynList(GroupInfoDao dao) {
        return dao.queryBuilder().where(dao.queryBuilder().or(GroupInfoDao.Properties.SynTag.eq("new"), GroupInfoDao.Properties.SynTag.eq("modify"))).list();
    }

    public static void delete(GroupInfoDao dao, String groupId) {
        dao.deleteByKey(groupId);
    }
}
