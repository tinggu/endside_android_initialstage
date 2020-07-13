package com.ctfww.commonlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class QRCodeUtils {
    public static final String TAG = "QRCodeUtils";
    static public Bitmap createDataQRCodeBitmap(String content, int deskId, String deskName) {
        int width = 1080;
        int height = 1080;

        if (content.length() <= 0) {
            return null;
        }

        Bitmap dataBitmap = ImageUtils.getDataBitmap("" + deskId, 100, 100);
        if (dataBitmap == null) {
            return null;
        }

        Bitmap qrBitmap = createQRCodeBitmap(content, width, height, "UTF-8",
                "H", "1", Color.BLACK, Color.WHITE, dataBitmap, 0.2F, null);

        Bitmap tittleBitmap = ImageUtils.getTittleBitmap(deskName, width, 80);
        qrBitmap = addTittle(qrBitmap, tittleBitmap);

        return qrBitmap;
    }

    /**
     *  生成自定义二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param characterSet          编码方式（一般使用UTF-8）
     * @param errorCorrectionLevel 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param colorBlack            黑色色块
     * @param colorWhite            白色色块
     * @param logoBitmap             logo图片（传null时不添加logo）
     * @param logoPercent            logo所占百分比
     * @param bitmapBlack           用来代替黑色色块的图片（传null时不代替）
     * @return
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height, String characterSet, String errorCorrectionLevel,
                                            String margin, int colorBlack, int colorWhite, Bitmap logoBitmap, float logoPercent, Bitmap bitmapBlack) {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 宽和高>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            /** 1.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            // 字符转码格式设置
            if (!TextUtils.isEmpty(characterSet)) {
                hints.put(EncodeHintType.CHARACTER_SET, characterSet);
            }
            // 容错率设置
            if (!TextUtils.isEmpty(errorCorrectionLevel)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            if (bitmapBlack != null) {
                //从当前位图按一定的比例创建一个新的位图
                bitmapBlack = Bitmap.createScaledBitmap(bitmapBlack, width, height, false);
            }
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {// 黑色色块像素设置
                        if (bitmapBlack != null) {//图片不为null，则将黑色色块换为新位图的像素。
                            pixels[y * width + x] = bitmapBlack.getPixel(x, y);
                        } else {
                            pixels[y * width + x] = colorBlack;
                        }
                    } else {
                        pixels[y * width + x] = colorWhite;// 白色色块像素设置
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            /** 5.为二维码添加logo图标 */
            if (logoBitmap != null) {
                return addLogo(bitmap, logoBitmap, logoPercent);
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向二维码中间添加logo图片(图片合成)
     *
     * @param srcBitmap   原图片（生成的简单二维码图片）
     * @param logoBitmap  logo图片
     * @param logoPercent 百分比 (用于调整logo图片在原图片中的显示大小, 取值范围[0,1] )
     *                    原图片是二维码时,建议使用0.2F,百分比过大可能导致二维码扫描失败。
     * @return
     */
    @Nullable
    private static Bitmap addLogo(@Nullable Bitmap srcBitmap, @Nullable Bitmap logoBitmap, float logoPercent) {
        if (srcBitmap == null) {
            return null;
        }
        if (logoBitmap == null) {
            return srcBitmap;
        }
        //传值不合法时使用0.2F
        if (logoPercent < 0F || logoPercent > 1F) {
            logoPercent = 0.2F;
        }

        /** 1. 获取原图片和Logo图片各自的宽、高值 */
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();

        /** 2. 计算画布缩放的宽高比 */
        float scaleWidth = srcWidth * logoPercent / logoWidth;
        float scaleHeight = srcHeight * logoPercent / logoHeight;

        /** 3. 使用Canvas绘制,合成图片 */
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        canvas.scale(scaleWidth, scaleHeight, srcWidth / 2, srcHeight / 2);
        canvas.drawBitmap(logoBitmap, srcWidth / 2 - logoWidth / 2, srcHeight / 2 - logoHeight / 2, null);

        return bitmap;
    }

    @Nullable
    private static Bitmap addTittle(@Nullable Bitmap srcBitmap, @Nullable Bitmap tittleBitmap) {
        if (srcBitmap == null) {
            return null;
        }
        if (tittleBitmap == null) {
            return srcBitmap;
        }

        /** 1. 获取原图片和Logo图片各自的宽、高值 */
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int tittleWidth = tittleBitmap.getWidth();
        int tittleHeight = tittleBitmap.getHeight();

        /** 3. 使用Canvas绘制,合成图片 */
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight + tittleHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        canvas.drawBitmap(tittleBitmap, 0, srcHeight, null);

        return bitmap;
    }

    public static int getQrDeskId(String content, final Context context) {
        if(TextUtils.isEmpty(content)) {
            LogUtils.i(TAG, "isValidQrContent: content is null");
            ToastUtils.showShort("二维码扫描的结果为空，请确认二维码是否正确！");
            return 0;
        }

        LogUtils.i(TAG, "onActivityResult: content = " + content);
        if (GlobeFun.isAllNumer(content)) {
            DialogUtils.onlyPrompt("二维码被识别成条形码，请确认二维码是否正确！", context);
            return 0;
        }

        int ret = 0;
        String deskIdStr = getSigninLocation(content);
        if ("empty".equals(deskIdStr)) {
            ToastUtils.showShort("二维码内容为空，签到失败！");
        }
        else if ("other_company".equals(deskIdStr)) {
            ToastUtils.showShort("是其他公司的二维码，签到失败！");
        }
        else if ("no_group_id".equals(deskIdStr)) {
            ToastUtils.showShort("该二维码中没有群组Id信息，签到失败！");
        }
        else if ("wrong_group_id".equals(deskIdStr)) {
            ToastUtils.showShort("该二维码对应的群组与你的工作群组不对应，签到失败！");
        }
        else if ("no_desk_id".equals(deskIdStr)) {
            ToastUtils.showShort("没有签到点编号信息，签到失败！");
        }
        else {
            ret = GlobeFun.parseInt(deskIdStr);
            if (ret == 0) {
                ToastUtils.showShort("签到点编号无效，签到失败！");
            }
        }

        return ret;
    }

    private static String getSigninLocation(String url) {
        if (TextUtils.isEmpty(url)) {
            return "empty";
        }

        int pos = url.indexOf("www.littlepine.cn");
        if (pos == -1) {
            return "other_company";
        }

        pos = url.indexOf("groupId=");
        if (pos == -1) {
            return "no_group_id";
        }

        int pos2 = url.indexOf('/', pos + 8);
        if (pos2 == -1) {
            return "wrong_group_id";
        }

        String groupId = url.substring(pos + 8, pos2);
        if (!groupId.equals(SPStaticUtils.getString("working_group_id"))) {
            return "wrong_group_id";
        }

        pos = url.indexOf("deskId=");
        if (pos == -1) {
            return "no_desk_id";
        }

        String deskId = url.substring(pos + 7);

        return deskId;
    }
}
