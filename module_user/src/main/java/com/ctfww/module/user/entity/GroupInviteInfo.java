package com.ctfww.module.user.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GroupInviteInfo implements EntityInterface {
    @Id
    private String inviteId;

    private String fromUserId;
    private String toUserId;
    private String groupId;
    private long timeStamp;
    private String status;

    @Index
    private String synTag;

    private String groupName;
    private String fromNickName;
    private String fromMobile;
    private String toNickName;
    private String toMobile;


    @Generated(hash = 195344053)
    public GroupInviteInfo(String inviteId, String fromUserId, String toUserId,
            String groupId, long timeStamp, String status, String synTag,
            String groupName, String fromNickName, String fromMobile,
            String toNickName, String toMobile) {
        this.inviteId = inviteId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.groupId = groupId;
        this.timeStamp = timeStamp;
        this.status = status;
        this.synTag = synTag;
        this.groupName = groupName;
        this.fromNickName = fromNickName;
        this.fromMobile = fromMobile;
        this.toNickName = toNickName;
        this.toMobile = toMobile;
    }

    @Generated(hash = 1262591850)
    public GroupInviteInfo() {
    }


    public String toString() {
        return "inviteId = " + inviteId
                + ", fromUserId = " + fromUserId
                + ", toUserId = " + toUserId
                + ", groupId = " + groupId
                + ", status = " + status
                + ", timeStamp = " + timeStamp
                + ", synTag = " + synTag
                + ", groupName = " + groupName
                + ", fromNickName = " + fromNickName
                + ", fromMobile = " + fromMobile
                + ", toNickName = " + toNickName
                + ", toMobile = " + toMobile;
    }

    public void combineInviteId() {
        inviteId = fromUserId + groupId + timeStamp;
    }

    public String getInviteId() {
        return this.inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getFromUserId() {
        return this.fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return this.toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFromNickName() {
        return this.fromNickName;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public String getFromMobile() {
        return this.fromMobile;
    }

    public void setFromMobile(String fromMobile) {
        this.fromMobile = fromMobile;
    }

    public String getToNickName() {
        return this.toNickName;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }

    public String getToMobile() {
        return this.toMobile;
    }

    public void setToMobile(String toMobile) {
        this.toMobile = toMobile;
    }
}
