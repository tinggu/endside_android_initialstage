package com.ctfww.module.keepwatch.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.Consts;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.location.GPSLocationManager;
import com.ctfww.commonlib.location.MyLocation;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.PermissionUtils;
import com.ctfww.module.fingerprint.entity.DistResult;
import com.ctfww.module.keepwatch.DataHelper.DBHelper;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;
import com.ctfww.module.keyevents.fragment.KeyEventReportFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

public class KeepWatchReportSigninActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "KeepWatchReportSigninActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mFinish;

    private TextView mMatchLevel;
    private TextView mDeskName;

    private RadioButton mNormal;
    private RadioButton mAbnormal;

    private Fragment mReportFragment;
    private LinearLayout mReportContentLL;

    private KeepWatchSigninInfo mKeepWatchSigninInfo;

    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepwatch_report_signin_activity);

        processIntent();
        initViews();

        setOnClickListener();

        EventBus.getDefault().register(this);

        if (!PermissionUtils.isCameraPermissionGranted(this)) {
            PermissionUtils.requestCameraPermission(this);
        }

        processLocation();
    }

    private void processIntent() {
        String str = getIntent().getStringExtra("signin");
        if (TextUtils.isEmpty(str)) {
            return;
        }

        mKeepWatchSigninInfo = GsonUtils.fromJson(str, KeepWatchSigninInfo.class);
        if ("qr".equals(mKeepWatchSigninInfo.getFinishType())) {
            String print = com.ctfww.module.fingerprint.Utils.getWifiCalculateFingerPrint();
            DistResult distResult = com.ctfww.module.fingerprint.Utils.getWifiDist(print, mKeepWatchSigninInfo.getDeskId());
            mKeepWatchSigninInfo.setMatchLevel(distResult.getStringMatchLevel());

            String wifiFingerPrintStr = com.ctfww.module.fingerprint.Utils.combineWifiStandardFingerPrint(print);
            String gpsFingerPrintStr = mLocation == null ? "" : com.ctfww.module.fingerprint.Utils.combineGpsStandardFingerPrint(mLocation);
            String fingerPrintStr = com.ctfww.module.fingerprint.Utils.appendOtherFingerPrint(wifiFingerPrintStr, gpsFingerPrintStr);
            mKeepWatchSigninInfo.setFingerPrint(fingerPrintStr);
        }

        SPStaticUtils.put("curr_desk_id", mKeepWatchSigninInfo.getDeskId());
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("上报签到");
        mFinish = findViewById(R.id.top_addition);
        mFinish.setVisibility(View.VISIBLE);
        mFinish.setText("完成");

        mMatchLevel = findViewById(R.id.keepwatch_match_level);
        showMatchLevel();
        mDeskName = findViewById(R.id.keepwatch_desk_name);

        mNormal = findViewById(R.id.keepwatch_select_normal);
        mAbnormal = findViewById(R.id.keepwatch_select_abnormal);

        mReportFragment = getSupportFragmentManager().findFragmentById(R.id.keyevent_report_fragment);
        mReportContentLL = mReportFragment.getView().findViewById(R.id.keyevent_report_all_ll);
        mReportContentLL.setVisibility(View.GONE);

        String groupId = SPStaticUtils.getString("working_group_id");
        KeepWatchDesk desk = DBHelper.getInstance().getDesk(groupId, mKeepWatchSigninInfo.getDeskId());
        if (desk != null) {
            mDeskName.setText("[" + desk.getDeskId() + "]" + "  " + desk.getDeskName());
        }
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mFinish.setOnClickListener(this);
        mNormal.setOnClickListener(this);
        mAbnormal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mFinish.getId()) {
            String status = getStatus();
            if (TextUtils.isEmpty(status)) {
                DialogUtils.onlyPrompt("请选择签到类型！", this);
                return;
            }

            mKeepWatchSigninInfo.setStatus(status);

            if (mAbnormal.isChecked()) {
                ((KeyEventReportFragment)mReportFragment).setDeskId(mKeepWatchSigninInfo.getDeskId());
                if (!((KeyEventReportFragment)mReportFragment).reportKeyEvent()) {
                    return;
                }
            }

            processSignin();
            finish();
        }
        else if (id == mNormal.getId()) {
            mReportContentLL.setVisibility(View.GONE);
        }
        else if (id == mAbnormal.getId()) {
            mReportContentLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("wifi_calculate_finger_print".equals(messageEvent.getMessage())) {
            String wifiFingerPrintStr = com.ctfww.module.fingerprint.Utils.combineWifiStandardFingerPrint(messageEvent.getValue());
            String gpsFingerPrintStr = mLocation == null ? "" : com.ctfww.module.fingerprint.Utils.combineGpsStandardFingerPrint(mLocation);
            String fingerPrintStr = com.ctfww.module.fingerprint.Utils.appendOtherFingerPrint(wifiFingerPrintStr, gpsFingerPrintStr);

            DistResult distResult = com.ctfww.module.fingerprint.Utils.getWifiDist(messageEvent.getValue(), mKeepWatchSigninInfo.getDeskId());
//            String aa = "[{\"mac\":\"e83f67db80fc\",\"rssi\":-88},{\"mac\":\"08107b0c38ef\",\"rssi\":-87},{\"mac\":\"08107b0c38eb\",\"rssi\":-87},{\"mac\":\"88bfe4089dd8\",\"rssi\":-90},{\"mac\":\"70192f80f810\",\"rssi\":-65},{\"mac\":\"50bd5f87d0cc\",\"rssi\":-76},{\"mac\":\"88bfe4089dd4\",\"rssi\":-59},{\"mac\":\"00e04cdbe8de\",\"rssi\":-71},{\"mac\":\"08107b0c38f2\",\"rssi\":-79},{\"mac\":\"88bfe4f89dd8\",\"rssi\":-92},{\"mac\":\"10327e821d24\",\"rssi\":-77},{\"mac\":\"10327e821d29\",\"rssi\":-75},{\"mac\":\"286c074e5dfe\",\"rssi\":-84},{\"mac\":\"34b20abb9e24\",\"rssi\":-86},{\"mac\":\"88bfe4089dd9\",\"rssi\":-65},{\"mac\":\"6cb749beb7c0\",\"rssi\":-77},{\"mac\":\"d88adcd45f80\",\"rssi\":-85},{\"mac\":\"b83a08197828\",\"rssi\":-86},{\"mac\":\"08107b0c38e9\",\"rssi\":-73},{\"mac\":\"a8ad3df367c8\",\"rssi\":-85},{\"mac\":\"94772b363190\",\"rssi\":-89},{\"mac\":\"7844fddf5989\",\"rssi\":-87},{\"mac\":\"e83f67b5d719\",\"rssi\":-87}]";
//            DistResult distResult = com.ctfww.module.fingerprint.Utils.getWifiDist(aa, 1500);
            LogUtils.i("aaaaaaaaaaaa", "distResult = " + distResult.toString());
            String level = distResult.getStringMatchLevel();
            if ("excellent".equals(level)) {
                mKeepWatchSigninInfo.setMatchLevel(level);
                mKeepWatchSigninInfo.setFingerPrint(fingerPrintStr);
            }
            else if ("good".equals(level) && !"excellent".equals(mKeepWatchSigninInfo.getMatchLevel())) {
                mKeepWatchSigninInfo.setMatchLevel(level);
                mKeepWatchSigninInfo.setFingerPrint(fingerPrintStr);
            }
            else if ("bad".equals(level) && !"excellent".equals(mKeepWatchSigninInfo.getMatchLevel()) && !"good".equals(mKeepWatchSigninInfo.getMatchLevel())) {
                mKeepWatchSigninInfo.setMatchLevel(level);
                mKeepWatchSigninInfo.setFingerPrint(fingerPrintStr);
            }
            else {
                mKeepWatchSigninInfo.setMatchLevel(level);
                mKeepWatchSigninInfo.setFingerPrint(fingerPrintStr);
            }

            showMatchLevel();
            LogUtils.i("aaaaaaaaaaaa", "deskId = " + mKeepWatchSigninInfo.getDeskId() + ", mWifiFingerPrintStr = " + messageEvent.getValue());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.i(TAG, "onRequestPermissionsResult: grequestCode = " + requestCode);
        if (PermissionUtils.isReceivedCameraRsp(permissions, requestCode)) {
            if (!PermissionUtils.isCameraPermissionGranted(this)) {
                LogUtils.i(TAG, "使用该App必须同意相机权限！");
                ToastUtils.showShort("使用该App必须同意相机权限！");
                finish();
            }
        }
    }

    private void showMatchLevel() {
        if ("excellent".equals(mKeepWatchSigninInfo.getMatchLevel())) {
            mMatchLevel.setBackgroundColor(0xFF7ED321);
            mMatchLevel.setText("匹配度：优");
        }
        else if ("good".equals(mKeepWatchSigninInfo.getMatchLevel())) {
            mMatchLevel.setBackgroundColor(0xFFFFC90E);
            mMatchLevel.setText("匹配度：良");
        }
        else if ("bad".equals(mKeepWatchSigninInfo.getMatchLevel())) {
            mMatchLevel.setBackgroundColor(0xFFF65066);
            mMatchLevel.setText("匹配度：差");
        }
        else {
            mMatchLevel.setBackgroundColor(Color.GRAY);
            mMatchLevel.setText("匹配度：无");
        }
    }

    private void processSignin() {
        NetworkHelper.getInstance().addKeepWatchSignin(mKeepWatchSigninInfo, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                ToastUtils.showShort("" + mKeepWatchSigninInfo.getDeskId() + "号点签到成功！");
                LogUtils.i(TAG, "processSignin: " + mKeepWatchSigninInfo.getDeskId() + "号点签到成功！");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "processSignin: fail, code = " + code);

                if (code == Consts.REST_FAIL) {
                    ToastUtils.showShort("签到点编号无效，签到失败！");
                }
                else if (code == Consts.REST_BELONG_FAIL) {
                    ToastUtils.showShort("这个签到点不属于你的任务！");
                }
                else {
                    ToastUtils.showShort("网络异常，进行了离线签到，统计信息在网络正常后刷新！");
                }
            }
        });
    }

    private String getStatus() {
        String ret = "";
        if (mNormal.isChecked()) {
            ret = "nomal";
        }
        else if (mAbnormal.isChecked()) {
            ret = "abnormal";
        }

        return ret;
    }

    private void processLocation() {
//        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isLocationEnabled()) {
        if (MyLocation.isGPSOPen(this)) {
            mLocation = MyLocation.getLocation(this);
            mLocation.setProvider("network");
            Location gpsLocation = GPSLocationManager.getInstances(this).getOptimalLocation();
            if (gpsLocation != null && System.currentTimeMillis() - gpsLocation.getTime() < 10000) {
                mLocation = gpsLocation;
            }
        }
        else {
            DialogUtils.selectDialog("添加签到点必须打开定位", this, new DialogUtils.Callback() {
                @Override
                public void onConfirm(int radioSelectItem) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancel() {
                    finish();
                }
            });
        }
    }
}
