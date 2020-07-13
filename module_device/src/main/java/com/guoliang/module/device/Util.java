package com.guoliang.module.device;

import android.content.Context;

import com.guoliang.module.device.network.CloudClient;
import com.guoliang.module.device.network.ICloudMethod;
import com.guoliang.module.device.network.datahelper.NetworkHelper;
import com.guoliang.module.device.storage.DatabaseUtil;

public class Util {
    public static void start(Context ctx){
        // 初始化用户中心网络化模块
        ICloudMethod deviceMethod = com.guoliang.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(deviceMethod);

        DatabaseUtil.getInstance().init(ctx);
    }

    public static void initDevices() {
        NetworkHelper.getInstance().getAllDevices(null);
    }
}
