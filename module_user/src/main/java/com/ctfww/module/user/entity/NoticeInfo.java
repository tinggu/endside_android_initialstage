package com.ctfww.module.user.entity;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Calendar;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NoticeInfo implements EntityInterface {
    @Id
    private String noticeId;

    @Index
    private String groupId;
    @Index
    private String userId;
    @Index
    private String toUserId;
    private String tittle;
    private String content;
    private int type;
    private long timeStamp;

    @Index
    private String status;

    @Index
    private String synTag;

    @Generated(hash = 103116940)
    public NoticeInfo(String noticeId, String groupId, String userId, String toUserId, String tittle, String content, int type, long timeStamp, String status, String synTag) {
        this.noticeId = noticeId;
        this.groupId = groupId;
        this.userId = userId;
        this.toUserId = toUserId;
        this.tittle = tittle;
        this.content = content;
        this.type = type;
        this.timeStamp = timeStamp;
        this.status = status;
        this.synTag = synTag;
    }

    @Generated(hash = 426617346)
    public NoticeInfo() {
    }

    public void combineNoticeId() {
        noticeId = userId + timeStamp;
    }

    public String toString() {
        return "noticeId = " + noticeId
                + ", groupId = " + groupId
                + ", userId = " + userId
                + ", tittle = " + tittle
                + ", content = " + content
                + ", type = " + type
                + ", timeStamp = " + timeStamp
                + ", status = " + status;
    }

    public String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        return String.format("%04d-%02d-%02d %02d:%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public String getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToUserId() {
        return this.toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getTittle() {
        return this.tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }

//    public String getReadStatusText() {
//        String ret = "未读";
//        if (flag == 0) {
//            ret = "未读";
//        }
//        else if (flag == 1) {
//            ret = "未读";
//        }
//        else if (flag == 2) {
//            ret = "已读";
//        }
//
//        return ret;
//    }
//
//    public int getReadStatusTextColor() {
//        int ret = 0xFF4A4A4A;
//        if (flag == 0) {
//            ret = 0xFFFF0000;
//        }
//        else if (flag == 1) {
//            ret = 0xFFFF0000;
//        }
//        else if (flag == 2) {
//            ret = 0xFF4A4A4A;
//        }
//
//        return ret;
//    }

     
}
