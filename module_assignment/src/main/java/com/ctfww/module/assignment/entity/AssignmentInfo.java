package com.ctfww.module.assignment.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.entity.MyDateTimeUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class AssignmentInfo implements EntityInterface {
    @Id
    private String id;

    @Index
    private String groupId;

    @Index
    private int objectId;

    @Index
    private String userId;
    private String circleType;
    private long startTime;
    private long endTime;
    private int frequency;

    private int score;

    @Index
    private long timeStamp;

    @Index
    private String status;

    @Index
    String fromUserId;

    private String type;

    private String synTag;

    @Generated(hash = 275022363)
    public AssignmentInfo(String id, String groupId, int objectId, String userId,
            String circleType, long startTime, long endTime, int frequency, int score,
            long timeStamp, String status, String fromUserId, String type, String synTag) {
        this.id = id;
        this.groupId = groupId;
        this.objectId = objectId;
        this.userId = userId;
        this.circleType = circleType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
        this.score = score;
        this.timeStamp = timeStamp;
        this.status = status;
        this.fromUserId = fromUserId;
        this.type = type;
        this.synTag = synTag;
    }

    @Generated(hash = 2097607415)
    public AssignmentInfo() {
    }

    public static String createTodayAssignmentId(String groupId, int objectId, String userId, String type) {
        return groupId + objectId + MyDateTimeUtils.getTodayStartTime() + userId + type;
    }

    public String toString() {
        return "id = " + id
                + ", groupId = " + groupId
                + ", objectId = " + objectId
                + ", userId = " + userId
                + ", circleType = " + circleType
                + ", startTime = " + startTime
                + ", endTime = " + endTime
                + ", frequency = " + frequency
                + ", timeStamp = " + timeStamp
                + ", type = " + type;
    }

    public void combineId() {
        id = groupId + objectId + userId + type;
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

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }
}
