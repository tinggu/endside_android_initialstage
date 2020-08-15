package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeepWatchRouteDesk implements EntityInterface {
    @Index
    private String routeId;
    @Index
    private int deskId;
    private double lat;
    private double lng;
    private String tag;
    private long timeStamp;
    private String status;

    @Index
    private String synTag;

    @Generated(hash = 1198326021)
    public KeepWatchRouteDesk(String routeId, int deskId, double lat, double lng,
            String tag, long timeStamp, String status, String synTag) {
        this.routeId = routeId;
        this.deskId = deskId;
        this.lat = lat;
        this.lng = lng;
        this.tag = tag;
        this.timeStamp = timeStamp;
        this.status = status;
        this.synTag = synTag;
    }

    @Generated(hash = 616149187)
    public KeepWatchRouteDesk() {
    }

    public String toString() {
        return "routeId = " + routeId
                + ", deskId = " + deskId
                + ", lat = " + deskId
                + ", lng = " + lng
                + ", tag = " + tag;
    }

    public String getRouteId() {
        return this.routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public int getDeskId() {
        return this.deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
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

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
