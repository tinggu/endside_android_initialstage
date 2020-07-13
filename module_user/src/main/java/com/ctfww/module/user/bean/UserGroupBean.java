package com.ctfww.module.user.bean;

public class UserGroupBean {
    private String groupId;
    private String groupName;
    private String role;

    public String toString() {
        return "groupId = " + groupId
                + "groupName = " + groupName
                + "role = " + role;
    }

    public UserGroupBean() {
    }

    public UserGroupBean(String groupId, String groupName, String role) {
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
}
