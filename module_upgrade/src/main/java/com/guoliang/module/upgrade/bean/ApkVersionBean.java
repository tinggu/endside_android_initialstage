package com.guoliang.module.upgrade.bean;

public class ApkVersionBean {
    private String apkName;
    private int apkSize;
    private String apkVersion;
    private String versionDesc;
    private String downloadUrl;
    private long createTimestamp;
    private int versionCode;

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkSize(int apkSize) {
        this.apkSize = apkSize;
    }

    public int getApkSize() {
        return apkSize;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }
}
