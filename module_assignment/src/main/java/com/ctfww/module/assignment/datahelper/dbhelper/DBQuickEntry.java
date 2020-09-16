package com.ctfww.module.assignment.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.assignment.datahelper.sp.Const;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.assignment.entity.TodayAssignment;

import java.util.ArrayList;
import java.util.List;

public class DBQuickEntry {
    public static List<AssignmentInfo> getAssignmentList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<AssignmentInfo>();
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getAssignmentList(groupId) : DBHelper.getInstance().getAssignmentList(groupId, userId);
    }

    public static List<TodayAssignment> getTodayAssignmentList(long dayTimeStamp) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<TodayAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getTodayAssignmentList(groupId, dayTimeStamp) : DBHelper.getInstance().getTodayAssignmentList(groupId, userId, dayTimeStamp);
    }

    public static List<TodayAssignment> getTodayAssignmentList(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<TodayAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getTodayAssignmentList(groupId, startTime, endTime) : DBHelper.getInstance().getTodayAssignmentList(groupId, userId, startTime, endTime);
    }

    public static long getTodayAssignmentCount(long dayTimeStamp) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getTodayAssignmentCount(groupId, dayTimeStamp) : DBHelper.getInstance().getTodayAssignmentCount(groupId, userId, dayTimeStamp);
    }

    public static long getTodayAssignmentCount(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getTodayAssignmentCount(groupId, startTime, endTime) : DBHelper.getInstance().getTodayAssignmentCount(groupId, userId, startTime, endTime);
    }

    public static List<TodayAssignment> getLeakList(long dayTimeStamp) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<TodayAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getLeakList(groupId, dayTimeStamp) : DBHelper.getInstance().getLeakList(groupId, userId, dayTimeStamp);
    }

    public static List<TodayAssignment> getLeakList(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<TodayAssignment>();
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getLeakList(groupId, startTime, endTime) : DBHelper.getInstance().getLeakList(groupId, userId, startTime, endTime);
    }

    public static long getLeakCount(long dayTimeStamp) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getLeakCount(groupId, dayTimeStamp) : DBHelper.getInstance().getLeakCount(groupId, userId, dayTimeStamp);
    }

    public static long getLeakCount(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getLeakCount(groupId, startTime, endTime) : DBHelper.getInstance().getLeakCount(groupId, userId, startTime, endTime);
    }

    public static long getFinishCount(long dayTimeStamp) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getFinishCount(groupId, dayTimeStamp) : DBHelper.getInstance().getFinishCount(groupId, userId, dayTimeStamp);
    }

    public static long getFinishCount(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return 0;
        }

        String role = SPStaticUtils.getString("role");
        return "admin".equals(role) ? DBHelper.getInstance().getFinishCount(groupId, startTime, endTime) : DBHelper.getInstance().getFinishCount(groupId, userId, startTime, endTime);
    }

    public static void produceTodayAssignment() {
        long timeStamp = SPStaticUtils.getLong(Const.PRODUCE_TODAY_ASSIGNMENT_TIME_STAMP, 0);
        if (MyDateTimeUtils.getDayStartTime(timeStamp) == MyDateTimeUtils.getTodayStartTime()) {
            return;
        }

        String weekDay = GlobeFun.getTodayWeekDayStr();
        List<AssignmentInfo> assignmentList = DBHelper.getInstance().getWeekDayAssignmentList(weekDay);
        for (int i = 0; i < assignmentList.size(); ++i) {
            AssignmentInfo assignment = assignmentList.get(i);
            TodayAssignment todayAssignment = DBHelper.getInstance().getTodayAssignment(assignment.getGroupId(),assignment.getObjectId(), assignment.getUserId(), MyDateTimeUtils.getTodayStartTime(), assignment.getType());
            if (todayAssignment != null) {
                continue;
            }

            todayAssignment = new TodayAssignment();
            todayAssignment.setObjectId(assignment.getObjectId());
            todayAssignment.setGroupId(assignment.getGroupId());
            todayAssignment.setUserId(assignment.getUserId());
            todayAssignment.setDayTimeStamp(MyDateTimeUtils.getTodayStartTime());
            todayAssignment.setStartTime(assignment.getStartTime());
            todayAssignment.setEndTime(assignment.getEndTime());
            todayAssignment.setFrequency(assignment.getFrequency());
            todayAssignment.setSigninCount(0);
            todayAssignment.setStatus("reserve");
            todayAssignment.setType(assignment.getType());
            todayAssignment.setScore(assignment.getScore());
            todayAssignment.setTimeStamp(System.currentTimeMillis());

            DBHelper.getInstance().addTodayAssignment(todayAssignment);
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
