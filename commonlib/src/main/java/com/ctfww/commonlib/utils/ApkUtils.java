package com.ctfww.commonlib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;

public class ApkUtils {
    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    public static synchronized String getPackageTailName(Context context) {
        String appPackageName = getPackageName(context);
        int index = appPackageName.lastIndexOf('.');
        String name = index == -1 ? appPackageName : appPackageName.substring(index + 1);

        return name;
    }

    public static synchronized String getNavigationName(Context context) {
        String appPackageName = getPackageName(context);
        int index = appPackageName.lastIndexOf('.');
        String name = index == -1 ? appPackageName : appPackageName.substring(index + 1);

        return "/" + name + "/" + name;
    }

    public static void installApk(Context context, String absolutePath) {
        File apkfile = new File(absolutePath);
        if (!apkfile.exists()) {
            LogUtils.i("ApkUtils", "!apkfile.exists()");
            return;
        }

        LogUtils.i("ApkUtils", "start install apk");

        LogUtils.i("ApkUtils", "file path = " + apkfile.getAbsolutePath());

        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = FileProvider.getUriForFile(context,"com.ctfww.commonlib.utils" + ".myprovider", apkfile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
