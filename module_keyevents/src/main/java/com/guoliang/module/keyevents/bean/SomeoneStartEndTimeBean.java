package com.guoliang.module.keyevents.bean;

public class SomeoneStartEndTimeBean {
    private String userId;
    private long startTime;
    private long endTime;

    public String toString() {
        return "userId = " + userId
                + "startTime = " + startTime
                + "endTime = " + endTime;
    }

    public SomeoneStartEndTimeBean() {
        userId = "";
        startTime = 0;
        endTime = 0;
    }

    public SomeoneStartEndTimeBean(String userId, long startTime, long endTime) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
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
}
