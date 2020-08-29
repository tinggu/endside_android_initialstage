package com.ctfww.module.desk.datahelper;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;
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

    public void synDeskInfoToCloud(CargoToCloud<DeskInfo> info, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synDeskInfoToCloud(info, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synDeskInfoToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synDeskInfoFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synDeskInfoFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<DeskInfo>>() {}.getType();
                List<DeskInfo> deskList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synDeskInfoFromCloud: deskList.size() = " + deskList.size());
                callback.onSuccess(deskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synDeskInfoFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synRouteSummaryToCloud(CargoToCloud<RouteSummary> info, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synRouteSummaryToCloud(info, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synRouteSummaryToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synRouteSummaryFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synRouteSummaryFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<RouteSummary>>() {}.getType();
                List<RouteSummary> routeSummaryList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synRouteSummaryFromCloud: routeSummaryList.size() = " + routeSummaryList.size());
                callback.onSuccess(routeSummaryList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synRouteSummaryFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synRouteDeskToCloud(CargoToCloud<RouteDesk> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synRouteDeskToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synRouteDeskToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synRouteDeskFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synRouteDeskFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<RouteDesk>>() {}.getType();
                List<RouteDesk> routeDeskList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synRouteDeskFromCloud: routeDeskList.size() = " + routeDeskList.size());
                callback.onSuccess(routeDeskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synRouteDeskFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
