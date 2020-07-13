package com.ctfww.module.keepwatch.bean;

public class KeepWatchDeskBean {
    private int deskId;
    private String deskName;
    private long createTimeStamp;
    private long modifyTimeStamp;
    private double lat;
    private double lng;
    private String deskAddress;
    private String groupId;
    private String fingerPrint;
    private String deskType;
    private String status;

    public KeepWatchDeskBean() {

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

    public void setCreateTimeStamp(long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setModifyTimeStamp(long modifyTimeStamp) {
        this.modifyTimeStamp = modifyTimeStamp;
    }

    public long getModifyTimeStamp() {
        return modifyTimeStamp;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public void setDeskAddress(String deskAddress) {
        this.deskAddress = deskAddress;
    }

    public String getDeskAddress() {
        return deskAddress;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
