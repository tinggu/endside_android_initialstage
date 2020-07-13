package com.guoliang.module.keepwatch.entity;

public class KeepWatchPersonTrends {
    private String userId;
    private String nickName;
    private int deskId;
    private String deskName;
    private long timeStamp;
    private String headUrl;
    private String deskType;
    private String status;

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

    public KeepWatchPersonTrends() {
        userId = "";
        nickName = "";
        deskId = 0;
        deskName = "";
        timeStamp = 0;
        headUrl = "";
        deskType = "";
        status = "signin";

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
