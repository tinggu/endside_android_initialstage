package com.ctfww.module.user.entity;

import com.ctfww.commonlib.entity.EntityInterface;
import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Calendar;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NoticeReadStatus implements EntityInterface {
    @Id
    private String id;
    @Index
    private String noticeId;
    @Index
    private String groupId;
    private String userId;
    private int flag;
    private long timeStamp;

    @Index
    private String synTag;

    @Generated(hash = 284380590)
    public NoticeReadStatus(String id, String noticeId, String groupId, String userId, int flag, long timeStamp, String synTag) {
        this.id = id;
        this.noticeId = noticeId;
        this.groupId = groupId;
        this.userId = userId;
        this.flag = flag;
        this.timeStamp = timeStamp;
        this.synTag = synTag;
    }

    @Generated(hash = 1635878122)
    public NoticeReadStatus() {
    }

    public String toString() {
        return "noticeId = " + noticeId
                + ", groupId = " + groupId
                + ", userId = " + userId
                + ", timeStamp = " + timeStamp
                + ", flag = " + flag;
    }

    public void combieId() {
        id = GlobeFun.getSHA(noticeId + userId);
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSynTag() {
        return this.synTag;
    }

    public void setSynTag(String synTag) {
        this.synTag = synTag;
    }
}
