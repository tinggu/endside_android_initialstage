package com.ctfww.module.user.bean;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.GlobeFun;

public class GroupInfoBean {
    private String groupId;
    private String groupName;
    private long timeStamp;
    private String appName;
    private String userId;

    public String toString() {
        return "groupId = " + groupId
                + "groupName = " + groupName
                + "timeStamp = " + timeStamp
                + "appName = " + appName
                + "userId = " + userId;
    }

    public GroupInfoBean() {
        appName = SPStaticUtils.getString("app_package_name");
        timeStamp = System.currentTimeMillis();
        userId = SPStaticUtils.getString("user_open_id");
        groupId = GlobeFun.getSHA(appName + userId + timeStamp);
    }

    public GroupInfoBean(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public GroupInfoBean(String groupId, String groupName, long timeStamp, String appName, String userId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.timeStamp = timeStamp;
        this.appName = appName;
        this.userId = userId;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
