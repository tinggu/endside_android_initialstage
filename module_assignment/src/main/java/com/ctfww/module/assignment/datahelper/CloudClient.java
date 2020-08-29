package com.ctfww.module.assignment.datahelper;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.CloudClientRsp;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.assignment.entity.AssignmentInfo;

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

    public void synAssignmentInfoToCloud(CargoToCloud<AssignmentInfo> info, final ICloudCallback callback) {
        LogUtils.i(TAG, "synAssignmentInfoToCloud: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synAssignmentInfoToCloud(info);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synAssignmentInfoFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synAssignmentInfoFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synAssignmentInfoFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }
}
