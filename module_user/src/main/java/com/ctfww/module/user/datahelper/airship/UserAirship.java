package com.ctfww.module.user.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.dbhelper.UserDBHelper;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class UserAirship {
    private final static String TAG = "UserAirship";

    // 同步用户信息上云
    public static void synToCloud() {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        final UserInfo userInfo = DBHelper.getInstance().getNoSynUser(userId);
        if (userInfo == null) {
            return;
        }

        NetworkHelper.getInstance().synUserInfoToCloud(userInfo, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                userInfo.setSynTag("cloud");
                DBHelper.getInstance().updateUser(userInfo);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步用户信息
    public static void synFromCloud() {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        NetworkHelper.getInstance().synUserInfoFromCloud(userId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<UserInfo> infoList = (List<UserInfo>)obj;

                UserInfo userInfo = (UserInfo)obj;
                if (updateByCloud(infoList)) {
                    EventBus.getDefault().post("finish_user_info_syn");
                };
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    public static boolean updateByCloud(List<UserInfo> infoList) {
        boolean ret = false;
        for (int i = 0; i < infoList.size(); ++i) {
            UserInfo info = infoList.get(i);
            info.setSynTag("cloud");
            if (DBHelper.getInstance().addUser(info)) {
                ret = true;
                continue;
            }

            UserInfo localInfo = DBHelper.getInstance().getUser(info.getUserId());
            if (localInfo.getTimeStamp() < info.getTimeStamp()) {
                info.setSynTag("cloud");
                DBHelper.getInstance().updateUser(info);
                ret = true;
            }
        }

        return ret;
    }
}
