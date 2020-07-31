package com.ctfww.commonlib.entity;

public class GeneralRsp<T> {
    /**
     * 响应码：0-成功，1-失败
     */
    private String resultCode;

    /**
     * 响应信息描述
     */
    private String resultDesc;

    /**
     * 响应数据
     */
    private T data;

    public GeneralRsp() {

    }

    public GeneralRsp(String resultCode, String resultDesc, T data) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.data = data;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
