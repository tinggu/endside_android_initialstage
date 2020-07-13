package com.ctfww.module.user.entity;

public class GroupInviteInfo {
    private String inviteId;
    private String fromUserId;
    private String toUserId;
    private String groupId;
    private long timeStamp;
    private String status;

    private String fromMobile;
    private String fromNickName;
    private String toMobile;
    private String toNickName;

    private String groupName;

    public String toString() {
        return "inviteId = " + inviteId
                + "fromUserId = " + fromUserId
                + "toUserId = " + toUserId
                + "groupId = " + groupId
                + "status = " + status
                + "timeStamp = " + timeStamp
                + "groupName = " + groupName
                + "fromMobile = " + fromMobile
                + "fromNickName = " + fromNickName
                + "toMobile = " + toMobile
                + "toNickName = " + toNickName;
    }

    public GroupInviteInfo() {

    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setFromMobile(String fromMobile) {
        this.fromMobile = fromMobile;
    }

    public String getFromMobile() {
        return fromMobile;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public String getFromNickName() {
        return fromNickName;
    }

    public void setToMobile(String toMobile) {
        this.toMobile = toMobile;
    }

    public String getToMobile() {
        return toMobile;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }

    public String getToNickName() {
        return toNickName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
