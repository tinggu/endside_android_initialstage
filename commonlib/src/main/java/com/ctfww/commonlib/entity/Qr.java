package com.ctfww.commonlib.entity;

import android.text.TextUtils;

public class Qr {
    private String url;
    private String logo;
    private String name;

    public boolean isValid() {
        return !TextUtils.isEmpty(url);
    }

    public Qr() {

    }

    public Qr(String url, String logo, String name) {
        this.url = url;
        this.logo = logo;
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
