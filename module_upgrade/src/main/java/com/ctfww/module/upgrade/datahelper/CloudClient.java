package com.ctfww.module.upgrade.datahelper;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.CloudRspData;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CloudClient {

    private static final String TAG = "SelfCloudClient";

    private final static int VALIDE_DATA = 80000000;
    private final static String mUrl = "http://www.littlepine.cn";

    private ICloudMethod mCloudMethod;

    public static CloudClient getInstance() {
        return CloudClient.Inner.INSTANCE;
    }

    private static class Inner {
        private static final CloudClient INSTANCE = new CloudClient();
    }

    private CloudClient(){
    }

    public void createCloudMethod() {
        mCloudMethod = NetworkUtils.createCloudMethod(ICloudMethod.class, mUrl);
    }

    private int getResultCode(String data) {
        try {
            JSONObject jsonObj = new JSONObject(data);
            return jsonObj.getInt("resultCode");
        }
        catch (JSONException e) {
            return -10;
        }
    }

    private CloudRspData parseRsp(retrofit2.Response<ResponseBody> response) {
        LogUtils.i(TAG, "response code = " + response.code());
        if (response.code() != 200) {
            return new CloudRspData(response.code(), "网络有问题");
        }

        if (!response.isSuccessful()) {
            return new CloudRspData(-100, "网络有问题");
        }

        try {
            if (response.body() != null) {
                String data = response.body().string();
                LogUtils.i(TAG, "data = " + data);
                CloudRspData cloudRspData = new CloudRspData(getResultCode(data), data) ;
                return cloudRspData;
            }else {
                return new CloudRspData(-9, "没有body");
            }
        } catch (IOException e) {
            LogUtils.e(TAG, "e.message = " + e.getMessage());
            return new CloudRspData(-8, "body数据有异常");
        }
    }

    private void processGeneralRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    callback.onSuccess(cloudRspData.getBodyStr());
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    private void processSingleObjRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        callback.onSuccess(jsonObjectData.toString());
                    }
                    catch (JSONException e) {
                        LogUtils.e(TAG, "e.message = " + e.getMessage());
                        callback.onError(-10, "数据格式错误");
                    }
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    private void processListRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        callback.onSuccess(jsonArray.toString());
                    }
                    catch (JSONException e) {
                        LogUtils.e(TAG, "e.message = " + e.getMessage());
                        callback.onError(-10, "数据格式错误");
                    }
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getLatestApkVersion(String appName, final ICloudCallback callback) {
        LogUtils.i(TAG, "getLatestApkVersion: appName = " + appName);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getLatestApkVersion(appName);
        processSingleObjRsp(responseBodyCall, callback);
    }
}
