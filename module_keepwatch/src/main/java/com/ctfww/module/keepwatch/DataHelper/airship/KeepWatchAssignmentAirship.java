package com.ctfww.module.keepwatch.DataHelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchAssignmentAirship {
    private final static String TAG = "KeepWatchAssignmentAirship";

    // 同步任务上云
    public static void synToCloud() {
        List<KeepWatchAssignment> assignmentList = DBHelper.getInstance().getNoSynKeepWatchAssignmentList();
        if (assignmentList.isEmpty()) {
            return;
        }

        CargoToCloud<KeepWatchAssignment> cargoToCloud = new CargoToCloud<>(assignmentList);

        NetworkHelper.getInstance().synKeepWatchAssignmentToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < assignmentList.size(); ++i) {
                    KeepWatchAssignment assignment = assignmentList.get(i);
                    assignment.setSynTag("cloud");
                    DBHelper.getInstance().updateKeepWatchAssignment(assignment);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步签到点
    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong("keepwatch_assignment_syn_time_stamp_cloud", CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeepWatchDeskFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchAssignment> assignmentList = (List<KeepWatchAssignment>)obj;

                if (!assignmentList.isEmpty()) {
                    if (updateByCloud(assignmentList)) {
                        EventBus.getDefault().post("finish_desk_syn");
                    }
                }

                SPStaticUtils.put("keepwatch_assignment_syn_time_stamp_cloud", condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<KeepWatchAssignment> assignmentList) {
        boolean ret = false;
        for (int i = 0; i < assignmentList.size(); ++i) {
            KeepWatchAssignment assignment = assignmentList.get(i);
            KeepWatchAssignment localAssignment = DBHelper.getInstance().getKeepWatchAssignment(assignment.getAssignmentId());

            assignment.setSynTag("cloud");
            if (localAssignment == null) {
                DBHelper.getInstance().addKeepWatchAssignment(assignment);
                ret = true;
            }
            else {
                if (localAssignment.getTimeStamp() < assignment.getTimeStamp()) {
                    DBHelper.getInstance().updateKeepWatchAssignment(assignment);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
