package com.ctfww.module.device.network;

import com.ctfww.module.device.bean.DeviceInfoBean;

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
     * 添加设备信息
     * @param info 设备信息
     * @return 返回值
     */
    @POST("/microdevicemgtserver/deviceMgt/addDeviceInfo")
    Call<ResponseBody> addDeviceInfo(@Body DeviceInfoBean info);

    /**
     * 删除设备
     * @param deviceId 设备ID
     * @return 返回值
     */
    @POST("/microdevicemgtserver/deviceMgt/deleteDevice")
    Call<ResponseBody> deleteDevice(@Body String deviceId);

    /**
     * 获取所有设备信息
     * @return 返回值
     */
    @POST("/microdevicemgtserver/deviceMgt/getAllDeviceInfo")
    Call<ResponseBody> getAllDeviceInfo();

    /**
     * 通过设备ID来获取设备信息
     * @param deviceId 设备ID
     * @return 返回值
     */
    @POST("/microdevicemgtserver/deviceMgt/getDeviceBaseInfoById")
    Call<ResponseBody> getDeviceBaseInfoById(@Body String deviceId);

    /**
     * 通过设备ID来更新设备信息
     * @param info 设备信息
     * @return 返回值
     */
    @POST("/microdevicemgtserver/deviceMgt/updateDeviceBaseInfoById")
    Call<ResponseBody> updateDeviceBaseInfoById(@Body DeviceInfoBean info);

}
