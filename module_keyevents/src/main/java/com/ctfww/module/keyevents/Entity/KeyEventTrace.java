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
    private int objectId;
    private String matchLevel;

    private String type;

    @Index
    private String synTag;

    @Generated(hash = 1756264121)
    public KeyEventTrace(String id, String eventId, long timeStamp, String groupId,
            String userId, String status, int objectId, String matchLevel,
            String type, String synTag) {
        this.id = id;
        this.eventId = eventId;
        this.timeStamp = timeStamp;
        this.groupId = groupId;
        this.userId = userId;
        this.status = status;
        this.objectId = objectId;
        this.matchLevel = matchLevel;
        this.type = type;
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
                + ", objectId = " + objectId
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

    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
