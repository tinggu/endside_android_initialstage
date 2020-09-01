package com.ctfww.module.user.datahelper;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;

public class Utils {
    public static void start(Context ctx){
        // 初始化用户中心网络化模块
        ICloudMethod userMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(userMethod);

        // 初始化用户中心数据库模块
        DBHelper.getInstance().init(ctx);

        // 初始化用户中心的数据同步
 //       com.ctfww.module.user.storage.db.SelfSynDB.getInstance().startSyn();
    }

    public static boolean isLogined() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        return !TextUtils.isEmpty(userId);
    }

    public static boolean isValidMobileNum(String str) {
        final int mobileNumLen = 11;
        return str.length() == mobileNumLen;
    }

    public static boolean isValidSMS(String str) {
        final int smsLen = 6;
        return str.length() == smsLen;
    }

    public static boolean isValidPassword(String str) {
        final int smsLen = 6;
        return str.length() >= smsLen;
    }
}
