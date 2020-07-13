package com.ctfww.commonlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_LOCATION = 2;
    public static final int REQUEST_CODE_NETWORK = 3;
    public static final int REQUEST_CODE_STORAGE = 4;

    /**
     * 申请camera权限
     * @param activity 上下文
     */
    public static void requestCameraPermission(Activity activity) {
        if (isCameraPermissionGranted(activity)) {
            return;
        }

        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
    }

    public static boolean isCameraPermissionGranted(Context context){
        if (Build.VERSION.SDK_INT <= 22) {
            return true;
        }

        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReceivedCameraRsp(String[] permissions, int requestCode) {
        if (requestCode != REQUEST_CODE_CAMERA) {
            return false;
        }

        if (permissions.length <= 0) {
            return false;
        }

        return Manifest.permission.CAMERA.equals(permissions[0]);
    }

    /**
     * 申请定位权限
     * @param activity 上下文
     */
    public static void requestLocationPermission(Activity activity) {
        if (isLocationPermissionGranted(activity)) {
            return;
        }

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
    }

    public static boolean isLocationPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReceivedLocationRsp(String[] permissions, int requestCode) {
        if (requestCode != REQUEST_CODE_LOCATION) {
            return false;
        }

        if (permissions.length <= 0) {
            return false;
        }

        return Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[0]);
    }

    /**
     * 申请读写权限
     * @param activity 上下文
     */
    public static void requestStoragePermission(Activity activity) {
        if (isStoragePermissionGranted(activity)) {
            return;
        }

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
    }

    public static boolean isStoragePermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isReceivedStorageRsp(String[] permissions, int requestCode) {
       if (requestCode != REQUEST_CODE_STORAGE) {
           return false;
       }

       if (permissions.length <= 0) {
           return false;
       }

       return Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0]);
    }
}
