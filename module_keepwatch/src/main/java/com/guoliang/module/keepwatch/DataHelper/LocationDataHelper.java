package com.guoliang.module.keepwatch.DataHelper;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.entity.MyPosition;
import com.guoliang.commonlib.utils.PermissionUtils;

import org.greenrobot.eventbus.EventBus;

public class LocationDataHelper {
    private final static String TAG = "LocationDataHelper";

    private Location mLocation;

    public LocationDataHelper() {

    }

    private static class Inner {
        private static final LocationDataHelper INSTANCE = new LocationDataHelper();
    }

    public static LocationDataHelper getInstance() {
        return LocationDataHelper.Inner.INSTANCE;
    }

    public Location getLocation() {
        return mLocation;
    }

    private final static long MIN_TIME = 1 * 1000;
    private final static float MIN_DISTANCE = 5.0f;
    private LocationManager mLocationManager;
    private Context context;

    @SuppressWarnings({"MissingPermission"})
    public void startListenLocationUpdate(Context context) {
        if (!PermissionUtils.isLocationPermissionGranted((Activity)context)) {
            return;
        }

        this.context = context;

        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, gpsLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, networkListener);
        }
        catch (NullPointerException e) {
            LogUtils.i(TAG, "更新位置有空指针");
        }

    }

    private LocationListener networkListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, mLocation)) {
                mLocation = location;
                postLatLng(location.getLatitude(), location.getLongitude(), "network");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

    };

    private LocationListener gpsLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, mLocation)) {
 //               mLocationManager.removeUpdates(networkListener);
                mLocation = location;
                postLatLng(location.getLatitude(), location.getLongitude(), "gps");
//                ToastUtils.showShort("is gps");
            }
//            if (mLocation != null) {
//                mLocationManager.removeUpdates(this);
//
//            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    public void removeListener() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(networkListener);
            mLocationManager.removeUpdates(gpsLocationListener);
        }
    }

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }
        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;
        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }
        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());
        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void postLatLng(double lat, double lng, String type) {
        MyPosition myPosition = new MyPosition(lat, lng, type);
        String value = GsonUtils.toJson(myPosition);
        EventBus.getDefault().post(new MessageEvent("update_location", value));
    }
}
