package com.guoliang.module.keyevents.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeyEventReportRepaired {
    @Id
    private String id;
    private String eventId;
    private long timeStamp;
    private String synTag;

    @Generated(hash = 732089120)
    public KeyEventReportRepaired(String id, String eventId, long timeStamp,
            String synTag) {
        this.id = id;
        this.eventId = eventId;
        this.timeStamp = timeStamp;
        this.synTag = synTag;
    }

    @Generated(hash = 1026762656)
    public KeyEventReportRepaired() {
    }

    public void combineId() {
        id = eventId + timeStamp;
    }

    public String toString() {
        return "eventId = " + eventId
                + ", timeStamp = " + timeStamp;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }
}
