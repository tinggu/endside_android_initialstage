package com.ctfww.module.assignment.datahelper;

import android.content.Context;

import com.ctfww.module.assignment.datahelper.dbhelper.DBHelper;
import com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry;

public class Utils {
    public static void start(Context ctx){
        ICloudMethod assignmentMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(assignmentMethod);

        DBHelper.getInstance().init(ctx);
        DBQuickEntry.produceTodayAssignment();
    }
}
