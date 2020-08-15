package com.ctfww.module.keepwatch.DataHelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.KeepWatchDeskDBHelper;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchDeskAirship {
    private final static String TAG = "KeepWatchDeskAirship";

    // 同步签到点上云
    public static void synToCloud() {
        List<KeepWatchDesk> deskList = DBHelper.getInstance().getNoSynKeepWatchDeskList();
        if (deskList.isEmpty()) {
            return;
        }

        CargoToCloud<KeepWatchDesk> cargoToCloud = new CargoToCloud<>(deskList);

        NetworkHelper.getInstance().synKeepWatchDeskToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < deskList.size(); ++i) {
                    KeepWatchDesk desk = deskList.get(i);
                    desk.setSynTag("cloud");
                    DBHelper.getInstance().updateKeepWatchDesk(desk);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步签到点
    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong("keepwatch_desk_syn_time_stamp_cloud", CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeepWatchDeskFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchDesk> deskList = (List<KeepWatchDesk>)obj;

                if (!deskList.isEmpty()) {
                    if (updateByCloud(deskList)) {
                        EventBus.getDefault().post("finish_desk_syn");
                    }
                }

                SPStaticUtils.put("keepwatch_desk_syn_time_stamp_cloud", condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<KeepWatchDesk> deskList) {
        boolean ret = false;
        for (int i = 0; i < deskList.size(); ++i) {
            KeepWatchDesk desk = deskList.get(i);
            KeepWatchDesk localDesk = DBHelper.getInstance().getKeepWatchDesk(desk.getGroupId(), desk.getDeskId());
            if (localDesk == null) {
                desk.setSynTag("cloud");
                DBHelper.getInstance().addKeepWatchDesk(desk);
                ret = true;
            }
            else {
                if (localDesk.getTimeStamp() < desk.getTimeStamp()) {
                    desk.setSynTag("cloud");
                    DBHelper.getInstance().updateKeepWatchDesk(desk);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
