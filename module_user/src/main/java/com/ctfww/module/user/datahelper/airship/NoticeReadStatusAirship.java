package com.ctfww.module.user.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.utils.AirshipUtils;
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        final String key = "notice_read_status_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, AirshipUtils.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString("user_open_id");
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synNoticeReadStatusFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<NoticeReadStatus> noticeReadStatusList = (List<NoticeReadStatus>)obj;

                if (!noticeReadStatusList.isEmpty()) {
                    if (updateByCloud(noticeReadStatusList)) {
                        EventBus.getDefault().post("finish_notice_read_status_syn");
                    }
                }

                SPStaticUtils.put(key, condition.getEndTime());
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
