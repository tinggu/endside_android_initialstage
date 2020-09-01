package com.ctfww.module.keyevents.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.utils.AirshipUtils;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.sp.Const;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeyEventTraceAirship {
    private static final String TAG = "KeyEventTraceAirship";

    public static void synToCloud() {
        List<KeyEventTrace> keyEventTraceList = DBHelper.getInstance().getNoSynKeyEventTraceList();
        if (keyEventTraceList == null || keyEventTraceList.isEmpty()) {
            return;
        }

        CargoToCloud<KeyEventTrace> cargoToCloud = new CargoToCloud<>(keyEventTraceList);
        NetworkHelper.getInstance().synKeyEventTraceToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < keyEventTraceList.size(); ++i) {
                    KeyEventTrace keyEventTrace = keyEventTraceList.get(i);
                    keyEventTrace.setSynTag("cloud");

                    DBHelper.getInstance().updateKeyEventTrace(keyEventTrace);
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String key = "keyevent_trace_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, AirshipUtils.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeyEventTraceFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeyEventTrace> keyEventList = (List<KeyEventTrace>)obj;

                if (!keyEventList.isEmpty()) {
                    if (updateByCloud(keyEventList)) {
                        EventBus.getDefault().post("finish_key_event_trace_syn");
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

    private static boolean updateByCloud(List<KeyEventTrace> keyEventTraceList) {
        boolean ret = false;
        for (int i = 0; i < keyEventTraceList.size(); ++i) {
            KeyEventTrace keyEventTrace = keyEventTraceList.get(i);
            KeyEventTrace localKeyEventTrace = DBHelper.getInstance().getKeyEventTrace(keyEventTrace.getEventId());
            keyEventTrace.setSynTag("cloud");
            if (localKeyEventTrace == null) {
                DBHelper.getInstance().addKeyEventTrace(keyEventTrace);
                ret = true;
            }
            else {
                if (localKeyEventTrace.getTimeStamp() < keyEventTrace.getTimeStamp()) {
                    DBHelper.getInstance().updateKeyEventTrace(keyEventTrace);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
