package com.ctfww.module.user.entity;

import com.ctfww.commonlib.entity.EntityInterface;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class UserInfo implements EntityInterface {
    @Id
    private String userId;

    private String nickName;

    private String password;

    private String mobile;

    private String email;

    private String headUrl;

    private String birthday;

    private int gender;

    private String wechatNum;

    private String blogNum;

    private String qqNum;
    
    private long timeStamp;

    @Index
    private String synTag;

    @Generated(hash = 143568552)
    public UserInfo(String userId, String nickName, String password, String mobile,
            String email, String headUrl, String birthday, int gender,
            String wechatNum, String blogNum, String qqNum, long timeStamp,
            String synTag) {
        this.userId = userId;
        this.nickName = nickName;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.headUrl = headUrl;
        this.birthday = birthday;
        this.gender = gender;
        this.wechatNum = wechatNum;
        this.blogNum = blogNum;
        this.qqNum = qqNum;
        this.timeStamp = timeStamp;
        this.synTag = synTag;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public String toString() {
        return "userId = " + userId
                + ", nickName = " + nickName
                + ", password = " + password
                + ", mobile = " + mobile
                + ", email = " + email
                + ", headUrl = " + headUrl
                + ", birthday = " + birthday
                + ", gender = " + gender
                + ", wechat = " + wechatNum
                + ", blog = " + blogNum
                + ", qq = " + qqNum
                + ", timeStamp = " + timeStamp;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getWechatNum() {
        return this.wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum;
    }

    public String getBlogNum() {
        return this.blogNum;
    }

    public void setBlogNum(String blogNum) {
        this.blogNum = blogNum;
    }

    public String getQqNum() {
        return this.qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
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
