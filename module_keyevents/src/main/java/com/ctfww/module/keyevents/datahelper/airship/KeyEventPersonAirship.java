package com.ctfww.module.keyevents.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.utils.AirshipUtils;
import com.ctfww.module.keyevents.Entity.KeyEventPerson;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keyevents.datahelper.sp.Const;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeyEventPersonAirship {
    private static final String TAG = "KeyEventPersonAirship";

    public static void synToCloud() {
        List<KeyEventPerson> infoList = DBHelper.getInstance().getNoSynKeyEventPersonList();
        if (infoList == null || infoList.isEmpty()) {
            return;
        }

        CargoToCloud<KeyEventPerson> cargoToCloud = new CargoToCloud<>(infoList);
        NetworkHelper.getInstance().synKeyEventPersonToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < infoList.size(); ++i) {
                    KeyEventPerson info = infoList.get(i);
                    info.setSynTag("cloud");

                    DBHelper.getInstance().updateKeyEventPerson(info);
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public static void synFromCloud() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong(Const.KEYEVENT_PERSON_SYN_TIME_STAMP_CLOUD, AirshipUtils.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeyEventPersonFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeyEventPerson> infoList = (List<KeyEventPerson>)obj;

                if (!infoList.isEmpty()) {
                    if (updateByCloud(infoList)) {
                        EventBus.getDefault().post(Const.FINISH_KEY_EVNET_PERSON_SYN);
                    }
                }

                SPStaticUtils.put(Const.KEYEVENT_PERSON_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<KeyEventPerson> infoList) {
        boolean ret = false;
        for (int i = 0; i < infoList.size(); ++i) {
            KeyEventPerson info = infoList.get(i);
            KeyEventPerson localInfo = DBHelper.getInstance().getKeyEventPerson(info.getEventId());
            info.setSynTag("cloud");
            if (localInfo == null) {
                DBHelper.getInstance().addKeyEventPerson(info);
                ret = true;
            }
            else {
                if (localInfo.getTimeStamp() < info.getTimeStamp()) {
                    DBHelper.getInstance().updateKeyEventPerson(info);
                    ret = true;
                }
            }
        }

        return ret;
    }
}
