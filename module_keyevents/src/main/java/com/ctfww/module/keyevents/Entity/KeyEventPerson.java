package com.ctfww.module.keyevents.Entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeyEventPerson implements EntityInterface {
    @Id
    private String eventId;
    @Index
    private String userId;
    @Index
    private String groupId;
    @Index
    private String status;
    private long timeStamp;

    private String synTag;

    public String toString() {
        return "eventId = " + eventId
                + ", userId = " + userId
                + ", groupId = " + groupId
                + ", status = " + status
                + ", timeStamp = " + timeStamp
                + ", synTag = " + synTag;
    }

    @Generated(hash = 1523281055)
    public KeyEventPerson(String eventId, String userId, String groupId,
            String status, long timeStamp, String synTag) {
        this.eventId = eventId;
        this.userId = userId;
        this.groupId = groupId;
        this.status = status;
        this.timeStamp = timeStamp;
        this.synTag = synTag;
    }
    @Generated(hash = 213860865)
    public KeyEventPerson() {
    }
    public String getEventId() {
        return this.eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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
    public long getTimeStamp() {
        return this.timeStamp;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getSynTag() {
        return this.synTag;
    }
    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }
}
