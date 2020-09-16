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
import com.ctfww.module.assignment.entity.TodayAssignment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class TodayAssignmentAirship {
    private final static String TAG = "TodayAssignmentAirship";

    // 从云上同步任务
    public static void synFromCloud() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong(Const.TODAY_ASSIGNMENT_SYN_TIME_STAMP_CLOUD, 0);
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synTodayAssignmentFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<TodayAssignment> assignmentList = (List<TodayAssignment>)obj;

                if (!assignmentList.isEmpty()) {
                    if (updateByCloud(assignmentList)) {
                        EventBus.getDefault().post(Const.FINISH_TODAY_ASSIGNMENT_SYN);
                    }
                }

                SPStaticUtils.put(Const.TODAY_ASSIGNMENT_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<TodayAssignment> assignmentList) {
        boolean ret = false;
        for (int i = 0; i < assignmentList.size(); ++i) {
            TodayAssignment assignment = assignmentList.get(i);
            assignment.combineId();
            TodayAssignment localAssignment = DBHelper.getInstance().getTodayAssignment(assignment.getGroupId(), assignment.getObjectId(), assignment.getUserId(), assignment.getDayTimeStamp(), assignment.getType());
            if (localAssignment == null) {
                DBHelper.getInstance().addTodayAssignment(assignment);
                ret = true;
            }
            else {
                if (localAssignment.getSigninCount() < assignment.getSigninCount()) {
                    DBHelper.getInstance().updateTodayAssignment(assignment);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
