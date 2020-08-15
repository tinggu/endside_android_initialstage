package com.ctfww.module.keepwatch.DataHelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.entity.Cargo;
import com.ctfww.module.keepwatch.entity.KeepWatchRoute;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;
import com.google.gson.reflect.TypeToken;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.keepwatch.bean.KeepWatchAssignmentBean;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchGroupSummary;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;
import com.ctfww.module.keepwatch.entity.KeepWatchRanking;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninStatistics;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByUser;

import java.lang.reflect.Type;
import java.util.List;

import static com.ctfww.commonlib.Consts.REST_FAIL;

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

    public void synKeepWatchDesk(Cargo<KeepWatchDesk> deskCargo, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        CloudClient.getInstance().synKeepWatchDesk(deskCargo, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<Cargo<KeepWatchDesk>>() {}.getType();
                Cargo<KeepWatchDesk> deskCargoRsp = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synKeepWatchDesk: deskCargoRsp = " + deskCargoRsp.toString());
                callback.onSuccess(deskCargoRsp);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchDesk fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchDeskToCloud(CargoToCloud<KeepWatchDesk> cargo, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchDeskToCloud(cargo, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchDeskToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchDeskFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchDeskFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchDesk>>() {}.getType();
                List<KeepWatchDesk> deskList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synKeepWatchDeskFromCloud: deskList.size() = " + deskList.size());
                callback.onSuccess(deskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchDeskFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchRouteSummaryToCloud(CargoToCloud<KeepWatchRouteSummary> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchRouteSummaryToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchRouteSummaryToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchRouteSummaryFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchRouteSummaryFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchRouteSummary>>() {}.getType();
                List<KeepWatchRouteSummary> routeSummaryList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synKeepWatchRouteSummaryFromCloud: routeSummaryList.size() = " + routeSummaryList.size());
                callback.onSuccess(routeSummaryList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchRouteSummaryFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchRouteDeskToCloud(CargoToCloud<KeepWatchRouteDesk> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchRouteDeskToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchRouteDeskToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchRouteDeskFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchRouteDeskFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchRouteDesk>>() {}.getType();
                List<KeepWatchRouteDesk> routeDeskList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synKeepWatchRouteDeskFromCloud: routeDeskList.size() = " + routeDeskList.size());
                callback.onSuccess(routeDeskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchRouteDeskFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchSigninToCloud(CargoToCloud<KeepWatchSigninInfo> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchSigninToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchSigninToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchSigninFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchSigninFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchSigninInfo>>() {}.getType();
                List<KeepWatchSigninInfo> signinList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synKeepWatchSigninFromCloud: signinList.size() = " + signinList.size());
                callback.onSuccess(signinList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchSigninFromCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchAssignmentToCloud(CargoToCloud<KeepWatchAssignment> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchAssignmentToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchAssignmentToCloud fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchAssignmentFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeepWatchAssignmentFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchAssignment>>() {}.getType();
                List<KeepWatchAssignment> assignmentList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "synKeepWatchAssignmentFromCloud: assignmentList.size() = " + assignmentList.size());
                callback.onSuccess(assignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchAssignmentFromCloud fail: code = " + code);
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "getPersonTrends: groupId is empty");
            return;
        }

        long startTime = MyDateTimeUtils.getTodayStartTime();
        long endTime = MyDateTimeUtils.getTodayEndTime();
        CloudClient.getInstance().getPersonTrends(groupId, startTime, endTime, count, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchPersonTrends>>() {}.getType();
                List<KeepWatchPersonTrends> keepWatchPersonTrendsList = GsonUtils.fromJson(data, type);
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "getKeepWatchRanking: groupId is empty");
            return;
        }

        CloudClient.getInstance().getKeepWatchRanking(groupId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchRanking>>() {}.getType();
                List<KeepWatchRanking> keepWatchRankingList = GsonUtils.fromJson(data, type);
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "transferKeepWatchAssignment: groupId is empty");
            return;
        }

        String userId = SPStaticUtils.getString("user_open_id");
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "takeBackKeepWatchAssignment: groupId is empty");
            return;
        }

        String userId = SPStaticUtils.getString("user_open_id");
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

    public void getKeepWatchGroupSummary(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        CloudClient.getInstance().getKeepWatchGroupSummary(groupId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                KeepWatchGroupSummary keepWatchGroupSummary = GsonUtils.fromJson(data, KeepWatchGroupSummary.class);
                callback.onSuccess(keepWatchGroupSummary);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchGroupSummary fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchSigninList(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

        CloudClient.getInstance().getKeepWatchSigninList(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchSigninInfo>>() {}.getType();
                List<KeepWatchSigninInfo> keepWatchSigninInfoList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchSigninInfoList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchSigninShow fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchSigninLeak(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

        CloudClient.getInstance().getKeepWatchSigninLeak(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchSigninStatistics>>() {}.getType();
                List<KeepWatchSigninStatistics> keepWatchSigninStatistics = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchSigninStatistics);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchSigninLeak fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchSigninStatistics(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

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

    public void getKeepWatchAssignmentList(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

        CloudClient.getInstance().getKeepWatchAssignmentList(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchAssignment>>() {}.getType();
                List<KeepWatchAssignment> keepWatchAssignmentList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchAssignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchAssignmentList fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchPeriodAssignmentList(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

        CloudClient.getInstance().getKeepWatchPeriodAssignmentList(groupId, userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchAssignment>>() {}.getType();
                List<KeepWatchAssignment> keepWatchAssignmentList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keepWatchAssignmentList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchPeriodAssignmentList fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getKeepWatchStatistics(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

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
        String groupId = SPStaticUtils.getString("working_group_id");
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");
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

    public void getKeepWatchRouteList(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        CloudClient.getInstance().getKeepWatchRoute(groupId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchRoute>>() {}.getType();
                List<KeepWatchRoute> routeList = GsonUtils.fromJson(data, type);
                callback.onSuccess(routeList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeepWatchRouteList fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }


}
