package com.ctfww.module.keepwatch.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeepWatchDesk {
    @Id
    private String id;
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
    private String synTag;

    private boolean isAssignmented;

    public boolean hasWifi() {
        int index = fingerPrint.indexOf("wifi:");
        return index == -1 ? false : true;
    }

    public boolean hasGps() {
        int start = fingerPrint.indexOf("gps:");
        if (start == -1) {
            return false;
        }

        int end = fingerPrint.indexOf(";", start);
        if (end == -1) {
            end = fingerPrint.length();
        }

        String gps = fingerPrint.substring(start + 4, end);
        int index = gps.lastIndexOf(",");
        if (index == -1) {
            return false;
        }

        return "1".equals(gps.substring(index + 1)) ? true : false;
    }

    @Generated(hash = 2074194470)
    public KeepWatchDesk(String id, int deskId, String deskName,
            long createTimeStamp, long modifyTimeStamp, double lat, double lng,
            String deskAddress, String groupId, String fingerPrint, String deskType,
            String status, String synTag, boolean isAssignmented) {
        this.id = id;
        this.deskId = deskId;
        this.deskName = deskName;
        this.createTimeStamp = createTimeStamp;
        this.modifyTimeStamp = modifyTimeStamp;
        this.lat = lat;
        this.lng = lng;
        this.deskAddress = deskAddress;
        this.groupId = groupId;
        this.fingerPrint = fingerPrint;
        this.deskType = deskType;
        this.status = status;
        this.synTag = synTag;
        this.isAssignmented = isAssignmented;
    }

    @Generated(hash = 395064693)
    public KeepWatchDesk() {
    }

    public String toString() {
        return "groupId = " + groupId
                + ", deskId = " + deskId
                + ", deskName = " + deskName
                + ", createTimeStamp = " + createTimeStamp
                + ", modifyTimeStamp = " + modifyTimeStamp
                + ", lat = " + lat
                + ", lng = " + lng
                + ", deskAddress = " + deskAddress
                + ", deskType = " + deskType
                + ", status = " + status
                + ", fingerPrint = " + fingerPrint
                + ", synTag = " + synTag;
    }

    public void combineId() {
        id = groupId + deskId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public void setCreateTimeStamp(long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public long getModifyTimeStamp() {
        return this.modifyTimeStamp;
    }

    public void setModifyTimeStamp(long modifyTimeStamp) {
        this.modifyTimeStamp = modifyTimeStamp;
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

    public String getDeskAddress() {
        return this.deskAddress;
    }

    public void setDeskAddress(String deskAddress) {
        this.deskAddress = deskAddress;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFingerPrint() {
        return this.fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
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

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

    public boolean getIsAssignmented() {
        return this.isAssignmented;
    }

    public void setIsAssignmented(boolean isAssignmented) {
        this.isAssignmented = isAssignmented;
    }

}
