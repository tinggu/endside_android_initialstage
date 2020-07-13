package com.ctfww.module.device.storage.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class DeviceInfo {
    @Index
    private long addTimestamp;

    private String addUserId;
    private double lat;
    private double lng;
    private String address;

    @Id
    private String devId;

    private String devName;
    private int devType;
    private int groupId;
    private String sim;

    @Generated(hash = 829170197)
    public DeviceInfo(long addTimestamp, String addUserId, double lat, double lng,
            String address, String devId, String devName, int devType, int groupId,
            String sim) {
        this.addTimestamp = addTimestamp;
        this.addUserId = addUserId;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.devId = devId;
        this.devName = devName;
        this.devType = devType;
        this.groupId = groupId;
        this.sim = sim;
    }

    @Generated(hash = 2125166935)
    public DeviceInfo() {
    }

    public String toString() {
        return "devId = " + devId
                + "devName = " + devName
                + "devType = " + devType
                + "groupId = " + groupId
                + "sim = " + sim
                + "lat = " + lat
                + "lng = " + lng
                + "address = " + address
                + "addUserId = " + addUserId
                + "addTimestamp = " + addTimestamp;
    }

    public long getAddTimestamp() {
        return this.addTimestamp;
    }

    public void setAddTimestamp(long addTimestamp) {
        this.addTimestamp = addTimestamp;
    }

    public String getAddUserId() {
        return this.addUserId;
    }

    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId;
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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDevId() {
        return this.devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return this.devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getDevType() {
        return this.devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getSim() {
        return this.sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }
}
