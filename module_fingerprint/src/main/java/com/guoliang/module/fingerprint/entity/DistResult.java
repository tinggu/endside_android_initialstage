package com.guoliang.module.fingerprint.entity;

public class DistResult {
    private int id;
    private double dist;
    private int level;

    public String toString() {
        return "id = " + id
                + ", dist = " + dist
                + ", level = " + level;
    }

    public String getStringMatchLevel() {
        String ret = "default";
        if (level == 1) {
            ret = "excellent";
        }
        else if (level == 2) {
            ret = "good";
        }
        else {
            ret = "bad";
        }

        return ret;
    }

    public DistResult() {
        id = 0;
        dist = 0.0;
        level = 0;
    }

    public DistResult(int id, double dist, int level) {
        this.id = id;
        this.dist = dist;
        this.level = level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public double getDist() {
        return dist;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
