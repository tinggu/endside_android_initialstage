package com.ctfww.module.tms.network;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CloudClient {

    private static final String TAG = "SelfCloudClient";

    private static final int VALIDE_DATA = 80000000;
    private static final String USER_OPEN_ID = "user_open_id";
    private static final String URL = "http://39.99.194.231:9090";

    private static final int MAX_CNT = 3;
    private static final int INTERVAL = 5 * 60 * 1000;

    private int lastRefreshTime = 0;

    ICloudMethod mCloudMethod;

    public static CloudClient getInstance() {
        return CloudClient.Inner.INSTANCE;
    }

    private static class Inner {
        private static final CloudClient INSTANCE = new CloudClient();
    }

    private CloudClient(){
    }

    public void init() {
        // 网络配置
        OkHttpClient.Builder okHttpBuild = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        // http log配置
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        okHttpBuild.addInterceptor(loggingInterceptor);

        // 添加header
        okHttpBuild.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder builder = request.newBuilder();

                return chain.proceed(builder.build());
            }
        });

        // 创建实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpBuild.build())
                .build();

        mCloudMethod = retrofit.create(ICloudMethod.class);
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

    private void processRsp(retrofit2.Response<ResponseBody> response, final String userId, final int cnt, final ICloudCallback callback) {
        LogUtils.i(TAG, "response code = " + response.code());

        int thisCnt = cnt;
        if (response.code() == 200) {
            try {
                if (response.body() != null) {
                    String data = response.body().string();
                    LogUtils.i(TAG, "data = " + data);
                    int resultCode = getResultCode(data);
                    LogUtils.i(TAG, "resultCode = " + resultCode);
                    if (resultCode == VALIDE_DATA) {
                        callback.onSuccess(data);
                    }
                    else {
                        if (thisCnt >= 3) {
                            callback.onError(resultCode, "数据错误！");
                            return;
                        }

                        ++thisCnt;
                        getAccessTokenByUserId(userId, thisCnt, callback);
                    }
                }else {
                    if (thisCnt >= 3) {
                        callback.onError(-9, "数据错误！");
                        return;
                    }

                    ++thisCnt;
                    getAccessTokenByUserId(userId, thisCnt, callback);
                }
            } catch (IOException e) {
                if (thisCnt >= 3) {
                    callback.onError(-8, "数据异常！");
                    LogUtils.e(TAG, "e.message = " + e.getMessage());
                    return;
                }

                ++thisCnt;
                getAccessTokenByUserId(userId, thisCnt, callback);
            }
        }

        if (response.code() != 200 || !response.isSuccessful()) {
            if (thisCnt >= 3) {
                callback.onError(response.code(), response.message());
                return;
            }

            ++thisCnt;
            getAccessTokenByUserId(userId, thisCnt, callback);
        }
    }

    public void getAccessTokenByUserId(final String userId, final int cnt, final ICloudCallback callback) {
        String url = "http://39.98.147.77:9090/token/getAccessTokenByUserId";
        Call<ResponseBody> responseBodyCall = mCloudMethod.getAccessTokenByUserId(userId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                processRsp(response, userId, cnt, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
