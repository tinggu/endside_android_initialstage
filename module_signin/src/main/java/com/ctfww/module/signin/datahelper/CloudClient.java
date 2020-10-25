package com.ctfww.module.signin.datahelper;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.CloudClientRsp;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.signin.entity.SigninInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CloudClient {

    private static final String TAG = "SelfCloudClient";

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


    public void synSigninInfoToCloud(CargoToCloud<SigninInfo> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "synSigninInfoToCloud: cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synSigninInfoToCloud(cargoToCloud);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synSigninInfoFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synSigninInfoFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synSigninInfoFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }
}
