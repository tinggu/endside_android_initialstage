package com.ctfww.module.assignment.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.assignment.datahelper.NetworkHelper;
import com.ctfww.module.assignment.datahelper.dbhelper.DBHelper;
import com.ctfww.module.assignment.datahelper.sp.Const;
import com.ctfww.module.assignment.entity.AssignmentInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AssignmentInfoAirship {
    private final static String TAG = "AssignmentInfoAirship";

    // 同步任务上云
    public static void synToCloud() {
        List<AssignmentInfo> assignmentList = DBHelper.getInstance().getNoSynAssignmentList();
        if (assignmentList.isEmpty()) {
            return;
        }

        CargoToCloud<AssignmentInfo> cargoToCloud = new CargoToCloud<>(assignmentList);

        NetworkHelper.getInstance().synAssignmentToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < assignmentList.size(); ++i) {
                    AssignmentInfo assignmentInfo = assignmentList.get(i);
                    assignmentInfo.setSynTag("cloud");
                    DBHelper.getInstance().updateAssignment(assignmentInfo);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步任务
    public static void synFromCloud() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong(Const.ASSIGNMENT_SYN_TIME_STAMP_CLOUD, 0);
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synAssignmentFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<AssignmentInfo> assignmentList = (List<AssignmentInfo>)obj;

                if (!assignmentList.isEmpty()) {
                    if (updateByCloud(assignmentList)) {
                        EventBus.getDefault().post(Const.FINISH_ASSIGNMENT_SYN);
                    }
                }

                SPStaticUtils.put(Const.ASSIGNMENT_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<AssignmentInfo> assignmentList) {
        boolean ret = false;
        for (int i = 0; i < assignmentList.size(); ++i) {
            AssignmentInfo assignment = assignmentList.get(i);
            assignment.combineId();
            assignment.setSynTag("cloud");
            AssignmentInfo localAssignment = DBHelper.getInstance().getAssignment(assignment.getGroupId(), assignment.getObjectId(), assignment.getUserId(), assignment.getType());
            if (localAssignment == null) {
                DBHelper.getInstance().addAssignment(assignment);
                DBHelper.getInstance().updateTodayAssignment(assignment);
                ret = true;
            }
            else {
                if (localAssignment.getTimeStamp() < assignment.getTimeStamp()) {
                    DBHelper.getInstance().updateAssignment(assignment);
                    DBHelper.getInstance().updateTodayAssignment(assignment);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
