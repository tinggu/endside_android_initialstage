package com.guoliang.module.user.bean;

public class LoginStatus {
    // 判断是否能发送验证码
    private boolean isSmsCanSend;

    // 判断是否在等待验证码
    private boolean isSmsSendOngoing;

    //
    private boolean isSmsContDownIsOngoing;



    private boolean isSubmitBtnClickOngoing;


    private boolean loginBySmsNum = true;

    private boolean loginByPassword = false;
}
