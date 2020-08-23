package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeepWatchStatisticsByDesk {
    @Id
    private String id;
    @Index
    private String groupId;
    @Index
    private String userId;
    @Index
    private int deskId;
    private int frequency;
    private int signinCount;

    private String nickName;
    private String deskName;
    private String deskType;

    private double dangerFactor;

    private long timeStamp;

    @Generated(hash = 601502531)
    public KeepWatchStatisticsByDesk(String id, String groupId, String userId,
            int deskId, int frequency, int signinCount, String nickName,
            String deskName, String deskType, double dangerFactor, long timeStamp) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.deskId = deskId;
        this.frequency = frequency;
        this.signinCount = signinCount;
        this.nickName = nickName;
        this.deskName = deskName;
        this.deskType = deskType;
        this.dangerFactor = dangerFactor;
        this.timeStamp = timeStamp;
    }

    @Generated(hash = 684637008)
    public KeepWatchStatisticsByDesk() {
    }

    public void combineDangerFactor() {
        dangerFactor = 0.0;
        if (frequency <= 3) {
            dangerFactor = 0.0;
        }
        else {
            if (signinCount == 0) {
                dangerFactor = (double)frequency;
            }
            else {
                dangerFactor = (double)frequency / signinCount;
            }
        }
    }

    public void combineId() {
        id = GlobeFun.getSHA(groupId + userId + deskId + timeStamp);
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

    public int getDeskId() {
        return this.deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
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

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public double getDangerFactor() {
        return this.dangerFactor;
    }

    public void setDangerFactor(double dangerFactor) {
        this.dangerFactor = dangerFactor;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
