package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PersonTrends {
    @Id
    private String id;
    @Index
    private String groupId;
    @Index
    private String userId;
    private String objectId;
    private long timeStamp;
    private String status;

    private double lat;
    private double lng;

    private String objectType;

    @Generated(hash = 149809315)
    public PersonTrends(String id, String groupId, String userId, String objectId,
            long timeStamp, String status, double lat, double lng,
            String objectType) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.objectId = objectId;
        this.timeStamp = timeStamp;
        this.status = status;
        this.lat = lat;
        this.lng = lng;
        this.objectType = objectType;
    }

    @Generated(hash = 1003366603)
    public PersonTrends() {
    }

    public void combineId() {
        id = GlobeFun.getSHA(groupId + userId);
    }

    public String toString() {
        return "userId = " + userId
                + ", objectId = " + objectId
                + ", timeStamp = " + timeStamp
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

    public String getObjectId() {
        return this.objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
