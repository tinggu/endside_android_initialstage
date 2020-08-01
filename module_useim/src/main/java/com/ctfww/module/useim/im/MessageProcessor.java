package com.ctfww.module.useim.im;

import android.util.Log;

import com.ctfww.module.useim.entity.AppMessage;
import com.ctfww.module.useim.entity.BaseMessage;
import com.ctfww.module.useim.entity.ContentMessage;
import com.ctfww.module.useim.im.handler.IMessageHandler;
import com.ctfww.module.useim.im.handler.MessageHandlerFactory;
import com.ctfww.module.useim.utils.CThreadPoolExecutor;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       MessageProcessor.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     消息处理器</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 03:27</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class MessageProcessor implements IMessageProcessor {

    private static final String TAG = MessageProcessor.class.getSimpleName();

    private MessageProcessor() {

    }

    private static class MessageProcessorInstance {
        private static final IMessageProcessor INSTANCE = new MessageProcessor();
    }

    public static IMessageProcessor getInstance() {
        return MessageProcessorInstance.INSTANCE;
    }

    /**
     * 接收消息
     * @param message
     */
    @Override
    public void receiveMsg(final AppMessage message, final IReceiveDataCallback callback) {
        CThreadPoolExecutor.runInBackground(new Runnable() {

            @Override
            public void run() {
                if (callback != null) {
                    callback.onReceived(message);
                }

                try {
                    IMessageHandler messageHandler = MessageHandlerFactory.getHandlerByMsgType(message.getHead().getMsgType());
                    if (messageHandler != null) {
                        messageHandler.execute(message);
                    } else {
                        Log.e(TAG, "未找到消息处理handler，msgType=" + message.getHead().getMsgType());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "消息处理出错，reason=" + e.getMessage());
                }
            }
        });
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMsg(final AppMessage message) {
        CThreadPoolExecutor.runInBackground(new Runnable() {

            @Override
            public void run() {
                boolean isActive = IMSClientBootstrap.getInstance().isActive();
                if (isActive) {
                    IMSClientBootstrap.getInstance().sendMessage(MessageBuilder.getProtoBufMessageBuilderByAppMessage(message).build());
                } else {
                    Log.e(TAG, "发送消息失败");
                }
            }
        });
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMsg(ContentMessage message) {
        this.sendMsg(MessageBuilder.buildAppMessage(message));
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMsg(BaseMessage message) {
        this.sendMsg(MessageBuilder.buildAppMessage(message));
    }
}
