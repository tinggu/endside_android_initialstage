package com.ctfww.commonlib.im;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpHelper {
    private final static String TAG = "UdpHelper";
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;

    public interface ICallback {
        public void onReceive(String data);
    }

    public void UdpHelper() {

    }

    private static class Inner {
        private static final UdpHelper INSTANCE = new UdpHelper();
    }

    public static UdpHelper getInstance() {
        return Inner.INSTANCE;
    }

    public boolean updateInetAddress() {
        try {
            inetAddress = InetAddress.getByName(DestIpMgt.getInstance().getIp());
            LogUtils.i(TAG, "updateInetAddress: success ");
            return true;
        }
        catch (UnknownHostException e) {
            LogUtils.i(TAG, "updateInetAddress: e = " + e.getMessage());
            return false;
        }
    }

    public boolean updateDatagramSocket() {
        if (datagramSocket != null) {
            datagramSocket.close();
        }

        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setReuseAddress(true);
            LogUtils.i(TAG, "updateDatagramSocket success");
            return true;
        }
        catch (SocketException e) {
            LogUtils.i(TAG, "updateDatagramSocket: e = " + e.getMessage());
            return false;
        }
    }

    public void updateAndRegister(String userId) {
        if (!updateDatagramSocket()) {
            LogUtils.i(TAG, "updateAndRegister: updateDatagramSocket fail!");
            return;
        }

        if (inetAddress == null) {
            if (!updateInetAddress()) {
                LogUtils.i(TAG, "updateAndRegister: updateInetAddress fail!");
                return;
            }
        }

        register(userId);

        receiveData();
    }

    public void register(String userId) {
        BasicData basicData = new BasicData();
        basicData.setUserId(userId);
        basicData.setType("register");
        basicData.setSerialNumber(getSerialNumber());

        sendData(GsonUtils.toJson(basicData));
    }

    public void unregister(String userId) {
        BasicData basicData = new BasicData();
        basicData.setUserId(userId);
        basicData.setType("unregister");
        basicData.setSerialNumber(getSerialNumber());

        sendData(GsonUtils.toJson(basicData));
    }

    public void sendBasicDataToOtherUser(String userId, String type, String toUserId) {
        BasicData basicData = new BasicData();
        basicData.setUserId(userId);
        basicData.setType(type);
        basicData.setToUserId(toUserId);
        basicData.setSerialNumber(getSerialNumber());

        sendData(GsonUtils.toJson(basicData));
    }

    public void sendBasicDataToGroup(String userId, String type, String groupId) {
        BasicData basicData = new BasicData();
        basicData.setUserId(userId);
        basicData.setType(type);
        basicData.setGroupId(groupId);
        basicData.setSerialNumber(getSerialNumber());

        sendData(GsonUtils.toJson(basicData));
    }

    public void sendData(String userId, String type, String groupId, String toUserId, String addition) {
        BasicData basicData = new BasicData();
        basicData.setUserId(userId);
        basicData.setSerialNumber(getSerialNumber());
        basicData.setType(type);
        basicData.setToUserId(toUserId);
        basicData.setGroupId(groupId);
        basicData.setAddition(addition);

        sendData(GsonUtils.toJson(basicData));
    }

    public void sendData(final String data) {
        LogUtils.i(TAG, "sendData: data = " + data);
        new Thread(){
            @Override
            public void run() {
                try{
                    DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), inetAddress, DestIpMgt.getInstance().getPort());
                    datagramSocket.send(dp);
                }catch (Exception e){
                    LogUtils.i(TAG, "sendData: e = " + e.getMessage());
                }
            }
        }.start();
    }

    public void receiveData() {
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    if (datagramSocket == null) {
                        break;
                    }

                    try{
                        LogUtils.i(TAG, "receiveData prepared...");
                        byte[] buf = new byte[1024];
                        DatagramPacket dp = new DatagramPacket(buf, 1024);
                        datagramSocket.receive(dp);
                        String data = new String(dp.getData(), 0, dp.getLength());
                        LogUtils.i(TAG, "receiveData: data = " + data);
                        EventBus.getDefault().post(new MessageEvent("udp_receive_data", data));
                    }catch (Exception e){
                        LogUtils.i(TAG, "receiveData: e = " + e.getMessage());
                        break;
                    }
                }

            }
        }.start();
    }

    private String getSerialNumber() {
        return "" + System.currentTimeMillis();
    }
}
