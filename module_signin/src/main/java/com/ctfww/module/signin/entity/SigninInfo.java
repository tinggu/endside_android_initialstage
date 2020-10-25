package com.ctfww.module.signin.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class SigninInfo implements EntityInterface {
    @Id
    private String id;

    @Index
    private String userId;
    @Index
    private long timeStamp;
    private int objectId;
    private String groupId;
    private String finishType;
    private String matchLevel;

    private String type;
    
    private String synTag;

    private String fingerPrint;

    @Generated(hash = 2144269356)
    public SigninInfo(String id, String userId, long timeStamp, int objectId,
            String groupId, String finishType, String matchLevel, String type,
            String synTag, String fingerPrint) {
        this.id = id;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.objectId = objectId;
        this.groupId = groupId;
        this.finishType = finishType;
        this.matchLevel = matchLevel;
        this.type = type;
        this.synTag = synTag;
        this.fingerPrint = fingerPrint;
    }

    @Generated(hash = 1750719260)
    public SigninInfo() {
    }

    public String toString() {
        return "userId = " + userId
                + ", timeStamp = " + timeStamp
                + ", objectId = " + objectId
                + ", groupId = " + groupId
                + ", finishType = " + finishType
                + ", matchLevel = " + matchLevel
                + ", synTag = " + synTag
                + ", id = " + id
                + ", fingerPrint = " + fingerPrint;
    }

    public void combineId() {
        id = userId + timeStamp;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFinishType() {
        return this.finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public String getMatchLevel() {
        return this.matchLevel;
    }

    public void setMatchLevel(String matchLevel) {
        this.matchLevel = matchLevel;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

    public String getFingerPrint() {
        return this.fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
