package com.ctfww.module.keepwatch.DataHelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchRouteSummaryAirship {
    private final static String TAG = "KeepWatchRouteSummaryAirship";

    // 同步签到点上云
    public static void synToCloud() {
        List<KeepWatchRouteSummary> routeSummaryList = DBHelper.getInstance().getNoSynKeepWatchRouteSummaryList();
        if (routeSummaryList.isEmpty()) {
            return;
        }

        CargoToCloud<KeepWatchRouteSummary> cargoToCloud = new CargoToCloud<>(routeSummaryList);

        NetworkHelper.getInstance().synKeepWatchRouteSummaryToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < routeSummaryList.size(); ++i) {
                    KeepWatchRouteSummary routeSummary = routeSummaryList.get(i);
                    routeSummary.setSynTag("cloud");
                    DBHelper.getInstance().updateKeepWatchRouteSummary(routeSummary);
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

        long startTime = SPStaticUtils.getLong("keepwatch_route_summary_syn_time_stamp_cloud", CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeepWatchRouteSummaryFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchRouteSummary> routeSummaryList = (List<KeepWatchRouteSummary>)obj;

                if (!routeSummaryList.isEmpty()) {
                    if (updateTabByCloud(routeSummaryList)) {
                        EventBus.getDefault().post("finish_desk_syn");
                    }
                }

                SPStaticUtils.put("keepwatch_route_summary_syn_time_stamp_cloud", condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateTabByCloud(List<KeepWatchRouteSummary> routeSummaryList) {
        boolean ret = false;
        for (int i = 0; i < routeSummaryList.size(); ++i) {
            KeepWatchRouteSummary routeSummary = routeSummaryList.get(i);
            KeepWatchRouteSummary localRouteSummary = DBHelper.getInstance().getKeepWatchRouteSummary(routeSummary.getRouteId());
            if (localRouteSummary == null) {
                routeSummary.setSynTag("cloud");
                DBHelper.getInstance().addKeepWatchRouteSummary(routeSummary);
                ret = true;
            }
            else {
                if (localRouteSummary.getTimeStamp() < routeSummary.getTimeStamp()) {
                    routeSummary.setSynTag("cloud");
                    DBHelper.getInstance().updateKeepWatchRouteSummary(routeSummary);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
