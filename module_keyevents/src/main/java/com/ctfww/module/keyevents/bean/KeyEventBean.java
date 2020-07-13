package com.ctfww.module.keyevents.bean;

public class KeyEventBean {
    private String eventId;
    private long timeStamp;
    private int type;
    private String eventName;
    private double lat;
    private double lng;
    private String address;
    private int deskId;
    private String description;
    private String voicePath;
    private String picPath;
    private String videoPath;
    private String status;
    private String userId;
    private String groupId;

    public KeyEventBean(String eventId, long timeStamp, int type, String eventName,
                        float lat, float lng, String address, int deskId,
                        String description, String voicePath, String picPath, String videoPath,
                        String status) {
        this.eventId = eventId;
        this.timeStamp = timeStamp;
        this.type = type;
        this.eventName = eventName;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.deskId = deskId;
        this.description = description;
        this.voicePath = voicePath;
        this.picPath = picPath;
        this.videoPath = videoPath;
        this.status = status;
    }

    public KeyEventBean() {
    }

    public String toString() {
        return "eventId = " + eventId
                + ", timeStamp = " + timeStamp
                + ", type = " + type
                + ", evnentName = " + eventName
                + ", lat = " + lat
                + ", lng = " + lng
                + ", address = " + address
                + ", deskId = " + deskId
                + ", description = " + description
                + ", voicePath = " + voicePath
                + ", picPath = " + picPath
                + ", videoPath = " + videoPath
                + ", userId = " + userId
                + ", status = " + status
                + ", groupId = " + groupId;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDeskId() {
        return this.deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVoicePath() {
        return this.voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getPicPath() {
        return this.picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
