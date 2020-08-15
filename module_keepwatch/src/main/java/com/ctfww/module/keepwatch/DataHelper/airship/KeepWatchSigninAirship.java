package com.ctfww.module.keepwatch.DataHelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchSigninAirship {
    private final static String TAG = "KeepWatchSigninAirship";

    // 同步签到信息上云
    public static void synToCloud() {
        List<KeepWatchSigninInfo> signinList = DBHelper.getInstance().getNoSynKeepWatchSignin();
        if (signinList.isEmpty()) {
            return;
        }

        CargoToCloud<KeepWatchSigninInfo> cargoToCloud = new CargoToCloud<>(signinList);
        NetworkHelper.getInstance().synKeepWatchSigninToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < signinList.size(); ++i) {
                    KeepWatchSigninInfo signin = signinList.get(i);
                    signin.setSynTag("cloud");
                    DBHelper.getInstance().updateKeepWatchSignin(signin);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步签到信息
    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong("keepwatch_signin_syn_time_stamp_cloud", CommonAirship.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");
        condition.setCondition(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeepWatchSigninFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchSigninInfo> signinList = (List<KeepWatchSigninInfo>)obj;

                if (!signinList.isEmpty()) {
                    if (updateTabByCloud(signinList)) {
                        EventBus.getDefault().post("finish_desk_syn");
                    }
                }

                SPStaticUtils.put("keepwatch_signin_syn_time_stamp_cloud", condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateTabByCloud(List<KeepWatchSigninInfo> signinList) {
        boolean ret = false;
        for (int i = 0; i < signinList.size(); ++i) {
            KeepWatchSigninInfo signin = signinList.get(i);
            signin.setSynTag("cloud");
            if (DBHelper.getInstance().addKeepWatchSignin(signin)) {
                ret = true;
            }
        }

        return ret;
    }
}
