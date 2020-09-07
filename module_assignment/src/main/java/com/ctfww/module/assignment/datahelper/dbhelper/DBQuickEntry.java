package com.ctfww.module.assignment.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.RouteAssignment;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.user.datahelper.sp.Const;

import java.util.ArrayList;
import java.util.List;

public class DBQuickEntry {
    public static List<DeskAssignment> getWorkingDeskAssignmentList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<DeskAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getDeskAssignmentList(groupId) : DBHelper.getInstance().getDeskAssignmentList(groupId, userId);
    }

    public static List<DeskAssignment> getTodayWorkingDeskAssignmentList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<DeskAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        String weekDay = GlobeFun.getTodayWeekDayStr();
        return "admin".equals(role) ? DBHelper.getInstance().getWeekDayDeskAssignmentList(groupId, weekDay) : DBHelper.getInstance().getWeekDayDeskAssignmentList(groupId, userId, weekDay);
    }

    public static List<RouteAssignment> getWorkingRouteAssignmentList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<RouteAssignment>();
        }

        String role = SPStaticUtils.getString("role");

        return "admin".equals(role) ? DBHelper.getInstance().getRouteAssignmentList(groupId) : DBHelper.getInstance().getRouteAssignmentList(groupId, userId);
    }

    public static List<RouteAssignment> getTodayWorkingRouteAssignmentList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<RouteAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        String weekDay = GlobeFun.getTodayWeekDayStr();
        return "admin".equals(role) ? DBHelper.getInstance().getWeekDayRouteAssignmentList(groupId, weekDay) : DBHelper.getInstance().getWeekDayRouteAssignmentList(groupId, userId, weekDay);
    }

//    public static boolean transfer(String toUserId, boolean isForever) {
//        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
//        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
//        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
//            return false;
//        }
//
//        List<DeskAssignment> assignmentList =  DBHelper.getInstance().getWeekDayAssignmentList(groupId, userId);
//        if (assignmentList.isEmpty()) {
//            return false;
//        }
//
//        for (int i = 0; i < assignmentList.size(); ++i) {
//            AssignmentInfo assignmentInfo = assignmentList.get(i);
//            if (!isForever) {
//                assignmentInfo.setFromUserId(assignmentInfo.getUserId());
//            }
//            assignmentInfo.setUserId(toUserId);
//            assignmentInfo.setTimeStamp(System.currentTimeMillis());
//            assignmentInfo.setSynTag("modify");
//
//            DBHelper.getInstance().updateAssignment(assignmentInfo);
//        }
//
//        return true;
//    }
}
