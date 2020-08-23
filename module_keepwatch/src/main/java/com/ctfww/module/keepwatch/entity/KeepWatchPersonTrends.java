package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeepWatchPersonTrends {
    @Id
    private String id;
    @Index
    private String groupId;
    @Index
    private String userId;
    private String nickName;
    private int deskId;
    private String deskName;
    private long timeStamp;
    private String headUrl;
    private String deskType;
    private String status;

    @Generated(hash = 668272117)
    public KeepWatchPersonTrends(String id, String groupId, String userId,
            String nickName, int deskId, String deskName, long timeStamp,
            String headUrl, String deskType, String status) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.nickName = nickName;
        this.deskId = deskId;
        this.deskName = deskName;
        this.timeStamp = timeStamp;
        this.headUrl = headUrl;
        this.deskType = deskType;
        this.status = status;
    }

    @Generated(hash = 1111903489)
    public KeepWatchPersonTrends() {
    }

    public void combineId() {
        id = GlobeFun.getSHA(groupId + userId);
    }

    public String toString() {
        return "userId = " + userId
                + ", nickName = " + nickName
                + ", deskId = " + deskId
                + ", deskName = " + deskName
                + ", timeStamp = " + timeStamp
                + ", headUrl = " + headUrl
                + ", deskType = " + deskType
                + ", status = " + status;
    }

    public String getStatusChinese() {
        String ret = "签到";
        if ("signin".equals(status)) {
            ret = "签到";
        }
        else if ("create".equals(status)) {
            ret = "上报事件";
        }
        else if ("assignment".equals(status)) {
            ret = "分派事件";
        }
        else if ("accepted".equals(status)) {
            ret = "接受事件";
        }
        else if ("end".equals(status)) {
            ret = "结束事件";
        }

        return ret;
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

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getDeskId() {
        return this.deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public String getDeskName() {
        return this.deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getDeskType() {
        return this.deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
