package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeepWatchRanking {
    @Id
    private String id;
    @Index
    private String groupId;
    @Index
    private String userId;
    private String headUrl;
    private String nickName;
    private int score;
    @Generated(hash = 122747394)
    public KeepWatchRanking(String id, String groupId, String userId,
            String headUrl, String nickName, int score) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.headUrl = headUrl;
        this.nickName = nickName;
        this.score = score;
    }
    @Generated(hash = 1251131850)
    public KeepWatchRanking() {
    }

    public void combineId() {
        id = GlobeFun.getSHA(groupId + userId);
    }

    public String toString() {
        return "userId = " + userId
                + ", headUrl = " + headUrl
                + ", nickName = " + nickName
                + ", score = " + score;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getHeadUrl() {
        return this.headUrl;
    }
    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
    }

}
