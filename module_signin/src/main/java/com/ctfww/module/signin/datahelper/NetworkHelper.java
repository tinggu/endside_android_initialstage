package com.ctfww.module.signin.datahelper;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.signin.entity.SigninInfo;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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

    public void synSigninToCloud(CargoToCloud<SigninInfo> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synSigninToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synSigninToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synSigninFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synSigninFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<SigninInfo>>() {}.getType();
                List<SigninInfo> signinList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synSigninFromCloud: signinList.size() = " + signinList.size());
                callback.onSuccess(signinList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synSigninFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
