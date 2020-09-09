package com.ctfww.module.signin.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SigninCount implements EntityInterface {
    @Id
    private String assignmentId;
    private int frequency;
    private int signinCount;
    private long dayTimeStamp;
    private long timeStamp;

    @Index
    private String synTag;

    @Generated(hash = 1187633257)
    public SigninCount(String assignmentId, int frequency, int signinCount,
            long dayTimeStamp, long timeStamp, String synTag) {
        this.assignmentId = assignmentId;
        this.frequency = frequency;
        this.signinCount = signinCount;
        this.dayTimeStamp = dayTimeStamp;
        this.timeStamp = timeStamp;
        this.synTag = synTag;
    }

    @Generated(hash = 1124608239)
    public SigninCount() {
    }

    public String toString() {
        return "assignmentId = " + assignmentId
                + ", frequency = " + frequency
                + ", signinCount = " + signinCount
                + ", dayTimeStamp = " + dayTimeStamp
                + ", timeStamp = " + timeStamp
                + ", synTag = " + timeStamp;

    }

    public String getAssignmentId() {
        return this.assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
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

    public long getDayTimeStamp() {
        return this.dayTimeStamp;
    }

    public void setDayTimeStamp(long dayTimeStamp) {
        this.dayTimeStamp = dayTimeStamp;
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
}
