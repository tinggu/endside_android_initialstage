package com.ctfww.commonlib.entity;

import android.location.Location;

public class LocationGson {
    private double lat;
    private double lng;
    private long timeStamp;
    private float accu;

    public String toString() {
        return "lat = " + lat
                + ", lng = " + lng
                + ", timeStamp = " + timeStamp
                + ", accu = " + accu;
    }

    public void set(Location location) {
        setLat(location.getLatitude());
        setLng(location.getLongitude());
        setTimeStamp(location.getTime());
        setAccu(location.getAccuracy());
    }

    public LocationGson() {

    }

    public LocationGson(Location location) {
        set(location);
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

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setAccu(float accu) {
        this.accu = accu;
    }

    public float getAccu() {
        return accu;
    }
}
