package com.ctfww.module.desk.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class RouteSummary implements EntityInterface {
    @Id
    private String routeId;
    private String routeName;
    @Index
    private String groupId;
    private long timeStamp;
    private String status;
    @Index
    private String synTag;
    @Generated(hash = 1504580283)
    public RouteSummary(String routeId, String routeName, String groupId,
            long timeStamp, String status, String synTag) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.groupId = groupId;
        this.timeStamp = timeStamp;
        this.status = status;
        this.synTag = synTag;
    }
    @Generated(hash = 397672026)
    public RouteSummary() {
    }
    public String getRouteId() {
        return this.routeId;
    }
    public void setRouteId(String routeId) {
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
