package com.ctfww.module.user.bean;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.GlobeFun;

import java.util.Calendar;

public class NoticeBean {
    private String noticeId;
    private String groupId;
    private String userId;
    private String nickName;
    private String tittle;
    private String content;
    private long timeStamp;
    private int flag;

    public String toString() {
        return "noticeId = " + noticeId
                + ", groupId = " + groupId
                + ", userId = " + userId
                + ", nickName = " + nickName
                + ", tittle = " + tittle
                + ", content = " + content
                + ", timeStamp = " + timeStamp
                + ", flag = " + flag;
    }

    public NoticeBean() {
        noticeId = GlobeFun.getSHA(SPStaticUtils.getString("user_open_id") + System.currentTimeMillis());
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getTittle() {
        return tittle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}
