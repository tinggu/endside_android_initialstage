package com.guoliang.module.user.bean;

public class GroupUserBean {
    private String userId;
    private String nickName;
    private String mobile;
    private String headUrl;
    private String role;

    public String toString() {
        return "userId = " + userId
                + ", nickName = " + nickName
                + ", mobile = " + mobile
                + ", headUrl = " + headUrl
                + ", role = " + role;
    }

    public GroupUserBean() {

    }

    public GroupUserBean(String userId, String nickName, String mobile, String headUrl, String role) {
        this.userId = userId;
        this.nickName = nickName;
        this.mobile = mobile;
        this.headUrl = headUrl;
        this.role = role;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
