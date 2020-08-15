package com.ctfww.module.keepwatch.bean;

public class KeepWatchAssignmentBean {
    private String groupId;
    private int deskId;
    private String routeId;
    private String userId;
    private String circleType;
    private long startTime;
    private long endTime;
    private int frequency;
    private String deskIdStr;

    public KeepWatchAssignmentBean() {

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

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteId() {
        return routeId;
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

    public void setDeskIdStr(String deskIdStr) {
        this.deskIdStr = deskIdStr;
    }

    public String getDeskIdStr() {
        return deskIdStr;
    }
}
