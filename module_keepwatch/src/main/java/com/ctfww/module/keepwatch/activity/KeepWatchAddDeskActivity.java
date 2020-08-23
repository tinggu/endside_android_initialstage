package com.ctfww.module.keepwatch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.location.GPSLocationManager;
import com.ctfww.commonlib.location.MyLocation;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.keepwatch.datahelper.airship.Airship;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = "/keepwatch/addDesk")
public class KeepWatchAddDeskActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "KeepWatchAddDeskActivity";

    private ImageView mBack;
    private TextView mTittle;
    private EditText mDeskId;
    private EditText mDeskName;
    private TextView mDeskAddress;
    private LinearLayout mLocationLL;
    private TextView mLatLng;
    private TextView mConfirm;
    private ImageView mQr;

    private Location mLocation;
    private String mWifiFingerPrintStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.keepwatch_add_desk_activity);

        initViews();

        setOnClickListener();

        EventBus.getDefault().register(this);

        processLocation();
        com.ctfww.module.fingerprint.Utils.startScan("create");

        Airship.getInstance().synKeepWatchDeskFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("添加签到点");
        mDeskId = findViewById(R.id.keepwatch_desk_id);
        mDeskName = findViewById(R.id.keepwatch_desk_name);
        mDeskAddress = findViewById(R.id.keepwatch_desk_address);
        mLocationLL = findViewById(R.id.keepwatch_location_ll);
        mLatLng = findViewById(R.id.keepwatch_lat_lng);
        mConfirm = findViewById(R.id.keepwatch_confirm);
        mQr = findViewById(R.id.keepwatch_qr);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mLocationLL.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mQr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mLocationLL.getId()) {
            if (mLocation == null) {
                return;
            }

            Intent intent = new Intent(this, ViewMapActivity.class);
            intent.putExtra("type", "center");
            intent.putExtra("lat", mLocation.getLatitude());
            intent.putExtra("lng", mLocation.getLongitude());
            startActivity(intent);
        }
        else if (id == mConfirm.getId()) {
            String deskIdStr = mDeskId.getText().toString();
            if (TextUtils.isEmpty(deskIdStr)) {
                DialogUtils.onlyPrompt("请输入编号", this);
                return;
            }

            int deskId = GlobeFun.parseInt(mDeskId.getText().toString());
            if (deskId <= 0) {
                DialogUtils.onlyPrompt("编号都需要时大于0的数字", this);
                return;
            }

            String nameStr = mDeskName.getText().toString();
            if (TextUtils.isEmpty(nameStr)) {
                DialogUtils.onlyPrompt("请输入签到点名称", this);
                return;
            }

            String groupId = SPStaticUtils.getString("working_group_id");
            KeepWatchDesk desk = DBHelper.getInstance().getKeepWatchDesk(groupId, deskId);
            if (desk != null) {
                DialogUtils.onlyPrompt("在一个群里面不能创建重复编号的签到点！", this);
                return;
            }

            mConfirm.setEnabled(false);
            addDesk();
        }
        else if (id == mQr.getId()) {
            String deskIdStr = mDeskId.getText().toString();
            if (TextUtils.isEmpty(deskIdStr)) {
                ToastUtils.showShort("必须有签到点编号！");
                return;
            }

            int deskId = GlobeFun.parseInt(deskIdStr);
            Intent intent = new Intent(this, KeepWatchQrActivity.class);
            intent.putExtra("desk_id", deskId);
            intent.putExtra("desk_name", mDeskName.getText().toString());
            startActivity(intent);
        }

    }

    // 处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("wifi_finger_print".equals(messageEvent.getMessage())) {
            mWifiFingerPrintStr = messageEvent.getValue();
        }
    }

//    private void addSigninDesk(final int deskId, final String deskName, final String address, double lat, double lng, String finishType, String fingerPrint) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("此处没有GPS信号，也没有精确的室内定位，只能创建扫码或NFC签到点，要创建吗？");
//        builder.setCancelable(true);
//
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                _addSigninDesk(deskId, deskName, address, lat, lng, finishType, fingerPrint);
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void addDesk() {
        KeepWatchDesk desk = new KeepWatchDesk();
        desk.setGroupId(SPStaticUtils.getString("working_group_id"));
        desk.setDeskId(GlobeFun.parseInt(mDeskId.getText().toString()));
        desk.setDeskName(mDeskName.getText().toString());
        desk.setDeskAddress(mDeskAddress.getText().toString());
        desk.setLat(mLocation.getLatitude());
        desk.setLng(mLocation.getLongitude());
        desk.setDeskType(getDeskType());
        String gpsFingerPrintStr = com.ctfww.module.fingerprint.Utils.combineGpsStandardFingerPrint(mLocation);
        String fingerPrintStr = com.ctfww.module.fingerprint.Utils.appendOtherFingerPrint(mWifiFingerPrintStr, gpsFingerPrintStr);
        desk.setFingerPrint(fingerPrintStr);
        desk.setSynTag("new");

        if (!DBHelper.getInstance().addKeepWatchDesk(desk)) {
            DialogUtils.onlyPrompt("该群组中该点号已经存在，请填写新的点号！", this);
            return;
        }

        Airship.getInstance().synKeepWatchDeskToCloud();

        finish();
    }

    private boolean isExistNFC() {
        PackageManager packageManager = this.getPackageManager();

        return packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    private String getDeskType() {
        return "else";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            _processLocation(mLocation);
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

    private void _processLocation(Location location) {
        if (mLocation != null) {
//            String locationPrompt = "gps".equals(mLocation.getProvider()) ? "GPS精确定位，" : "网络概要定位，";
            mLatLng.setText("纬度：" + GlobeFun.degree2DegreeMinuteSecond(mLocation.getLatitude())
                    + "、" + "经度：" + GlobeFun.degree2DegreeMinuteSecond(mLocation.getLongitude()));

            String addr = MyLocation.getAddr(this, mLocation);
            mDeskAddress.setText(addr);
            LogUtils.i("bbbbbbbbbbbb", "_processLocationa; provider = " + mLocation.getProvider() + ", lat = " + mLocation.getLatitude() + ", lng = " + mLocation.getLongitude());
        }
        else {
            ToastUtils.showShort("获取不到定位，不能完成建点！");
            finish();
        }
    }


}
