package com.ctfww.module.keepwatch.DataHelper.airship;

public class CommonAirship {
    public final static long MILLISECONDS_IN_ONE_YEAR = 365l * 24l * 3600l * 1000l;

    public static long getDefaultStartTime() {
        return System.currentTimeMillis() - MILLISECONDS_IN_ONE_YEAR;
    }
}
