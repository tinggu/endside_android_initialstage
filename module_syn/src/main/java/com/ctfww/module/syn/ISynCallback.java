package com.ctfww.module.syn;

import java.util.List;

public interface ISynCallback {
    public List<Object> readDB();
    public void addToDB(Object object);
    public void addToCloud(Object object);
}
