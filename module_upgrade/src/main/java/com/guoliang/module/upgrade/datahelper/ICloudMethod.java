package com.guoliang.module.upgrade.datahelper;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 暂时写成函数的形式，后续改成通用的接口处理函数
 *
 */
public interface ICloudMethod {
    /**
     * 获取App最新版本信息
     * @param appName
     * @return 返回值
     */
    @POST("/microapkmgtserver/apkMgt/getLatestApkVersion")
    Call<ResponseBody> getLatestApkVersion(@Body String appName);
}
