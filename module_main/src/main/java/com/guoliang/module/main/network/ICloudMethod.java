package com.guoliang.module.main.network;

import com.guoliang.module.main.bean.ConditionBean;

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
     * 根据时间段获取每个时间点各个设备状态的个数
     * @param condition 查询条件
     * @return 返回值
     */
    @POST("/microdevicemgtserver/deviceStatus/getDevCountGroupByStatusWithCond")
    Call<ResponseBody> getDevCountGroupByStatusWithCond(@Body ConditionBean condition);

    /**
     * 通过设备Id和时间段来获取设备电流信息
     * @param condition 查询条件
     * @return 返回值
     */
    @POST("/microdevicemgtserver/deviceStatus/getDevElecStatusInfoByDevIdAndCond")
    Call<ResponseBody> getDevElecStatusInfoByDevIdAndCond(@Body ConditionBean condition);

    /**
     * 通过设备Id和时间段来获取设备状态
     * @param condition 查询条件
     * @return 返回值
     */
    @POST("/microdevicemgtserver/deviceStatus/getDevStatusInfoByDevIdAndCond")
    Call<ResponseBody> getDeviceBaseInfoById(@Body ConditionBean condition);

}
