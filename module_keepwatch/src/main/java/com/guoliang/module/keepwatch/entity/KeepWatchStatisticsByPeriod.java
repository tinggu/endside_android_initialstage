package com.guoliang.module.keepwatch.entity;

public class KeepWatchStatisticsByPeriod {
    private int shouldCount;
    private int signinCount;
    private int leakCount;
    private long timeStamp;
    private int abnormalCount;
    private int endCount;
    private int shouldDeskCount;
    private int deskCount;
    private int shouldMemberCount;
    private int memberCount;
    private int shouldAssignmentCount;
    private int assignmentCount;

    public KeepWatchStatisticsByPeriod() {

    }

    public double getCompletionRate() {
        if (shouldCount == 0) {
            return 0.0;
        }

        return (double)signinCount / shouldCount;
    }

    public double getCoverageRate() {
        if (shouldDeskCount == 0) {
            return 0.0;
        }

        return (double)deskCount / shouldDeskCount;
    }

    public String getCompletionRatePercent() {
        double rate = getCompletionRate();
        return String.format("%.1f%%", rate * 100);
    }

    public String getCoverageRatePercent() {
        double rate = getCoverageRate();
        return String.format("%.1f%%", rate * 100);
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

    public void setLeakCount(int leakCount) {
        this.leakCount = leakCount;
    }

    public int getLeakCount() {
        return leakCount;
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

    public void setShouldDeskCount(int shouldDeskCount) {
        this.shouldDeskCount = shouldDeskCount;
    }

    public int getShouldDeskCount() {
        return shouldDeskCount;
    }

    public void setDeskCount(int deskCount) {
        this.deskCount = deskCount;
    }

    public int getDeskCount() {
        return deskCount;
    }

    public void setShouldMemberCount(int shouldMemberCount) {
        this.shouldMemberCount = shouldMemberCount;
    }
    public int getShouldMemberCount() {
        return shouldMemberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getMemberCount() {
        return memberCount;
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
