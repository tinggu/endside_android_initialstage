package com.guoliang.module.keepwatch.DataHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.guoliang.module.keepwatch.entity.DaoMaster;
import com.guoliang.module.keepwatch.entity.DaoSession;
import com.guoliang.module.keepwatch.entity.KeepWatchDesk;
import com.guoliang.module.keepwatch.entity.KeepWatchDeskDao;
import com.guoliang.module.keepwatch.entity.KeepWatchSigninInfo;
import com.guoliang.module.keepwatch.entity.KeepWatchSigninInfoDao;

import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private KeepWatchDeskDao keepWatchDeskDao;
    private KeepWatchSigninInfoDao keepWatchSigninInfoDao;

    private static class Inner {
        private static final DBHelper INSTANCE = new DBHelper();
    }

    public static DBHelper getInstance() {
        return Inner.INSTANCE;
    }

    public void init(Context ctx) {
        if (ctx == null) {
            return;
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "keepwatch");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        keepWatchDeskDao = daoSession.getKeepWatchDeskDao();
        keepWatchSigninInfoDao = daoSession.getKeepWatchSigninInfoDao();

    }

    public void setDeskList(List<KeepWatchDesk> keepWatchDeskList) {
        keepWatchDeskDao.deleteAll();
        for (int i = 0; i < keepWatchDeskList.size(); ++i) {
            KeepWatchDesk keepWatchDesk = keepWatchDeskList.get(i);
            keepWatchDesk.combineId();
            keepWatchDeskDao.insert(keepWatchDesk);
        }
    }

    public void modifyDeskList(long synTime, List<KeepWatchDesk> keepWatchDeskList) {
        for (int i = 0; i < keepWatchDeskList.size(); ++i) {
            if (keepWatchDeskList.get(i).getModifyTimeStamp() > synTime) {
                addDesk(keepWatchDeskList.get(i));
            }
            else {
                updateDesk(keepWatchDeskList.get(i));
            }
        }
    }

    public long getSigninDeskSynTime(String groupId) {
        long ret = 0;
        try
        {
            String  sql = "SELECT MAX(ModifyTimestamp) as modify_timestamp FROM KEEP_WATCH_DESK WHERE GroupId = '" + groupId + "'";
            Cursor c  = keepWatchDeskDao.getDatabase().rawQuery(sql,null);
            if(c.moveToFirst()) {
                ret=c.getLong(c.getColumnIndex("modify_timestamp"));
            }
            c.close();
        }
        catch (Exception ex) {
            LogUtils.i(TAG, "getSynTime: no data!");
        }

        return ret;
//        String  sql = "SELECT MAX(ModifyTimestamp) FROM SIGNIN_DESK WHERE GroupId = '" + groupId + "'";
//        return signinDeskDao.queryBuilder().where(new WhereCondition.StringCondition(sql)).build();
    }

    public void addDesk(KeepWatchDesk keepWatchDesk) {
        try {
            keepWatchDesk.combineId();
            keepWatchDeskDao.insertOrReplace(keepWatchDesk);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "addDesk fail: e = " + e.getMessage());
        }
    }

    public void updateDesk(KeepWatchDesk keepWatchDesk) {
        try {
            keepWatchDeskDao.update(keepWatchDesk);
        }
        catch (SQLiteConstraintException e) {
            LogUtils.i(TAG, "updateDesk fail: e = " + e.getMessage());
        }
    }

    public KeepWatchDesk getDesk(String groupId, int deskId) {
        return keepWatchDeskDao.queryBuilder().where(keepWatchDeskDao.queryBuilder().and(KeepWatchDeskDao.Properties.GroupId.eq(groupId), KeepWatchDeskDao.Properties.DeskId.eq(deskId))).unique();
    }

    public List<KeepWatchDesk> getDesk(String groupId) {
        return keepWatchDeskDao.queryBuilder().where(KeepWatchDeskDao.Properties.GroupId.eq(groupId)).list();
    }

    public List<KeepWatchDesk> getNoSynAddDesk(String groupId) {
        return keepWatchDeskDao.queryBuilder().where(keepWatchDeskDao.queryBuilder().and(KeepWatchDeskDao.Properties.GroupId.eq(groupId), KeepWatchDeskDao.Properties.SynTag.eq("new"))).list();
    }

    public List<KeepWatchDesk> getNoSynModifyDesk(String groupId) {
        return keepWatchDeskDao.queryBuilder().where(keepWatchDeskDao.queryBuilder().and(KeepWatchDeskDao.Properties.GroupId.eq(groupId), KeepWatchDeskDao.Properties.SynTag.eq("modify"))).list();
    }

    public int getDeskId(double lat, double lng, String groupId, String userId) {
        List<KeepWatchDesk> deskList;
        if (TextUtils.isEmpty(userId)) {
            deskList = keepWatchDeskDao.queryBuilder().where(KeepWatchDeskDao.Properties.GroupId.eq(groupId)).list();
        }
        else {
            deskList = keepWatchDeskDao.queryBuilder().where(KeepWatchDeskDao.Properties.GroupId.eq(groupId)).list();
        }

        if (deskList == null || deskList.isEmpty()) {
            return -1;
        }

        for (int i = 0; i < deskList.size(); ++i) {
            double detLat = Math.abs(lat - deskList.get(i).getLat());
            double detLng = Math.abs(lng - deskList.get(i).getLng());
            double detX = detLat * 3600.0 * 31.0;
            double angle = (double) ((lat + deskList.get(i).getLat()) / 2 * 3.14159 / 180.0);
            double detY = detLng * 3600.0 * 31.0 * (float)Math.cos(angle);

            if (Math.sqrt((double)(detX * detX) + (detY * detY)) <= 20.0) {
                return deskList.get(i).getDeskId();
            }
        }

        return -1;
    }

    public int getDeskId(String fingerPrint) {
        return -1;
    }

    public String getDeskName(String groupId, int deskId) {
        KeepWatchDesk desk = keepWatchDeskDao.queryBuilder().where(keepWatchDeskDao.queryBuilder().and(KeepWatchDeskDao.Properties.GroupId.eq(groupId), KeepWatchDeskDao.Properties.DeskId.eq(deskId))).unique();
        return desk == null ? "" : desk.getDeskName();
    }

    public boolean isNewSigninDesk(String groupId, int deskId) {
        KeepWatchDesk desk = keepWatchDeskDao.queryBuilder().where(keepWatchDeskDao.queryBuilder().and(KeepWatchDeskDao.Properties.GroupId.eq(groupId), KeepWatchDeskDao.Properties.DeskId.eq(deskId))).unique();
        return desk == null ? true : false;
    }

    public void deleteDesk(String groupId, int deskId) {
        KeepWatchDesk desk = new KeepWatchDesk();
        desk.setGroupId(groupId);
        desk.setDeskId(deskId);
        desk.combineId();
        keepWatchDeskDao.delete(desk);
    }

    public void clearAllDesk() {
        keepWatchDeskDao.deleteAll();
    }

    public void deleteAllDeskCloud() {
        String  sql = "delete KEEP_WATCH_DESK where SynTag = 'cloud'";
        keepWatchDeskDao.getDatabase().execSQL(sql);
    }

    public void addSignin(KeepWatchSigninInfo keepWatchSigninInfo) {
        try {
            keepWatchSigninInfoDao.insertOrReplace(keepWatchSigninInfo);
        }
        catch (SQLiteConstraintException e) {

        }
    }

    public List<KeepWatchSigninInfo> getNoSynAddSignin(String groupId) {
        return keepWatchSigninInfoDao.queryBuilder().where(keepWatchSigninInfoDao.queryBuilder().and(KeepWatchSigninInfoDao.Properties.GroupId.eq(groupId), KeepWatchSigninInfoDao.Properties.SynTag.eq("new"))).list();
    }

    public void deleteSignin(KeepWatchSigninInfo signinInfo) {
        keepWatchSigninInfoDao.delete(signinInfo);
    }
}
