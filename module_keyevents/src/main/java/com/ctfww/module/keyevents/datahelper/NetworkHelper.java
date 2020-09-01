package com.ctfww.module.keyevents.datahelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.user.datahelper.sp.Const;
import com.google.gson.reflect.TypeToken;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventStatisticsByUserUnit;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.bean.KeyEventStatisticsByUser;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void synKeyEventToCloud(CargoToCloud<KeyEvent> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeyEventToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeyEventToCloud fail: codee = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeyEventFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeyEventFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEvent>>() {}.getType();
                List<KeyEvent> keyEventList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeyEventFromCloud fail: codee = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeyEventTraceToCloud(CargoToCloud<KeyEventTrace> cargoToCloud, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeyEventTraceToCloud(cargoToCloud, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeyEventTraceToCloud fail: codee = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synKeyEventTraceFromCloud(QueryCondition condition, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().synKeyEventTraceFromCloud(condition, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEventTrace>>() {}.getType();
                List<KeyEventTrace> keyEventTraceList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventTraceList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "synKeyEventTraceFromCloud fail: codee = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void uploadFile(final String filePath, final IUIDataHelperCallback callback) {
        if (TextUtils.isEmpty(filePath)) {
            callback.onSuccess("");
            return;
        }

        CloudClient.getInstance().uploadFile(filePath, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                String url;
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    url = jsonObject.getString("data");
                } catch (JSONException e) {
                    LogUtils.e(TAG, "e.message = " + e.getMessage());
                    return;
                }

                if (callback != null) {
                    callback.onSuccess(url);
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "failUploadFile = " + code);
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

    public void getKeyEventActionList(String eventId, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().getKeyEventActionList(eventId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEventTrace>>() {}.getType();
                List<KeyEventTrace> keyEventTraceList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventTraceList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getKeyEventActionList fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getNoEndKeyEventCount(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);

        CloudClient.getInstance().getNoEndKeyEventCount(groupId, userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int count = jsonObject.getInt("data");
                    callback.onSuccess(count);
                }
                catch (JSONException e) {
                    LogUtils.i(TAG, "getNoEndKeyEventCount: wrong json data, data = " + data);
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getNoEndKeyEventCount fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getEndKeyEventList(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);
        getEndKeyEventList(startTime, endTime, userId, callback);
    }

    public void getEndKeyEventList(long startTime, long endTime, String userId, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        CloudClient.getInstance().getEndKeyEventList(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEvent>>() {}.getType();
                List<KeyEvent> keyEventList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getEndKeyEventList fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getNoEndKeyEventList(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);

        CloudClient.getInstance().getNoEndKeyEventList(groupId, userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEvent>>() {}.getType();
                List<KeyEvent> keyEventList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getNoEndKeyEventList fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getHistoryEveryDayKeyEventStatistics(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);

        CloudClient.getInstance().getHistoryEveryDayKeyEventStatistics(groupId, userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEventStatisticsByUser>>() {}.getType();
                List<KeyEventStatisticsByUser> keyEventStatisticsByUserList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventStatisticsByUserList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getHistoryEveryDayKeyEventStatistics fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getEveryOneEndKeyEventStatistics(long startTime, long endTime, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        CloudClient.getInstance().getEveryOneEndKeyEventStatistics(groupId, "", startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEventStatisticsByUserUnit>>() {}.getType();
                List<KeyEventStatisticsByUserUnit> keyEventStatisticsByUserUnitList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventStatisticsByUserUnitList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getEveryOneEndKeyEventStatistics fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getSomeOneDoingKeyEvent(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup()) ? "" : SPStaticUtils.getString(Const.USER_OPEN_ID);
        CloudClient.getInstance().getSomeOneDoingKeyEvent(groupId, userId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEvent>>() {}.getType();
                List<KeyEvent> keyEventList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getSomeOneDoingKeyEvent fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getCanBeSnatchedKeyEvent(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        CloudClient.getInstance().getCanBeSnatchedKeyEvent(groupId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<List<KeyEvent>>() {}.getType();
                List<KeyEvent> keyEventList = GsonUtils.fromJson(data, type);
                callback.onSuccess(keyEventList);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "getCanBeSnatchedKeyEvent fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void snatchKeyEvent(String eventId, int deskId, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        CloudClient.getInstance().snatchKeyEvent(groupId, userId, eventId, deskId, System.currentTimeMillis(),  new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "snatchKeyEvent fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void freeKeyEvent(String eventId, int deskId, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        CloudClient.getInstance().freeKeyEvent(groupId, userId, eventId, deskId, System.currentTimeMillis(),  new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "freeKeyEvent fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }
}
