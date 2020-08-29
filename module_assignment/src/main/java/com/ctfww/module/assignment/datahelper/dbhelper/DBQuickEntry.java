package com.ctfww.module.assignment.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.assignment.entity.AssignmentInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBQuickEntry {
    public static List<AssignmentInfo> getWorkingAssignmentList() {
        String groupId = SPStaticUtils.getString("working_group_id");
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<AssignmentInfo>();
        }

        String role = SPStaticUtils.getString("role");

        return "admin".equals(role) ? DBHelper.getInstance().getAssignmentList(groupId) : DBHelper.getInstance().getAssignmentList(groupId, userId);
    }

    public static List<AssignmentInfo> getTodayWorkingAssignmentList() {
        String groupId = SPStaticUtils.getString("working_group_id");
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return new ArrayList<AssignmentInfo>();
        }

        String role = SPStaticUtils.getString("role");
        String weekDay = GlobeFun.getTodayWeekDayStr();
        return "admin".equals(role) ? DBHelper.getInstance().getWeekDayAssignmentList(groupId, weekDay) : DBHelper.getInstance().getWeekDayAssignmentList(groupId, userId, weekDay);
    }

    public static boolean transfer(String toUserId, boolean isForever) {
        String groupId = SPStaticUtils.getString("working_group_id");
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return false;
        }

        List<AssignmentInfo> assignmentList =  DBHelper.getInstance().getWeekDayAssignmentList(groupId, userId);
        if (assignmentList.isEmpty()) {
            return false;
        }

        for (int i = 0; i < assignmentList.size(); ++i) {
            AssignmentInfo assignmentInfo = assignmentList.get(i);
            if (!isForever) {
                assignmentInfo.setFromUserId(assignmentInfo.getUserId());
            }
            assignmentInfo.setUserId(toUserId);
            assignmentInfo.setTimeStamp(System.currentTimeMillis());
            assignmentInfo.setSynTag("modify");

            DBHelper.getInstance().updateAssignment(assignmentInfo);
        }

        return true;
    }
}
