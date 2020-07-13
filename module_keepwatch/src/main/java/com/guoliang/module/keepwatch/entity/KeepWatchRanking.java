package com.guoliang.module.keepwatch.entity;

public class KeepWatchRanking {
    private String userId;
    private String headUrl;
    private String nickName;
    private int score;

    public KeepWatchRanking() {
        userId = "";
        headUrl = "";
        nickName = "";
        score = 0;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setScore(int power) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
