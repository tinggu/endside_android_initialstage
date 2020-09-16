package com.ctfww.module.keyevents.Entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeyEventTrace implements EntityInterface {
    @Id
    private String id;
    private String eventId;
    private long timeStamp;
    @Index
    private String groupId;
    @Index
    private String userId;
    @Index
    private String status;
    private int deskId;
    private String matchLevel;

    @Index
    private String synTag;

    @Generated(hash = 1600781397)
    public KeyEventTrace(String id, String eventId, long timeStamp, String groupId,
            String userId, String status, int deskId, String matchLevel,
            String synTag) {
        this.id = id;
        this.eventId = eventId;
        this.timeStamp = timeStamp;
        this.groupId = groupId;
        this.userId = userId;
        this.status = status;
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
                + ", deskId = " + deskId
                + ", matchLevel = " + matchLevel;
    }

    public void combineId() {
        id = eventId + timeStamp;
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
