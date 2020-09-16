package com.ctfww.module.signin.datahelper;

import android.content.Context;

import com.ctfww.commonlib.entity.AbcLine;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.signin.datahelper.dbhelper.DBHelper;

import java.util.List;

public class Utils {
    public static void start(Context ctx){
        // 初始化签到点网络化模块
        ICloudMethod deskMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(deskMethod);

        DBHelper.getInstance().init(ctx);

        // 初始化用户中心的数据同步
 //       com.ctfww.module.user.storage.db.SelfSynDB.getInstance().startSyn();
    }
}
