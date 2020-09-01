package com.ctfww.module.desk.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.desk.datahelper.NetworkHelper;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.datahelper.sp.Const;
import com.ctfww.module.desk.entity.RouteDesk;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RouteDeskAirship {
    private final static String TAG = "RouteDeskAirship";

    // 同步签到点上云
    public static void synToCloud() {
        List<RouteDesk> routeDeskList = DBHelper.getInstance().getNoSynRouteDeskList();
        if (routeDeskList.isEmpty()) {
            return;
        }

        CargoToCloud<RouteDesk> cargoToCloud = new CargoToCloud<>(routeDeskList);

        NetworkHelper.getInstance().synRouteDeskToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < routeDeskList.size(); ++i) {
                    RouteDesk routeDesk = routeDeskList.get(i);
                    routeDesk.setSynTag("cloud");
                    DBHelper.getInstance().updateRouteDesk(routeDesk);
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
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String key = "route_desk_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, 0);
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synRouteDeskFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<RouteDesk> routeDeskList = (List<RouteDesk>)obj;

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

    private static boolean updateTabByCloud(List<RouteDesk> routeDeskList) {
        boolean ret = false;
        for (int i = 0; i < routeDeskList.size(); ++i) {
            RouteDesk routeDesk = routeDeskList.get(i);
            RouteDesk localRouteDesk = DBHelper.getInstance().getRouteDesk(routeDesk.getRouteId(), routeDesk.getDeskId());
            if (localRouteDesk == null) {
                routeDesk.setSynTag("cloud");
                DBHelper.getInstance().addRouteDesk(routeDesk);
                ret = true;
            }
            else {
                if (localRouteDesk.getTimeStamp() < routeDesk.getTimeStamp()) {
                    routeDesk.setSynTag("cloud");
                    DBHelper.getInstance().updateRouteDesk(routeDesk);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
