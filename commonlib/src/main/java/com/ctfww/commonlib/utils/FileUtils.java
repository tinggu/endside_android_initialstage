package com.ctfww.commonlib.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.loader.content.CursorLoader;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {
    private static String TAG = "FileUtils";
    public static File downloadFile(String filePath, Context context, String saveDir, String saveName) {
        LogUtils.i(TAG, "downloadFile: filePath = " + filePath + ", saveDir = " + saveDir + ", saveName = " + saveName);
        try {
            URL url = new URL(filePath);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            int contentLength = conn.getContentLength();
            LogUtils.e(TAG, "contentLength = " + contentLength);

            File dir = new File(context.getExternalFilesDir(""), saveDir);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    LogUtils.e(TAG, "downloadVoiceFile: make dir failed");
                    return null;
                }
            }

            String fileName = saveName;
            if (TextUtils.isEmpty(fileName)) {
                int position = filePath.lastIndexOf('/');
                fileName = position == -1 ? "test.dat" : filePath.substring(position + 1);
            }

            File file = new File(dir, fileName);
            if (file.exists()) {
                if (!file.delete()) {
                    LogUtils.e(TAG, "downloadFile: delete file failed = " + file.getCanonicalPath());
                    return null;
                }
            }

            //创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(file.getCanonicalPath());
            //写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

            //完成后关闭流
            LogUtils.e(TAG, "downloadFile: download-finish");
            os.close();
            is.close();

            return file;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNetworkUrl(String filePath) {
        String filePathTemp = filePath.toLowerCase();
        int position = filePathTemp.indexOf("http://");
        if (position == 0) {
            return true;
        }

        position = filePathTemp.indexOf("https://");
        if (position == 0) {
            return true;
        }

        return false;
    }

    public static String getRealPathFromUri(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }
}
