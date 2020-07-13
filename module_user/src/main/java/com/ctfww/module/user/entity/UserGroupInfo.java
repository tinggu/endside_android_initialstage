package com.ctfww.module.user.entity;

public class UserGroupInfo {
    private String groupId;
    private String groupName;
    private String role;
    private String userId;

    public String toString() {
        return "groupId = " + groupId
                + "groupName = " + groupName
                + "role = " + role
                + "userId = " + userId;
    }

    public UserGroupInfo() {
    }

    public UserGroupInfo(String groupId, String groupName, String role) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.role = role;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String role) {
        this.userId = userId;
    }
}
