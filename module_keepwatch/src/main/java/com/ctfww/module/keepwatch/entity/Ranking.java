package com.ctfww.module.keepwatch.entity;

import com.ctfww.commonlib.utils.GlobeFun;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Ranking {
    @Id
    private String id;
    @Index
    private String groupId;
    @Index
    private String userId;
    private int score;

    @Generated(hash = 1834159379)
    public Ranking(String id, String groupId, String userId, int score) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.score = score;
    }

    @Generated(hash = 1361760905)
    public Ranking() {
    }

    public void combineId() {
        id = GlobeFun.getSHA(groupId + userId);
    }

    public String toString() {
        return "userId = " + userId
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
    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
    }

}
