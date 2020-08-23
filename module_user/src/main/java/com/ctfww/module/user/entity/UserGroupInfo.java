package com.ctfww.module.user.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserGroupInfo implements EntityInterface {
    @Id
    private String id;
    @Index
    private String groupId;
    private String groupName;
    private String role;
    @Index
    private String userId;
    private String status;

    private long timeStamp;

    @Index
    private String synTag;

    @Generated(hash = 1171778231)
    public UserGroupInfo(String id, String groupId, String groupName, String role,
            String userId, String status, long timeStamp, String synTag) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.role = role;
        this.userId = userId;
        this.status = status;
        this.timeStamp = timeStamp;
        this.synTag = synTag;
    }

    @Generated(hash = 1141495075)
    public UserGroupInfo() {
    }

    public String toString() {
        return "groupId = " + groupId
                + ", groupName = " + groupName
                + ", role = " + role
                + ", userId = " + userId
                + ", status = " + status;
    }

    public void combineId() {
        id = GlobeFun.getSHA(groupId + userId);
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

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
