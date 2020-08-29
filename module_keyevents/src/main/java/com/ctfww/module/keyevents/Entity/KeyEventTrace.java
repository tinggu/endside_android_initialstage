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

    @Index
    private String synTag;

    @Generated(hash = 38369210)
    public KeyEventTrace(String eventId, long timeStamp, String userId,
            String status, String groupId, int deskId, String matchLevel,
            String synTag) {
        this.eventId = eventId;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.status = status;
        this.groupId = groupId;
        this.deskId = deskId;
        this.matchLevel = matchLevel;
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
                + ", matchLevel = " + matchLevel;
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

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }
}
