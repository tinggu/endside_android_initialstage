package com.ctfww.module.assignment.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AssignmentInfo implements EntityInterface {
    @Id
    private String assignmentId;

    @Index
    private String groupId;

    @Index
    private int deskId;

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

    @Generated(hash = 832228156)
    public AssignmentInfo(String assignmentId, String groupId, int deskId, String routeId,
            String userId, String circleType, long startTime, long endTime, int frequency,
            long timeStamp, String status, String fromUserId, String synTag) {
        this.assignmentId = assignmentId;
        this.groupId = groupId;
        this.deskId = deskId;
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

    @Generated(hash = 2097607415)
    public AssignmentInfo() {
    }

    public String toString() {
        return "assignmentId = " + assignmentId
                + ", groupId = " + groupId
                + ", deskId = " + deskId
                + ", routeId = " + routeId
                + ", userId = " + userId
                + ", circleType = " + circleType
                + ", startTime = " + startTime
                + ", endTime = " + endTime
                + ", frequency = " + frequency
                + ", timeStamp = " + timeStamp;
    }

    public void combineAssignmentId() {
        assignmentId = GlobeFun.getSHA(groupId + deskId + routeId + userId);
    }

    public String getAssignmentId() {
        return this.assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
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

    public String getRouteId() {
        return this.routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

    public String getFromUserId() {
        return this.fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }
}
