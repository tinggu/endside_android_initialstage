package com.guoliang.module.keepwatch.entity;

public class KeepWatchSigninStatistics {
    private String userId;
    private String nickName;
    private int deskId;
    private String deskName;
    private String deskType;
    private int frequency;
    private int signinCount;
    private long timeStamp;
    private int abnormalCount;
    private int repairedCount;

    public String toString() {
        return "userId = " + userId
                + ", nickName = " + nickName
                + ", deskId = " + deskId
                + ", deskName = " + deskName
                + ", deskType = " + deskType
                + ", frequency = " + frequency
                + ", signinCount = " + signinCount
                + ", timeStamp = " + timeStamp
                + ", abnormalCount = " + abnormalCount
                + ", repairedCount = " + repairedCount;
    }

    public KeepWatchSigninStatistics() {

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setSigninCount(int signinCount) {
        this.signinCount = signinCount;
    }

    public int getSigninCount() {
        return signinCount;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setAbnormalCount(int abnormalCount) {
        this.abnormalCount = abnormalCount;
    }

    public int getAbnormalCount() {
        return abnormalCount;
    }

    public void setRepairedCount(int repairedCount) {
        this.repairedCount = repairedCount;
    }

    public int getRepairedCount() {
        return repairedCount;
    }
}
