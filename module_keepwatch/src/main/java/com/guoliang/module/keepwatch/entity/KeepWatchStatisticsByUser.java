package com.guoliang.module.keepwatch.entity;

import androidx.annotation.Nullable;

public class KeepWatchStatisticsByUser {
    private long startTimeStamp;
    private int shouldCount;
    private int signinCount;
    private int abnormalCount;
    private int endCount;
    private int deskCount;
    private int shouldAssignmentCount;
    private int assignmentCount;

    public KeepWatchStatisticsByUser() {

    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setShouldCount(int shouldCount) {
        this.shouldCount = shouldCount;
    }

    public int getShouldCount() {
        return shouldCount;
    }

    public void setSigninCount(int signinCount) {
        this.signinCount = signinCount;
    }

    public int getSigninCount() {
        return signinCount;
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

    public void setDeskCount(int deskCount) {
        this.deskCount = deskCount;
    }

    public int getDeskCount() {
        return deskCount;
    }

    public void setShouldAssignmentCount(int shouldAssignmentCount) {
        this.shouldAssignmentCount = shouldAssignmentCount;
    }

    public int getShouldAssignmentCount() {
        return shouldAssignmentCount;
    }

    public void setAssignmentCount(int assignmentCount) {
        this.assignmentCount = assignmentCount;
    }

    public int getAssignmentCount() {
        return assignmentCount;
    }
}
