package com.guoliang.module.keyevents.datahelper;

import com.guoliang.module.keyevents.Entity.KeyEvent;
import com.guoliang.module.keyevents.bean.KeyEventBean;

public class Utils {
    public static KeyEventBean keyEvent2KeyEventBean(KeyEvent keyEvent) {
        KeyEventBean keyEventBean = new KeyEventBean();
        keyEventBean.setAddress(keyEvent.getAddress());
        keyEventBean.setLat(keyEvent.getLat());
        keyEventBean.setLng(keyEvent.getLng());
        keyEventBean.setDescription(keyEvent.getDescription());
        keyEventBean.setEventId(keyEvent.getEventId());
        keyEventBean.setEventName(keyEvent.getEventName());
        keyEventBean.setPicPath(keyEvent.getPicPath());
        keyEventBean.setStatus(keyEvent.getStatus());
        keyEventBean.setDeskId(keyEvent.getDeskId());
        keyEventBean.setTimeStamp(keyEvent.getTimeStamp());
        keyEventBean.setType(keyEvent.getType());
        keyEventBean.setVideoPath(keyEvent.getVideoPath());
        keyEventBean.setVoicePath(keyEvent.getVoicePath());
        keyEventBean.setUserId(keyEvent.getUserId());
        keyEventBean.setGroupId(keyEvent.getGroupId());

        return keyEventBean;
    }

    public static KeyEvent keyEventBean2KeyEvent(KeyEventBean keyEventBean) {
        KeyEvent keyEvent = new KeyEvent();
        keyEvent.setAddress(keyEventBean.getAddress());
        keyEvent.setLat(keyEventBean.getLat());
        keyEvent.setLng(keyEventBean.getLng());
        keyEvent.setDescription(keyEventBean.getDescription());
        keyEvent.setEventId(keyEventBean.getEventId());
        keyEvent.setEventName(keyEventBean.getEventName());
        keyEvent.setPicPath(keyEventBean.getPicPath());
        keyEvent.setStatus(keyEventBean.getStatus());
        keyEvent.setDeskId(keyEventBean.getDeskId());
        keyEvent.setTimeStamp(keyEventBean.getTimeStamp());
        keyEvent.setType(keyEventBean.getType());
        keyEvent.setVideoPath(keyEventBean.getVideoPath());
        keyEvent.setVoicePath(keyEventBean.getVoicePath());
        keyEvent.setUserId(keyEventBean.getUserId());
        keyEvent.setGroupId(keyEventBean.getGroupId());

        return keyEvent;
    }
}
