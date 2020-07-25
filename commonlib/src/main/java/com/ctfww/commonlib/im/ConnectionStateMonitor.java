package com.ctfww.commonlib.im;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;

import org.greenrobot.eventbus.EventBus;

public class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback {
    private static final String TAG = "ConnectionStateMonitor";

    final NetworkRequest networkRequest;
    public ConnectionStateMonitor() {
        networkRequest = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build();
    }

    public void enable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(networkRequest , this);
    }

    // Likewise, you can have a disable method that simply calls ConnectivityManager#unregisterCallback(networkRequest) too.
    @Override
    public void onAvailable(Network network) {
        LogUtils.i(TAG, "onAvailable = " + network.toString());
 //       UdpHelper.getInstance().updateDatagramSocket();
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            LogUtils.i(TAG, "onAvailable: userId is empty!");
            return;
        }

        UdpHelper.getInstance().updateAndRegister(userId);
    }
}
