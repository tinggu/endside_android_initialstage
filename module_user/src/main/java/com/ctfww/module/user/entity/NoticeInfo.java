package com.ctfww.module.user.entity;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.GlobeFun;

import java.util.Calendar;

public class NoticeInfo {
    private String noticeId;
    private String groupId;
    private String userId;
    private String nickName;
    private String tittle;
    private String content;
    private int type;
    private long timeStamp;
    private int flag;

    public String toString() {
        return "noticeId = " + noticeId
                + ", groupId = " + groupId
                + ", userId = " + userId
                + ", nickName = " + nickName
                + ", tittle = " + tittle
                + ", content = " + content
                + ", type = " + type
                + ", timeStamp = " + timeStamp
                + ", flag = " + flag;
    }

    public String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        return String.format("%04d-%02d-%02d %02d:%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public String getReadStatusText() {
        String ret = "未读";
        if (flag == 0) {
            ret = "未读";
        }
        else if (flag == 1) {
            ret = "未读";
        }
        else if (flag == 2) {
            ret = "已读";
        }

        return ret;
    }

    public int getReadStatusTextColor() {
        int ret = 0xFF4A4A4A;
        if (flag == 0) {
            ret = 0xFFFF0000;
        }
        else if (flag == 1) {
            ret = 0xFFFF0000;
        }
        else if (flag == 2) {
            ret = 0xFF4A4A4A;
        }

        return ret;
    }

    public NoticeInfo() {
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

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
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
