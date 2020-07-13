package com.guoliang.monitor.application;

import com.guoliang.commonlib.base.BaseApplication;
import com.guoliang.commonlib.network.CloudClient;
import com.guoliang.commonlib.storage.sp.SPUtil;
//import com.guoliang.module.device.network.ICloudMethod;


/**
 * 原则：
 * 1. 不要Application对象中缓存数据，组件间数据共享用intent机制或者SP
 *
 *
 */
public class MonitorApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化SP，设置name
        SPUtil.setSPFileName("monitor");

        // 初始化tms
        com.guoliang.module.tms.network.CloudClient.getInstance().init();
        com.guoliang.module.tms.datahelper.NetworkHelper.getInstance().startTimedRefresh();

        // 初始化cloud
        CloudClient.getInstance().init("http://www.littlepine.cn");

        // 集成模式下初始化各个业务模块的网络模块和数据库模块
//        com.guoliang.module.user.Util.start(this);
//        com.guoliang.module.keepwatch.Utils.start(this);
//        com.guoliang.module.device.Util.start(this);


        // 电流监控App专属初始化代码，获取用户信息后获取设备信息
//        com.guoliang.module.user.Util.setInitDataCallback(new IInitDataCallback() {
//            @Override
//            public void init() {
//                com.guoliang.module.device.Util.initDevices();
//            }
//        });

    }
}
