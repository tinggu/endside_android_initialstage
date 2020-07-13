package com.ctfww.module.user.datahelper;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.constant.UserSPConstant;
import com.ctfww.module.user.entity.UserInfo;
import com.ctfww.module.user.storage.sp.SPConstant;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataHelper {
    private static final String TAG = "DataHelper";

    private DataHelper() {

    }

    private static class Inner {
        private static final DataHelper INSTANCE = new DataHelper();
    }

    public static DataHelper getInstance() {
        return DataHelper.Inner.INSTANCE;
    }

    public boolean isLogined() {
        return SPStaticUtils.getBoolean(UserSPConstant.USER_HAD_LOGIN_FLAG);
    }

    public boolean isValidMobileNum(String str) {
        final int mobileNumLen = 11;
        return str.length() == mobileNumLen;
    }

    public boolean isValidSMS(String str) {
        final int smsLen = 6;
        return str.length() == smsLen;
    }

    public boolean isValidPassword(String str) {
        final int smsLen = 6;
        return str.length() >= smsLen;
    }

    public UserInfo getUserInfo() {
        String userId = SPStaticUtils.getString(SPConstant.USER_OPEN_ID);
        List<UserInfo> userInfos = DBHelper.getInstance().queryUserByUserId(userId);

        return userInfos == null ? null : userInfos.get(0);
    }

    public void startTimedSyn() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                        .daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synDataToCloud();
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    private void synDataToCloud() {
        final UserInfo userInfo = getUserInfo();
        if ("cloud".equals(userInfo.getSynTag())) {
            return;
        }

        NetworkHelper.getInstance().updateUserInfo(userInfo, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                userInfo.setSynTag("cloud");
                DBHelper.getInstance().updateUser(userInfo);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "更新用户信息失败！");
            }
        });
    }
}
