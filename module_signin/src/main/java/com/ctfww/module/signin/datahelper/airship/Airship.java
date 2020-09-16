package com.ctfww.module.signin.datahelper.airship;

import com.blankj.utilcode.util.LogUtils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Airship {
    private final static String TAG = "Airship";

    private Airship() {

    }

    private static class Inner {
        private static final Airship INSTANCE = new Airship();
    }

    public static Airship getInstance() {
        return Airship.Inner.INSTANCE;
    }

    public void synToCloud() {
        synSigninToCloud();
    }

    public void synFromCloud() {
        synSigninFromCloud();
    }

    public void synSigninToCloud() {
        SigninInfoAirship.synToCloud();
    }

    public void synSigninFromCloud() {
        SigninInfoAirship.synFromCloud();
    }

}
