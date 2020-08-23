package com.ctfww.module.keepwatch.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDesk;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchRouteDeskAirship {
    private final static String TAG = "KeepWatchRouteDeskAirship";

    // 同步签到点上云
    public static void synToCloud() {
        List<KeepWatchRouteDesk> routeDeskList = DBHelper.getInstance().getNoSynKeepWatchRouteDeskList();
        if (routeDeskList.isEmpty()) {
            return;
        }

        CargoToCloud<KeepWatchRouteDesk> cargoToCloud = new CargoToCloud<>(routeDeskList);

        NetworkHelper.getInstance().synKeepWatchRouteDeskToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < routeDeskList.size(); ++i) {
                    KeepWatchRouteDesk routeDesk = routeDeskList.get(i);
                    routeDesk.setSynTag("cloud");
                    DBHelper.getInstance().updateKeepWatchRouteDesk(routeDesk);
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

        String key = "keepwatch_route_desk_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeepWatchRouteDeskFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchRouteDesk> routeDeskList = (List<KeepWatchRouteDesk>)obj;

                if (!routeDeskList.isEmpty()) {
                    if (updateTabByCloud(routeDeskList)) {
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

    private static boolean updateTabByCloud(List<KeepWatchRouteDesk> routeDeskList) {
        boolean ret = false;
        for (int i = 0; i < routeDeskList.size(); ++i) {
            KeepWatchRouteDesk routeDesk = routeDeskList.get(i);
            KeepWatchRouteDesk localRouteDesk = DBHelper.getInstance().getKeepWatchRouteDesk(routeDesk.getRouteId(), routeDesk.getDeskId());
            if (localRouteDesk == null) {
                routeDesk.setSynTag("cloud");
                DBHelper.getInstance().addKeepWatchRouteDesk(routeDesk);
                ret = true;
            }
            else {
                if (localRouteDesk.getTimeStamp() < routeDesk.getTimeStamp()) {
                    routeDesk.setSynTag("cloud");
                    DBHelper.getInstance().updateKeepWatchRouteDesk(routeDesk);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
