package com.ctfww.module.assignment.datahelper;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.assignment.entity.TodayAssignment;
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

    public void synAssignmentToCloud(CargoToCloud<AssignmentInfo> info, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synAssignmentToCloud(info, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synAssignmentToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synAssignmentFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synAssignmentFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<AssignmentInfo>>() {}.getType();
                List<AssignmentInfo> assignmentList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synAssignmentFromCloud: assignmentList.size() = " + assignmentList.size());
                callback.onSuccess(assignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synAssignmentFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synTodayAssignmentFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synTodayAssignmentFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<TodayAssignment>>() {}.getType();
                List<TodayAssignment> assignmentList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synTodayAssignmentFromCloud: assignmentList.size() = " + assignmentList.size());
                callback.onSuccess(assignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synTodayAssignmentFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
