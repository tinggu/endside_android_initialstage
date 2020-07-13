package com.guoliang.module.fingerprint.entity;

public class FingerPrintHistory {
    private long timeStamp;
    private String fingerPrint;

    public FingerPrintHistory() {

    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }
}
