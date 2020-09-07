package com.ctfww.module.assignment.datahelper;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.RouteAssignment;
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

    public void synDeskAssignmentToCloud(CargoToCloud<DeskAssignment> info, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synDeskAssignmentToCloud(info, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synDeskAssignmentToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synDeskAssignmentFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synDeskAssignmentFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<DeskAssignment>>() {}.getType();
                List<DeskAssignment> assignmentList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synDeskAssignmentFromCloud: assignmentList.size() = " + assignmentList.size());
                callback.onSuccess(assignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synDeskAssignmentFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synRouteAssignmentToCloud(CargoToCloud<RouteAssignment> info, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synRouteAssignmentToCloud(info, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synRouteAssignmentToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synRouteAssignmentFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synRouteAssignmentFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<RouteAssignment>>() {}.getType();
                List<RouteAssignment> assignmentList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synRouteAssignmentFromCloud: assignmentList.size() = " + assignmentList.size());
                callback.onSuccess(assignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synRouteAssignmentFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
