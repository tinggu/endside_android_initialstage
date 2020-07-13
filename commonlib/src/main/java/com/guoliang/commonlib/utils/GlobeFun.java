package com.guoliang.commonlib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class GlobeFun {
    public static int parseInt(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }

        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long parseLong(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }

        try {
            return Long.parseLong(s);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public static float parseFloat(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0.0f;
        }

        try {
            return Float.parseFloat(s);
        }
        catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    public static double parseDouble(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0.0;
        }

        try {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static int getKeyCount(String str, char key) {
        int index = 0; //定义变量。记录每一次找到的key的位置。
        int count = 0; //定义变量，记录出现的次数。

        //定义循环。只要索引到的位置不是-1，继续查找。
        while((index = str.indexOf(key,index))!=-1){
            //每循环一次，就要明确下一次查找的起始位置。
            index = index + 1;
            //每查找一次，count自增。
            count++;
        }
        return count;
    }

    public static int getKeyCount(String str, String key) {
        int index = 0; //定义变量。记录每一次找到的key的位置。
        int count = 0; //定义变量，记录出现的次数。

        //定义循环。只要索引到的位置不是-1，继续查找。
        while((index = str.indexOf(key,index))!=-1){
            //每循环一次，就要明确下一次查找的起始位置。
            index = index + key.length();
            //每查找一次，count自增。
            count++;
        }
        return count;
    }

    public static int[] getYearMonthDay(String s, char key) {
        if (getKeyCount(s, key) != 2) {
            return null;
        }

        int[] arr = new int[3];

        int start = 0;
        int end = s.indexOf(key);
        String year = s.substring(start, end);
        arr[0] = GlobeFun.parseInt(year);

        start = end + 1;
        end = s.indexOf(key, start);
        String month = s.substring(start, end);
        arr[1] = GlobeFun.parseInt(month);

        start = end + 1;
        String day = s.substring(start, s.length());
        arr[2] = GlobeFun.parseInt(day);

        return arr;
    }

    public static String getAppVersion(Context context) {
        String version;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            version = packageInfo.versionName;
        } catch (Exception e) {
            LogUtils.e("VersionInfo", "Exception", e);
            version = "";
        }
        return version;
    }

    public static String getSHA(String inputStr) {
        BigInteger sha = null;
        byte[] inputData = inputStr.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(inputData);
            sha = new BigInteger(messageDigest.digest());
            return sha.toString(32);
        }
        catch (Exception e) {
            return "";
        }
    }

    public static void downLoad(final String path, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            File dir = new File(path);
                            if (!dir.exists()) {
                                dir.mkdir();
                            }

                            File file = new File(path, fileName);
                            fileOutputStream = new FileOutputStream(file);//指定文件保存路径，代码看下一步
                            byte[] buf = new byte[1024];
                            int ch;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String stampToDateTime(long utcTime) {
        String res = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            res = simpleDateFormat.format(utcTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        return res;
    }

    public static String stampToDate(long utcTime) {
        String res = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            res = simpleDateFormat.format(utcTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        return res;
    }

    public static String degree2DegreeMinuteSecond(double val) {
        int degree = (int)val;
        int minute = (int)((val - degree) * 60);
        double second = (val - degree - (double) minute / 60) * 3600;

        return String.format("%d°%d′%.2f″", degree, minute, second);
    }

    public static boolean isAllNumer(String str){

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();

    }
}
