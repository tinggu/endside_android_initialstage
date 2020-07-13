package com.guoliang.module.keepwatch.entity;

public class KeepWatchGroupSummary {
    private int memberCount;
    private int deskCount;

    public KeepWatchGroupSummary() {
        memberCount = 0;
        deskCount = 0;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setDeskCount(int deskCount) {
        this.deskCount = deskCount;
    }

    public int getDeskCount() {
        return deskCount;
    }
}
