package com.ctfww.module.user.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.UserGroupInfo;
import com.ctfww.module.user.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class DBQuickEntry {
    // 获得自己的用户信息
    public static UserInfo getSelfInfo() {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return null;
        }

        return DBHelper.getInstance().getUser(userId);
    }

    // 获得用户在工作组中的成员信息
    public static GroupUserInfo getWorkingGroupUser(String userId) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }

        if (TextUtils.isEmpty(userId)) {
            userId = SPStaticUtils.getString("user_open_id");
            if (TextUtils.isEmpty(userId)) {
                return null;
            }
        }

        return DBHelper.getInstance().getGroupUser(groupId, userId);
    }

    // 获得工作组中的成员信息列表
    public static List<GroupUserInfo> getWorkingGroupUserList() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<GroupUserInfo>();
        }

        return DBHelper.getInstance().getGroupUserList(groupId);
    }

    // 获得自己在工作组中的角色
    public static String getRoleInWorkingGroup() {
        GroupUserInfo groupUserInfo = getWorkingGroupUser("");
        if (groupUserInfo == null) {
            return "member";
        }

        return groupUserInfo.getRole();
    }

    // 获得自己对应的群组信息
    public static UserGroupInfo getSelfGroup(String groupId) {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return null;
        }

        if (TextUtils.isEmpty(groupId)) {
            groupId = SPStaticUtils.getString("working_group_id");
            if (TextUtils.isEmpty(groupId)) {
                return null;
            }
        }

        return DBHelper.getInstance().getUserGroup(groupId, userId);
    }

    // 获得自己对应的群组列表
    public static List<UserGroupInfo> getSelfGroupList() {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return new ArrayList<UserGroupInfo>();
        }

        return DBHelper.getInstance().getUserGroupList(userId);
    }

    // 获得工作群组的名称
    public static String getWorkingGroupName() {
        UserGroupInfo userGroupInfo = getSelfGroup("");
        if (userGroupInfo == null) {
            return "";
        }

        return userGroupInfo.getGroupName();
    }

    // 获得工作群组信息
    public static GroupInfo getWorkingGroup() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }

        return DBHelper.getInstance().getGroup(groupId);
    }

    // 获得工作群组的通知信息
    public static List<NoticeInfo> getWorkingNoticeList() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<NoticeInfo>();
        }

        return DBHelper.getInstance().getNoticeList(groupId);
    }

    // 获得当前使用者的发出邀请信息
    public static List<GroupInviteInfo> getSelfSendInviteList() {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return new ArrayList<GroupInviteInfo>();
        }

        return DBHelper.getInstance().getSendInviteList(userId);
    }

    // 获得当前使用者的接收邀请信息
    public static List<GroupInviteInfo> getSelfReceivedInviteList() {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return new ArrayList<GroupInviteInfo>();
        }

        return DBHelper.getInstance().getReceivedInviteList(userId);
    }

    // 获得本人本群未读取通知个数
    public static long getSelfNoLookOverNoticeCount() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return 0;
        }

        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return 0;
        }

        return DBHelper.getInstance().getNoLookOverNoticeCount(groupId, userId);
    }
}
