package com.guoliang.commonlib.storage.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NullBean {
    @Id
    int index;

    @Generated(hash = 1419138349)
    public NullBean(int index) {
        this.index = index;
    }

    @Generated(hash = 874639358)
    public NullBean() {
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
