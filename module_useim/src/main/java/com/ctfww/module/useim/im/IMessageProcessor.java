package com.ctfww.module.useim.im;

import com.ctfww.module.useim.entity.AppMessage;
import com.ctfww.module.useim.entity.BaseMessage;
import com.ctfww.module.useim.entity.ContentMessage;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       IMessageProcessor.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     消息处理器接口</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 00:11</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public interface IMessageProcessor {

    void receiveMsg(AppMessage message, final IReceiveDataCallback callback);
    void sendMsg(AppMessage message);
    void sendMsg(ContentMessage message);
    void sendMsg(BaseMessage message);
}
