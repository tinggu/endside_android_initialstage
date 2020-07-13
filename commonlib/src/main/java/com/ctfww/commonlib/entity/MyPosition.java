package com.ctfww.commonlib.entity;

public class MyPosition {
    private double lat;
    private double lng;
    private String type;
    private String fingerPrint;

    public String toString() {
        return "lat = " + lat
                + ", lng = " + lng
                + ", type = " + type
                + ", fingerPrint = " + fingerPrint;
    }

    public MyPosition() {
        lat = 0.0;
        lng = 0.0;
        type = "default";
    }

    public MyPosition(double lat, double lng, String type) {
        this.lat = lat;
        this.lng = lng;
        this.type = type;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }
}
