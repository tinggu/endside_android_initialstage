package com.guoliang.module.keyevents.bean;

public class KeyEventStatisticsByUser {
    private long timeStamp;
    private int abnormalCount;
    private int endCount;

    public KeyEventStatisticsByUser() {

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

    public void setEndCount(int endCount) {
        this.endCount = endCount;
    }

    public int getEndCount() {
        return endCount;
    }
}
