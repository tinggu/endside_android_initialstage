package com.ctfww.commonlib.crash;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * UncaughtException处理类，当程序发生Uncaught异常的时候，由该类来处理异常
 *
 * Created by Diamond on 10/21/19.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    // 默认的处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    // Context对象
    private Context mContext;

    // 存储异常信息
    private String mExceptionInfo;

    // 存储版本信息
    private Map<String, String> mVersionInfo = new HashMap<>();

    // 存储设备信息
    private Map<String, String> mDeviceInfo = new HashMap<>();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        @SuppressLint("StaticFieldLeak")
        private static final CrashHandler INSTANCE = new CrashHandler();
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        if (!handleException(throwable) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LogUtils.e(TAG, "uncaughtException: e.msg = " + e.getMessage());
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义异常处理方法
     *
     * @param ex 异常信息
     * @return true: 处理了该异常 false:没有处理
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        collectExceptionInfo(ex);

        collectVersionInfo();

        collectDeviceInfo();

        saveCrashInfo2File();

        return true;
    }

    /**
     * 收集异常信息
     *
     * @param ex 异常信息
     */
    private void collectExceptionInfo(Throwable ex) {
        Writer mWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mWriter);
        ex.printStackTrace(mPrintWriter);
        ex.printStackTrace();
        Throwable mThrowable = ex.getCause();
        while (mThrowable != null) {
            mThrowable.printStackTrace(mPrintWriter);
            mPrintWriter.append("\r\n");
            mThrowable = mThrowable.getCause();
        }
        mPrintWriter.close();

        mExceptionInfo =  mWriter.toString();
    }

    /**
     * 收集版本信息
     */
    private void collectVersionInfo() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mVersionInfo.put("versionName", versionName);
                mVersionInfo.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "e.msg = " + e.getMessage());
        }
    }

    /**
     * 收集设备信息
     */
    private void collectDeviceInfo() {
        // 反射机制
        Field[] mFields =Build.class.getDeclaredFields();
        // 迭代Build的key-value，为了分析各种版本手机报错的原因
        for (Field field : mFields) {
            try {
                field.setAccessible(true);
                mDeviceInfo.put(field.getName(), field.get("").toString());
            } catch (IllegalArgumentException e) {
                LogUtils.e(TAG, "collectDeviceInfo: e.message = " + e.getMessage());
            } catch (IllegalAccessException e) {
                LogUtils.e(TAG, "collectDeviceInfo: e.message = " + e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     */
    private void saveCrashInfo2File() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getInfoStr(mVersionInfo));
        stringBuilder.append(getInfoStr(mDeviceInfo));
        stringBuilder.append(mExceptionInfo);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File crashFile = new File(mContext.getExternalFilesDir("crash"), getFileName());
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(crashFile);
                fileOutputStream.write(stringBuilder.toString().getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                LogUtils.e(TAG, "saveCrashInfo2File: e.message = " + e.getMessage());
            } catch (IOException e) {
                LogUtils.e(TAG, "saveCrashInfo2File: e.message = " + e.getMessage());
            }
        }
    }

    /**
     * 生成文件名
     * @return 文件名
     */
    private String getFileName() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
        String mTime = formatter.format(new Date());
        return "CrashLog-" + mTime + ".log";
    }

    /**
     * map集合装换成string
     * @param info map集合
     * @return string
     */
    private String getInfoStr(Map<String, String> info) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append("\n");
        }
        return sb.toString();
    }
}
