package com.ctfww.module.desk.datahelper;

import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ICloudMethod {
    /**
     * 同步签到点上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/desk/synDeskInfoToCloud")
    Call<ResponseBody> synDeskInfoToCloud(@Body CargoToCloud<DeskInfo> info);

    /**
     * 从云上同步签到点
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/desk/synDeskInfoFromCloud")
    Call<ResponseBody> synDeskInfoFromCloud(@Body QueryCondition condition);

    /**
     * 同步签到路线概要信息
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/desk/synRouteSummaryToCloud")
    Call<ResponseBody> synRouteSummaryToCloud(@Body CargoToCloud<RouteSummary> info);

    /**
     * 从云上同步路线概要信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/desk/synRouteSummaryFromCloud")
    Call<ResponseBody> synRouteSummaryFromCloud(@Body QueryCondition condition);

    /**
     * 同步签到路线签到点信息
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/desk/synRouteDeskToCloud")
    Call<ResponseBody> synRouteDeskToCloud(@Body CargoToCloud<RouteDesk> info);

    /**
     * 从云上同步路线签到点信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/desk/synRouteDeskFromCloud")
    Call<ResponseBody> synRouteDeskFromCloud(@Body QueryCondition condition);
}
