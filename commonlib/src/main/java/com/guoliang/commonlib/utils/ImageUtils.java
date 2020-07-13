package com.guoliang.commonlib.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    /**
     * 保存图片到指定路径
     *
     * @param context
     * @param bitmap   要保存的图片
     * @param fileName 自定义图片名称
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bitmap, String fileName) {
        // 保存图片至指定路径
        String storePath = context.getExternalFilesDir(null).toString() + File.separator + "qrcode";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片(80代表压缩20%)
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();

            //发送广播通知系统图库刷新数据
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                ToastUtils.showShort("二维码已保存为：" + appDir + "/" + fileName);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap getDataBitmap(String data, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        TextPaint textPaint = new TextPaint();//myTextPaint();
        textPaint.setColor(Color.argb(255, 255, 201, 14));
        textPaint.setTextSize(40);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = (int) (height / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式

        canvas.drawColor(Color.rgb(255, 255, 255));

        canvas.drawText(data, width / 2, baseLineY, textPaint);

        return bitmap;
    }

    public static Bitmap getTittleBitmap(String tittle, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        TextPaint textPaint = new TextPaint();//myTextPaint();
        textPaint.setColor(Color.argb(255, 20, 20, 20));
        textPaint.setTextSize(60);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = (int) (height / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式

        canvas.drawColor(Color.rgb(255, 255, 255));

        canvas.drawText(tittle, width / 2, baseLineY, textPaint);

        return bitmap;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
