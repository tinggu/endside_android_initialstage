package com.ctfww.module.assignment.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.assignment.datahelper.NetworkHelper;
import com.ctfww.module.assignment.datahelper.dbhelper.DBHelper;
import com.ctfww.module.assignment.entity.AssignmentInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AssignmentAirship {
    private final static String TAG = "AssignmentAirship";

    // 同步任务上云
    public static void synToCloud() {
        List<AssignmentInfo> assignmentList = DBHelper.getInstance().getNoSynAssignmentList();
        if (assignmentList.isEmpty()) {
            return;
        }

        CargoToCloud<AssignmentInfo> cargoToCloud = new CargoToCloud<>(assignmentList);

        NetworkHelper.getInstance().synAssignmentInfoToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < assignmentList.size(); ++i) {
                    AssignmentInfo assignment = assignmentList.get(i);
                    assignment.setSynTag("cloud");
                    DBHelper.getInstance().updateAssignment(assignment);
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong(AirshipConst.ASSIGNMENT_SYN_TIME_STAMP_CLOUD, 0);
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synAssignmentInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<AssignmentInfo> assignmentList = (List<AssignmentInfo>)obj;

                if (!assignmentList.isEmpty()) {
                    if (updateByCloud(assignmentList)) {
                        EventBus.getDefault().post(AirshipConst.FINISH_ASSIGNMENT_SYN);
                    }
                }

                SPStaticUtils.put(AirshipConst.ASSIGNMENT_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
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
            assignment.setSynTag("cloud");
            AssignmentInfo localAssignment = DBHelper.getInstance().getAssignment(assignment.getGroupId(), assignment.getDeskId(), assignment.getRouteId(), assignment.getUserId());
            if (localAssignment == null) {
                DBHelper.getInstance().addAssignment(assignment);
                ret = true;
            }
            else {
                if (localAssignment.getTimeStamp() < assignment.getTimeStamp()) {
                    DBHelper.getInstance().updateAssignment(assignment);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
