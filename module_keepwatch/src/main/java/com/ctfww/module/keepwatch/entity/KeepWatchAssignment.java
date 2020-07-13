package com.ctfww.module.keepwatch.entity;

public class KeepWatchAssignment {
    private String groupId;
    private int deskId;
    private String userId;
    private String circleType;
    private long startTime;
    private long endTime;
    private int frequency;

    private String nickName;
    private String deskName;
    private String deskType;

    public String toString() {
        return "groupId = " + groupId
                + ", deskId = " + deskId
                + ", userId = " + userId
                + ", circleType = " + circleType
                + ", startTime = " + startTime
                + ", endTime = " + endTime
                + ", frequency = " + frequency
                + ", nickName = " + nickName
                + ", deskName = " + deskName
                + ", deskType = " + deskType;
    }

    public KeepWatchAssignment() {

    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setCircleType(String circleType) {
        this.circleType = circleType;
    }

    public String getCircleType() {
        return circleType;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
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
}
