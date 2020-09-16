package com.ctfww.module.keepwatch.entity;

public class Ranking {
    private String userId;
    private int score;

    public Ranking(String userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    public Ranking() {
    }


    public String toString() {
        return "userId = " + userId
                + ", score = " + score;
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
