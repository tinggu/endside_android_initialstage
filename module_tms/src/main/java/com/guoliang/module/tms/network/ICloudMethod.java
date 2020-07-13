package com.guoliang.module.tms.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 暂时写成函数的形式，后续改成通用的接口处理函数
 *
 */
public interface ICloudMethod {


    /**
     * 获取用户token
     * @param userId 用户ID
     * @return 返回值
     */
    @POST("/token/getAccessTokenByUserId")
    Call<ResponseBody> getAccessTokenByUserId(@Body String userId);
}
