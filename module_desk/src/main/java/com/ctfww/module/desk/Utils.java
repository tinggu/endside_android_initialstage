package com.ctfww.module.desk;

import android.content.Context;
import android.graphics.Bitmap;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.Qr;
import com.ctfww.commonlib.utils.ImageUtils;
import com.ctfww.commonlib.utils.QRCodeUtils;

import java.util.Calendar;

public class Utils {
    public static String getDeskQrUrl(int deskId) {
        String groupId = SPStaticUtils.getString("working_group_id");
        return "http://www.littlepine.cn/groupId=" + groupId + "/deskId=" + deskId;
    }

    public static void produceSigninDeskQrFile(Context context, int deskId, String deskName) {
        String url = Utils.getDeskQrUrl(deskId);
        Qr qr = new Qr(url, "" + deskId, deskName);
        Bitmap bitmap = QRCodeUtils.createDataQRCodeBitmap(qr);
        if (bitmap != null) {
            Calendar calendar = Calendar.getInstance();
            String dateTimeStr = String.format("%d%02d%02d%02d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            ImageUtils.saveImageToGallery(context, bitmap, "" + deskId + "_" + dateTimeStr + ".jpg");
        }
    }
}
