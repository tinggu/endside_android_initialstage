package com.ctfww.module.device.network.datahelper;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.device.bean.DeviceInfoBean;
import com.ctfww.module.device.network.CloudClient;
import com.ctfww.module.device.storage.DatabaseUtil;
import com.ctfww.module.device.storage.table.DeviceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetworkHelper {
    private final static String TAG = "NetworkHelper";

    private NetworkHelper() {

    }

    private static class Inner {
        private static final NetworkHelper INSTANCE = new NetworkHelper();
    }

    public static NetworkHelper getInstance() {
        return NetworkHelper.Inner.INSTANCE;
    }

    public void addDevice(final DeviceInfo deviceInfo, final IUIDataHelperCallback callback) {
        DeviceInfoBean deviceInfoBean = DataHelper.getInstance().dbDeviceInfo2NetworkDeviceInfo(deviceInfo);
        CloudClient.getInstance().addDevice(deviceInfoBean, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(deviceInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "failAddDevice = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getAllDevices(final IUIDataHelperCallback callback) {
        CloudClient.getInstance().getAllDevices(new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                List<DeviceInfo> deviceInfos = processGetAllDevices(data);
                if (callback != null) {
                    callback.onSuccess(deviceInfos);
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "failAddDevice = " + code);
                if (callback != null) {
                    callback.onError(code);
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
                }
            }
        });
    }

    private List<DeviceInfo> processGetAllDevices(String data) {
        LogUtils.i(TAG, "data = " + data);

        JSONArray jsonArr;
        try {
            JSONObject jsonObject = new JSONObject(data);
            jsonArr = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            LogUtils.e(TAG, "e.message = " + e.getMessage());
            return null;
        }

        List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
        for (int i = 0; i < jsonArr.length(); ++i) {
            JSONObject jsonObj;
            try {
                jsonObj = jsonArr.getJSONObject(i);
            }
            catch (JSONException e) {
                continue;
            }

            DeviceInfoBean deviceInfoBean = GsonUtils.fromJson(jsonObj.toString(), DeviceInfoBean.class);
            DeviceInfo deviceInfo = DataHelper.getInstance().networkDeviceInfo2DbDeviceInfo(deviceInfoBean);
            DatabaseUtil.getInstance().addDevice(deviceInfo);

            deviceInfos.add(deviceInfo);
        }

        return deviceInfos;
    }

    public void updateDevice(final DeviceInfo deviceInfo, final IUIDataHelperCallback callback) {
        DeviceInfoBean deviceInfoBean = DataHelper.getInstance().dbDeviceInfo2NetworkDeviceInfo(deviceInfo);
        CloudClient.getInstance().updateDeviceInfo(deviceInfoBean, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(deviceInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "failAddDevice = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
