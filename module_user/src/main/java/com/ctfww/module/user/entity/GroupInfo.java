package com.ctfww.module.user.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class GroupInfo implements EntityInterface {
    @Id
    private String groupId;
    private String groupName;
    private long timeStamp;
    private String appName;
    private String userId;
    private String status;

    @Index
    private String synTag;

    public String toString() {
        return "groupId = " + groupId
                + ", groupName = " + groupName
                + ", timeStamp = " + timeStamp
                + ", appName = " + appName
                + ", userId = " + userId
                + ", status = " + status;
    }

    public void combineGroupId() {
        groupId = GlobeFun.getSHA(userId + timeStamp);
    }

    @Generated(hash = 651166669)
    public GroupInfo(String groupId, String groupName, long timeStamp,
            String appName, String userId, String status, String synTag) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.timeStamp = timeStamp;
        this.appName = appName;
        this.userId = userId;
        this.status = status;
        this.synTag = synTag;
    }

    @Generated(hash = 1250265142)
    public GroupInfo() {
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
    public long getTimeStamp() {
        return this.timeStamp;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getAppName() {
        return this.appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
