package com.ctfww.module.assignment.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class RouteAssignment implements EntityInterface {
    @Id
    private String id;

    @Index
    private String groupId;

    @Index
    private String routeId;

    @Index
    private String userId;
    private String circleType;
    private long startTime;
    private long endTime;
    private int frequency;

    @Index
    private long timeStamp;

    @Index
    private String status;

    @Index String fromUserId;

    private String synTag;

    @Generated(hash = 283785628)
    public RouteAssignment(String id, String groupId, String routeId, String userId,
            String circleType, long startTime, long endTime, int frequency,
            long timeStamp, String status, String fromUserId, String synTag) {
        this.id = id;
        this.groupId = groupId;
        this.routeId = routeId;
        this.userId = userId;
        this.circleType = circleType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
        this.timeStamp = timeStamp;
        this.status = status;
        this.fromUserId = fromUserId;
        this.synTag = synTag;
    }

    @Generated(hash = 1635870015)
    public RouteAssignment() {
    }

    public String toString() {
        return "id = " + id
                + ", groupId = " + groupId
                + ", route = " + routeId
                + ", userId = " + userId
                + ", circleType = " + circleType
                + ", startTime = " + startTime
                + ", endTime = " + endTime
                + ", frequency = " + frequency
                + ", timeStamp = " + timeStamp;
    }

    public void combineId() {
        id = groupId + routeId + userId;
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

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCircleType() {
        return this.circleType;
    }

    public void setCircleType(String circleType) {
        this.circleType = circleType;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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

    public String getFromUserId() {
        return this.fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

    public String getRouteId() {
        return this.routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

}
