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
import com.ctfww.module.assignment.entity.RouteAssignment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RouteAssignmentAirship {
    private final static String TAG = "RouteAssignmentAirship";

    // 同步任务上云
    public static void synToCloud() {
        List<RouteAssignment> assignmentList = DBHelper.getInstance().getNoSynRouteAssignmentList();
        if (assignmentList.isEmpty()) {
            return;
        }

        CargoToCloud<RouteAssignment> cargoToCloud = new CargoToCloud<>(assignmentList);

        NetworkHelper.getInstance().synRouteAssignmentToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < assignmentList.size(); ++i) {
                    RouteAssignment assignment = assignmentList.get(i);
                    assignment.setSynTag("cloud");
                    DBHelper.getInstance().updateRouteAssignment(assignment);
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

        long startTime = SPStaticUtils.getLong(Const.ROUTE_ASSIGNMENT_SYN_TIME_STAMP_CLOUD, 0);
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synRouteAssignmentFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<RouteAssignment> assignmentList = (List<RouteAssignment>)obj;

                if (!assignmentList.isEmpty()) {
                    if (updateByCloud(assignmentList)) {
                        EventBus.getDefault().post(Const.FINISH_ROUTE_ASSIGNMENT_SYN);
                    }
                }

                SPStaticUtils.put(Const.ROUTE_ASSIGNMENT_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<RouteAssignment> assignmentList) {
        boolean ret = false;
        for (int i = 0; i < assignmentList.size(); ++i) {
            RouteAssignment assignment = assignmentList.get(i);
            assignment.setSynTag("cloud");
            RouteAssignment localAssignment = DBHelper.getInstance().getRouteAssignment(assignment.getGroupId(), assignment.getRouteId(), assignment.getUserId());
            if (localAssignment == null) {
                DBHelper.getInstance().addRouteAssignment(assignment);
                ret = true;
            }
            else {
                if (localAssignment.getTimeStamp() < assignment.getTimeStamp()) {
                    DBHelper.getInstance().updateRouteAssignment(assignment);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
