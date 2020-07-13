package com.ctfww.module.keepwatch.entity;

public class KeepWatchPeriodShowData {
    private String periodId;
    private String deskName;
    private long startTime;
    private long endTime;

    public String toString() {
        return "periodId = " + periodId
                + "deskName = " + deskName
                + "startTime = " + startTime
                + "endTime = " + endTime;
    }

    public KeepWatchPeriodShowData() {

    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskName() {
        return deskName;
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
