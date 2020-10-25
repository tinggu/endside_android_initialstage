package com.ctfww.module.desk.datahelper.dbhelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.desk.datahelper.sp.Const;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;

import java.util.ArrayList;
import java.util.List;

public class DBQuickEntry {
    public static List<DeskInfo> getWorkingDeskList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<DeskInfo>();
        }

        return DBHelper.getInstance().getDeskList(groupId);
    }

    public static long getDeskCount() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return 0;
        }

        return DBHelper.getInstance().getDeskCount(groupId);
    }

    public static List<RouteSummary> getRouteSummaryList() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<RouteSummary>();
        }

        return DBHelper.getInstance().getRouteSummaryList(groupId);
    }
}
