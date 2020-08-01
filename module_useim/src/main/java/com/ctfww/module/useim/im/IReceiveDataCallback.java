package com.ctfww.module.useim.im;

import com.ctfww.module.useim.entity.AppMessage;

public interface IReceiveDataCallback {
    public void onReceived(AppMessage appMessage);
}
