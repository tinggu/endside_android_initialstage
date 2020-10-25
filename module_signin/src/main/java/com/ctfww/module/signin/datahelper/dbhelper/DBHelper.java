package com.ctfww.module.signin.datahelper.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ctfww.module.signin.entity.DaoMaster;
import com.ctfww.module.signin.entity.DaoSession;
import com.ctfww.module.signin.entity.SigninInfo;
import com.ctfww.module.signin.entity.SigninInfoDao;

import java.util.List;

public class DBHelper {
    private final static String TAG = "DBHelper";

    private SigninInfoDao signinInfoDao;

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

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "signin");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        signinInfoDao = daoSession.getSigninInfoDao();
    }

    // 10. 与巡检点签到有关

    // 存储签到信息，用于签到
    public boolean addSignin(SigninInfo info) {
        return SigninInfoDBHelper.add(signinInfoDao, info);
    }

    public SigninInfo getSignin(String groupId, int deskId, String userId, long timeStamp) {
        return SigninInfoDBHelper.get(signinInfoDao, groupId, deskId, userId, timeStamp);
    }

    // 获取未同步上云的签到，用于签到信息的同步上云
    public List<SigninInfo> getNoSynSigninList() {
        return SigninInfoDBHelper.getNoSynList(signinInfoDao);
    }

    public List<SigninInfo> getSigninList(String groupId) {
        return SigninInfoDBHelper.getList(signinInfoDao, groupId);
    }

    public List<SigninInfo> getSigninList(String groupId, long startTime, long endTime) {
        return SigninInfoDBHelper.getList(signinInfoDao, groupId, startTime, endTime);
    }

    public List<SigninInfo> getSigninList(String groupId, String userId) {
        return SigninInfoDBHelper.getList(signinInfoDao, groupId, userId);
    }

    public List<SigninInfo> getSigninList(String groupId, String userId, long startTime, long endTime) {
        return SigninInfoDBHelper.getList(signinInfoDao, groupId, userId, startTime, endTime);
    }

    public List<SigninInfo> getSigninList(String groupId, int deskId, String userId, String type, long startTime, long endTime) {
        return SigninInfoDBHelper.getList(signinInfoDao, groupId, deskId, userId, type, startTime, endTime);
    }

    public long getSigninCount(String groupId, long startTime, long endTime) {
        return SigninInfoDBHelper.getCount(signinInfoDao, groupId, startTime, endTime);
    }

    public long getSigninCount(String groupId, String userId, long startTime, long endTime) {
        return SigninInfoDBHelper.getCount(signinInfoDao, groupId, userId, startTime, endTime);
    }

    // 用于同步上云后更改同步状态
    public void updateSignin(SigninInfo info) {
        SigninInfoDBHelper.update(signinInfoDao, info);
    }

//    public void updateSigninCount(DeskSignin info) {
//        SigninCount signinCount = getSigninCount(info.getGroupId(), info.getDeskId(), info.getUserId(), MyDateTimeUtils.getDayStartTime(info.getTimeStamp()), "desk");
//        if (signinCount == null) {
//            signinCount = new SigninCount();
//            signinCount.setAssignmentId(DeskAssignment.createTodayAssignmentId(info.getGroupId(), info.getDeskId()));
//            signinCount.setSigninCount(1);
//            signinCount.setGroupId(info.getGroupId());
//            signinCount.setObjectId(info.getDeskId());
//            signinCount.setUserId(info.getUserId());
//            signinCount.setDayTimeStamp(MyDateTimeUtils.getTodayEndTime());
//            signinCount.setType("desk");
//            addSigninCount(signinCount);
//        }
//        else {
//            int count = signinCount.getSigninCount() + 1;
//            signinCount.setSigninCount(count);
//            updateSigninCount(info);
//        }
//    }
//
//    public void updateSigninCount(RouteSignin info) {
//        SigninCount signinCount = getSigninCount(info.getGroupId(), info.getRouteId(), info.getUserId(), MyDateTimeUtils.getDayStartTime(info.getTimeStamp()), "route");
//        if (signinCount == null) {
//            signinCount = new SigninCount();
//            signinCount.setAssignmentId(RouteAssignment.createTodayAssignmentId(info.getGroupId(), info.getRouteId()));
//            signinCount.setSigninCount(1);
//            signinCount.setGroupId(info.getGroupId());
//            signinCount.setObjectId(info.getRouteId());
//            signinCount.setUserId(info.getUserId());
//            signinCount.setDayTimeStamp(MyDateTimeUtils.getTodayEndTime());
//            signinCount.setType("route");
//            addSigninCount(signinCount);
//        }
//        else {
//            int count = signinCount.getSigninCount() + 1;
//            signinCount.setSigninCount(count);
//            updateSigninCount(info);
//        }
//    }
}
