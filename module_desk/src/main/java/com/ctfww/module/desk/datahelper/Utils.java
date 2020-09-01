package com.ctfww.module.desk.datahelper;

import android.content.Context;

import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;

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
}
