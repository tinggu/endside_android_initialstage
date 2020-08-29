package com.ctfww.module.desk.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.desk.datahelper.NetworkHelper;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.entity.DeskInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DeskAirship {
    private final static String TAG = "DeskAirship";

    // 同步签到点上云
    public static void synToCloud() {
        List<DeskInfo> deskList = DBHelper.getInstance().getNoSynDeskList();
        if (deskList.isEmpty()) {
            return;
        }

        CargoToCloud<DeskInfo> cargoToCloud = new CargoToCloud<>(deskList);

        NetworkHelper.getInstance().synDeskInfoToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < deskList.size(); ++i) {
                    DeskInfo desk = deskList.get(i);
                    desk.setSynTag("cloud");
                    DBHelper.getInstance().updateDesk(desk);
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

        String key = "desk_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, 0);
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synDeskInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<DeskInfo> deskList = (List<DeskInfo>)obj;

                if (!deskList.isEmpty()) {
                    if (updateByCloud(deskList)) {
                        EventBus.getDefault().post("finish_desk_syn");
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

    private static boolean updateByCloud(List<DeskInfo> deskList) {
        boolean ret = false;
        for (int i = 0; i < deskList.size(); ++i) {
            DeskInfo desk = deskList.get(i);
            DeskInfo localDesk = DBHelper.getInstance().getDesk(desk.getGroupId(), desk.getDeskId());
            if (localDesk == null) {
                desk.setSynTag("cloud");
                DBHelper.getInstance().addDesk(desk);
                ret = true;
            }
            else {
                if (localDesk.getTimeStamp() < desk.getTimeStamp()) {
                    desk.setSynTag("cloud");
                    DBHelper.getInstance().updateDesk(desk);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
