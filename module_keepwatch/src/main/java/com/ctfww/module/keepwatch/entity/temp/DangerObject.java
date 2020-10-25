package com.ctfww.module.keepwatch.entity.temp;

public class DangerObject {
    private String groupId;
    private int objectId;
    private String type;

    private int signinCount;
    private int shouldCount;
    private int keyeventCount;

    private int signinScore;
    private int shouldScore;
    private int keyeventScore;

    public String toString() {
        return "groupId = " + groupId
                + ", objectId = " + objectId
                + ", type = " + type
                + ", signinCount = " + signinCount
                + ", shouldCount = " + shouldCount
                + ", keyeventCount = " + keyeventCount
                + ", signinScore = " + signinScore
                + ", shouldScore = " + shouldScore
                + ", keyeventScore = " + keyeventScore;
    }

    public String combineReason() {
        String ret = "";
        if (keyeventScore >= 20 && shouldScore - signinScore >= 20) {
            ret = "有严重的事故上报且巡检率极低！";
        }
        else if (keyeventScore > 0 && keyeventScore < 20 && shouldScore - signinScore >= 20) {
            ret = "有事故上报且巡检率极低！";
        }
        else if (keyeventScore == 0 && shouldScore - signinScore >= 20) {
            ret = "巡检率极低！";
        }
        else if (keyeventScore >= 20 && shouldScore - signinScore > 0 && shouldScore - signinScore < 20) {
            ret = "有严重的事故上报且巡检率较低！";
        }
        else if (keyeventScore >= 20 && shouldScore == signinScore) {
            ret = "有严重的事故上报！";
        }
        else {
            ret = "暂无大的风险！";
        }

        return ret;
    }

    public DangerObject() {
        signinCount = 0;
        shouldCount = 0;
        keyeventCount = 0;
        signinScore = 0;
        shouldScore = 0;
        keyeventScore = 0;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSigninCount() {
        return this.signinCount;
    }

    public void setSigninCount(int signinCount) {
        this.signinCount = signinCount;
    }

    public int getShouldCount() {
        return this.shouldCount;
    }

    public void setShouldCount(int shouldCount) {
        this.shouldCount = shouldCount;
    }

    public int getKeyeventCount() {
        return this.keyeventCount;
    }

    public void setKeyeventCount(int shouldCount) {
        this.keyeventCount = keyeventCount;
    }

    public int getSigninScore() {
        return this.signinScore;
    }

    public void setSigninScore(int signinCount) {
        this.signinScore = signinScore;
    }

    public int getShouldScore() {
        return this.shouldScore;
    }

    public void setShouldScore(int shouldScore) {
        this.shouldScore = shouldScore;
    }

    public int getKeyeventScore() {
        return this.keyeventScore;
    }

    public void setKeyeventScore(int shouldScore) {
        this.keyeventScore = keyeventScore;
    }
}
