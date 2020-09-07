package com.ctfww.module.desk.datahelper;

import android.content.Context;

import com.ctfww.commonlib.entity.AbcLine;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.entity.RouteDesk;

import java.util.List;

public class Utils {
    public static void start(Context ctx){
        // 初始化签到点网络化模块
        ICloudMethod deskMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(deskMethod);

        // 初始化用户中心数据库模块
        DBHelper.getInstance().init(ctx);

        // 初始化用户中心的数据同步
 //       com.ctfww.module.user.storage.db.SelfSynDB.getInstance().startSyn();
    }

    public static boolean isExtendRoute(double lat, double lng, List<RouteDesk> routeDeskList) {
        if (routeDeskList.isEmpty()) {
            return true;
        }

        RouteDesk routeDesk = routeDeskList.get(routeDeskList.size() - 1);
        double dist = GlobeFun.calcLocationDist(lat, lng, routeDesk.getLat(), routeDesk.getLng());
        if (dist >= 30.0 || dist < 10.0) {
            return false;
        }

        if (routeDeskList.size() == 1) {
            return true;
        }

        RouteDesk routeDesk1 = routeDeskList.get(routeDeskList.size() - 2);
        RouteDesk routeDesk2 = routeDeskList.get(routeDeskList.size() - 1);
        AbcLine abc = new AbcLine(routeDesk1.getLat(), routeDesk1.getLng(), routeDesk2.getLat(), routeDesk2.getLng());
        double[] pedal = abc.calcPedal(lat, lng);
        dist = GlobeFun.calcLocationDist(lat, lng, pedal[0], pedal[1]);
        if (dist >= 30.0 || dist < 10.0) {
            return false;
        }

        return true;
    }
}
