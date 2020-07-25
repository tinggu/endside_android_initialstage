package com.ctfww.commonlib.im;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPStaticUtils;

public class DestIpMgt {
    private static final String defaultIp = "192.168.0.102";
    private static final int defaultPort = 7011;

    public DestIpMgt() {

    }

    private static class Inner {
        private static final DestIpMgt INSTANCE = new DestIpMgt();
    }

    public static DestIpMgt getInstance() {
        return Inner.INSTANCE;
    }

    public String getIp() {
        return SPStaticUtils.getString("im_dest_ip", defaultIp);
    }

    public int getPort() {
        return SPStaticUtils.getInt("im_dest_port", defaultPort);
    }

    public void updateIpPort() {

    }
}
