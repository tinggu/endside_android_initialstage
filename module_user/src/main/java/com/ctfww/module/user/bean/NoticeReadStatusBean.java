package com.ctfww.module.user.bean;

import java.util.Calendar;

public class NoticeReadStatusBean {
    private String noticeId;
    private String userId;
    private int flag;
    private long timeStamp;

    private String nickName;

    public String toString() {
        return "noticeId = " + noticeId
                + ", userId = " + userId
                + ", nickName = " + nickName
                + ", timeStamp = " + timeStamp
                + ", flag = " + flag;
    }

    public String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        return String.format("%04d-%02d-%02d %02d:%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public NoticeReadStatusBean() {

    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }
}
