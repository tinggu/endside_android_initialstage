package com.guoliang.module.keepwatch.bean;

public class KeepWatchSigninBean {
    private String userId;
    private long timeStamp;
    private int deskId;
    private String groupId;
    private String finishType;
    private String status;
    private String reportId;
    private String matchLevel;
    private String fingerPrint;

    public String toString() {
        return "userId = " + userId
                + ", timeStamp = " + timeStamp
                + ", deskId = " + deskId
                + ", groupId = " + groupId
                + ", finishType = " + finishType
                + ", status = " + status
                + ", reportId = " + reportId
                + ", matchLevel = " + matchLevel
                + ", fingerPrint = " + fingerPrint;
    }

    public KeepWatchSigninBean() {

    }

    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getTimeStamp() {
        return this.timeStamp;
    }
    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public int getDeskId() {
        return this.deskId;
    }
    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getFinishType() {
        return this.finishType;
    }
    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getReportId() {
        return this.reportId;
    }
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
    public String getMatchLevel() {
        return this.matchLevel;
    }
    public void setMatchLevel(String matchLevel) {
        this.matchLevel = matchLevel;
    }
    public String getFingerPrint() {
        return this.fingerPrint;
    }
    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }
}
