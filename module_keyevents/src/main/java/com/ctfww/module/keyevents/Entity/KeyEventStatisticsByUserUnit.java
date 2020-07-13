package com.ctfww.module.keyevents.Entity;

public class KeyEventStatisticsByUserUnit {
    private String userId;
    private int abnormalCount;
    private int endCount;

    private String nickName;
    private String headUrl;

    public KeyEventStatisticsByUserUnit() {

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setAbnormalCount(int abnormalCount) {
        this.abnormalCount = abnormalCount;
    }

    public int getAbnormalCount() {
        return abnormalCount;
    }

    public void setEndCount(int endCount) {
        this.endCount = endCount;
    }

    public int getEndCount() {
        return endCount;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getHeadUrl() {
        return headUrl;
    }
}
