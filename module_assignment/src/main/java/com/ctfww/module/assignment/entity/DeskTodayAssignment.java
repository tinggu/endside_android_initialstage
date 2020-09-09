package com.ctfww.module.assignment.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DeskTodayAssignment {
    @Id
    private String assignmentId;

    @Index
    private String groupId;

    @Index
    private int deskId;

    @Index
    private String userId;
    private long startTime;
    private long endTime;
    private int frequency;

    @Generated(hash = 1824000481)
    public DeskTodayAssignment(String assignmentId, String groupId, int deskId,
            String userId, long startTime, long endTime, int frequency) {
        this.assignmentId = assignmentId;
        this.groupId = groupId;
        this.deskId = deskId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
    }

    @Generated(hash = 847343223)
    public DeskTodayAssignment() {
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

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
