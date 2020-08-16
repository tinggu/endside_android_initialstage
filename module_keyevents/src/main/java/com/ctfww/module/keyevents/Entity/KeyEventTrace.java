package com.ctfww.module.keyevents.Entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeyEventTrace implements EntityInterface {
    @Id
    private String eventId;
    private long timeStamp;
    @Index
    private String userId;
    @Index
    private String status;
    @Index
    private String groupId;
    private int deskId;
    private String matchLevel;
    private String nickName;
    private String headUrl;

    @Index
    private String synTag;

    @Generated(hash = 387030589)
    public KeyEventTrace(String eventId, long timeStamp, String userId,
            String status, String groupId, int deskId, String matchLevel,
            String nickName, String headUrl, String synTag) {
        this.eventId = eventId;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.status = status;
        this.groupId = groupId;
        this.deskId = deskId;
        this.matchLevel = matchLevel;
        this.nickName = nickName;
        this.headUrl = headUrl;
        this.synTag = synTag;
    }

    @Generated(hash = 2074150836)
    public KeyEventTrace() {
    }

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

    public int getDeskId() {
        return this.deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public String getMatchLevel() {
        return this.matchLevel;
    }

    public void setMatchLevel(String matchLevel) {
        this.matchLevel = matchLevel;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }
}
