package com.ctfww.module.keyevents.bean;

public class KeyEventTraceBean {
    private String eventId;
    private long timeStamp;
    private String userId;
    private String status;
    private String groupId;
    private int deskId;
    private String nickName;

    public KeyEventTraceBean() {

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

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }
}
