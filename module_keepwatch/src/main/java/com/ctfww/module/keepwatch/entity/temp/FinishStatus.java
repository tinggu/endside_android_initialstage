package com.ctfww.module.keepwatch.entity.temp;

public class FinishStatus {
    private String groupId;
    private int objectId;
    private String createUserId;
    private String endUserId;
    private long dayTimeStamp;
    private int frequency;
    private int signinCount;

    private long createTimeStamp;
    private long endTimeStamp;

    private String type;

    private String addition;

    public FinishStatus() {

    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    public String getEndUserId() {
        return endUserId;
    }

    public void setDayTimeStamp(long dayTimeStamp) {
        this.dayTimeStamp = dayTimeStamp;
    }

    public long getDayTimeStamp() {
        return dayTimeStamp;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setSigninCount(int signinCount) {
        this.signinCount = signinCount;
    }

    public int getSigninCount() {
        return signinCount;
    }

    public void setCreateTimeStamp(long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getAddition() {
        return addition;
    }
}
