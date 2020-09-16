package com.ctfww.module.keepwatch.datahelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.keepwatch.entity.PersonTrends;
import com.ctfww.module.keepwatch.entity.Ranking;
import com.ctfww.module.user.datahelper.sp.Const;
import com.google.gson.reflect.TypeToken;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninStatistics;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByUser;

import java.lang.reflect.Type;
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

    public void synTodayKeepWatchPersonTrendsFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synTodayKeepWatchPersonTrendsFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<PersonTrends>>() {}.getType();
                List<PersonTrends> assignmentList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synTodayKeepWatchPersonTrendsFromCloud: assignmentList.size() = " + assignmentList.size());
                callback.onSuccess(assignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synTodayKeepWatchPersonTrendsFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synTodayKeepWatchRankingFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synTodayKeepWatchRankingFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<Ranking>>() {}.getType();
                List<Ranking> assignmentList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synTodayKeepRankingTrendsFromCloud: assignmentList.size() = " + assignmentList.size());
                callback.onSuccess(assignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synTodayKeepRankingTrendsFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synTodayKeepWatchAssignmetnFinishStatusFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synTodayKeepWatchAssignmetnFinishStatusFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchStatisticsByDesk>>() {}.getType();
                List<KeepWatchStatisticsByDesk> statisticsByDeskList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synTodayKeepWatchAssignmetnFinishStatusFromCloud: statisticsByDeskList.size() = " + statisticsByDeskList.size());
                callback.onSuccess(statisticsByDeskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synTodayKeepWatchAssignmetnFinishStatusFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    /***********************************new*************************************************/

    public void getPersonTrends(int count, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "getPersonTrends: groupId is empty");
            return;
        }

        long startTime = MyDateTimeUtils.getTodayStartTime();
        long endTime = MyDateTimeUtils.getTodayEndTime();
        CloudClient.getInstance().getPersonTrends(groupId, startTime, endTime, count, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<PersonTrends>>() {}.getType();
                List<PersonTrends> keepWatchPersonTrendsList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchPersonTrendsList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getPersonTrends fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchRanking(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "getKeepWatchRanking: groupId is empty");
            return;
        }

        CloudClient.getInstance().getKeepWatchRanking(groupId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<Ranking>>() {}.getType();
                List<Ranking> keepWatchRankingList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchRankingList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getRanking fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void transferKeepWatchAssignment(String toUserId, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "transferKeepWatchAssignment: groupId is empty");
            return;
        }

        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        CloudClient.getInstance().transferKeepWatchAssignment(groupId, userId, toUserId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "transferKeepWatchAssignment fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void takeBackKeepWatchAssignment(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "takeBackKeepWatchAssignment: groupId is empty");
            return;
        }

        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        CloudClient.getInstance().takeBackKeepWatchAssignment(groupId, userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "takeBackKeepWatchAssignment fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchSigninStatistics(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);

        CloudClient.getInstance().getKeepWatchSigninStatistics(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchSigninStatistics>>() {}.getType();
                List<KeepWatchSigninStatistics> keepWatchSigninStatistics = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchSigninStatistics);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchSigninStatistics fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchStatistics(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);

        CloudClient.getInstance().getKeepWatchStatistics(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
               KeepWatchStatisticsByPeriod keepWatchStatisticsByPeriod = GsonUtils.fromJson(data, KeepWatchStatisticsByPeriod.class);
                callback.onSuccess(keepWatchStatisticsByPeriod);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchStatistics fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchAssignmentAndSigninStatisticsForDesk(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);

        CloudClient.getInstance().getKeepWatchAssignmentAndSigninStatisticsForDesk(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchStatisticsByDesk>>() {}.getType();
                List<KeepWatchStatisticsByDesk> keepWatchStatisticsByDeskList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchStatisticsByDeskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchAssignmentAndSigninStatisticsForDesk fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchStatisticsByPeriodByDayUnit(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);

        CloudClient.getInstance().getKeepWatchStatisticsByPeriodByDayUnit(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchStatisticsByPeriod>>() {}.getType();
                List<KeepWatchStatisticsByPeriod> keepWatchStatisticsByPeriodList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchStatisticsByPeriodList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchStatisticsByPeriodByDayUnit fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchLeakStatisticsForDesk(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);

        CloudClient.getInstance().getKeepWatchLeakStatisticsForDesk(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchStatisticsByDesk>>() {}.getType();
                List<KeepWatchStatisticsByDesk> keepWatchStatisticsByDeskList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchStatisticsByDeskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchLeakStatisticsForDesk fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchSigninStatisticsForDesk(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        CloudClient.getInstance().getKeepWatchSigninStatisticsForDesk(groupId, "", startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchStatisticsByDesk>>() {}.getType();
                List<KeepWatchStatisticsByDesk> keepWatchStatisticsByDeskList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchStatisticsByDeskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchSigninStatisticsForDesk fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getHistoryEveryDayStatistics(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);
        CloudClient.getInstance().getHistoryEveryDayStatistics(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchStatisticsByUser>>() {}.getType();
                List<KeepWatchStatisticsByUser> keepWatchStatisticsByUsers = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchStatisticsByUsers);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getHistoryEveryDayStatistics fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
