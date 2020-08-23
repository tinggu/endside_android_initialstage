package com.ctfww.module.keepwatch.datahelper;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.entity.Cargo;
import com.ctfww.commonlib.entity.CloudRspData;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.utils.NetworkUtils;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRoute;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CloudClient {

    private static final String TAG = "SelfCloudClient";

    private final static int VALIDE_DATA = 80000000;
    private final static String mUrl = "http://39.98.147.77:7001";

    private ICloudMethod mCloudMethod;

    public static CloudClient getInstance() {
        return CloudClient.Inner.INSTANCE;
    }

    private static class Inner {
        private static final CloudClient INSTANCE = new CloudClient();
    }

    private CloudClient(){
    }

    public void setCloudMethod(ICloudMethod method) {
        mCloudMethod = method;
    }

    public void createCloudMethod() {
        mCloudMethod = NetworkUtils.createCloudMethod(ICloudMethod.class, mUrl);
    }

    private int getResultCode(String data) {
        try {
            JSONObject jsonObj = new JSONObject(data);
            return jsonObj.getInt("resultCode");
        }
        catch (JSONException e) {
            return -10;
        }
    }

    private void processRsp(retrofit2.Response<ResponseBody> response, final ICloudCallback callback) {
        LogUtils.i(TAG, "response code = " + response.code());
        if (response.code() == 200) {
            try {
                if (response.body() != null) {
                    String data = response.body().string();
                    LogUtils.i(TAG, "data = " + data);
                    int resultCode = getResultCode(data);
                    LogUtils.i(TAG, "resultCode = " + resultCode);
                    if (resultCode == VALIDE_DATA) {
                        callback.onSuccess(data);
                    }
                    else {
                        callback.onError(resultCode, "数据错误！");
                    }
                }else {
                    callback.onError(-9, "没有返回数据！");
                }
            } catch (IOException e) {
                LogUtils.e(TAG, "e.message = " + e.getMessage());
                callback.onError(-8, "数据异常！");
            }
        }

        if (response.code() != 200 || !response.isSuccessful()) {
            callback.onError(response.code(), response.message());
        }
    }

    private CloudRspData parseRsp(retrofit2.Response<ResponseBody> response) {
        LogUtils.i(TAG, "response code = " + response.code());
        if (response.code() != 200) {
            return new CloudRspData(response.code(), "网络有问题");
        }

        if (!response.isSuccessful()) {
            return new CloudRspData(-100, "网络有问题");
        }

        try {
            if (response.body() != null) {
                String data = response.body().string();
                LogUtils.i(TAG, "data = " + data);
                CloudRspData cloudRspData = new CloudRspData(getResultCode(data), data) ;
                return cloudRspData;
            }else {
                return new CloudRspData(-9, "没有body");
            }
        } catch (IOException e) {
            LogUtils.e(TAG, "e.message = " + e.getMessage());
            return new CloudRspData(-8, "body数据有异常");
        }
    }

    private void processGeneralRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    callback.onSuccess(cloudRspData.getBodyStr());
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    private void processSingleObjRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        callback.onSuccess(jsonObjectData.toString());
                    }
                    catch (JSONException e) {
                        LogUtils.e(TAG, "e.message = " + e.getMessage());
                        callback.onError(-10, "数据格式错误");
                    }
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    private void processListRsp(Call<ResponseBody> responseBodyCall, final ICloudCallback callback) {
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                CloudRspData cloudRspData = parseRsp(response);
                if (cloudRspData.getErrCode() == VALIDE_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(cloudRspData.getBodyStr());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        callback.onSuccess(jsonArray.toString());
                    }
                    catch (JSONException e) {
                        LogUtils.e(TAG, "e.message = " + e.getMessage());
                        callback.onError(-10, "数据格式错误");
                    }
                }
                else {
                    callback.onError(cloudRspData.getErrCode(), cloudRspData.getBodyStr());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    /****************************new*****************************************/

    public void synKeepWatchDesk(Cargo<KeepWatchDesk> deskCargo, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchDesk: deskCargo = " + deskCargo.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchDesk(deskCargo);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void synKeepWatchDeskToCloud(CargoToCloud<KeepWatchDesk> cargo, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchDeskToCloud: cargo = " + cargo.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchDeskToCloud(cargo);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synKeepWatchDeskFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchDeskFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchDeskFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synKeepWatchRouteSummaryToCloud(CargoToCloud<KeepWatchRouteSummary> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchRouteSummaryToCloud: cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchRouteSummaryToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synKeepWatchRouteSummaryFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchRouteSummaryFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchRouteSummaryFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synKeepWatchRouteDeskToCloud(CargoToCloud<KeepWatchRouteDesk> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchRouteDeskToCloud: cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchRouteDeskToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synKeepWatchRouteDeskFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchRouteDeskFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchRouteDeskFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synKeepWatchSigninToCloud(CargoToCloud<KeepWatchSigninInfo> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchSigninToCloud: cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchSigninToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synKeepWatchSigninFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchSigninFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchSigninFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synKeepWatchAssignmentToCloud(CargoToCloud<KeepWatchAssignment> cargoToCloud, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchAssignmentToCloud: cargoToCloud = " + cargoToCloud.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchAssignmentToCloud(cargoToCloud);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void synKeepWatchAssignmentFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synKeepWatchAssignmentFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synKeepWatchAssignmentFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synTodayKeepWatchPersonTrendsFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synTodayKeepWatchPersonTrendsFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synTodayKeepWatchPersonTrendsFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synTodayKeepWatchRankingFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synTodayKeepWatchRankingFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synTodayKeepWatchRankingFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void synTodayKeepWatchAssignmetnFinishStatusFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synTodayKeepWatchAssignmetnFinishStatusFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synTodayKeepWatchAssignmetnFinishStatusFromCloud(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void getPersonTrends(String groupId, long startTime, long endTime, int count, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(groupId, "", startTime, endTime);
        condition.setCondition("" + count);
        LogUtils.i(TAG, "getPersonTrends: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getPersonTrends(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchRanking(String groupId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(groupId, "", startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchRanking: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchRanking(condition);
        processListRsp(responseBodyCall, callback);
    }

    public void transferKeepWatchAssignment(String groupId, String userId, String toUserId, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        condition.setCondition(toUserId);
        LogUtils.i(TAG, "transferKeepWatchAssignment: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.transferKeepWatchAssignment(condition);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void takeBackKeepWatchAssignment(String groupId, String userId, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        LogUtils.i(TAG, "takeBackKeepWatchAssignment: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.takeBackKeepWatchAssignment(condition);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void getKeepWatchGroupSummary(String groupId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getKeepWatchGroupSummary: groupId = " + groupId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchGroupSummary(groupId);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninLeak(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninLeak: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninLeak(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninStatistics(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninList(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninList(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchAssignmentList(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchAssignmentList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchAssignmentList(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchPeriodAssignmentList(String groupId, String userId, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId);
        LogUtils.i(TAG, "getKeepWatchPeriodAssignmentList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchPeriodAssignmentList(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchStatistics(queryConditionBean);
        processSingleObjRsp(responseBodyCall, callback);
    }

    public void getKeepWatchAssignmentAndSigninStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchAssignmentAndSigninStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchAssignmentAndSigninStatisticsForDesk(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchStatisticsByPeriodByDayUnit(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchStatisticsByPeriodByDayUnit: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchStatisticsByPeriodByDayUnit(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchLeakStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchLeakStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchLeakStatisticsForDesk(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninStatisticsForDesk(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void getHistoryEveryDayStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getHistoryEveryDayStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getHistoryEveryDayStatistics(queryConditionBean);
        processListRsp(responseBodyCall, callback);
    }

    public void addKeepWatchRoute(KeepWatchRoute keepWatchRouteTemp, final ICloudCallback callback) {
        LogUtils.i(TAG, "addKeepWatchRoute: keepWatchRouteTemp = " + keepWatchRouteTemp.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.addKeepWatchRoute(keepWatchRouteTemp);
        processGeneralRsp(responseBodyCall, callback);
    }

    public void getKeepWatchRoute(String groupId, final ICloudCallback callback) {
        LogUtils.i(TAG, "getKeepWatchRoute: groupId = " + groupId);
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchRoute(groupId);
        processListRsp(responseBodyCall, callback);
    }
}
