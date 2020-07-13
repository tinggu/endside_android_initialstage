package com.guoliang.commonlib.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.guoliang.commonlib.BuildConfig;
import com.guoliang.commonlib.crash.CrashHandler;

/**
 * Application基类
 *
 * 常用工具类：
 *
 * 1. 全局context获取：Utils.getApp()
 * 2. SharedPreferences: SPStaticUtils
 * 3. Log打印：LogUtils
 *
 * 备用工具类：
 *
 * 1. crash抓取：CrashUtils
 *
 */

public class BaseApplication extends Application {

//    private DaoSession daoSession;

    public static BaseApplication instances;

    @Override
    public void onCreate() {
        super.onCreate();

        instances = this;

        // 初始化组件路由
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);

        // 初始化utilCode
        Utils.init(this);

        // 初始化crashHandler
        CrashHandler.getInstance().init(this);
    }

    public static BaseApplication getInstances() {
        return instances;
    }

}
