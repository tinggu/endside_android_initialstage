package com.ctfww.module.keepwatch.utils;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.useim.entity.AppMessage;
import com.ctfww.module.useim.entity.SingleMessage;
import com.ctfww.module.useim.event.CEventCenter;
import com.ctfww.module.useim.event.I_CEventListener;
import com.ctfww.module.useim.im.IMSClientBootstrap;
import com.ctfww.module.useim.im.IReceiveDataCallback;
import com.ctfww.module.useim.im.MessageProcessor;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

public class IM {
    private static final String TAG = "IM";
    public static void startIM(Context context) {
        String userId = SPStaticUtils.getString("user_open_id");
        String token = "token_" + userId;
        IMSClientBootstrap bootstrap = IMSClientBootstrap.getInstance();
//        String hosts = "[{\"host\":\"192.168.0.102\", \"port\":7012}]";
        String hosts = "[{\"host\":\"39.98.147.77\", \"port\":7012}]";
        bootstrap.init(userId, token, hosts, 0, context, new IReceiveDataCallback() {
            @Override
            public void onReceived(AppMessage appMessage) {
                LogUtils.i(TAG, "onReceived: appMessage.getHead() = " + appMessage.getHead().toString());
                EventBus.getDefault().post(new MessageEvent("im_received_data", GsonUtils.toJson(appMessage.getHead())));
            }
        });
    }

    public static void sendBaseMsg(String toId, int msgType, int msgContentType) {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        SingleMessage message = new SingleMessage();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType(msgType);
        message.setFromId(userId);
        message.setToId(toId);
        message.setMsgContentType(msgContentType);
        message.setTimestamp(System.currentTimeMillis());
        String extend = "\"token\":\"token_" + userId + "\"}";
        message.setExtend(extend);
        MessageProcessor.getInstance().sendMsg(message);
    }

    public static void sendBaseMsgToThisGroup(int msgType, int msgContentType) {
        String groupId = SPStaticUtils.getString("working_group_id");
        sendBaseMsg(groupId, msgType, msgContentType);
    }

    public static void invite(String toUserId) {
        sendBaseMsg(toUserId, 2001, 1);
    }

    public static void acceptInvite(String toUserId) {
        sendBaseMsg(toUserId, 2001, 2);
    }

    public static void refuseInvite(String toUserId) {
        sendBaseMsg(toUserId, 2001, 3);
    }

    public static void releaseNotice() {
        sendBaseMsgToThisGroup(3001, 1);
    }

    public static void reportAbnormal() {
        sendBaseMsgToThisGroup(3001, 2);
    }

    public static void reportSignin() {
        sendBaseMsgToThisGroup(3001, 3);
    }
}
