package com.ctfww.module.user.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.user.entity.DaoMaster;
import com.ctfww.module.user.entity.DaoSession;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupInfoDao;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.GroupInviteInfoDao;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.GroupUserInfoDao;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeInfoDao;
import com.ctfww.module.user.entity.NoticeReadStatus;
import com.ctfww.module.user.entity.NoticeReadStatusDao;
import com.ctfww.module.user.entity.UserInfo;
import com.ctfww.module.user.entity.UserInfoDao;

import java.util.List;

public class DBHelper {

//    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String TAG ="DBHelper";

    private UserInfoDao userInfoDao;
    private GroupInfoDao groupInfoDao;
    private GroupInviteInfoDao groupInviteInfoDao;
    private NoticeInfoDao noticeInfoDao;
    private GroupUserInfoDao groupUserInfoDao;
    private NoticeReadStatusDao noticeReadStatusDao;

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
        groupInfoDao = daoSession.getGroupInfoDao();
        groupInviteInfoDao = daoSession.getGroupInviteInfoDao();
        noticeInfoDao = daoSession.getNoticeInfoDao();
        groupUserInfoDao = daoSession.getGroupUserInfoDao();
        noticeReadStatusDao = daoSession.getNoticeReadStatusDao();
    }

    // 1. 与用户有关

    /**
     * 插入用户信息
     * @param userInfo 用户信息
     */
   public boolean addUser(UserInfo userInfo) {
       return UserDBHelper.add(userInfoDao, userInfo);
    }

    /**
     * 更新用户信息
     * @param userInfo 用户信息
     */
    public void updateUser(UserInfo userInfo) {
        UserDBHelper.update(userInfoDao, userInfo);
    }

    /**
     * 根据userID查询用户
     * @param userId 用户ID
     * @return 对应的用户信息
     */
    public UserInfo getUser(String userId) {
        return UserDBHelper.get(userInfoDao, userId);
    }


    /**
     * 查询未同步记录
     * @return
     */
    public List<UserInfo> getNoSynUserList() {
        return UserDBHelper.getNoSynList(userInfoDao);
    }

    public List<UserInfo> getNoSynAdditionUserList() {
        return UserDBHelper.getNoSynAdditionList(userInfoDao);
    }

    public void deleteUser(String userId) {
        UserDBHelper.delete(userInfoDao, userId);
    }

    // 2. 与群组有关

    /**
     * 插入群组信息
     * @param groupInfo 群组信息
     */
    public boolean addGroup(GroupInfo groupInfo) {
        return GroupDBHelper.add(groupInfoDao, groupInfo);
    }

    /**
     * 更新群组信息
     * @param groupInfo 群组信息
     */
    public void updateGroup(GroupInfo groupInfo) {
        GroupDBHelper.update(groupInfoDao, groupInfo);
    }

    /**
     * 根据GroupId查询群组
     * @param groupId 群组Id
     * @return 对应的群组信息
     */
    public GroupInfo getGroup(String groupId) {
        return GroupDBHelper.get(groupInfoDao, groupId);
    }

    /**
     * 查询未同步的群组记录
     * @return
     */
    public List<GroupInfo> getNoSynGroupList() {
        return GroupDBHelper.getNoSynList(groupInfoDao);
    }

    public void deleteGroup(String groupId) {
        GroupDBHelper.delete(groupInfoDao, groupId);
    }

    // 3. 与邀请有关

    /**
     * 插入邀请信息
     * @param inviteInfo 用户信息
     */
    public boolean addInvite(GroupInviteInfo inviteInfo) {
        return GroupInviteDBHelper.add(groupInviteInfoDao, inviteInfo);
    }

    /**
     * 更新邀请信息
     * @param inviteInfo 邀请信息
     */
    public void updateInvite(GroupInviteInfo inviteInfo) {
        GroupInviteDBHelper.update(groupInviteInfoDao, inviteInfo);
    }

    /**
     * 根据inviteId查询邀请
     * @param inviteId 邀请ID
     * @return 对应的邀请信息
     */
    public GroupInviteInfo getInvite(String inviteId) {
        return GroupInviteDBHelper.get(groupInviteInfoDao, inviteId);
    }

    public List<GroupInviteInfo> getSendInviteList(String userId) {
        return GroupInviteDBHelper.getSendList(groupInviteInfoDao, userId);
    }

    public List<GroupInviteInfo> getReceivedInviteList(String userId) {
        return GroupInviteDBHelper.getReceivedList(groupInviteInfoDao, userId);
    }

    /**
     * 查询未同步邀请记录
     * @return
     */
    public List<GroupInviteInfo> getNoSynInviteList() {
        return GroupInviteDBHelper.getNoSynList(groupInviteInfoDao);
    }

    // 4. 与通知有关

    /**
     * 插入通知信息
     * @param noticeInfo 通知信息
     */
    public boolean addNotice(NoticeInfo noticeInfo) {
        return NoticeDBHelper.add(noticeInfoDao, noticeInfo);
    }

    /**
     * 更新通知信息
     * @param noticeInfo 通知信息
     */
    public void updateNotice(NoticeInfo noticeInfo) {
        NoticeDBHelper.update(noticeInfoDao, noticeInfo);
    }

    /**
     * 查询通知
     * @param noticeId 通知ID
     * @return 对应的通知信息
     */
    public NoticeInfo getNotice(String noticeId) {
        return NoticeDBHelper.get(noticeInfoDao, noticeId);
    }

    public List<NoticeInfo> getNoticeList(String groupId) {
        return NoticeDBHelper.getList(noticeInfoDao, groupId);
    }

    /**
     * 查询未同步通知记录
     * @return
     */
    public List<NoticeInfo> getNoSynNoticeList() {
        return NoticeDBHelper.getNoSynList(noticeInfoDao);
    }

    // 5. 与群组成员有关

    /**
     * 插入成员信息
     * @param groupUserInfo 成员信息
     */
    public boolean addGroupUser(GroupUserInfo groupUserInfo) {
        return GroupUserDBHelper.add(groupUserInfoDao, groupUserInfo);
    }

    /**
     * 更新成员信息
     * @param groupUserInfo 成员信息
     */
    public void updateGroupUser(GroupUserInfo groupUserInfo) {
        GroupUserDBHelper.update(groupUserInfoDao, groupUserInfo);
    }

    /**
     * 查询成员信息
     * @param groupId 群组ID
     * @param userId 成员Id
     * @return 对应的成员信息
     */
    public GroupUserInfo getGroupUser(String groupId, String userId) {
        return GroupUserDBHelper.get(groupUserInfoDao, groupId, userId);
    }

    /**
     * 查询未同步成员记录
     * @return
     */
    public List<GroupUserInfo> getNoSynGroupUserList() {
        return GroupUserDBHelper.getNoSynList(groupUserInfoDao);
    }

    /**
     * 查询所有成员记录
     * @return
     */
    public List<GroupUserInfo> getGroupUserList(String groupId) {
        return GroupUserDBHelper.getListByGroupId(groupUserInfoDao, groupId);
    }

    public List<GroupUserInfo> getUserGroupList(String userId) {
        return GroupUserDBHelper.getListByUserId(groupUserInfoDao, userId);
    }

    public void deleteGroupUser(String groupId, String userId) {
        GroupUserDBHelper.delete(groupUserInfoDao, groupId, userId);
    }

    public void deleteGroupUserLeaveUserId(String groupId, String userId) {
        GroupUserDBHelper.deleteLeaveUserId(groupUserInfoDao, groupId, userId);
    }

    public long getGroupUserAdminCount(String groupId) {
        return GroupUserDBHelper.getAdminCount(groupUserInfoDao, groupId);
    }

    // 6. 与通知读取状态有关

    /**
     * 插入通知读取状态
     * @param noticeReadStatus 通知读取状态
     */
    public boolean addNoticeReadStatus(NoticeReadStatus noticeReadStatus) {
        return NoticeReadStatusDBHelper.add(noticeReadStatusDao, noticeReadStatus);
    }

    /**
     * 更新通知读取状态
     * @param noticeReadStatus 通知读取状态
     */
    public void updateNoticeReadStatus(NoticeReadStatus noticeReadStatus) {
        NoticeReadStatusDBHelper.update(noticeReadStatusDao, noticeReadStatus);
    }

    public NoticeReadStatus getNoticeReadStatus(String noticeId, String userId) {
        return NoticeReadStatusDBHelper.get(noticeReadStatusDao, noticeId, userId);
    }

    /**
     * 查询通知读取状态列表
     * @param noticeId 通知ID
     * @return 对应的通知的读取状态
     */
    public List<NoticeReadStatus> getNoticeReadStatusList(String noticeId) {
        return NoticeReadStatusDBHelper.getList(noticeReadStatusDao, noticeId);
    }

    /**
     * 查询未同步通知读取状态
     * @return
     */
    public List<NoticeReadStatus> getNoSynNoticeReadStatusList() {
        return NoticeReadStatusDBHelper.getNoSynList(noticeReadStatusDao);
    }

    // 获得没有查看的通知个数
    public long getNoLookOverNoticeCount(String groupId, String userId) {
        return NoticeReadStatusDBHelper.getNoLookOverCount(noticeReadStatusDao, groupId, userId);
    }
}

