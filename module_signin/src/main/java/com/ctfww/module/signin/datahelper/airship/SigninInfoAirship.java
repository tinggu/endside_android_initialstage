package com.ctfww.module.signin.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.signin.datahelper.NetworkHelper;
import com.ctfww.module.signin.datahelper.dbhelper.DBHelper;
import com.ctfww.module.signin.datahelper.sp.Const;
import com.ctfww.module.signin.entity.SigninInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SigninInfoAirship {
    private final static String TAG = "SigninInfoAirship";

    // 同步签到信息上云
    public static void synToCloud() {
        List<SigninInfo> infoList = DBHelper.getInstance().getNoSynSigninList();
        if (infoList.isEmpty()) {
            return;
        }

        CargoToCloud<SigninInfo> cargoToCloud = new CargoToCloud<>(infoList);
        NetworkHelper.getInstance().synSigninToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < infoList.size(); ++i) {
                    SigninInfo info = infoList.get(i);
                    info.setSynTag("cloud");
                    DBHelper.getInstance().updateSignin(info);
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
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        long startTime = SPStaticUtils.getLong(Const.SIGNIN_SYN_TIME_STAMP_CLOUD, MyDateTimeUtils.getTodayStartTime());
        long endTime = System.currentTimeMillis();
        QueryCondition condition = new QueryCondition();
        condition.setUserId(userId);
        condition.setCondition(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synSigninFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<SigninInfo> infoList = (List<SigninInfo>)obj;

                if (!infoList.isEmpty()) {
                    if (updateTabByCloud(infoList)) {
                        EventBus.getDefault().post(Const.FINISH_SIGNIN_SYN);
                    }
                }

                SPStaticUtils.put(Const.SIGNIN_SYN_TIME_STAMP_CLOUD, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateTabByCloud(List<SigninInfo> infoList) {
        boolean ret = false;
        for (int i = 0; i < infoList.size(); ++i) {
            SigninInfo info = infoList.get(i);
            info.setSynTag("cloud");
            if (DBHelper.getInstance().addSignin(info)) {
                ret = true;
            }
        }

        return ret;
    }
}
