package com.ctfww.module.keepwatch.entity;

import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.signin.entity.SigninInfo;

public class PersonTrends {
    private String userId;
    private int objectId;
    private long timeStamp;

    private String type; // 动态类型，如"desk", "route", "key_event"
    private String status; // 进展状态，如"create", "signin"
    private String addition; // 附加信息，如事件Id

    public String toString() {
        return "userId = " + userId
                + ", userId = " + userId
                + ", objectId = " + objectId
                + ", timeStamp = " + timeStamp
                + ", type = " + type
                + ", status = " + status
                + ", addition = " + addition;
    }

    public String getStatusChinese() {
        String ret = "签到";
        if ("desk".equals(type)) {
            if ("signin".equals(status)) {
                ret = "完成某点位签到";
            }
            else if ("snatch".equals(status)) {
                ret = "抢到任务";
            }
        }
        else if ("route".equals(type)) {
            if ("signin".equals(status)) {
                ret = "完成某线路签到";
            }
            else if ("snatch".equals(status)) {
                ret = "抢到任务";
            }
        }
        else if ("key_event".equals(type)) {
            if ("create".equals(status)) {
                ret = "上报事件";
            }
            else if ("received".equals(status)) {
                ret = "收到事件分配";
            }
            else if ("snatch".equals(status)) {
                ret = "抢到工单";
            }
            else if ("accepted".equals(status)) {
                ret = "接受事件";
            }
            else if ("end".equals(status)) {
                ret = "结束事件";
            }
        }

        return ret;
    }

    public PersonTrends() {

    }

    public void set(SigninInfo signin) {
        userId = signin.getUserId();
        objectId = signin.getObjectId();
        timeStamp = signin.getTimeStamp();
        type = signin.getType();
        status = "signin";
    }

    public void set(KeyEventTrace keyEventTrace) {
        userId = keyEventTrace.getUserId();
        objectId = keyEventTrace.getDeskId();
        timeStamp = keyEventTrace.getTimeStamp();
        type = "key_event";
        status = keyEventTrace.getStatus();
        addition = keyEventTrace.getEventId();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getAddition() {
        return addition;
    }
}
