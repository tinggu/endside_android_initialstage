package com.ctfww.module.keepwatch.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.entity.SigninInfo;
import com.ctfww.module.user.datahelper.sp.Const;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class KeepWatchSigninAirship {
    private final static String TAG = "KeepWatchSigninAirship";

    // 同步签到信息上云
    public static void synToCloud() {
        List<SigninInfo> signinList = DBHelper.getInstance().getNoSynSigninList();
        if (signinList.isEmpty()) {
            return;
        }

        CargoToCloud<SigninInfo> cargoToCloud = new CargoToCloud<>(signinList);
        NetworkHelper.getInstance().synKeepWatchSigninToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < signinList.size(); ++i) {
                    SigninInfo signin = signinList.get(i);
                    signin.setSynTag("cloud");
                    DBHelper.getInstance().updateSignin(signin);
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
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong(AirshipConst.SIGNIN_SYN_TIME_STAMP_CLOUD, MyDateTimeUtils.getTodayStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);
        condition.setCondition(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synKeepWatchSigninFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<SigninInfo> signinList = (List<SigninInfo>)obj;

                if (!signinList.isEmpty()) {
                    if (updateTabByCloud(signinList)) {
                        EventBus.getDefault().post("finish_desk_syn");
                    }
                }

                SPStaticUtils.put(AirshipConst.SIGNIN_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateTabByCloud(List<SigninInfo> signinList) {
        boolean ret = false;
        for (int i = 0; i < signinList.size(); ++i) {
            SigninInfo signin = signinList.get(i);
            signin.setSynTag("cloud");
            if (DBHelper.getInstance().addSignin(signin)) {
                ret = true;
            }
        }

        return ret;
    }
}
