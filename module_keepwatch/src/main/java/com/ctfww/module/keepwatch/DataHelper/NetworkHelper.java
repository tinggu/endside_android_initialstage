package com.ctfww.module.keepwatch.DataHelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.im.UdpHelper;
import com.google.gson.reflect.TypeToken;
import com.ctfww.commonlib.Consts;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.fingerprint.entity.FingerPrintHistory;
import com.ctfww.module.keepwatch.bean.KeepWatchAssignmentBean;
import com.ctfww.module.keepwatch.bean.KeepWatchSigninBean;
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

import org.greenrobot.eventbus.EventBus;

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

    public void addKeepWatchDesk(int deskId, String deskName, String address, double lat, double lng, String deskType, String fingerPrint, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        long timeStamp = System.currentTimeMillis();
        long modifyTimestamp = timeStamp;

        KeepWatchDesk desk = new KeepWatchDesk();
        desk.setGroupId(groupId);

        desk.setLat(lat);
        desk.setLng(lng);
        desk.setDeskName(deskName);
        desk.setDeskAddress(address);

        desk.setDeskId(deskId);
        desk.setDeskType(deskType);
        desk.setFingerPrint(fingerPrint);
        desk.setCreateTimeStamp(timeStamp);
        desk.setModifyTimeStamp(modifyTimestamp);
        desk.setStatus("reserve");

        CloudClient.getInstance().addKeepWatchDesk(groupId,
                lat, lng, deskName, address,
                deskId, deskType, fingerPrint,
                timeStamp, modifyTimestamp, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                desk.setSynTag("cloud");
                DBHelper.getInstance().addDesk(desk);
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                if (code != REST_FAIL) {
                    desk.setSynTag("new");
                    DBHelper.getInstance().addDesk(desk);
                }

                LogUtils.i(TAG, "addSigninDesk fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                desk.setSynTag("new");
                DBHelper.getInstance().addDesk(desk);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synAddDeskToCloud(KeepWatchDesk desk, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().addKeepWatchDesk(desk.getGroupId(),
                desk.getLat(), desk.getLng(), desk.getDeskName(), desk.getDeskAddress(),
                desk.getDeskId(), desk.getDeskType(), desk.getFingerPrint(),
                desk.getCreateTimeStamp(), desk.getModifyTimeStamp(), new ICloudCallback() {
                    @Override
                    public void onSuccess(String data) {
                        desk.setSynTag("cloud");
                        DBHelper.getInstance().updateDesk(desk);
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        LogUtils.i(TAG, "synAddSigninDesk fail: code = " + code);
                        callback.onError(code);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
                    }
                });
    }

    public void modifyDesk(KeepWatchDesk desk, final IUIDataHelperCallback callback) {
        desk.setModifyTimeStamp(System.currentTimeMillis());
        CloudClient.getInstance().updateKeepWatchDeskAddition(desk.getGroupId(),
                desk.getDeskName(), desk.getDeskAddress(),
                desk.getDeskId(), desk.getDeskType(), desk.getFingerPrint(),
                desk.getModifyTimeStamp(), new ICloudCallback() {
                    @Override
                    public void onSuccess(String data) {
                        desk.setSynTag("cloud");
                        DBHelper.getInstance().updateDesk(desk);
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        desk.setSynTag("modify");
                        DBHelper.getInstance().updateDesk(desk);
                        LogUtils.i(TAG, "modifyDesk fail: code = " + code);
                        callback.onError(code);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        desk.setSynTag("modify");
                        DBHelper.getInstance().updateDesk(desk);
                        callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
                    }
                });
    }

    public void synModifyDeskToCloud(KeepWatchDesk desk, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().updateKeepWatchDeskAddition(desk.getGroupId(),
                desk.getDeskName(), desk.getDeskAddress(),
                desk.getDeskId(), desk.getDeskType(), desk.getFingerPrint(),
                desk.getModifyTimeStamp(), new ICloudCallback() {
                    @Override
                    public void onSuccess(String data) {
                        desk.setSynTag("cloud");
                        DBHelper.getInstance().updateDesk(desk);
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        LogUtils.i(TAG, "synModifyDeskToCloud fail: code = " + code);
                        callback.onError(code);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
                    }
                });
    }

    private void updateKeepWatchDeskFingerPrint(int deskId, long timeStamp, List<FingerPrintHistory> fingerPrintHistoryList) {
        if (fingerPrintHistoryList.isEmpty()) {
            LogUtils.i(TAG, "updateKeepWatchDeskFingerPrint: no history finger print data!");
            return;
        }

        String fingerPrint = com.ctfww.module.fingerprint.Utils.synthesizeFingerPrint(fingerPrintHistoryList);
        LogUtils.i(TAG, "updateKeepWatchDeskFingerPrint: fingerPrint = " + fingerPrint);
        if (TextUtils.isEmpty(fingerPrint)) {
            LogUtils.i(TAG, "updateKeepWatchDeskFingerPrint: synthesizeFingerPrint fail!");
            return;
        }

        String groupId = SPStaticUtils.getString("working_group_id");
        CloudClient.getInstance().updateKeepWatchDeskFingerPrint(groupId, deskId, timeStamp, fingerPrint, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                LogUtils.i(TAG, "updateKeepWatchDeskFingerPrint: CloudClient.getInstance().updateKeepWatchDeskFingerPrint success!");

                // 对本地的签到点指纹进行修改
                KeepWatchDesk desk = DBHelper.getInstance().getDesk(groupId, deskId);
                desk.setModifyTimeStamp(timeStamp);
                desk.setFingerPrint(fingerPrint);
                DBHelper.getInstance().updateDesk(desk);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "updateKeepWatchDeskFingerPrint: CloudClient.getInstance().updateKeepWatchDeskFingerPrint fail: code = " + code);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.i(TAG, "updateKeepWatchDeskFingerPrint: CloudClient.getInstance().updateKeepWatchDeskFingerPrint fail: code = " + NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getAllDesk(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        CloudClient.getInstance().getKeepWatchDesk(groupId, 0, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeepWatchDesk>>() {}.getType();
                List<KeepWatchDesk> deskList = GsonUtils.fromJson(data, type);
                LogUtils.i(TAG, "getAllDesk: deskList.size() = " + deskList.size());
                DBHelper.getInstance().setDeskList(deskList);
                callback.onSuccess(deskList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getAllDesk fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void deleteKeepWatchDesk(int deskId, final  IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        CloudClient.getInstance().deleteKeepWatchDesk(groupId, deskId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                DBHelper.getInstance().deleteDesk(groupId, deskId);
                callback.onSuccess(deskId);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "deleteDesk fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    /***********************************new*************************************************/

    public void addKeepWatchSignin(KeepWatchSigninInfo keepWatchSigninInfo, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        keepWatchSigninInfo.setGroupId(groupId);
        String userId = SPStaticUtils.getString("user_open_id");
        keepWatchSigninInfo.setUserId(userId);
        long timeStamp = System.currentTimeMillis();
        keepWatchSigninInfo.setTimeStamp(timeStamp);

        LogUtils.i(TAG, "addKeepWatchSignin: keepWatchSigninInfo = " + keepWatchSigninInfo.toString());

        String data = GsonUtils.toJson(keepWatchSigninInfo);
        KeepWatchSigninBean keepWatchSigninBean = GsonUtils.fromJson(data, KeepWatchSigninBean.class);
        CloudClient.getInstance().addKeepWatchSignin(keepWatchSigninBean, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
                // 更新签到指纹
                if ("qr".equals(keepWatchSigninInfo.getFinishType())) {
                    Type type = new TypeToken<List<FingerPrintHistory>>() {}.getType();
                    List<FingerPrintHistory> fingerPrintHistoryList = GsonUtils.fromJson(data, type);
                    updateKeepWatchDeskFingerPrint(keepWatchSigninInfo.getDeskId(), keepWatchSigninInfo.getTimeStamp(), fingerPrintHistoryList);
                }

                EventBus.getDefault().post(new MessageEvent("send_signin_success"));
            }

            @Override
            public void onError(int code, String errorMsg) {
                if (code != Consts.REST_FAIL && code != Consts.REST_BELONG_FAIL) {
                    keepWatchSigninInfo.setSynTag("new");
                    DBHelper.getInstance().addSignin(keepWatchSigninInfo);
                }

                LogUtils.i(TAG, "addSignin: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                keepWatchSigninInfo.setSynTag("new");
                DBHelper.getInstance().addSignin(keepWatchSigninInfo);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeepWatchSigninToCloud(KeepWatchSigninInfo keepWatchSigninInfo, final IUIDataHelperCallback callback) {
        String data = GsonUtils.toJson(keepWatchSigninInfo);
        KeepWatchSigninBean keepWatchSigninBean = GsonUtils.fromJson(data, KeepWatchSigninBean.class);
        CloudClient.getInstance().addKeepWatchSignin(keepWatchSigninBean, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                DBHelper.getInstance().deleteSignin(keepWatchSigninInfo);
                callback.onSuccess(data);
                // 更新签到指纹
                if ("qr".equals(keepWatchSigninInfo.getFinishType())) {
                    Type type = new TypeToken<List<FingerPrintHistory>>() {}.getType();
                    List<FingerPrintHistory> fingerPrintHistoryList = GsonUtils.fromJson(data, type);
                    updateKeepWatchDeskFingerPrint(keepWatchSigninInfo.getDeskId(), keepWatchSigninInfo.getTimeStamp(), fingerPrintHistoryList);
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeepWatchSigninToCloud fail: code = " + code);
                if (code == Consts.REST_FAIL || code == Consts.REST_BELONG_FAIL) {
                    DBHelper.getInstance().deleteSignin(keepWatchSigninInfo);
                }
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

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

    public void addKeepWatchAssignment(String deskIdList, String circleType, long startTime, long endTime, String userId, int frequency, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "getRanking: groupId is empty");
            return;
        }

        KeepWatchAssignmentBean keepWatchAssignmentBean = new KeepWatchAssignmentBean();
        keepWatchAssignmentBean.setDeskId(0);
        keepWatchAssignmentBean.setDeskIdStr(deskIdList);
        keepWatchAssignmentBean.setCircleType(circleType);
        keepWatchAssignmentBean.setUserId(userId);
        keepWatchAssignmentBean.setGroupId(groupId);
        keepWatchAssignmentBean.setFrequency(frequency);
        keepWatchAssignmentBean.setStartTime(startTime);
        keepWatchAssignmentBean.setEndTime(endTime);
        CloudClient.getInstance().addKeepWatchAssignment(keepWatchAssignmentBean, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "addKeepWatchAssignment fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void deleteKeepWatchAssignment(String userId, int deskId, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            LogUtils.i(TAG, "deleteKeepWatchAssignment: groupId is empty");
            return;
        }

        CloudClient.getInstance().deleteKeepWatchAssignment(groupId, userId, deskId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "deleteKeepWatchAssignment fail: code = " + code);
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
}
