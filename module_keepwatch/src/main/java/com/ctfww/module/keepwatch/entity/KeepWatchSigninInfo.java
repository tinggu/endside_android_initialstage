package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeepWatchSigninInfo implements EntityInterface {
    @Id
    private String id;

    private String userId;
    private long timeStamp;
    private int deskId;
    private String groupId;
    private String finishType;
    private String matchLevel;
    private String synTag;

    private String deskName;
    private String deskType;
    private String nickName;
    private String fingerPrint;

    private String status;

    public void combineId() {
        id = userId + timeStamp;
    }

    @Generated(hash = 645362104)
    public KeepWatchSigninInfo(String id, String userId, long timeStamp, int deskId,
            String groupId, String finishType, String matchLevel, String synTag,
            String deskName, String deskType, String nickName, String fingerPrint,
            String status) {
        this.id = id;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.deskId = deskId;
        this.groupId = groupId;
        this.finishType = finishType;
        this.matchLevel = matchLevel;
        this.synTag = synTag;
        this.deskName = deskName;
        this.deskType = deskType;
        this.nickName = nickName;
        this.fingerPrint = fingerPrint;
        this.status = status;
    }

    @Generated(hash = 519721089)
    public KeepWatchSigninInfo() {
    }

    public String toString() {
        return "userId = " + userId
                + ", timeStamp = " + timeStamp
                + ", deskId = " + deskId
                + ", groupId = " + groupId
                + ", finishType = " + finishType
                + ", matchLevel = " + matchLevel
                + ", synTag = " + synTag
                + ", deskName =" + deskName
                + ", deskType = " + deskType
                + ", nickName = " + nickName
                + ", fingerPrint = " + fingerPrint;
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

    public int getDeskId() {
        return this.deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
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

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFingerPrint() {
        return this.fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
