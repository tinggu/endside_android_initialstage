package com.ctfww.monitor.application;

import com.ctfww.commonlib.base.BaseApplication;
import com.ctfww.commonlib.network.CloudClient;
import com.ctfww.commonlib.storage.sp.SPUtil;
//import com.ctfww.module.device.network.ICloudMethod;


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
        com.ctfww.module.tms.network.CloudClient.getInstance().init();
        com.ctfww.module.tms.datahelper.NetworkHelper.getInstance().startTimedRefresh();

        // 初始化cloud
        CloudClient.getInstance().init("http://www.littlepine.cn");

        // 集成模式下初始化各个业务模块的网络模块和数据库模块
//        com.ctfww.module.user.Util.start(this);
//        com.ctfww.module.keepwatch.Utils.start(this);
//        com.ctfww.module.device.Util.start(this);


        // 电流监控App专属初始化代码，获取用户信息后获取设备信息
//        com.ctfww.module.user.Util.setInitDataCallback(new IInitDataCallback() {
//            @Override
//            public void init() {
//                com.ctfww.module.device.Util.initDevices();
//            }
//        });

    }
}
