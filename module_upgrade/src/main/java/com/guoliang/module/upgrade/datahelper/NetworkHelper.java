package com.guoliang.module.upgrade.datahelper;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.network.ICloudCallback;
import com.guoliang.commonlib.network.NetworkConst;
import com.guoliang.commonlib.utils.ApkUtils;
import com.guoliang.module.upgrade.entity.ApkVersionInfo;

public class NetworkHelper {
    private final static String TAG = "NetworkHelper";

    private NetworkHelper() {

    }

    private static class Inner {
        private static final NetworkHelper INSTANCE = new NetworkHelper();
    }

    public static NetworkHelper getInstance() {
        return NetworkHelper.Inner.INSTANCE;
    }

    public void getLatestApkVersion(String apkName, final IUIDataHelperCallback callback) {
        LogUtils.i("current", "getLatestApkVersion: " + apkName);
        CloudClient.getInstance().getLatestApkVersion(apkName, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                ApkVersionInfo apkVersionInfo = GsonUtils.fromJson(data, ApkVersionInfo.class);
                callback.onSuccess(apkVersionInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getLatestApkVersion: fail! errcode = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
