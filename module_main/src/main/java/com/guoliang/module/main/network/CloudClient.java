package com.guoliang.module.main.network;

public class CloudClient {

    private static final String TAG = "MainCloudClient";

    private final static int VALIDE_DATA = 80000000;

    private ICloudMethod mCloudMethod;


    public static CloudClient getInstance() {
        return CloudClient.Inner.INSTANCE;
    }

    private static class Inner {
        private static final CloudClient INSTANCE = new CloudClient();
    }

    private CloudClient(){
    }

    public void setmCloudMethod(ICloudMethod mCloudMethod) {
        this.mCloudMethod = mCloudMethod;
    }




}
