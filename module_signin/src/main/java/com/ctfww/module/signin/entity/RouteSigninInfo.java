package com.ctfww.module.signin.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class RouteSigninInfo implements EntityInterface {
    @Id
    private String id;

    private String userId;
    private long timeStamp;
    private int routeId;
    private String groupId;
    private String matchLevel;
    private String synTag;
    private long startTimeStamp;

    @Generated(hash = 1969662235)
    public RouteSigninInfo(String id, String userId, long timeStamp, int routeId,
            String groupId, String matchLevel, String synTag, long startTimeStamp) {
        this.id = id;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.routeId = routeId;
        this.groupId = groupId;
        this.matchLevel = matchLevel;
        this.synTag = synTag;
        this.startTimeStamp = startTimeStamp;
    }

    @Generated(hash = 2126984187)
    public RouteSigninInfo() {
    }

    public void combineId() {
        id = userId + timeStamp;
    }

    public String toString() {
        return "userId = " + userId
                + ", timeStamp = " + timeStamp
                + ", routeId = " + routeId
                + ", groupId = " + groupId
                + ", matchLevel = " + matchLevel
                + ", synTag = " + synTag
                + ", startTimeStamp = " + startTimeStamp;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getRouteId() {
        return this.routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public long getStartTimeStamp() {
        return this.startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }
}
