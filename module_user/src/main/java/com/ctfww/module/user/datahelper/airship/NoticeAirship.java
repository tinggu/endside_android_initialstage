package com.ctfww.module.user.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.utils.AirshipUtils;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.NoticeInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NoticeAirship {
    private final static String TAG = "NoticeAirship";

    // 同步邀请信息上云
    public static void synToCloud() {
        final List<NoticeInfo> noticeList = DBHelper.getInstance().getNoSynNoticeList();
        if (noticeList.isEmpty()) {
            return;
        }

        CargoToCloud<NoticeInfo> cargoToCloud = new CargoToCloud<>(noticeList);

        NetworkHelper.getInstance().synNoticeInfoToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < noticeList.size(); ++i) {
                    NoticeInfo noticeInfo = noticeList.get(i);
                    if ("new".equals(noticeInfo.getSynTag())) {
                        EventBus.getDefault().post(new MessageEvent("send_notice_success", GsonUtils.toJson(noticeInfo)));
                    }

                    noticeInfo.setSynTag("cloud");
                    DBHelper.getInstance().updateNotice(noticeInfo);
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
        long startTime = SPStaticUtils.getLong(Const.NOTICE_SYN_TIME_STAMP_CLOUD, AirshipUtils.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);
        LogUtils.i(TAG, "synFromCloud: condition = " + condition.toString());

        NetworkHelper.getInstance().synNoticeInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<NoticeInfo> noticeList = (List<NoticeInfo>)obj;
                LogUtils.i(TAG, "synFromCloud: noticeList.size() = " + noticeList.size());

                if (!noticeList.isEmpty()) {
                    if (updateByCloud(noticeList)) {
                        EventBus.getDefault().post(Const.FINISH_NOTICE_SYN);
                    }
                }

                SPStaticUtils.put(Const.NOTICE_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<NoticeInfo> noticeList) {
        boolean ret = false;
        for (int i = 0; i < noticeList.size(); ++i) {
            NoticeInfo notice = noticeList.get(i);
            notice.setSynTag("cloud");
            if (DBHelper.getInstance().addNotice(notice)) {
                ret = true;
                continue;
            }

            NoticeInfo localNotice = DBHelper.getInstance().getNotice(notice.getNoticeId());
            if (localNotice.getTimeStamp() < notice.getTimeStamp()) {
                notice.setSynTag("cloud");
                DBHelper.getInstance().updateNotice(notice);
                ret = true;
            }
        }

        return ret;
    }
}
