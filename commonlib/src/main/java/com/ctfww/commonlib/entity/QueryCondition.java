package com.ctfww.commonlib.entity;

public class QueryCondition {
    private String groupId;
    private String userId;
    private long startTime;
    private long endTime;
    private String condition;

    public String toString() {
        return "groupId = " + groupId
                + ", userId = " + userId
                + ", startTime = " + startTime
                + ", endTime = " + endTime
                + ", condition = " + condition;
    }

    public QueryCondition() {

    }

    public QueryCondition(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public QueryCondition(String groupId, String userId, long startTime, long endTime) {
        this.groupId = groupId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
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

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
