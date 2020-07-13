package com.ctfww.commonlib.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.utils.PermissionUtils;

import java.util.List;
import java.util.Locale;

public class MyLocation {

    @SuppressWarnings({"MissingPermission"})
    public static Location getLocation(Context context){
        if (!PermissionUtils.isLocationPermissionGranted(context)) {
            return null;
        }

        String provider;
        //获取定位服务
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) {
            return null;
        }

        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.NETWORK_PROVIDER;//NETWORK_PROVIDER GPS_PROVIDER
            LogUtils.i("Location", "is GPS");
        }
        else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;
            LogUtils.i("Location", "is network");
        }
        else {
            return null;
        }

        return locationManager.getLastKnownLocation(provider);
    }

    public static String getAddr(Context context, Location location) {
        //用来接收位置的详细信息
        List<Address> result = null;
        String addressLine = "";
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result != null && result.get(0) != null){
            //这块获取到的是个数组我们取一个就好 下面是具体的方法查查API就能知道自己要什么
            //result.get(0).getCountryName()

            addressLine = result.get(0).getAddressLine(0);
            LogUtils.i("address",addressLine);
            //Toast.makeText(mContext,result.get(0).toString(),Toast.LENGTH_LONG).show();
        }

        return addressLine;
    }

    public static final boolean isGPSOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps|| network) {
            return true;
        }

        return false;
    }
}
