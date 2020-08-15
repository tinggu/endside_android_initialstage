package com.ctfww.module.keyevents.datahelper;

import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.bean.KeyEventBean;
import com.ctfww.module.keyevents.bean.KeyEventTraceBean;
import com.ctfww.module.keyevents.bean.SomeoneStartEndTimeBean;
import com.ctfww.module.keyevents.bean.StartEndTimeBean;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 暂时写成函数的形式，后续改成通用的接口处理函数
 *
 */
public interface ICloudMethod {
    /**
     * 添加关键事件
     * @param keyEvent
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/uploadKeyEvent")
    Call<ResponseBody> addKeyEvent(@Body KeyEventBean keyEvent);

    /**
     * 删除关键事件
     * @param eventId 设备ID
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/deleteKeyEvent")
    Call<ResponseBody> deleteKeyEvent(@Body String eventId);

    /**
     * 获取所有关键事件
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/findKeyEvents")
    Call<ResponseBody> getAllKeyEvent(@Body StartEndTimeBean startEndTimeBean);

    /**
     * 获取某人所有关键事件
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/findSomeoneKeyEvents")
    Call<ResponseBody> getSomeoneAllKeyEvent(@Body SomeoneStartEndTimeBean someoneStartEndTimeBean);

    /**
     * 通过关键事件ID获取关键事件
     * @param eventId 设备ID
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getKeyEventById")
    Call<ResponseBody> getKeyEventById(@Body String eventId);

    /**
     * 通过ID来更新关键事件
     * @param keyEventBean 关键事件
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/updateDeviceBaseInfoById")
    Call<ResponseBody> updateKeyEventById(@Body KeyEventBean keyEventBean);

    /**
     * 上传文件 http://39.98.147.77:8003/fileMgt/uploadFile
     * @param file 文件
     * @return 返回值
     */
    @Multipart
    @POST("/microapkmgtserver/fileMgt/uploadFile")
    Call<ResponseBody> uploadFile(@Header("userId") String userId, @Part MultipartBody.Part file);

    @POST("/microcloudkeyevents/keyEvents/reportRepaired")
    Call<ResponseBody> reportRepaired(@Body String eventId);

    /**
     * 获取某个事件的处理过程
     * @param eventId
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getKeyEventActionList")
    Call<ResponseBody> getKeyEventActionList(@Body String eventId);

    /**
     * 上传事件操作
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/uploadKeyEventTrace")
    Call<ResponseBody> uploadKeyEventTrace(@Body KeyEventTrace info);

    /**
     * 获得未处理异常个数
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getNoEndKeyEventCount")
    Call<ResponseBody> getNoEndKeyEventCount(@Body QueryCondition condition);

    /**
     * 获得已处理异常列表
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getEndKeyEventList")
    Call<ResponseBody> getEndKeyEventList(@Body QueryCondition condition);

    /**
     * 获得未处理异常列表
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getNoEndKeyEventList")
    Call<ResponseBody> getNoEndKeyEventList(@Body QueryCondition condition);

    /**
     * 获得历史每天的异常统计
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getHistoryEveryDayKeyEventStatistics")
    Call<ResponseBody> getHistoryEveryDayKeyEventStatistics(@Body QueryCondition condition);

    /**
     * 获得某段时间内每个人的完成的异常单
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getEveryOneEndKeyEventStatistics")
    Call<ResponseBody> getEveryOneEndKeyEventStatistics(@Body QueryCondition condition);

    /**
     * 获得某人正处理的事件
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getSomeOneDoingKeyEvent")
    Call<ResponseBody> getSomeOneDoingKeyEvent(@Body QueryCondition condition);

    /**
     * 获得能抢的工单
     * @param groupId
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getCanBeSnatchedKeyEvent")
    Call<ResponseBody> getCanBeSnatchedKeyEvent(@Body String groupId);

    /**
     * 抢工单
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/snatchKeyEvent")
    Call<ResponseBody> snatchKeyEvent(@Body KeyEventTraceBean info);

    /**
     * 释放工单
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/freeKeyEvent")
    Call<ResponseBody> freeKeyEvent(@Body KeyEventTraceBean info);
}
