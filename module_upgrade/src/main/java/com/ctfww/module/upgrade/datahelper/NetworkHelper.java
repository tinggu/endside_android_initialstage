package com.ctfww.module.upgrade.datahelper;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.upgrade.entity.ApkVersionInfo;

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
