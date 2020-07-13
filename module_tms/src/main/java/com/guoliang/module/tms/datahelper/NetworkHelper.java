package com.guoliang.module.tms.datahelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.module.tms.network.CloudClient;
import com.guoliang.module.tms.network.ICloudCallback;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.guoliang.module.tms.datahelper.SPConstant.USER_OPEN_ID;

public class NetworkHelper {
    private static final String TAG = "TMSNetworkHelper";

    private long lastTime = 0;
    private int cnt = 0;

    private NetworkHelper() {

    }

    private static class Inner {
        private static final NetworkHelper INSTANCE = new NetworkHelper();
    }

    public static NetworkHelper getInstance() {
        return NetworkHelper.Inner.INSTANCE;
    }

     private String processSuccessToken(String data) {
        LogUtils.i(TAG, "data = " + data);
        if (TextUtils.isEmpty(data)){
            return "";
        }

        String token = "";
        try {
            JSONObject jsonObjData = new JSONObject(data);
            token = jsonObjData.getString("data");
        } catch (JSONException e) {
            LogUtils.e(TAG, "e.message = " + e.getMessage());
            return "";
        }

        LogUtils.i(TAG, "token = " + token);
        SPStaticUtils.put(SPConstant.USER_ACCESS_TOKEN, token);

        return token;
    }

    public void refreshToken(boolean isForce) {
        final long utcTime = System.currentTimeMillis();
        if (utcTime - lastTime < 4 * 60 * 1000 && !isForce) {
            return;
        }

        LogUtils.i(TAG, "触发刷新token！");

        lastTime = utcTime;

        String userId = SPStaticUtils.getString(USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            LogUtils.i(TAG, "userId is empty!");
            return;
        }

        CloudClient.getInstance().getAccessTokenByUserId(userId, 0, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                String token = NetworkHelper.getInstance().processSuccessToken(data);
                lastTime = utcTime;
                LogUtils.i(TAG, "token 刷新成功！");

                if (cnt == 0 && !TextUtils.isEmpty(token)) {
                    MessageEvent messageEvent = new MessageEvent("tms_first_token", token);
                    EventBus.getDefault().postSticky(messageEvent);
                }

                if (!TextUtils.isEmpty(token)) {
                    ++cnt;
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "由于TMS微服务的原因，token刷新失败！");
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "由于网络原因，token刷新失败!");
            }
        });
    }

    public void startTimedRefresh() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                        .daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                refreshToken(false);
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt  = cnt;
    }
}
