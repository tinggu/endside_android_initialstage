package com.ctfww.module.user.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class DBQuickEntry {
    // 获得自己的用户信息
    public static UserInfo getSelfInfo() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return null;
        }

        return DBHelper.getInstance().getUser(userId);
    }

    // 获得用户在工作组中的成员信息
    public static GroupUserInfo getGroupUserInfo(String userId) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }

        if (TextUtils.isEmpty(userId)) {
            userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
            if (TextUtils.isEmpty(userId)) {
                return null;
            }
        }

        return DBHelper.getInstance().getGroupUser(groupId, userId);
    }

    // 获得工作组中的成员信息列表
    public static List<GroupUserInfo> getGroupUserList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<GroupUserInfo>();
        }

        return DBHelper.getInstance().getGroupUserList(groupId);
    }

    public static List<GroupUserInfo> getUserGroupList() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return new ArrayList<GroupUserInfo>();
        }

        return DBHelper.getInstance().getUserGroupList(userId);
    }

    // 获得自己在工作组中的角色
    public static String getRoleInWorkingGroup() {
        GroupUserInfo groupUserInfo = getGroupUserInfo("");
        if (groupUserInfo == null) {
            return "member";
        }

        return groupUserInfo.getRole();
    }

    public static long getAdminCountInWorkingGroup() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return 0;
        }

        return DBHelper.getInstance().getGroupUserAdminCount(groupId);
    }

    public static long getGroupUserCount() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return 0;
        }

        return DBHelper.getInstance().getGroupUserCount(groupId);
    }

    // 获得工作群组信息
    public static GroupInfo getWorkingGroup() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }

        return DBHelper.getInstance().getGroup(groupId);
    }

    // 获得工作群组的通知信息
    public static List<NoticeInfo> getWorkingNoticeList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<NoticeInfo>();
        }

        return DBHelper.getInstance().getNoticeList(groupId);
    }

    // 获得当前使用者的发出邀请信息
    public static List<GroupInviteInfo> getSelfSendInviteList() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return new ArrayList<GroupInviteInfo>();
        }

        return DBHelper.getInstance().getSendInviteList(userId);
    }

    // 获得当前使用者的接收邀请信息
    public static List<GroupInviteInfo> getSelfReceivedInviteList() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return new ArrayList<GroupInviteInfo>();
        }

        return DBHelper.getInstance().getReceivedInviteList(userId);
    }

    // 获得本人本群未读取通知个数
    public static long getSelfNoLookOverNoticeCount() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return 0;
        }

        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return 0;
        }

        return DBHelper.getInstance().getNoLookOverNoticeCount(groupId, userId);
    }
}
