package com.ctfww.module.assignment.datahelper;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.CloudClientRsp;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.assignment.entity.TodayAssignment;

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

    public void synAssignmentToCloud(CargoToCloud<AssignmentInfo> info, final ICloudCallback callback) {
        LogUtils.i(TAG, "synAssignmentToCloud: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synAssignmentToCloud(info);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synAssignmentFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synAssignmentFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synAssignmentFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void synTodayAssignmentFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synTodayAssignmentFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synTodayAssignmentFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }
}
