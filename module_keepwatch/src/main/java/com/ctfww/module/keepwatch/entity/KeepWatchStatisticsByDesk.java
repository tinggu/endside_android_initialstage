package com.ctfww.module.keepwatch.entity;

public class KeepWatchStatisticsByDesk {
    private String userId;
    private int deskId;
    private int frequency;
    private int signinCount;

    private String nickName;
    private String deskName;
    private String deskType;

    private double dangerFactor;

    public void combineDangerFactor() {
        dangerFactor = 0.0;
        if (frequency <= 3) {
            dangerFactor = 0.0;
        }
        else {
            if (signinCount == 0) {
                dangerFactor = (double)frequency;
            }
            else {
                dangerFactor = (double)frequency / signinCount;
            }
        }
    }

    public KeepWatchStatisticsByDesk() {

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setSigninCount(int signinCount) {
        this.signinCount = signinCount;
    }

    public int getSigninCount() {
        return signinCount;
    }

    public void  setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDangerFactor(double dangerFactor) {
        this.dangerFactor = dangerFactor;
    }

    public double getDangerFactor() {
        return dangerFactor;
    }
}
