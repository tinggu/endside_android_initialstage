package com.ctfww.module.device.network.datahelper;

import com.ctfww.module.device.bean.DeviceInfoBean;
import com.ctfww.module.device.storage.table.DeviceInfo;

public class DataHelper {
    private DataHelper() {

    }

    private static class Inner {
        private static final DataHelper INSTANCE = new DataHelper();
    }

    public static DataHelper getInstance() {
        return DataHelper.Inner.INSTANCE;
    }

    public DeviceInfo networkDeviceInfo2DbDeviceInfo(DeviceInfoBean diviceInfoBean) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setAddress(diviceInfoBean.getAddress());
        deviceInfo.setLat(diviceInfoBean.getLat());
        deviceInfo.setLng(diviceInfoBean.getLng());
        deviceInfo.setAddTimestamp(diviceInfoBean.getAddTimestamp());
        deviceInfo.setAddUserId(diviceInfoBean.getAddUserId());
        deviceInfo.setDevId(diviceInfoBean.getDevId());
        deviceInfo.setDevName(diviceInfoBean.getDevName());
        deviceInfo.setDevType(diviceInfoBean.getDevType());
        deviceInfo.setGroupId(diviceInfoBean.getGroupId());
//        deviceInfo.setSim(diviceInfoBean.getSim());
        return deviceInfo;
    }

    public DeviceInfoBean dbDeviceInfo2NetworkDeviceInfo(DeviceInfo diviceInfo) {
        DeviceInfoBean deviceInfoBean = new DeviceInfoBean();
        deviceInfoBean.setAddress(diviceInfo.getAddress());
        deviceInfoBean.setLat(diviceInfo.getLat());
        deviceInfoBean.setLng(diviceInfo.getLng());
        deviceInfoBean.setAddTimestamp(diviceInfo.getAddTimestamp());
        deviceInfoBean.setAddUserId(diviceInfo.getAddUserId());
        deviceInfoBean.setDevId(diviceInfo.getDevId());
        deviceInfoBean.setDevName(diviceInfo.getDevName());
        deviceInfoBean.setDevType(diviceInfo.getDevType());
        deviceInfoBean.setGroupId(diviceInfo.getGroupId());
//        deviceInfo.setSim(diviceInfoBean.getSim());
        return deviceInfoBean;
    }
}
