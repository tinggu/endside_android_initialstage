package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeepWatchAssignment implements EntityInterface {
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

    private String nickName;
    private String deskName;
    private String deskType;

    private String synTag;

    @Generated(hash = 1933129543)
    public KeepWatchAssignment(String assignmentId, String groupId, int deskId,
            String routeId, String userId, String circleType, long startTime, long endTime,
            int frequency, long timeStamp, String status, String nickName, String deskName,
            String deskType, String synTag) {
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
        this.nickName = nickName;
        this.deskName = deskName;
        this.deskType = deskType;
        this.synTag = synTag;
    }

    @Generated(hash = 1257721508)
    public KeepWatchAssignment() {
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
                + ", timeStamp = " + timeStamp
                + ", nickName = " + nickName
                + ", deskName = " + deskName
                + ", deskType = " + deskType;
    }

    public void combineAssignmentId() {
        assignmentId = GlobeFun.getSHA(groupId + deskId + routeId + userId + timeStamp);
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

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDeskName() {
        return this.deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskType() {
        return this.deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
