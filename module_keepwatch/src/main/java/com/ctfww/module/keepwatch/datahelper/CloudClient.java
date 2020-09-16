package com.ctfww.module.keepwatch.datahelper;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.network.CloudClientRsp;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.utils.NetworkUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;

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

    /****************************new*****************************************/

    public void synTodayKeepWatchPersonTrendsFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synTodayKeepWatchPersonTrendsFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synTodayKeepWatchPersonTrendsFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void synTodayKeepWatchRankingFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synTodayKeepWatchRankingFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synTodayKeepWatchRankingFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void synTodayKeepWatchAssignmetnFinishStatusFromCloud(QueryCondition condition, final ICloudCallback callback) {
        LogUtils.i(TAG, "synTodayKeepWatchAssignmetnFinishStatusFromCloud: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.synTodayKeepWatchAssignmetnFinishStatusFromCloud(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getPersonTrends(String groupId, long startTime, long endTime, int count, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(groupId, "", startTime, endTime);
        condition.setCondition("" + count);
        LogUtils.i(TAG, "getPersonTrends: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getPersonTrends(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchRanking(String groupId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition(groupId, "", startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchRanking: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchRanking(condition);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void transferKeepWatchAssignment(String groupId, String userId, String toUserId, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        condition.setCondition(toUserId);
        LogUtils.i(TAG, "transferKeepWatchAssignment: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.transferKeepWatchAssignment(condition);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void takeBackKeepWatchAssignment(String groupId, String userId, final ICloudCallback callback) {
        QueryCondition condition = new QueryCondition();
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        LogUtils.i(TAG, "takeBackKeepWatchAssignment: condition = " + condition.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.takeBackKeepWatchAssignment(condition);
        CloudClientRsp.processGeneralRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninLeak(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninLeak: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninLeak(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninStatistics(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninList(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninList(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchAssignmentList(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchAssignmentList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchAssignmentList(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchPeriodAssignmentList(String groupId, String userId, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId);
        LogUtils.i(TAG, "getKeepWatchPeriodAssignmentList: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchPeriodAssignmentList(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchStatistics(queryConditionBean);
        CloudClientRsp.processSingleObjRsp(responseBodyCall, callback);
    }

    public void getKeepWatchAssignmentAndSigninStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchAssignmentAndSigninStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchAssignmentAndSigninStatisticsForDesk(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchStatisticsByPeriodByDayUnit(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchStatisticsByPeriodByDayUnit: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchStatisticsByPeriodByDayUnit(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchLeakStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchLeakStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchLeakStatisticsForDesk(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getKeepWatchSigninStatisticsForDesk(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getKeepWatchSigninStatisticsForDesk: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getKeepWatchSigninStatisticsForDesk(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }

    public void getHistoryEveryDayStatistics(String groupId, String userId, long startTime, long endTime, final ICloudCallback callback) {
        QueryCondition queryConditionBean = new QueryCondition(groupId, userId, startTime, endTime);
        LogUtils.i(TAG, "getHistoryEveryDayStatistics: queryConditionBean = " + queryConditionBean.toString());
        Call<ResponseBody> responseBodyCall = mCloudMethod.getHistoryEveryDayStatistics(queryConditionBean);
        CloudClientRsp.processListRsp(responseBodyCall, callback);
    }
}
