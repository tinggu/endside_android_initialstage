package com.guoliang.module.user.bean;

public class SMSLoginBean {

    private String mobile;

    private String verifiCode;

    public SMSLoginBean(String mobile, String verifiCode) {
        this.mobile = mobile;
        this.verifiCode = verifiCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifiCode() {
        return verifiCode;
    }

    public void setVerifiCode(String verifiCode) {
        this.verifiCode = verifiCode;
    }
}
