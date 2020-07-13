package com.ctfww.module.attend.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class AttendSigninInfo {
    private String userId;
    @Id
    private Long timeStamp;
    private int deskId;
    private String groupId;
    private String status;
    private String matchLevel;
    private String synTag;

    private String deskName;
    private String nickName;

    @Generated(hash = 43372745)
    public AttendSigninInfo(String userId, Long timeStamp, int deskId,
            String groupId, String status, String matchLevel, String synTag,
            String deskName, String nickName) {
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.deskId = deskId;
        this.groupId = groupId;
        this.status = status;
        this.matchLevel = matchLevel;
        this.synTag = synTag;
        this.deskName = deskName;
        this.nickName = nickName;
    }

    @Generated(hash = 1636700890)
    public AttendSigninInfo() {
    }

    public String toString() {
        return "userId = " + userId
                + ", timeStamp = " + timeStamp
                + ", deskId = " + deskId
                + ", groupId = " + groupId
                + ", status = " + status
                + ", matchLevel = " + matchLevel
                + ", synTag = " + synTag
                + ", deskName =" + deskName
                + ", nickName = " + nickName;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatchLevel() {
        return this.matchLevel;
    }

    public void setMatchLevel(String matchLevel) {
        this.matchLevel = matchLevel;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

    public String getDeskName() {
        return this.deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
