package com.ctfww.module.keepwatch.datahelper;

import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.entity.SigninInfo;

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
     * 同步签到信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchSigninToCloud")
    Call<ResponseBody> synKeepWatchSigninToCloud(@Body CargoToCloud<SigninInfo> info);

    /**
     * 从云上同步签到信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synKeepWatchSigninFromCloud")
    Call<ResponseBody> synKeepWatchSigninFromCloud(@Body QueryCondition condition);

    /**
     * 从云上同步今天动态
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synTodayKeepWatchPersonTrendsFromCloud")
    Call<ResponseBody> synTodayKeepWatchPersonTrendsFromCloud(@Body QueryCondition condition);

    /**
     * 从云上同步今天排行
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synTodayKeepWatchRankingFromCloud")
    Call<ResponseBody> synTodayKeepWatchRankingFromCloud(@Body QueryCondition condition);

    /**
     * 从云上同步今天签到点签到统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/synTodayKeepWatchAssignmetnFinishStatusFromCloud")
    Call<ResponseBody> synTodayKeepWatchAssignmetnFinishStatusFromCloud(@Body QueryCondition condition);

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
}
