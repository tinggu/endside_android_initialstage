package com.ctfww.module.assignment.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.entity.MyDateTimeUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class TodayAssignment {
    @Id
    private String assignmentId;

    @Index
    private String groupId;

    @Index
    private int objectId;

    @Index
    private String userId;
    @Index
    private long dayTimeStamp;

    private long startTime;
    private long endTime;
    private int frequency;
    private int signinCount;

    private String status;

    private String type;

    private int score;

    private long timeStamp;

    public void combineId() {
        assignmentId = groupId + objectId + MyDateTimeUtils.getTodayStartTime() + userId + type;
    }

    @Generated(hash = 1633288211)
    public TodayAssignment(String assignmentId, String groupId, int objectId, String userId,
            long dayTimeStamp, long startTime, long endTime, int frequency, int signinCount,
            String status, String type, int score, long timeStamp) {
        this.assignmentId = assignmentId;
        this.groupId = groupId;
        this.objectId = objectId;
        this.userId = userId;
        this.dayTimeStamp = dayTimeStamp;
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
        this.signinCount = signinCount;
        this.status = status;
        this.type = type;
        this.score = score;
        this.timeStamp = timeStamp;
    }

    @Generated(hash = 1810339815)
    public TodayAssignment() {
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

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getDayTimeStamp() {
        return this.dayTimeStamp;
    }

    public void setDayTimeStamp(long dayTimeStamp) {
        this.dayTimeStamp = dayTimeStamp;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getSigninCount() {
        return this.signinCount;
    }

    public void setSigninCount(int signinCount) {
        this.signinCount = signinCount;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
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

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
