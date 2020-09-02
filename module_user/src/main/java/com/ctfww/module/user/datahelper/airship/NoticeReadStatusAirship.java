package com.ctfww.module.user.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.utils.AirshipUtils;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.NoticeReadStatus;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NoticeReadStatusAirship {
    private final static String TAG = "NoticeReadStatusAirship";

    // 同步通知读取状态上云
    public static void synToCloud() {
        final List<NoticeReadStatus> noticeReadStatusList = DBHelper.getInstance().getNoSynNoticeReadStatusList();
        if (noticeReadStatusList.isEmpty()) {
            return;
        }

        CargoToCloud<NoticeReadStatus> cargoToCloud = new CargoToCloud<>(noticeReadStatusList);

        NetworkHelper.getInstance().synNoticeReadStatusToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < noticeReadStatusList.size(); ++i) {
                    NoticeReadStatus noticeReadStatus = noticeReadStatusList.get(i);
                    noticeReadStatus.setSynTag("cloud");
                    DBHelper.getInstance().updateNoticeReadStatus(noticeReadStatus);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步通知信息
    public static void synFromCloud() {
        long startTime = SPStaticUtils.getLong(Const.NOTICE_READ_STATUS_SYN_TIME_STAMP_CLOUD, AirshipUtils.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        LogUtils.i(TAG, "synFromCloud: condition = " + condition.toString());

        NetworkHelper.getInstance().synNoticeReadStatusFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<NoticeReadStatus> noticeReadStatusList = (List<NoticeReadStatus>)obj;
                LogUtils.i(TAG, "synFromCloud: noticeReadStatusList.size() = " + noticeReadStatusList.size());

                if (!noticeReadStatusList.isEmpty()) {
                    if (updateByCloud(noticeReadStatusList)) {
                        EventBus.getDefault().post(Const.FINISH_NOTICE_READ_STATUS_SYN);
                    }
                }

                SPStaticUtils.put(Const.NOTICE_READ_STATUS_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<NoticeReadStatus> noticeReadStatusList) {
        boolean ret = false;
        for (int i = 0; i < noticeReadStatusList.size(); ++i) {
            NoticeReadStatus noticeReadStatus = noticeReadStatusList.get(i);
            noticeReadStatus.setSynTag("cloud");
            noticeReadStatus.combieId();
            if (DBHelper.getInstance().addNoticeReadStatus(noticeReadStatus)) {
                ret = true;
                continue;
            }

            NoticeReadStatus localNoticeReadStatus = DBHelper.getInstance().getNoticeReadStatus(noticeReadStatus.getNoticeId(), noticeReadStatus.getUserId());
            if (localNoticeReadStatus.getTimeStamp() < noticeReadStatus.getTimeStamp()) {
                noticeReadStatus.setSynTag("cloud");
                DBHelper.getInstance().updateNoticeReadStatus(noticeReadStatus);
                ret = true;
            }
        }

        return ret;
    }
}
