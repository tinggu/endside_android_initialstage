package com.guoliang.module.user.bean;

public class UserInfoBean {

    /**
     * accessToken : string
     * birthday : string
     * blogNum : string
     * email : string
     * gender : 0
     * headUrl : string
     * mobile : string
     * nickName : string
     * password : string
     * qqNum : string
     * registerTimestamp : 0
     * userId : string
     * wechatNum : string
     */

    private String accessToken;
    private String birthday;
    private String blogNum;
    private String email;
    private int gender;
    private String headUrl;
    private String mobile;
    private String nickName;
    private String password;
    private String qqNum;
    private long registerTimestamp;
    private long modifyTimestamp;
    private String userId;
    private String wechatNum;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBlogNum() {
        return blogNum;
    }

    public void setBlogNum(String blogNum) {
        this.blogNum = blogNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public long getModifyTimestamp() {
        return modifyTimestamp;
    }

    public void setModifyTimestamp(long modifyTimestamp) {
        this.modifyTimestamp = modifyTimestamp;
    }

    public long getRegisterTimestamp() {
        return registerTimestamp;
    }

    public void setRegisterTimestamp(long registerTimestamp) {
        this.registerTimestamp = registerTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "accessToken='" + accessToken + '\'' +
                ", birthday='" + birthday + '\'' +
                ", blogNum='" + blogNum + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", headUrl='" + headUrl + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", qqNum='" + qqNum + '\'' +
                ", registerTimestamp=" + registerTimestamp +
                ", userId='" + userId + '\'' +
                ", wechatNum='" + wechatNum + '\'' +
                '}';
    }
}
