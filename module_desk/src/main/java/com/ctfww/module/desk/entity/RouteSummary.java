package com.ctfww.module.desk.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class RouteSummary implements EntityInterface {
    @Id
    private String id;
    private int routeId;
    private String routeName;
    @Index
    private String groupId;
    private long timeStamp;
    private long startTimeStamp;
    private long endTimeStamp;
    private String status;
    @Index
    private String synTag;

    @Generated(hash = 1055261666)
    public RouteSummary(String id, int routeId, String routeName, String groupId,
            long timeStamp, long startTimeStamp, long endTimeStamp, String status,
            String synTag) {
        this.id = id;
        this.routeId = routeId;
        this.routeName = routeName;
        this.groupId = groupId;
        this.timeStamp = timeStamp;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
        this.status = status;
        this.synTag = synTag;
    }

    @Generated(hash = 397672026)
    public RouteSummary() {
    }

    public String toString() {
        return "routeId = " + routeId
                + ", routeName = " + routeName
                + ", groupId = " + groupId
                + ", timeStamp = " + timeStamp
                + ", startTimeStamp = " + startTimeStamp
                + ", endTimeStamp = " + endTimeStamp
                + ", status = " + status
                + ", synTag = " + synTag;
    }

    public String getIdName() {
        return "[" + routeName + "] " + routeName;
    }

    public void combineId() {
        id = groupId + routeId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRouteId() {
        return this.routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return this.routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getStartTimeStamp() {
        return this.startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        return this.endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }
}
