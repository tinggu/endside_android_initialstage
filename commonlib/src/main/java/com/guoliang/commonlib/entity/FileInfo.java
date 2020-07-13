package com.guoliang.commonlib.entity;

import androidx.annotation.Nullable;

public class FileInfo {
    private String url;
    private String path;
    private String name;

    private String addition1;
    private String addition2;

    public FileInfo() {

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddition1(String addition1) {
        this.addition1 = addition1;
    }

    public String getAddition1() {
        return addition1;
    }

    public void setAddition2(String addition2) {
        this.addition2 = addition2;
    }

    public String getAddition2() {
        return addition2;
    }
}
