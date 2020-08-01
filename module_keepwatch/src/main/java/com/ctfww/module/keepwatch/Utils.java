package com.ctfww.module.keepwatch;

import android.content.Context;
import android.graphics.Bitmap;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.ImageUtils;
import com.ctfww.commonlib.utils.QRCodeUtils;
import com.ctfww.commonlib.utils.SynDB;
import com.ctfww.module.keepwatch.DataHelper.CloudClient;
import com.ctfww.module.keepwatch.DataHelper.DBHelper;
import com.ctfww.module.keepwatch.DataHelper.ICloudMethod;
import com.ctfww.module.useim.im.IMSClientBootstrap;

import java.util.Calendar;

public class Utils {
    private final static String TAG = "Utils";

    public static void start(Context ctx){
        // 初始化keepwatch网络化模块
        ICloudMethod keepWatchMethod = com.ctfww.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(keepWatchMethod);
//        CloudClient.getInstance().createCloudMethod();

        // 初始化keyevents网络设置
        com.ctfww.module.keyevents.datahelper.CloudClient.getInstance().
                setCloudMethod(com.ctfww.commonlib.network.CloudClient.getInstance().
                        create(com.ctfww.module.keyevents.datahelper.ICloudMethod.class));

        // 初始化apk升级的网络设置
        com.ctfww.module.upgrade.datahelper.CloudClient.getInstance().createCloudMethod();

        // 初始化keepwatch数据库模块
        DBHelper.getInstance().init(ctx);

        // 初始化keyevents数据库模块
        com.ctfww.module.keyevents.datahelper.DBHelper.getInstance().init(ctx);
    }

    public static void synData() {
        SynDB.startSyn(new SynDB.ISynHelper() {
            @Override
            public void doThing() {
//                synTracePoint();
            }
        });
    }

    public static String getDeskQrUrl(int deskId) {
        String groupId = SPStaticUtils.getString("working_group_id");
        return "http://www.littlepine.cn/groupId=" + groupId + "/deskId=" + deskId;
    }

    public static void produceSigninDeskQrFile(Context context, int deskId, String deskName) {
        String url = Utils.getDeskQrUrl(deskId);
        Bitmap bitmap = QRCodeUtils.createDataQRCodeBitmap(url, deskId, deskName);
        if (bitmap != null) {
            Calendar calendar = Calendar.getInstance();
            String dateTimeStr = String.format("%d%02d%02d%02d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            ImageUtils.saveImageToGallery(context, bitmap, "" + deskId + "_" + dateTimeStr + ".jpg");
        }
    }

    private static boolean mIsFirstToken = false;
    public static void setFirstToken(boolean isFirstToken) {
        mIsFirstToken = isFirstToken;
    }

    public static boolean isFirstToken() {
        return mIsFirstToken;
    }
}
