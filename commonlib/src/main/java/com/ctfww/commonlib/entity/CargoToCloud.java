package com.ctfww.commonlib.entity;

import java.util.ArrayList;
import java.util.List;

public class CargoToCloud<T extends EntityInterface> {
    private List<T> newList;
    private List<T> modifyList;

    public CargoToCloud() {

    }

    public CargoToCloud(List<T> list) {
        setList(list);
    }

    public String toString() {
        return "newList.size() = " + newList.size()
                + ", modifyList.size() = " + modifyList.size();
    }

    public void setList(List<T> list) {
        newList = new ArrayList<>();
        modifyList = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            T t = list.get(i);
            if ("new".equals(t.getSynTag())) {
                newList.add(t);
            }
            else {
                modifyList.add(t);
            }
        }
    }
}
