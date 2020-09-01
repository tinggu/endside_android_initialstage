package com.ctfww.module.keepwatch.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.PersonTrends;
import com.ctfww.module.user.datahelper.sp.Const;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PersonTrendsAirship {
    private final static String TAG = "PersonTrendsAirship";

    // 从云上同步动态
    public static void synFromCloud() {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong(AirshipConst.PERSON_TRENDS_SYN_TIME_STAMP_CLOUD, MyDateTimeUtils.getTodayStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synTodayKeepWatchPersonTrendsFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<PersonTrends> personTrendsList = (List<PersonTrends>)obj;

                if (!personTrendsList.isEmpty()) {
                    updateByCloud(personTrendsList);
                    EventBus.getDefault().post("finish_person_trends_syn");
                }

                SPStaticUtils.put(AirshipConst.PERSON_TRENDS_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static void updateByCloud(List<PersonTrends> personTrendsList) {
        for (int i = 0; i < personTrendsList.size(); ++i) {
            PersonTrends personTrends = personTrendsList.get(i);
            DBHelper.getInstance().addPersonTrends(personTrends);
        }
    }
}
