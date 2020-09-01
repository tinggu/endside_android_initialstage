package com.ctfww.module.user.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GroupUserInfo implements EntityInterface {
    @Id
    private String id;

    @Index
    private String groupId;
    @Index
    private String userId;
    private String role;

    private long timeStamp;
    private String status;

    @Index
    private String synTag;

    @Generated(hash = 1423913977)
    public GroupUserInfo(String id, String groupId, String userId, String role,
            long timeStamp, String status, String synTag) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.role = role;
        this.timeStamp = timeStamp;
        this.status = status;
        this.synTag = synTag;
    }

    @Generated(hash = 397523636)
    public GroupUserInfo() {
    }

    public String toString() {
        return "group = " + groupId
                + ", userId = " + userId
                + ", role = " + role
                + ", status = " + status;
    }

    public void combineId() {
        id = groupId + userId;
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

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
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
}
