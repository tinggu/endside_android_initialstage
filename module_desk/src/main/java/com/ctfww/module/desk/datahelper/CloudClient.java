package com.ctfww.module.desk.datahelper;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.CloudClientRsp;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.ctfww.commonlib.network.CloudClientRsp.processListRsp;

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

    public void synDeskInfoToCloud(CargoToCloud<DeskInfo> info, final ICloudCallback callback) {
        LogUtils.i(TAG, "synDeskInfoToCloud: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synDeskInfoToCloud(info);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synDeskInfoFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synDeskInfoFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synDeskInfoFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void synRouteSummaryToCloud(CargoToCloud<RouteSummary> info, final ICloudCallback callback) {
        LogUtils.i(TAG, "synRouteSummaryToCloud: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synRouteSummaryToCloud(info);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synRouteSummaryFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synRouteSummaryFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synRouteSummaryFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void synRouteDeskToCloud(CargoToCloud<RouteDesk> info, final ICloudCallback callback) {
        LogUtils.i(TAG, "synRouteDeskToCloud: info = " + info.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synRouteDeskToCloud(info);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synRouteDeskFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synRouteDeskFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synRouteDeskFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }
}
