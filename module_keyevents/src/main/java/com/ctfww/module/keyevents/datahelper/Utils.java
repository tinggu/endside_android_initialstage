package com.ctfww.module.keyevents.datahelper;

import android.content.Context;

public class Utils {
    public static void init(Context context) {
        // 初始化keyevents网络设置
        com.ctfww.module.keyevents.datahelper.CloudClient.getInstance().
                setCloudMethod(com.ctfww.commonlib.network.CloudClient.getInstance().
                        create(com.ctfww.module.keyevents.datahelper.ICloudMethod.class));

        // 初始化keyevents数据库模块
        com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper.getInstance().init(context);
    }
}
