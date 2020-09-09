package com.ctfww.module.assignment.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.assignment.datahelper.sp.Const;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.DeskTodayAssignment;
import com.ctfww.module.assignment.entity.RouteAssignment;
import com.ctfww.module.assignment.entity.RouteTodayAssignment;
import com.ctfww.module.desk.entity.RouteDesk;

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

    public static List<DeskTodayAssignment> getTodayWorkingDeskAssignmentList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<DeskTodayAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getDeskTodayAssignmentList(groupId) : DBHelper.getInstance().getDeskTodayAssignmentList(groupId, userId);
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

    public static List<RouteTodayAssignment> getTodayWorkingRouteAssignmentList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<RouteTodayAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        String weekDay = GlobeFun.getTodayWeekDayStr();
        return "admin".equals(role) ? DBHelper.getInstance().getRouteTodayAssignmentList(groupId) : DBHelper.getInstance().getRouteTodayAssignmentList(groupId, userId);
    }

    public static void produceTodayAssignment() {
        long timeStamp = SPStaticUtils.getLong(Const.PRODUCE_TODAY_ASSIGNMENT_TIME_STAMP, 0);
        if (MyDateTimeUtils.getDayStartTime(timeStamp) == MyDateTimeUtils.getTodayStartTime()) {
            return;
        }

        DBHelper.getInstance().clearDeskTodayAssignment();
        DBHelper.getInstance().clearRouteTodayAssignment();

        String weekDay = GlobeFun.getTodayWeekDayStr();
        List<DeskAssignment> deskAssignmentList = DBHelper.getInstance().getWeekDayDeskAssignmentList(weekDay);
        for (int i = 0; i < deskAssignmentList.size(); ++i) {
            DeskAssignment deskAssignment = deskAssignmentList.get(i);
            DeskTodayAssignment deskTodayAssignment = new DeskTodayAssignment();
            deskTodayAssignment.setAssignmentId(deskAssignment.getGroupId() + deskAssignment.getId() + MyDateTimeUtils.getTodayStartTime());
            deskTodayAssignment.setDeskId(deskAssignment.getDeskId());
            deskTodayAssignment.setGroupId(deskAssignment.getGroupId());
            deskTodayAssignment.setUserId(deskAssignment.getUserId());
            deskTodayAssignment.setFrequency(deskAssignment.getFrequency());
            deskTodayAssignment.setStartTime(deskAssignment.getStartTime());
            deskTodayAssignment.setEndTime(deskAssignment.getEndTime());

            DBHelper.getInstance().addDeskTodayAssignment(deskTodayAssignment);
        }

        List<RouteAssignment> routeAssignmentList = DBHelper.getInstance().getWeekDayRouteAssignmentList(weekDay);
        for (int i = 0; i < routeAssignmentList.size(); ++i) {
            RouteAssignment routeAssignment = routeAssignmentList.get(i);
            RouteTodayAssignment routeTodayAssignment = new RouteTodayAssignment();
            routeTodayAssignment.setAssignmentId(routeAssignment.getRouteId() + MyDateTimeUtils.getTodayStartTime());
            routeTodayAssignment.setGroupId(routeAssignment.getGroupId());
            routeTodayAssignment.setUserId(routeAssignment.getUserId());
            routeTodayAssignment.setFrequency(routeAssignment.getFrequency());
            routeTodayAssignment.setStartTime(routeAssignment.getStartTime());
            routeTodayAssignment.setEndTime(routeAssignment.getEndTime());

            DBHelper.getInstance().addRouteTodayAssignment(routeTodayAssignment);
        }

        SPStaticUtils.put(Const.PRODUCE_TODAY_ASSIGNMENT_TIME_STAMP, timeStamp);
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
