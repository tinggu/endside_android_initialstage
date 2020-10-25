package com.ctfww.module.signin.datahelper;

import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.signin.entity.SigninInfo;

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
     * 同步签到信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/signin/synSigninInfoToCloud")
    Call<ResponseBody> synSigninInfoToCloud(@Body CargoToCloud<SigninInfo> info);

    /**
     * 从云上同步签到信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/signin/synSigninInfoFromCloud")
    Call<ResponseBody> synSigninInfoFromCloud(@Body QueryCondition condition);
}
