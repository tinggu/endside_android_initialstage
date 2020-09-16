package com.ctfww.module.keepwatch.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.activity.ScanActivity;
import com.ctfww.module.signin.entity.SigninInfo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.location.GPSLocationManager;
import com.ctfww.commonlib.location.MyLocation;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.QRCodeUtils;
import com.ctfww.module.fingerprint.entity.DistResult;
import com.ctfww.module.keepwatch.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class KeepWatchSigninFragment extends Fragment {
    private final static String TAG = "KeepWatchSigninFragment";

    private View mV;

    private RelativeLayout mGPSSignin;
    private RelativeLayout mScanSignin;
    private RelativeLayout mNfcSignin;

    private NfcAdapter nfcAdapter;
    private Location mLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_signin_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return mV;
    }

    private void initViews(View v) {
        mGPSSignin = v.findViewById(R.id.keepwatch_gps_sign_in);
        mScanSignin = v.findViewById(R.id.keepwatch_scan_signin);
        mNfcSignin = v.findViewById(R.id.keepwatch_nfc_signin);

        mLocation = MyLocation.getLocation(v.getContext());
    }

    private void setOnClickListener() {
        mGPSSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LocationManager locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
//                if (locationManager.isLocationEnabled()) {
                if (MyLocation.isGPSOPen(getContext())) {
                    Location gpsLocation = GPSLocationManager.getInstances(getActivity()).getCurrLocation();
                    if (gpsLocation == null || System.currentTimeMillis() - gpsLocation.getTime() > 10000) {
                        DialogUtils.onlyPrompt("GPS不能定位，请选择其他签到方式！", v.getContext());
                        return;
                    }

                    DistResult distResult = com.ctfww.module.fingerprint.Utils.getGpsId(gpsLocation);
                    if (distResult.getLevel() > 2) {
                        DialogUtils.onlyPrompt("附近没有可用GPS匹配能签到的点，请选择其他签到方式！", v.getContext());
                        return;
                    }

                    LogUtils.i(TAG, "distResult = " + distResult.toString());
                    SigninInfo signin = new SigninInfo();
                    signin.setObjectId(distResult.getId());
                    signin.setFinishType("gps");
                    signin.setType("desk");
                    signin.setMatchLevel(distResult.getStringMatchLevel());
                    ARouter.getInstance().build("/keepwatch/reportSignin")
                            .withString("signin", GsonUtils.toJson(signin))
                            .navigation();
                }
                else {
                    DialogUtils.selectDialog("要签到，必须打开定位！", getContext(), new DialogUtils.Callback() {
                        @Override
                        public void onConfirm(int radioSelectItem) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }
        });

        mScanSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                LogUtils.i(TAG, "qr step 1: id == mScanSignin.getId()");
            }
        });

        mNfcSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!isExistNFC()) {
//                    ToastUtils.showShort("该手机不支持NFC");
//                    return;
//                }

                ToastUtils.showShort("暂不开放NFC签到功能");
            }
        });
    }

    private boolean isExistNFC() {
        PackageManager packageManager = mV.getContext().getPackageManager();

        return packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
//        boolean b2 = packageManager
//                .hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION);
//        Toast.makeText(context, "是否支持hce===" + b2, 1).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            int deskId = QRCodeUtils.getQrDeskId(result.getContents(), getContext());
            if (deskId != 0) {
                com.ctfww.module.fingerprint.Utils.startScan("calc");

                SigninInfo signin = new SigninInfo();
                signin.setObjectId(deskId);
                signin.setFinishType("qr");
                signin.setType("desk");
                ARouter.getInstance().build("/keepwatch/reportSignin")
                        .withString("signin", GsonUtils.toJson(signin))
                        .navigation();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
            ToastUtils.showShort("no data");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    private String flipHexStr(String s){
        StringBuilder  result = new StringBuilder();
        for (int i = 0; i <=s.length()-2; i=i+2) {
            result.append(new StringBuilder(s.substring(i,i+2)));
        }
        return result.toString();
    }
    public String getTag(Intent intent) {//处理tag
        //获取到卡对象
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //获取卡id这里即uid
        byte[] aa = tagFromIntent.getId();
        String str = ByteArrayToHexString(aa);
        str = flipHexStr(str);

        ToastUtils.showShort(str);

        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    // 处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onGetMessage(MessageEvent messageEvent) {

    }
}