package com.guoliang.module.keyevents.datahelper;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.network.ICloudCallback;
import com.guoliang.commonlib.network.NetworkConst;
import com.guoliang.module.keyevents.Entity.KeyEvent;
import com.guoliang.module.keyevents.Entity.KeyEventReportRepaired;
import com.guoliang.module.keyevents.Entity.KeyEventStatisticsByUserUnit;
import com.guoliang.module.keyevents.Entity.KeyEventTrace;
import com.guoliang.module.keyevents.bean.KeyEventBean;
import com.guoliang.module.keyevents.bean.KeyEventStatisticsByUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public void addKeyEvent(final KeyEvent keyEvent, final IUIDataHelperCallback callback) {
        KeyEventBean keyEventBean = Utils.keyEvent2KeyEventBean(keyEvent);
        CloudClient.getInstance().addKeyEvent(keyEventBean, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                DBHelper.getInstance().deleteKeyEvent(keyEvent);
                callback.onSuccess(keyEvent);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "failAddKeyEvent = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getAllKeyEvent(long startTime, long endTime, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().getAllKeyEvent(startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                List<KeyEvent> keyEvents = processGetAllKeyEvent(data);
                if (callback != null) {
                    callback.onSuccess(keyEvents);
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "failGetAllKeyEvent = " + code);
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

    public void getSomeoneAllKeyEvent(String userId, long startTime, long endTime, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().getSomeoneAllKeyEvent(userId, startTime, endTime, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                List<KeyEvent> keyEvents = processGetAllKeyEvent(data);
                if (callback != null) {
                    callback.onSuccess(keyEvents);
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "failGetAllKeyEvent = " + code);
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

    private List<KeyEvent> processGetAllKeyEvent(String data) {
        LogUtils.i(TAG, "data = " + data);

        JSONArray jsonArr;
        try {
            JSONObject jsonObject = new JSONObject(data);
            jsonArr = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            LogUtils.e(TAG, "e.message = " + e.getMessage());
            return null;
        }

        List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
        for (int i = 0; i < jsonArr.length(); ++i) {
            JSONObject jsonObj;
            try {
                jsonObj = jsonArr.getJSONObject(i);
            } catch (JSONException e) {
                continue;
            }

            KeyEventBean keyEventBean = GsonUtils.fromJson(jsonObj.toString(), KeyEventBean.class);
            KeyEvent keyEvent = Utils.keyEventBean2KeyEvent(keyEventBean);
            keyEvent.setSynTag("cloud");
            DBHelper.getInstance().addKeyEvent(keyEvent);

            keyEvents.add(keyEvent);
        }

        return keyEvents;
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

    public void reportRepaired(String eventId, final IUIDataHelperCallback callback) {
        KeyEventReportRepaired keyEventReportRepaired = new KeyEventReportRepaired();
        keyEventReportRepaired.setTimeStamp(System.currentTimeMillis());
        keyEventReportRepaired.setEventId(eventId);
        keyEventReportRepaired.setSynTag("new");
        keyEventReportRepaired.combineId();

        CloudClient.getInstance().reportRepaired(eventId, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                if (code != 80000001) {
                    DBHelper.getInstance().addReportRepaired(keyEventReportRepaired);
                }

                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                DBHelper.getInstance().addReportRepaired(keyEventReportRepaired);
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void synReportRepaired(KeyEventReportRepaired keyEventReportRepaired, final IUIDataHelperCallback callback) {
        CloudClient.getInstance().reportRepaired(keyEventReportRepaired.getEventId(), new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                DBHelper.getInstance().deleteReportRepaired(keyEventReportRepaired);
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
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

    public void uploadKeyEventTrace(String eventId, String status, int deskId, String matchLevel, final IUIDataHelperCallback callback) {
        String userId = SPStaticUtils.getString("user_open_id");
        uploadKeyEventTrace(userId, eventId, status, deskId, matchLevel, callback);
    }

    public void uploadKeyEventTrace(String userId, String eventId, String status, int deskId, String matchLevel, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        long timeStamp = System.currentTimeMillis();
        if ("transfer".equals(status) || "assignment".equals(status)) {
            ++timeStamp;
        }

        CloudClient.getInstance().uploadKeyEventTrace(eventId, timeStamp, userId, status, groupId, deskId, matchLevel, new ICloudCallback() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.i(TAG, "uploadKeyEventTrace fail: code = " + code);
                callback.onError(code);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(NetworkConst.ERR_CODE_NETWORK_FIAL);
            }
        });
    }

    public void getNoEndKeyEventCount(final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

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
        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");
        getEndKeyEventList(startTime, endTime, userId, callback);
    }

    public void getEndKeyEventList(long startTime, long endTime, String userId, final IUIDataHelperCallback callback) {
        String groupId = SPStaticUtils.getString("working_group_id");
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");

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
        String groupId = SPStaticUtils.getString("working_group_id");
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = "admin".equals(SPStaticUtils.getString("role")) ? "" : SPStaticUtils.getString("user_open_id");
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
        String groupId = SPStaticUtils.getString("working_group_id");
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = SPStaticUtils.getString("user_open_id");
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
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        String userId = SPStaticUtils.getString("user_open_id");
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
