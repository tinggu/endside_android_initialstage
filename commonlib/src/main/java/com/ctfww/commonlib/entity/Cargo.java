package com.ctfww.commonlib.entity;

import java.util.List;

public class Cargo<T> {
    private long timeStamp;
    private List<T> list;

    public String toString() {
        return "timeStamp = " + timeStamp
                + ", list.size() = " + (list == null ? 0 : list.size());
    }

    public boolean isEmpty() {
        return list == null || list.isEmpty();
    }

    public Cargo() {

    }

    public Cargo(long timeStamp, List<T> list) {
        this.timeStamp = timeStamp;
        this.list = list;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }
}
