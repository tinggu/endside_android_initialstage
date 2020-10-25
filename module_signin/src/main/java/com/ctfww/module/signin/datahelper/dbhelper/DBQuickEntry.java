package com.ctfww.module.signin.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.signin.datahelper.sp.Const;
import com.ctfww.module.signin.entity.SigninInfo;

import java.util.ArrayList;
import java.util.List;

public class DBQuickEntry {
    public static List<SigninInfo> getSigninList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<SigninInfo>();
        }

        return "admin".equals(role) ? DBHelper.getInstance().getSigninList(groupId) : DBHelper.getInstance().getSigninList(groupId, userId);
    }

    public static List<SigninInfo> getSigninList(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<SigninInfo>();
        }

        return "admin".equals(role) ? DBHelper.getInstance().getSigninList(groupId, startTime, endTime) : DBHelper.getInstance().getSigninList(groupId, userId, startTime, endTime);
    }

    public static long getSigninCount(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        return "admin".equals(role) ? DBHelper.getInstance().getSigninCount(groupId, startTime, endTime) : DBHelper.getInstance().getSigninCount(groupId, userId, startTime, endTime);
    }
}
