package com.ctfww.module.fingerprint;

public class JNI {
    static {
        System.loadLibrary("finger_print");
    }
    public native void setFingerPrintDatabase(String fingerPrintDatabase);
    public native String getWifiDist(String wifiList, int id);
    public native String getGpsDist(String gps, int id);
    public native String getWifiId(String wifiList);
    public native String getGpsId(String gps);
    public native String synthesizeFingerPrint(String fingerPrintArrStr);
}
