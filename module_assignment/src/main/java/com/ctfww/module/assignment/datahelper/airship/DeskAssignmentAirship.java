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
import com.ctfww.module.assignment.entity.DeskAssignment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DeskAssignmentAirship {
    private final static String TAG = "AssignmentAirship";

    // 同步任务上云
    public static void synToCloud() {
        List<DeskAssignment> deskAssignmentList = DBHelper.getInstance().getNoSynDeskAssignmentList();
        if (deskAssignmentList.isEmpty()) {
            return;
        }

        CargoToCloud<DeskAssignment> cargoToCloud = new CargoToCloud<>(deskAssignmentList);

        NetworkHelper.getInstance().synDeskAssignmentToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < deskAssignmentList.size(); ++i) {
                    DeskAssignment deskAssignment = deskAssignmentList.get(i);
                    deskAssignment.setSynTag("cloud");
                    DBHelper.getInstance().updateDeskAssignment(deskAssignment);
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

        long startTime = SPStaticUtils.getLong(Const.DESK_ASSIGNMENT_SYN_TIME_STAMP_CLOUD, 0);
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synDeskAssignmentFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<DeskAssignment> deskAssignmentList = (List<DeskAssignment>)obj;

                if (!deskAssignmentList.isEmpty()) {
                    if (updateByCloud(deskAssignmentList)) {
                        EventBus.getDefault().post(Const.FINISH_DESK_ASSIGNMENT_SYN);
                    }
                }

                SPStaticUtils.put(Const.DESK_ASSIGNMENT_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<DeskAssignment> deskAssignmentList) {
        boolean ret = false;
        for (int i = 0; i < deskAssignmentList.size(); ++i) {
            DeskAssignment assignment = deskAssignmentList.get(i);
            assignment.combineId();
            assignment.setSynTag("cloud");
            DeskAssignment localAssignment = DBHelper.getInstance().getDeskAssignment(assignment.getGroupId(), assignment.getDeskId(), assignment.getUserId());
            if (localAssignment == null) {
                DBHelper.getInstance().addDeskAssignment(assignment);
                DBHelper.getInstance().updateDeskTodayAssignment(assignment);
                ret = true;
            }
            else {
                if (localAssignment.getTimeStamp() < assignment.getTimeStamp()) {
                    DBHelper.getInstance().updateDeskAssignment(assignment);
                    DBHelper.getInstance().updateDeskTodayAssignment(assignment);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
