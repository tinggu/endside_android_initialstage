package com.ctfww.module.user.bean;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.GlobeFun;

public class GroupInviteBean {
    private String inviteId;
    private String fromUserId;
    private String toMobile;
    private String groupId;
    private long timeStamp;
    private String status;

    public String toString() {
        return "inviteId = " + inviteId
                + "fromUserId = " + fromUserId
                + "toMobile = " + toMobile
                + "groupId = " + groupId
                + "timeStamp = " + timeStamp
                + "status = " + status;
    }

    public GroupInviteBean() {
        timeStamp = System.currentTimeMillis();
        fromUserId = SPStaticUtils.getString("user_open_id");
        status = "send";
        inviteId = GlobeFun.getSHA(fromUserId + timeStamp);
    }

    public GroupInviteBean(String inviteId, String status) {
        this.inviteId = inviteId;
        this.status = status;
    }

    public GroupInviteBean(String inviteId, String fromUserId, String toMobile, String groupId, long timeStamp, String status) {
        this.inviteId = inviteId;
        this.fromUserId = fromUserId;
        this.toMobile = toMobile;
        this.groupId = groupId;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public String getInviteId() {
        return this.inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getFromUserId() {
        return this.fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToMobile() {
        return toMobile;
    }

    public void setToMobile(String toMobile) {
        this.toMobile = toMobile;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
