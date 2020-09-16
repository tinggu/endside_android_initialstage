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


    public void synSigninToCloud(CargoToCloud<SigninInfo> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "synSigninToCloud: cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synSigninToCloud(cargoToCloud);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void synSigninFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synSigninFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synSigninFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }
}
