package com.ctfww.module.keepwatch.DataHelper;

import com.ctfww.commonlib.bean.QueryConditionBean;
import com.ctfww.module.keepwatch.bean.KeepWatchAssignmentBean;
import com.ctfww.module.keepwatch.bean.KeepWatchDeskBean;
import com.ctfww.module.keepwatch.bean.KeepWatchSigninBean;

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
     * 签到
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/addKeepWatchSignin")
    Call<ResponseBody> addKeepWatchSignin(@Body KeepWatchSigninBean info);

    /**
     * 获得今天的巡检信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getTodayKeepWatchInfo")
    Call<ResponseBody> getTodayKeepWatchInfo(@Body QueryConditionBean condition);

    /**
     * 获得成员动态
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getPersonTrends")
    Call<ResponseBody> getPersonTrends(@Body QueryConditionBean condition);

    /**
     * 获得排行榜
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchRanking")
    Call<ResponseBody> getKeepWatchRanking(@Body QueryConditionBean condition);

    /**
     * 增加任务
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/addKeepWatchAssignment")
    Call<ResponseBody> addKeepWatchAssignment(@Body KeepWatchAssignmentBean info);

    /**
     * 删除任务
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/deleteKeepWatchAssignment")
    Call<ResponseBody> deleteKeepWatchAssignment(@Body KeepWatchAssignmentBean info);

    /**
     * 转移任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/transferKeepWatchAssignment")
    Call<ResponseBody> transferKeepWatchAssignment(@Body QueryConditionBean condition);

    /**
     * 收回任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/takeBackKeepWatchAssignment")
    Call<ResponseBody> takeBackKeepWatchAssignment(@Body QueryConditionBean condition);

    /**
     * 增加签到点
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/addKeepWatchDesk")
    Call<ResponseBody> addKeepWatchDesk(@Body KeepWatchDeskBean info);

    /**
     * 删除签到点
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/deleteKeepWatchDesk")
    Call<ResponseBody> deleteKeepWatchDesk(@Body KeepWatchDeskBean info);

    /**
     * 更新签到点的附加信息
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/updateKeepWatchDeskAddition")
    Call<ResponseBody> updateKeepWatchDeskAddition(@Body KeepWatchDeskBean info);

    /**
     * 更新签到点指纹
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/updateKeepWatchDeskFingerPrint")
    Call<ResponseBody> updateKeepWatchDeskFingerPrint(@Body KeepWatchDeskBean info);

    /**
     * 获取签到点
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchDesk")
    Call<ResponseBody> getKeepWatchDesk(@Body KeepWatchDeskBean info);

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
    Call<ResponseBody> getKeepWatchSigninLeak(@Body QueryConditionBean condition);

    /**
     * 获得签到统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchSigninStatistics")
    Call<ResponseBody> getKeepWatchSigninStatistics(@Body QueryConditionBean condition);

    /**
     * 获得签到信息
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchSigninList")
    Call<ResponseBody> getKeepWatchSigninList(@Body QueryConditionBean condition);

    /**
     * 获得某天的任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchAssignmentList")
    Call<ResponseBody> getKeepWatchAssignmentList(@Body QueryConditionBean condition);

    /**
     * 获得周期任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchPeriodAssignmentList")
    Call<ResponseBody> getKeepWatchPeriodAssignmentList(@Body QueryConditionBean condition);

    /**
     * 获得某段时间的统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchStatistics")
    Call<ResponseBody> getKeepWatchStatistics(@Body QueryConditionBean condition);

    /**
     * 获得某段时间内签到点任务和签到情况统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchAssignmentAndSigninStatisticsForDesk")
    Call<ResponseBody> getKeepWatchAssignmentAndSigninStatisticsForDesk(@Body QueryConditionBean condition);

    /**
     * 获得某段时间内按日的统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchStatisticsByPeriodByDayUnit")
    Call<ResponseBody> getKeepWatchStatisticsByPeriodByDayUnit(@Body QueryConditionBean condition);

    /**
     * 获得某段时间内漏检点
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchLeakStatisticsForDesk")
    Call<ResponseBody> getKeepWatchLeakStatisticsForDesk(@Body QueryConditionBean condition);

    /**
     * 获得一段时间内点的签到状态
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getKeepWatchSigninStatisticsForDesk")
    Call<ResponseBody> getKeepWatchSigninStatisticsForDesk(@Body QueryConditionBean condition);

    /**
     * 获得历史每一天人员执行任务情况统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keepWatch/getHistoryEveryDayStatistics")
    Call<ResponseBody> getHistoryEveryDayStatistics(@Body QueryConditionBean condition);
}
