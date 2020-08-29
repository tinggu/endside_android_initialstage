package com.ctfww.commonlib.entity;

public class IdName {
    private String id;
    private String name;

    public String toString() {
        return "id = " + id
                + ", name = " + name;
    }

    public IdName() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
