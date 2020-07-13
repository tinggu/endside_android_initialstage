package com.ctfww.module.keyevents.Entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class KeyEvent {
    @Id
    private String eventId;
    private long timeStamp;
    private String userId;
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
    private String groupId;
    private int level;

    @Index
    private String synTag;

    private String nickName;
    private String deskName;
    private String deskType;

    @Generated(hash = 1669352667)
    public KeyEvent(String eventId, long timeStamp, String userId, int type, String eventName, double lat,
            double lng, String address, int deskId, String description, String voicePath, String picPath,
            String videoPath, String status, String groupId, int level, String synTag, String nickName,
            String deskName, String deskType) {
        this.eventId = eventId;
        this.timeStamp = timeStamp;
        this.userId = userId;
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
        this.groupId = groupId;
        this.level = level;
        this.synTag = synTag;
        this.nickName = nickName;
        this.deskName = deskName;
        this.deskType = deskType;
    }

    @Generated(hash = 427852174)
    public KeyEvent() {
    }

    public String toString() {
        return "eventId = " + eventId
                + "userId = " + userId
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
                + ", status = " + status
                + ", level = " + level
                + ", groupId = " + groupId
                + ", synTag = " + synTag
                + ", nickName = " + nickName
                + ", deskName = " + deskName
                + ", deskType = " + deskType;

    }

    public boolean hasAddtion() {
        return !TextUtils.isEmpty(picPath) && !TextUtils.isEmpty(videoPath) && !TextUtils.isEmpty(voicePath);
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

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDeskName() {
        return this.deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskType() {
        return this.deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }
}