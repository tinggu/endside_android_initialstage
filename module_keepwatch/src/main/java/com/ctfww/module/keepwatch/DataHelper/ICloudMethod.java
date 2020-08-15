package com.ctfww.module.keepwatch.DataHelper;

import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.entity.Cargo;
import com.ctfww.module.keepwatch.bean.KeepWatchAssignmentBean;
import com.ctfww.module.keepwatch.bean.KeepWatchSigninBean;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRoute;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;

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
    /***********************new**********************/

    /**
     * 同步签到点
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchDeskList")
    Call<ResponseBody> synKeepWatchDesk(@Body Cargo<KeepWatchDesk> info);

    /**
     * 同步签到点上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchDeskToCloud")
    Call<ResponseBody> synKeepWatchDeskToCloud(@Body CargoToCloud<KeepWatchDesk> info);

    /**
     * 从云上同步签到点
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchDeskFromCloud")
    Call<ResponseBody> synKeepWatchDeskFromCloud(@Body QueryCondition condition);

    /**
     * 同步签到路线概要信息
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchRouteSummaryToCloud")
    Call<ResponseBody> synKeepWatchRouteSummaryToCloud(@Body CargoToCloud<KeepWatchRouteSummary> info);

    /**
     * 从云上同步路线概要信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchRouteSummaryFromCloud")
    Call<ResponseBody> synKeepWatchRouteSummaryFromCloud(@Body QueryCondition condition);

    /**
     * 同步签到路线签到点信息
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchRouteDeskToCloud")
    Call<ResponseBody> synKeepWatchRouteDeskToCloud(@Body CargoToCloud<KeepWatchRouteDesk> info);

    /**
     * 从云上同步路线签到点信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchRouteDeskFromCloud")
    Call<ResponseBody> synKeepWatchRouteDeskFromCloud(@Body QueryCondition condition);


    /**
     * 同步签到信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchSigninToCloud")
    Call<ResponseBody> synKeepWatchSigninToCloud(@Body CargoToCloud<KeepWatchSigninInfo> info);

    /**
     * 从云上同步签到信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchSigninFromCloud")
    Call<ResponseBody> synKeepWatchSigninFromCloud(@Body QueryCondition condition);

    /**
     * 同步任务上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchAssignmentToCloud")
    Call<ResponseBody> synKeepWatchAssignmentToCloud(@Body CargoToCloud<KeepWatchAssignment> info);

    /**
     * 从云上同步任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchAssignmentFromCloud")
    Call<ResponseBody> synKeepWatchAssignmentFromCloud(@Body QueryCondition condition);

    /**
     * 获得成员动态
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getPersonTrends")
    Call<ResponseBody> getPersonTrends(@Body QueryCondition condition);

    /**
     * 获得排行榜
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchRanking")
    Call<ResponseBody> getKeepWatchRanking(@Body QueryCondition condition);

    /**
     * 转移任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/transferKeepWatchAssignment")
    Call<ResponseBody> transferKeepWatchAssignment(@Body QueryCondition condition);

    /**
     * 收回任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/takeBackKeepWatchAssignment")
    Call<ResponseBody> takeBackKeepWatchAssignment(@Body QueryCondition condition);



    /**
     * 获取群组概要
     * @param groupId
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchGroupSummary")
    Call<ResponseBody> getKeepWatchGroupSummary(@Body String groupId);

    /**
     * 获得漏检点
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchSigninLeak")
    Call<ResponseBody> getKeepWatchSigninLeak(@Body QueryCondition condition);

    /**
     * 获得签到统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchSigninStatistics")
    Call<ResponseBody> getKeepWatchSigninStatistics(@Body QueryCondition condition);

    /**
     * 获得签到信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchSigninList")
    Call<ResponseBody> getKeepWatchSigninList(@Body QueryCondition condition);

    /**
     * 获得某天的任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchAssignmentList")
    Call<ResponseBody> getKeepWatchAssignmentList(@Body QueryCondition condition);

    /**
     * 获得周期任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchPeriodAssignmentList")
    Call<ResponseBody> getKeepWatchPeriodAssignmentList(@Body QueryCondition condition);

    /**
     * 获得某段时间的统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchStatistics")
    Call<ResponseBody> getKeepWatchStatistics(@Body QueryCondition condition);

    /**
     * 获得某段时间内签到点任务和签到情况统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchAssignmentAndSigninStatisticsForDesk")
    Call<ResponseBody> getKeepWatchAssignmentAndSigninStatisticsForDesk(@Body QueryCondition condition);

    /**
     * 获得某段时间内按日的统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchStatisticsByPeriodByDayUnit")
    Call<ResponseBody> getKeepWatchStatisticsByPeriodByDayUnit(@Body QueryCondition condition);

    /**
     * 获得某段时间内漏检点
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchLeakStatisticsForDesk")
    Call<ResponseBody> getKeepWatchLeakStatisticsForDesk(@Body QueryCondition condition);

    /**
     * 获得一段时间内点的签到状态
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchSigninStatisticsForDesk")
    Call<ResponseBody> getKeepWatchSigninStatisticsForDesk(@Body QueryCondition condition);

    /**
     * 获得历史每一天人员执行任务情况统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getHistoryEveryDayStatistics")
    Call<ResponseBody> getHistoryEveryDayStatistics(@Body QueryCondition condition);

    /**
     * 增加巡检路线路线
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/addKeepWatchRoute")
    Call<ResponseBody> addKeepWatchRoute(@Body KeepWatchRoute info);

    /**
     * 获得线路
     * @param groupId
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchRoute")
    Call<ResponseBody> getKeepWatchRoute(@Body String groupId);
}
