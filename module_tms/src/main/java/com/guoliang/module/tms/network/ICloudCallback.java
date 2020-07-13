package com.guoliang.module.tms.network;

public interface ICloudCallback {
    /**
     * 请求响应成功
     * @param data 返回的数据
     */
    void onSuccess(String data);

    /**
     * 请求响应成功，返回错误码
     * @param code 错误码
     * @param errorMsg 错误信息
     */
    void onError(int code, String errorMsg);

    /**
     * 请求失败
     * @param errorMsg 错误信息
     */
    void onFailure(String errorMsg);
}
