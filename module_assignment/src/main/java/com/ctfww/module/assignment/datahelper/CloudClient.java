package com.ctfww.module.assignment.datahelper;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.CloudClientRsp;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.RouteAssignment;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CloudClient {
    private final static String TAG = "CloudClient";

    private ICloudMethod mCloudMethod;

    public static CloudClient getInstance() {
        return CloudClient.Inner.INSTANCE;
    }

    private static class Inner {
        private static final CloudClient INSTANCE = new CloudClient();
    }

    private CloudClient(){
    }

    public void setCloudMethod(ICloudMethod method) {
        mCloudMethod = method;
    }

    public void synDeskAssignmentToCloud(CargoToCloud<DeskAssignment> info, final ICloudCallback callback) {
        LogUtils.i(TAG, "synDeskAssignmentToCloud: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synDeskAssignmentToCloud(info);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synDeskAssignmentFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synDeskAssignmentFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synDeskAssignmentFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void synRouteAssignmentToCloud(CargoToCloud<RouteAssignment> info, final ICloudCallback callback) {
        LogUtils.i(TAG, "synRouteAssignmentToCloud: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synRouteAssignmentToCloud(info);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synRouteAssignmentFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synRouteAssignmentFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synRouteAssignmentFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }
}
