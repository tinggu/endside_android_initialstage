package com.guoliang.module.device.storage;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.guoliang.module.device.storage.table.DaoMaster;
import com.guoliang.module.device.storage.table.DaoSession;
import com.guoliang.module.device.storage.table.DeviceInfo;
import com.guoliang.module.device.storage.table.DeviceInfoDao;

import java.util.List;

public class DatabaseUtil {
    private DeviceInfoDao deviceInfoDao;

    private DatabaseUtil() {

    }

    private static class Inner {
        private static final DatabaseUtil INSTANCE = new DatabaseUtil();
    }

    public static DatabaseUtil getInstance() {
        return Inner.INSTANCE;
    }

    public void init(Context ctx) {
        if (ctx == null) {
            return;
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "device");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        deviceInfoDao = daoSession.getDeviceInfoDao();
    }


    /**
     * 插入设备信息
     * @param deviceInfo 设备信息
     */
    public boolean addDevice(DeviceInfo deviceInfo) {
        try {
            deviceInfoDao.insert(deviceInfo);
            return true;
        }
        catch (SQLiteConstraintException e) {
            return false;
        }
    }

    /**
     * 删除设备信息
     * @param deviceInfo 设备信息
     */
    public void deleteDevice(DeviceInfo deviceInfo) {
        deviceInfoDao.delete(deviceInfo);
    }

    /**
     * 更新设备信息
     * @param deviceInfo 设备信息
     */
    public void updateDevice(DeviceInfo deviceInfo) {
        deviceInfoDao.update(deviceInfo);
    }

    /**
     * 根据deviceID查询设备
     * @param deviceId 设备ID
     * @return 对应的社保费IE信息
     */
    public DeviceInfo queryDeviceByUserId(String deviceId) {
        List<DeviceInfo> deviceInfos = deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.DevId.eq(deviceId)).list();
        if (deviceInfos == null) {
            return null;
        }

        return deviceInfos.get(0);
    }


    /**
     * 查询设备信息表中的所以项
     * @return 所有设备信息
     */
    public List<DeviceInfo> queryAllDevice() {
        return deviceInfoDao.queryBuilder().list();
    }

    /**
     * 按时间查询设备信息表中的所以项
     * @return 所有设备信息
     */
    public List<DeviceInfo> queryAllDevice(long utcTime) {
        return deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.AddTimestamp.gt(utcTime)).list();
    }


    /**
     * 删除设备信息表中所以项
     */
    public void deleteAllDevice() {
        deviceInfoDao.deleteAll();
    }
}
