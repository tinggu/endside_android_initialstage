package com.ctfww.module.desk.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.desk.datahelper.NetworkHelper;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.entity.RouteSummary;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RouteSummaryAirship {
    private final static String TAG = "RouteSummaryAirship";

    // 同步签到点上云
    public static void synToCloud() {
        List<RouteSummary> routeSummaryList = DBHelper.getInstance().getNoSynRouteSummaryList();
        if (routeSummaryList.isEmpty()) {
            return;
        }

        CargoToCloud<RouteSummary> cargoToCloud = new CargoToCloud<>(routeSummaryList);

        NetworkHelper.getInstance().synRouteSummaryToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < routeSummaryList.size(); ++i) {
                    RouteSummary routeSummary = routeSummaryList.get(i);
                    routeSummary.setSynTag("cloud");
                    DBHelper.getInstance().updateRouteSummary(routeSummary);
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

        String key = "route_summary_syn_time_stamp_cloud";
        long startTime = SPStaticUtils.getLong(key, 0);
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synRouteSummaryFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<RouteSummary> routeSummaryList = (List<RouteSummary>)obj;

                if (!routeSummaryList.isEmpty()) {
                    if (updateTabByCloud(routeSummaryList)) {
                        EventBus.getDefault().post("finish_desk_syn");
                    }
                }

                SPStaticUtils.put(key, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateTabByCloud(List<RouteSummary> routeSummaryList) {
        boolean ret = false;
        for (int i = 0; i < routeSummaryList.size(); ++i) {
            RouteSummary routeSummary = routeSummaryList.get(i);
            RouteSummary localRouteSummary = DBHelper.getInstance().getRouteSummary(routeSummary.getRouteId());
            if (localRouteSummary == null) {
                routeSummary.setSynTag("cloud");
                DBHelper.getInstance().addRouteSummary(routeSummary);
                ret = true;
            }
            else {
                if (localRouteSummary.getTimeStamp() < routeSummary.getTimeStamp()) {
                    routeSummary.setSynTag("cloud");
                    DBHelper.getInstance().updateRouteSummary(routeSummary);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
