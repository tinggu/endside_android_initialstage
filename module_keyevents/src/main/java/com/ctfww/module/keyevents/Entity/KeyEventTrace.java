package com.ctfww.module.keyevents.Entity;

public class KeyEventTrace {
    private String eventId;
    private long timeStamp;
    private String userId;
    private String status;
    private String groupId;
    private int deskId;
    private String matchLevel;
    private String nickName;
    private String headUrl;

    public String toString() {
        return "eventId = " + eventId
                + ", timeStamp = " + timeStamp
                + ", userId = " + userId
                + ", status = " + status
                + ", groupId = " + groupId
                + ", deskId = " + deskId
                + ", matchLevel = " + matchLevel
                + ", nickName = " + nickName
                + ", headUrl = " + headUrl;
    }

    public KeyEventTrace() {

    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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

    public void setMatchLevel(String matchLevel) {
        this.matchLevel = matchLevel;
    }

    public String getMatchLevel() {
        return matchLevel;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getHeadUrl() {
        return headUrl;
    }
}
