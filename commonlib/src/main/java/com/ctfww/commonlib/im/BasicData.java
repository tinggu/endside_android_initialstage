package com.ctfww.commonlib.im;

import com.google.gson.annotations.SerializedName;

public class BasicData {
    private String userId;
    private String serialNumber;
    private String type;
    private String toUserId;
    private String groupId;
    private String addition;

    public boolean isValid() {
        return userId == null || "".equals(userId) || serialNumber == null || "".equals(serialNumber) || type == null || "".equals(type) ? false : true;
    }

    public String toString() {
        return "userId = " + userId
                + ", serialNumber = " + serialNumber
                + ", type = " + type
                + ", toUserId = " + toUserId
                + ", groupId = " + groupId
                + ", addition = " + addition;
    }

    public BasicData() {

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getAddition() {
        return addition;
    }
}
