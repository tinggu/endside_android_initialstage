package com.ctfww.module.fingerprint.entity;

public class MacRssi {
    private String mac;
    private int rssi;
    public MacRssi() {

    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }
}
