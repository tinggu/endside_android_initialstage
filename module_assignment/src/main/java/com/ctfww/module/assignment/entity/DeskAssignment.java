package com.ctfww.module.assignment.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class DeskAssignment implements EntityInterface {
    @Id
    private String id;

    @Index
    private String groupId;

    @Index
    private int deskId;

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

    @Generated(hash = 1907950339)
    public DeskAssignment(String id, String groupId, int deskId, String userId,
            String circleType, long startTime, long endTime, int frequency,
            long timeStamp, String status, String fromUserId, String synTag) {
        this.id = id;
        this.groupId = groupId;
        this.deskId = deskId;
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

    @Generated(hash = 528046118)
    public DeskAssignment() {
    }

    public String toString() {
        return "id = " + id
                + ", groupId = " + groupId
                + ", deskId = " + deskId
                + ", userId = " + userId
                + ", circleType = " + circleType
                + ", startTime = " + startTime
                + ", endTime = " + endTime
                + ", frequency = " + frequency
                + ", timeStamp = " + timeStamp;
    }

    public void combineId() {
        id = groupId + deskId + userId;
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

    public int getDeskId() {
        return this.deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
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

}
