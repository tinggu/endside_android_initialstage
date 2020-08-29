package com.ctfww.module.keepwatch;

import android.content.Context;
import android.graphics.Bitmap;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.ImageUtils;
import com.ctfww.commonlib.utils.QRCodeUtils;
import com.ctfww.commonlib.utils.SynDB;
import com.ctfww.module.keepwatch.datahelper.CloudClient;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.datahelper.ICloudMethod;

import java.util.Calendar;

public class Utils {
    private final static String TAG = "Utils";

    public static void start(Context ctx){
        // 初始化keepwatch网络化模块
        ICloudMethod keepWatchMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(keepWatchMethod);
//        CloudClient.getInstance().createCloudMethod();

        // 初始化keyevents网络设置
        com.ctfww.module.keyevents.datahelper.CloudClient.getInstance().
                setCloudMethod(com.ctfww.commonlib.network.CloudClient.getInstance().
                        create(com.ctfww.module.keyevents.datahelper.ICloudMethod.class));

        // 初始化签到点的网络设置
//        com.ctfww.module.

        // 初始化apk升级的网络设置
        com.ctfww.module.upgrade.datahelper.CloudClient.getInstance().createCloudMethod();

        // 初始化keepwatch数据库模块
        DBHelper.getInstance().init(ctx);

        // 初始化keyevents数据库模块
        com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper.getInstance().init(ctx);
    }

    public static void synData() {
        SynDB.startSyn(new SynDB.ISynHelper() {
            @Override
            public void doThing() {
//                synTracePoint();
            }
        });
    }



    private static boolean mIsFirstToken = false;
    public static void setFirstToken(boolean isFirstToken) {
        mIsFirstToken = isFirstToken;
    }

    public static boolean isFirstToken() {
        return mIsFirstToken;
    }
}
