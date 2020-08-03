package com.ctfww.module.attend.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ctfww.commonlib.activity.ScanActivity;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.location.GPSLocationManager;
import com.ctfww.commonlib.location.MyLocation;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.QRCodeUtils;
import com.ctfww.module.attend.R;
import com.ctfww.module.attend.entity.AttendSigninInfo;
import com.ctfww.module.fingerprint.entity.DistResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class AttendSigninFragment extends Fragment {
    private final static String TAG = "AttendSigninFragment";

    private View mV;

    private RelativeLayout mInformationRL;
    private TextView mUnreadCount;
    private LinearLayout mGroupLL;
    private ImageView mGroupSelect;
    private TextView mGroupName;

    private RelativeLayout mGPSSignin;
    private RelativeLayout mScanSignin;
    private RelativeLayout mNfcSignin;

    private Location mLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.attend_signin_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return mV;
    }

    private void initViews(View v) {
        mInformationRL = v.findViewById(R.id.top_information_rl);
        mUnreadCount = v.findViewById(R.id.information_unread_count);
        mGroupLL = v.findViewById(R.id.top_tittle_ll);
        mGroupSelect = v.findViewById(R.id.top_select);
        mGroupSelect.setVisibility(View.VISIBLE);
        mGroupName = v.findViewById(R.id.top_tittle);
        String groupName = SPStaticUtils.getString("working_group_name");
        if (TextUtils.isEmpty(groupName)) {
            mGroupName.setText("请选择群组");
        }
        else {
            mGroupName.setText(groupName);
        }

        mGPSSignin = v.findViewById(R.id.keepwatch_gps_sign_in);
        mScanSignin = v.findViewById(R.id.keepwatch_scan_signin);
        mNfcSignin = v.findViewById(R.id.keepwatch_nfc_signin);
    }

    private void setOnClickListener() {
        mInformationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/invite").navigation();
            }
        });

        mGroupLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/selectGroup").navigation();
            }
        });

        mGPSSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    AttendSigninInfo attendSigninInfo = new AttendSigninInfo();
                    attendSigninInfo.setDeskId(distResult.getId());
                    attendSigninInfo.setMatchLevel(distResult.getStringMatchLevel());
//                    Intent intent = new Intent(getContext(), KeepWatchReportSigninActivity.class);
//                    intent.putExtra("signin", GsonUtils.toJson(attendSigninInfo));
//                    startActivity(intent);
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

    private int mCurrDeskId = 0;
    private String mSigninType = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            int deskId = QRCodeUtils.getQrDeskId(result.getContents(), getContext());
            if (deskId != 0) {
                com.ctfww.module.fingerprint.Utils.startScan("calc");
                AttendSigninInfo attendSigninInfo = new AttendSigninInfo();
                attendSigninInfo.setDeskId(deskId);
//                Intent intent = new Intent(getContext(), KeepWatchReportSigninActivity.class);
//                intent.putExtra("signin", GsonUtils.toJson(keepWatchSigninInfo));
//                startActivity(intent);
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
        String groupName = SPStaticUtils.getString("working_group_name");
        if (TextUtils.isEmpty(groupName)) {
            mGroupName.setText("请选择群组");
        }
        else {
            mGroupName.setText(groupName);
        }

        LogUtils.i(TAG, "onResume()");
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
        if ("tms_first_token".equals(messageEvent.getMessage())) {

        }
        else if ("bind_group".equals(messageEvent.getMessage())) {
            String groupName = SPStaticUtils.getString("working_group_name");
            if (TextUtils.isEmpty(groupName)) {
                mGroupName.setText("请选择群组");
            }
            else {
                mGroupName.setText(groupName);
            }
        }
    }
}