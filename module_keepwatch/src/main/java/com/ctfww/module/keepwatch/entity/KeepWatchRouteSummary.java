package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class KeepWatchRouteSummary implements EntityInterface {
    @Id
    private String routeId;
    private String routeName;
    @Index
    private String groupId;
    private long timeStamp;
    private String status;
    @Index
    private String synTag;

    private boolean isAssignmented;

    @Generated(hash = 15518894)
    public KeepWatchRouteSummary(String routeId, String routeName, String groupId,
            long timeStamp, String status, String synTag, boolean isAssignmented) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.groupId = groupId;
        this.timeStamp = timeStamp;
        this.status = status;
        this.synTag = synTag;
        this.isAssignmented = isAssignmented;
    }

    @Generated(hash = 846526617)
    public KeepWatchRouteSummary() {
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

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

    public boolean getIsAssignmented() {
        return this.isAssignmented;
    }

    public void setIsAssignmented(boolean isAssignmented) {
        this.isAssignmented = isAssignmented;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
