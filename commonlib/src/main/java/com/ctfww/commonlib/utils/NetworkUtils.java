package com.ctfww.commonlib.utils;

import android.util.Log;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.storage.sp.SPConstant;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    public static <T> T createCloudMethod(final Class<T> service, String url) {
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

                Request.Builder builder = request.newBuilder()
                        .header("openId", SPStaticUtils.getString(SPConstant.USER_OPEN_ID))
                        .header("accessToken", SPStaticUtils.getString(SPConstant.USER_ACCESS_TOKEN));
                Log.i("CloudClient", SPStaticUtils.getString(SPConstant.USER_OPEN_ID) + ", " + SPStaticUtils.getString(SPConstant.USER_ACCESS_TOKEN));

                return chain.proceed(builder.build());
            }
        });

        // 创建实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpBuild.build())
                .build();

        return retrofit.create(service);
    }
}
