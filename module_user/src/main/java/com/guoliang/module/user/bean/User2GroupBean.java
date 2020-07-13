package com.guoliang.module.user.bean;

public class User2GroupBean {
    private String userId;
    private String groupId;
    private String role;

    public String toString() {
        return "userId = " + userId
                + "groupId = " + groupId
                + "role = " + role;
    }

    public User2GroupBean() {

    }

    public User2GroupBean(String userId, String groupId, String role) {
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
