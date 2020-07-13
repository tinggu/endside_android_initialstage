package com.ctfww.module.device;

import android.content.Context;

import com.ctfww.module.device.network.CloudClient;
import com.ctfww.module.device.network.ICloudMethod;
import com.ctfww.module.device.network.datahelper.NetworkHelper;
import com.ctfww.module.device.storage.DatabaseUtil;

public class Util {
    public static void start(Context ctx){
        // 初始化用户中心网络化模块
        ICloudMethod deviceMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(deviceMethod);

        DatabaseUtil.getInstance().init(ctx);
    }

    public static void initDevices() {
        NetworkHelper.getInstance().getAllDevices(null);
    }
}
