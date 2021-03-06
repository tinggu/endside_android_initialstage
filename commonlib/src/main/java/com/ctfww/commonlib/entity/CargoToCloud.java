package com.ctfww.commonlib.entity;

import java.util.ArrayList;
import java.util.List;

public class CargoToCloud<T extends EntityInterface> {
    private List<T> newList;
    private List<T> updateList;

    public CargoToCloud() {

    }

    public CargoToCloud(List<T> list) {
        setList(list);
    }

    public String toString() {
        return "newList.size() = " + newList.size()
                + ", updateList.size() = " + updateList.size();
//        return updateList.toString();
    }

    public void setList(List<T> list) {
        newList = new ArrayList<>();
        updateList = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            T t = list.get(i);
            if ("new".equals(t.getSynTag())) {
                newList.add(t);
            }
            else {
                updateList.add(t);
            }
        }
    }
}
