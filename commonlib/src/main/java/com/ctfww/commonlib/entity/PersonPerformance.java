package com.ctfww.commonlib.entity;

public class PersonPerformance {
    private String userId;
    private String headUrl;
    private String nickName;
    private int score;

    public PersonPerformance() {

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

    public void setScore(int score) {
        this.score =score;
    }

    public int getScore() {
        return score;
    }
}
