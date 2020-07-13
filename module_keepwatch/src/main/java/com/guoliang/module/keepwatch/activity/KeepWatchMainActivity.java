package com.guoliang.module.keepwatch.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.FileInfo;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.entity.MyPosition;
import com.guoliang.commonlib.location.GPSLocationListener;
import com.guoliang.commonlib.location.GPSLocationManager;
import com.guoliang.commonlib.location.GPSProviderStatus;
import com.guoliang.commonlib.location.MyLocation;
import com.guoliang.commonlib.utils.ApkUtils;
import com.guoliang.commonlib.utils.DialogUtils;
import com.guoliang.commonlib.utils.FileUtils;
import com.guoliang.commonlib.utils.PermissionUtils;
import com.guoliang.module.keepwatch.DataHelper.DBHelper;
import com.guoliang.module.keepwatch.DataHelper.LocationDataHelper;
import com.guoliang.module.keepwatch.DataHelper.SynData;
import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.Utils;
import com.guoliang.module.keepwatch.entity.KeepWatchDesk;
import com.guoliang.module.keepwatch.fragment.KeepWatchMyFragment;
import com.guoliang.module.keepwatch.fragment.KeepWatchSigninFragment;
import com.guoliang.module.keepwatch.fragment.KeepWatchStatisticsFragment;
import com.guoliang.module.upgrade.entity.ApkVersionInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Route(path = "/keepwatch/keepwatch")
public class KeepWatchMainActivity extends FragmentActivity implements View.OnClickListener {
    private final static String TAG = "KeepWatchMainActivity";

    private final static int REQUEST_CODE_ADD_SIGNIN_DESK = 1;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter; //ViewPager适配器
    private List<Fragment> mFragments;

    private LinearLayout mStatisticsTab;
    private LinearLayout mKeepWatchTab;
    private LinearLayout mMyTab;

    private ImageButton mStatisticsImg;
    private ImageButton mKeepWatchImg;
    private ImageButton mMyImg;

    private MyPosition mMyPos = new MyPosition();

    private GPSLocationManager mGPSLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepwatch_main_activity);
        upgrade();
        initViews();
        setOnClickListener();

        com.guoliang.module.fingerprint.Utils.init();
        refreshFingerprint();

        //注册一个eventbus
        EventBus.getDefault().register(this);

        // 开始刷新token
        int cnt = com.guoliang.module.tms.datahelper.NetworkHelper.getInstance().getCnt();
        LogUtils.i(TAG, "onCreate: cnt = " + cnt);
        if (cnt == 0) {
            com.guoliang.module.tms.datahelper.NetworkHelper.getInstance().startTimedRefresh();
        }
        else {
            EventBus.getDefault().post(new MessageEvent("tms_first_token"));
        }

        EventBus.getDefault().post(new MessageEvent("update_head"));

        mGPSLocationManager = GPSLocationManager.getInstances(this);
        mGPSLocationManager.start(new MyListener());
    }

    @Override
    protected void onResume() {
        PermissionUtils.requestLocationPermission(this);
        PermissionUtils.requestStoragePermission(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        com.guoliang.module.fingerprint.Utils.endScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.i(TAG, "onRequestPermissionsResult: grequestCode = " + requestCode);
        if (PermissionUtils.isReceivedLocationRsp(permissions, requestCode)) {
            if (!PermissionUtils.isLocationPermissionGranted(this)) {
                LogUtils.i(TAG, "使用该App必须同意定位权限！");
                ToastUtils.showShort("使用该App必须同意定位权限");
                finish();
            }
        }
        else if (PermissionUtils.isReceivedStorageRsp(permissions, requestCode)) {
            if (!PermissionUtils.isStoragePermissionGranted(this)) {
                LogUtils.i(TAG, "使用该App必须同意存取权限！");
                ToastUtils.showShort("使用该App必须同意存取权限");
                finish();
            }
        }
    }

//    private void getLocation() {
//        Location location = MyLocation.getLocation(this);
//        if (location != null) {
//            mMyPos = new MyPosition(location.getLatitude(), location.getLongitude(), location.getProvider());
//            LocationDataHelper.getInstance().startListenLocationUpdate(this);
//        }
//        else {
//            LogUtils.i(TAG, "getLocation: location is null");
//        }
//    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.keepwatch_viewpager);

        mStatisticsTab = (LinearLayout) findViewById(R.id.keepwatch_statistics);
        mKeepWatchTab = (LinearLayout) findViewById(R.id.keepwatch_keep_watch);
        mMyTab = (LinearLayout) findViewById(R.id.keepwatch_my);

        mStatisticsImg = (ImageButton) findViewById(R.id.keepwatch_statistics_image);
        mKeepWatchImg = (ImageButton) findViewById(R.id.keepwatch_keep_watch_image);
        mMyImg = (ImageButton) findViewById(R.id.keepwatch_my_image);

        //数据的初始化
        mFragments = new ArrayList<Fragment>();
        Fragment mTab_01 = new KeepWatchStatisticsFragment();
        Fragment mTab_02 = new KeepWatchSigninFragment();
        Fragment mTab_03 = new KeepWatchMyFragment();

        mFragments.add(mTab_01);
        mFragments.add(mTab_02);
        mFragments.add(mTab_03);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                //返回数据源的个数
                return mFragments.size();

            }

            @Override
            public Fragment getItem(int arg0) {
                // TODO Auto-generated method stub
                return mFragments.get(arg0);
            }
        };

        mViewPager.setAdapter(mAdapter);
        //监听内容区域的滑动效果
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                int currentItem  = mViewPager.getCurrentItem();
                resetImage();
                setSelectImage(currentItem);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        resetImage();
        setSelectImage(0);
        mViewPager.setCurrentItem(0);
    }

    private void setOnClickListener() {
        mStatisticsTab.setOnClickListener(this);
        mKeepWatchTab.setOnClickListener(this);
        mMyTab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mStatisticsTab.getId()) { // 添加签到点
            resetImage();
            setSelectImage(0);
            mViewPager.setCurrentItem(0);
        }
        else if (id == mKeepWatchTab.getId()) { // 上报事件
            resetImage();
            setSelectImage(1);
            mViewPager.setCurrentItem(1);
        }
        else if (id == mMyTab.getId()) {
            resetImage();
            setSelectImage(2);
            mViewPager.setCurrentItem(2);
        }
    }

    // 处理事件
    @Subscribe(threadMode= ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        LogUtils.i(TAG, "onGetMessage: messageEvent = " + messageEvent.toString());
        String msg = messageEvent.getMessage();
        if ("tms_first_token".equals(msg)) {
            Utils.setFirstToken(true);
            LogUtils.i(TAG, "onGetMessage: tms_first_token");
            checkUpgrade();
            com.guoliang.module.user.datahelper.DataHelper.getInstance().startTimedSyn();
            com.guoliang.module.keyevents.datahelper.SynData.startTimedSyn();
            SynData.getAllDesk();
            SynData.startTimedSyn();
        }
        else if ("update_location".equals(msg)) {
            mMyPos = GsonUtils.fromJson(messageEvent.getValue(), MyPosition.class);
            LogUtils.i(TAG, "update_location: myPosition = " + mMyPos.toString());
            EventBus.getDefault().post(new MessageEvent("location"));
        }
        else if ("bind_group".equals(msg)) {
            SynData.getAllDesk();
        }
    }

    // 独立线程处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onGetAsycMessage(MessageEvent messageEvent) {
        if ("upgrade".equals(messageEvent.getMessage())) {
            ApkVersionInfo apkVersionInfo = GsonUtils.fromJson(messageEvent.getValue(), ApkVersionInfo.class);
            File file = FileUtils.downloadFile(apkVersionInfo.getDownloadUrl(), this, "apk", "keepwatch.apk");
            if (file == null) {
                LogUtils.i(TAG, "FileUtils.downloadFile: 失败--" + messageEvent.getValue());
            }
            else {
                String fileName = getExternalFilesDir("") + "/apk" + "/keepwatch.apk";
                apkVersionInfo.setDownloadUrl(fileName);
                LogUtils.i("current", file.getAbsolutePath());
                SPStaticUtils.put("upgrade", GsonUtils.toJson(apkVersionInfo));
                LogUtils.i("current", "FileUtils.downloadFile: 成功--" + messageEvent.getValue());
            }
        }
        else if ("has_get_all_desk".equals(messageEvent.getMessage())) {
            LogUtils.i(TAG, "refreshFingerprint()");
            refreshFingerprint();
        }
        else if ("download_file".equals(messageEvent.getMessage())) {
            FileInfo fileInfo = GsonUtils.fromJson(messageEvent.getValue(), FileInfo.class);
            File file = FileUtils.downloadFile(fileInfo.getUrl(), this, fileInfo.getPath(), fileInfo.getName());
            if (file == null) {
                LogUtils.i(TAG, "FileUtils.downloadFile: 失败--" + messageEvent.getValue());
            }
            else {
                LogUtils.i("current", "FileUtils.downloadFile: 成功--" + messageEvent.getValue());
                messageEvent.setMessage("has_download_file");
                EventBus.getDefault().post(messageEvent);
            }
        }
    }

    private void checkUpgrade() {
        String apkName = ApkUtils.getPackageTailName(this);
        com.guoliang.module.upgrade.datahelper.NetworkHelper.getInstance().getLatestApkVersion(apkName, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                ApkVersionInfo apkVersionInfo = (ApkVersionInfo)obj;
                int versionCode = ApkUtils.getVersionCode(KeepWatchMainActivity.this);
                if (versionCode >= apkVersionInfo.getVersionCode()) {
                    LogUtils.i(TAG, "checkUpgrade: 目前是最新版本");
                    return;
                }

                EventBus.getDefault().post(new MessageEvent("upgrade", GsonUtils.toJson(apkVersionInfo)));
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "checkUpgrade: fail code = " + code);
            }
        });
    }

    private void upgrade() {
        String apkVersionInfoStr = SPStaticUtils.getString("upgrade");
        if (TextUtils.isEmpty(apkVersionInfoStr)) {
            return;
        }

        ApkVersionInfo apkVersionInfo = GsonUtils.fromJson(apkVersionInfoStr, ApkVersionInfo.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("发现新的下载版本：" + apkVersionInfo.getApkVersion() + "\n其新特征有：\n" + apkVersionInfo.getVersionDesc() + "\n要更新吗？");
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SPStaticUtils.remove("upgrade");
                ApkUtils.installApk(KeepWatchMainActivity.this, apkVersionInfo.getDownloadUrl());
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
//        LogUtils.i("current", this.getExternalFilesDir("").getAbsolutePath() + "/apk" + "/keepwatch.apk");
//        ApkUtils.installApk(KeepWatchActivity.this, this.getExternalFilesDir("").getAbsolutePath() + "/apk" + "/keepwatch.apk");
    }

    private void refreshFingerprint() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        List<Integer> idList = new ArrayList<Integer>();
        List<String> fingerPrintList = new ArrayList<>();
        List<KeepWatchDesk> deskList = DBHelper.getInstance().getDesk(groupId);
        for (int i = 0; i < deskList.size(); ++i) {
            KeepWatchDesk desk = deskList.get(i);
            idList.add(desk.getDeskId());
            fingerPrintList.add(desk.getFingerPrint());
        }

        com.guoliang.module.fingerprint.Utils.setDatabase(idList, fingerPrintList);
    }

    private void resetImage() {
        // TODO Auto-generated method stub
        mStatisticsImg.setImageResource(R.drawable.keepwatch_statistics_normal);
        mKeepWatchImg.setImageResource(R.drawable.keepwatch_keep_watch_normal);
        mMyImg.setImageResource(R.drawable.keepwatch_my_normal);
    }

    private void setSelectImage(int select) {
        switch (select) {
            case 0:
                mStatisticsImg.setImageResource(R.drawable.keepwatch_statistics_pressed);
                break;
            case 1:
                mKeepWatchImg.setImageResource(R.drawable.keepwatch_keep_watch_pressed);
                break;
            case 2:
                mMyImg.setImageResource(R.drawable.keepwatch_my_pressed);
                break;
            default:
                break;
        }
    }

    class MyListener implements GPSLocationListener {
        @Override
        public void UpdateLocation(Location location) {
            if (location != null) {
                mGPSLocationManager.setCurrLocation(location);
                if ("gps".equals(location.getProvider()) && location.getAccuracy() < 20.0f) {
                    mGPSLocationManager.setOptimalLocation(location);
                }

                LogUtils.i(TAG, "UpdateLocation: provider = " + location.getProvider() + ", lat = " + location.getLatitude() + ", lng = " + location.getLongitude() + ", timeStamp = " + location.getTime() + ", accu = " + location.getAccuracy());
            }
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {
//            if ("gps" == provider) {
//
//            }

            ToastUtils.showShort("UpdateStatus: provider = " + provider);

            LogUtils.i(TAG, "UpdateStatus: provider = " + provider + ", status = " + status);
        }

        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) {
            switch (gpsStatus) {
                case GPSProviderStatus.GPS_ENABLED:
                    ToastUtils.showShort("UpdateGPSProviderStatus: GPS开启");
                    LogUtils.i(TAG, "UpdateGPSProviderStatus: GPS开启");
                    break;
                case GPSProviderStatus.GPS_DISABLED:
                    ToastUtils.showShort("UpdateGPSProviderStatus: GPS关闭");
                    LogUtils.i(TAG, "UpdateGPSProviderStatus: GPS关闭");
                    break;
                case GPSProviderStatus.GPS_OUT_OF_SERVICE:
                    ToastUtils.showShort("UpdateGPSProviderStatus: GPS不可用");
                    LogUtils.i(TAG, "UpdateGPSProviderStatus: GPS不可用");
                    break;
                case GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE:
                    ToastUtils.showShort("UpdateGPSProviderStatus: GPS暂时不可用");
                    LogUtils.i(TAG, "UpdateGPSProviderStatus: GPS暂时不可用");
                    break;
                case GPSProviderStatus.GPS_AVAILABLE:
                    ToastUtils.showShort("UpdateGPSProviderStatus: GPS可用了");
                    LogUtils.i(TAG, "UpdateGPSProviderStatus: GPS可用了");
                    break;
            }
        }
    }
}
