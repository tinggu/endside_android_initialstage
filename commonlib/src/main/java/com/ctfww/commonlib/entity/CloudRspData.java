package com.ctfww.commonlib.entity;

public class CloudRspData {
    private int errCode;
    private String bodyStr;

    public CloudRspData() {
        errCode = 0;
        bodyStr = "未知数";
    }

    public CloudRspData(int errCode) {
        errCode = errCode;
        bodyStr = "未知数";
    }

    public CloudRspData(int errCode, String valStr) {
        this.errCode = errCode;
        this.bodyStr = valStr;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getBodyStr() {
        return bodyStr;
    }

    public void setBodyStr(String valStr) {
        this.bodyStr = bodyStr;
    }
}
