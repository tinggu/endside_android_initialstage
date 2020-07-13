package com.ctfww.module.keyevents.bean;

public class StartEndTimeBean {
    private long startTime;
    private long endTime;

    public String toString() {
        return "startTime = " + startTime
                + "endTime = " + endTime;
    }

    public StartEndTimeBean() {
        startTime = 0;
        endTime = 0;
    }

    public StartEndTimeBean(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
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
