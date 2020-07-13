package com.ctfww.module.device.bean;

public class DeviceInfoBean {

    /**
     * addTimestamp : 0
     * addUserId : string
     * lat : string
     * lng : string
     * address : string
     * devId : string
     * devName : string
     * devType : 0
     * groupId : 0
     */

    private long addTimestamp;
    private String addUserId;
    private double lat;
    private double lng;
    private String address;
    private String devId;
    private String devName;
    private int devType;
    private int groupId;

    public DeviceInfoBean() {

    }

    public long getAddTimestamp() {
        return addTimestamp;
    }

    public void setAddTimestamp(long addTimestamp) {
        this.addTimestamp = addTimestamp;
    }

    public String getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
