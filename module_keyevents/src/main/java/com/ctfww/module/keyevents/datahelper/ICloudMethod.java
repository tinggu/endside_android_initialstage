package com.ctfww.module.keyevents.datahelper;

import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;

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
     * 同步关键事件上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyevent/synKeyEventToCloud")
    Call<ResponseBody> synKeyEventToCloud(@Body CargoToCloud<KeyEvent> info);

    /**
     * 从云上同步关键事件
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyevent/synKeyEventFromCloud")
    Call<ResponseBody> synKeyEventFromCloud(@Body QueryCondition condition);

    /**
     * 同步关键事件处理状态上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyevent/synKeyEventTraceToCloud")
    Call<ResponseBody> synKeyEventTraceToCloud(@Body CargoToCloud<KeyEventTrace> info);

    /**
     * 从云上同步关键事件处理状态
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyevent/synKeyEventTraceFromCloud")
    Call<ResponseBody> synKeyEventTraceFromCloud(@Body QueryCondition condition);

    /**
     * 上传文件 http://39.98.147.77:8003/fileMgt/uploadFile
     * @param file 文件
     * @return 返回值
     */
    @Multipart
    @POST("/microapkmgtserver/fileMgt/uploadFile")
    Call<ResponseBody> uploadFile(@Header("userId") String userId, @Part MultipartBody.Part file);

    /**
     * 获取某个事件的处理过程
     * @param eventId
     * @return 返回值
     */
    @POST("/microcloudkeyevents/keyEvents/getKeyEventActionList")
    Call<ResponseBody> getKeyEventActionList(@Body String eventId);


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
}
