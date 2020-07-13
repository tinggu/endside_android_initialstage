package com.ctfww.module.fingerprint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.base.BaseApplication;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.PermissionUtils;
import com.ctfww.module.fingerprint.entity.DistResult;
import com.ctfww.module.fingerprint.entity.FingerPrintHistory;
import com.ctfww.module.fingerprint.entity.MacRssi;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Utils {
    private static String TAG = "Utils";

    private static BroadcastReceiver mReceiver;
    private static WifiManager mWifiManager;
    private static JNI jni;
    private static String wifiCalculateFingerPrint = "";
    private static String wifiFingerPrint = "";
    private static boolean isSuccessScan = false;
    private static String currType;

    public static void init() {
        mWifiManager = (WifiManager) BaseApplication.getInstances().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        broadcast();
        jni = new JNI();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        BaseApplication.getInstances().getApplicationContext().registerReceiver(mReceiver,intentFilter);
    }

    public static void startScan(String type) {
        if (!PermissionUtils.isLocationPermissionGranted(BaseApplication.getInstances().getApplicationContext())) {
            LogUtils.i(TAG, "startScan: step 2.1.1");
            return;
        }

        LogUtils.i(TAG, "startScan: step 2");
        currType = type;
        isSuccessScan = mWifiManager.startScan();
        if (!isSuccessScan) {
            LogUtils.i(TAG, "startScan: step 2.2");
            if ("create".equals(currType)) {
                LogUtils.i(TAG, "startScan: step 2.2.1");
                EventBus.getDefault().post(new MessageEvent("wifi_finger_print", wifiFingerPrint));
            }
            else if ("calc".equals(currType)) {
                LogUtils.i(TAG, "startScan: step 2.2.2");
                EventBus.getDefault().post(new MessageEvent("wifi_calculate_finger_print", wifiCalculateFingerPrint));
            }
            currType = "";
        }
    }

    public static void endScan() {
        BaseApplication.getInstances().getApplicationContext().unregisterReceiver(mReceiver);
    }

    public static void setDatabase(List<Integer> idList, List<String> fingerPrintStrList) {
        JSONArray jsonArray  = new JSONArray();
        for (int i = 0; i < idList.size() && i < fingerPrintStrList.size(); ++i) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", idList.get(i));
                jsonObject.put("finger_print", fingerPrintStrList.get(i));
                jsonArray.put(jsonObject);
            }
            catch (JSONException e) {

            }
        }

        if (jsonArray.length() > 0) {
            LogUtils.i("current", "setDatabase: jsonArray.toString() = " + jsonArray.toString());
            jni.setFingerPrintDatabase(jsonArray.toString());
        }
    }

    public static void setDatabase(int id, String fingerPrintStr) {
        JSONArray jsonArray  = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("finger_print", fingerPrintStr);
            jsonArray.put(jsonObject);
        }
        catch (JSONException e) {

        }

        if (jsonArray.length() > 0) {
            jni.setFingerPrintDatabase(jsonArray.toString());
        }
    }

    // 形成整体的指纹
    public static String combineFingerPrint(List<ScanResult> scanResultList, Location location) {
        String ret = "";

        String wifiFingerPrint = combineWifiStandardFingerPrint(scanResultList);
        if (!TextUtils.isEmpty(wifiFingerPrint)) {
            ret = appendOtherFingerPrint(ret, wifiFingerPrint);
        }

        String gpsFingerPrint = combineGpsStandardFingerPrint(location);
        if (!TextUtils.isEmpty(gpsFingerPrint)) {
            ret = appendOtherFingerPrint(ret, gpsFingerPrint);
        }

        return ret;
    }

    public static String appendOtherFingerPrint(String thisFingerPrint, String otherFingerPrint) {
        if (TextUtils.isEmpty(thisFingerPrint)) {
            return otherFingerPrint;
        }

        return thisFingerPrint + ";" + otherFingerPrint;
    }

    // 形成wifi计算指纹
    private static String combineWifiCalculateFingerPrint(List<ScanResult> scanResultList) {
        JSONArray jsonArray = scanResults2JsonArr(scanResultList);
        return jsonArray == null || jsonArray.length() == 0 ? "" : jsonArray.toString();
    }

    // 形成wifi标准指纹
    public static String combineWifiStandardFingerPrint(List<ScanResult> scanResultList) {
        if (scanResultList == null || scanResultList.isEmpty()) {
            return "";
        }

        List<ScanResult> selectScanResultList = selectScanResult(scanResultList);

        String ret = "wifi:";
        for (int i = 0; i < selectScanResultList.size(); ++i) {
            if (i != 0) {
                ret += ",";
            }

            String mac = selectScanResultList.get(i).BSSID;
            if (mac.length() == 11) {
                mac += ":00:00";
            }

            mac = mac.replace(":", "");
            int rssi = selectScanResultList.get(i).level;
            ret += (mac + rssi);
        }

        return ret;
    }

    // 形成wifi标准指纹
    public static String combineWifiStandardFingerPrint(String calculateFingerPrint) {
        if (TextUtils.isEmpty(calculateFingerPrint)) {
            return "";
        }

        try {
            JSONArray jsonArray = new JSONArray(calculateFingerPrint);
            List<MacRssi> macRssiList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                MacRssi macRssi = new MacRssi();
                macRssi.setMac(jsonArray.getJSONObject(i).getString("mac"));
                macRssi.setRssi(jsonArray.getJSONObject(i).getInt("rssi"));
                macRssiList.add(macRssi);
            }

            macRssiList.sort(new Comparator<MacRssi>() {
                @Override
                public int compare(MacRssi o1, MacRssi o2) {
                    Integer val1 = o1.getRssi();
                    Integer val2 = o2.getRssi();
                    return val2.compareTo(val1);
                }
            });

            List<MacRssi> subMacRssiList = macRssiList.size() <= MAX_COUNT ? macRssiList : macRssiList.subList(0, MAX_COUNT);
            String ret = "wifi:";
            for (int i = 0; i < subMacRssiList.size(); ++i) {
                if (i != 0) {
                    ret += ",";
                }

                String mac = subMacRssiList.get(i).getMac();
                int rssi = subMacRssiList.get(i).getRssi();
                ret += (mac + rssi);
            }

            return ret;
        }
        catch (JSONException e) {
            return "";
        }
    }

    // 形成标准GPS指纹
    public static String combineGpsStandardFingerPrint(Location location) {
        int gps = "gps".equals(location.getProvider()) ? 1 : 0;
       return "gps:" + String.format("%.6f,%.6f,%d", location.getLatitude(), location.getLongitude(), gps);
    }

    // 形成GPS计算指纹
    public static String combineGpsCalculateFingerPrint(Location location) {
        int gps = "gps".equals(location.getProvider()) ? 1 : 0;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", location.getLatitude());
            jsonObject.put("lng", location.getLongitude());
            jsonObject.put("gps", gps);
        }
        catch (JSONException e) {

        }

//        return "{\"lat\":22.615099964226,\"lng\":114.064543769612,\"gps\":1}";

        return jsonObject.toString();
    }

    public static DistResult getWifiDist(String wifiFingerPrintStr, int id) {
        String resultStr = jni.getWifiDist(wifiFingerPrintStr, id);
        return GsonUtils.fromJson(resultStr, DistResult.class);
    }

    public static DistResult getGpsDist(Location location, int id) {
        String gpsFingerPrintStr = combineGpsCalculateFingerPrint(location);
        String resultStr = jni.getGpsDist(gpsFingerPrintStr, id);
        return GsonUtils.fromJson(resultStr, DistResult.class);
    }

    public static DistResult getWifiId(String wifiFingerPrintStr) {
        String resultStr = jni.getWifiId(wifiFingerPrintStr);
        return GsonUtils.fromJson(resultStr, DistResult.class);
    }

    public static DistResult getGpsId(Location location) {
        String gpsFingerPrintStr = combineGpsCalculateFingerPrint(location);
        LogUtils.i(TAG, "getGpsId: gpsFingerPrintStr = " + gpsFingerPrintStr);
        String resultStr = jni.getGpsId(gpsFingerPrintStr);
        return GsonUtils.fromJson(resultStr, DistResult.class);
    }

    public static String synthesizeFingerPrint(List<FingerPrintHistory> fingerPrintHistoryList) {
        if (fingerPrintHistoryList.isEmpty()) {
            return "";
        }

        JSONArray jsonArray  = new JSONArray();
        for (int i = 0; i < fingerPrintHistoryList.size(); ++i) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("time_stamp", fingerPrintHistoryList.get(i).getTimeStamp());
                jsonObject.put("finger_print", fingerPrintHistoryList.get(i).getFingerPrint());
                jsonArray.put(jsonObject);
            }
            catch (JSONException e) {

            }
        }

        return jni.synthesizeFingerPrint(jsonArray.toString());
    }

    public static String getWifiCalculateFingerPrint() {
        return wifiCalculateFingerPrint;
    }

    public static String getWifiFingerPrint() {
        return wifiFingerPrint;
    }

    private static void broadcast() {
        mReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    LogUtils.i(TAG, "qr step 3: intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)");

                    //获得列表
                    List<ScanResult> scanResultList = mWifiManager.getScanResults();
                    wifiCalculateFingerPrint = combineWifiCalculateFingerPrint(scanResultList);
                    wifiFingerPrint = combineWifiStandardFingerPrint(scanResultList);
                    if (isSuccessScan) {
                        if ("create".equals(currType)) {
                            EventBus.getDefault().post(new MessageEvent("wifi_finger_print", wifiFingerPrint));
                        }
                        else if ("calc".equals(currType)) {
                            EventBus.getDefault().post(new MessageEvent("wifi_calculate_finger_print", wifiCalculateFingerPrint));
                        }
                    }

                    currType = "";
                    isSuccessScan = false;
                }
            }
        };
    }

    private static List<JSONObject> addJsonObject(List<JSONObject> jsonObjectList, JSONObject jsonObject) {
        if (jsonObjectList == null) {
            jsonObjectList = new ArrayList<>();
        }

        int i = 0;
        for (; i < jsonObjectList.size(); ++i) {
            try {
                if (jsonObject.getInt("rssi") > jsonObjectList.get(i).getInt("rssi")) {
                    jsonObjectList.add(i, jsonObject);
                    break;
                }
            }
            catch (JSONException e) {

            }
        }

        if (i == jsonObjectList.size()) {
            jsonObjectList.add(jsonObject);
        }

        return jsonObjectList;
    }

    private static JSONArray scanResults2JsonArr(List<ScanResult> scanResultList) {
        if (scanResultList == null || scanResultList.isEmpty()) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < scanResultList.size(); ++i) {
            String mac = scanResultList.get(i).BSSID;
            if (mac.length() == 11) {
                mac += ":00:00";
            }

            mac = mac.replace(":", "");
            int rssi = scanResultList.get(i).level;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mac", mac);
                jsonObject.put("rssi", rssi);
            }
            catch (JSONException e) {

            }

            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    private static final int MAX_COUNT = 10;
    private static List<ScanResult> selectScanResult(List<ScanResult> scanResultList) {
        scanResultList.sort(new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult o1, ScanResult o2) {
                if (o1 == null || o2 == null) {
                    return 0;
                }

                Integer val1 = o1.level;
                Integer val2 = o2.level;
                return val2.compareTo(val1);
            }
        });

        return scanResultList.size() <= MAX_COUNT ? scanResultList : scanResultList.subList(0, 10);
    }
}
