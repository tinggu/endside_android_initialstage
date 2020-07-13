package com.guoliang.module.device.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.location.MyLocation;
import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.module.device.R;
import com.guoliang.module.device.network.datahelper.NetworkHelper;
import com.guoliang.module.device.storage.DatabaseUtil;
import com.guoliang.module.device.storage.table.DeviceInfo;

@Route(path = "/device/info")
public class DeviceInfoActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private final static String TAG = "DeviceInfoActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mDeviceIdEdit;
    private TextView mDeviceIdText;
    private TextView mDeviceGroupId;
    private TextView mDeviceType;
    private EditText mDeviceNameEdit;
    private TextView mDeviceNameText;
    private EditText mDeviceLngEdit;
    private TextView mDeviceLngText;
    private EditText mDeviceLatEdit;
    private TextView mDeviceLatText;
    private EditText mDeviceAddrEdit;
    private TextView mDeviceAddrText;
    private EditText mDeviceSimEdit;
    private TextView mDeviceSimText;
    private Button mFinish;
    private LinearLayout mModifyGroupId;
    private LinearLayout mModifyType;

    private TextView mModify;
    private String mType;

    private DeviceInfo mDeviceInfo = new DeviceInfo();
    private int mPos;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.device_info_activity);

        processIntent();

        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.device_back);
        mTittle = findViewById(R.id.device_top_bar_tittle);
        mDeviceIdEdit = findViewById(R.id.device_id_edit);
        mDeviceIdText = findViewById(R.id.device_id_text);
        mDeviceGroupId = findViewById(R.id.device_group_id_text);
        mDeviceType = findViewById(R.id.device_type_text);
        mDeviceNameEdit = findViewById(R.id.device_name_edit);
        mDeviceNameText = findViewById(R.id.device_name_text);
        mDeviceLngEdit = findViewById(R.id.device_lng_edit);
        mDeviceLngText = findViewById(R.id.device_lng_text);
        mDeviceLatEdit = findViewById(R.id.device_lat_edit);
        mDeviceLatText = findViewById(R.id.device_lat_text);
        mDeviceAddrEdit = findViewById(R.id.device_addr_edit);
        mDeviceAddrText = findViewById(R.id.device_addr_text);
        mDeviceSimEdit = findViewById(R.id.device_sim_edit);
        mDeviceSimText = findViewById(R.id.device_sim_text);
        mFinish = findViewById(R.id.device_blue_btn);
        mModify = findViewById(R.id.device_top_bar_right_btn);
        mModifyGroupId = findViewById(R.id.device_modify_group_id);
        mModifyType = findViewById(R.id.device_modify_type);

        mDeviceIdEdit.setText(mDeviceInfo.getDevId());
        mDeviceNameEdit.setText(mDeviceInfo.getDevName());
        mDeviceLngEdit.setText("" + mDeviceInfo.getLng());
        mDeviceLatEdit.setText("" + mDeviceInfo.getLat());
        mDeviceAddrEdit.setText(mDeviceInfo.getAddress());
        mDeviceSimEdit.setText(mDeviceInfo.getSim());
        mDeviceIdText.setText(mDeviceInfo.getDevId());
        mDeviceNameText.setText(mDeviceInfo.getDevName());
        mDeviceLngText.setText("" + mDeviceInfo.getLng());
        mDeviceLatText.setText("" + mDeviceInfo.getLat());
        mDeviceAddrText.setText(mDeviceInfo.getAddress());
        mDeviceSimText.setText(mDeviceInfo.getSim());
        mDeviceGroupId.setText("" + mDeviceInfo.getGroupId());

        if ("modify".equals(mType)) {
            mDeviceIdEdit.setVisibility(View.GONE);
            mDeviceNameEdit.setVisibility(View.GONE);
            mDeviceLngEdit.setVisibility(View.GONE);
            mDeviceLatEdit.setVisibility(View.GONE);
            mDeviceAddrEdit.setVisibility(View.GONE);
            mDeviceSimEdit.setVisibility(View.GONE);
            mDeviceIdText.setVisibility(View.VISIBLE);
            mDeviceNameText.setVisibility(View.VISIBLE);
            mDeviceLngText.setVisibility(View.VISIBLE);
            mDeviceLatText.setVisibility(View.VISIBLE);
            mDeviceAddrText.setVisibility(View.VISIBLE);
            mDeviceSimText.setVisibility(View.VISIBLE);

            mDeviceType.setText("电流监控");

            mTittle.setText("设备信息");

            mModify.setVisibility(View.VISIBLE);
            mModify.setText("编辑");

            mFinish.setVisibility(View.GONE);
        }
        else {
            mDeviceIdEdit.setVisibility(View.VISIBLE);
            mDeviceNameEdit.setVisibility(View.VISIBLE);
            mDeviceLngEdit.setVisibility(View.VISIBLE);
            mDeviceLatEdit.setVisibility(View.VISIBLE);
            Location location = MyLocation.getLocation(this);
            if (location != null) {
                mDeviceLngEdit.setText("" + location.getLongitude());
                mDeviceLatEdit.setText("" + location.getLatitude());
            }

            mDeviceAddrEdit.setVisibility(View.VISIBLE);
            mDeviceSimEdit.setVisibility(View.VISIBLE);
            mDeviceIdText.setVisibility(View.GONE);
            mDeviceNameText.setVisibility(View.GONE);
            mDeviceLngText.setVisibility(View.GONE);
            mDeviceLatText.setVisibility(View.GONE);
            mDeviceAddrText.setVisibility(View.GONE);
            if (location != null) {
                String addr = MyLocation.getAddr(this, location);
                mDeviceAddrEdit.setText(addr);
            }
            mDeviceSimText.setVisibility(View.GONE);

            mTittle.setText("增加设备");

            mModify.setVisibility(View.VISIBLE);

            mFinish.setVisibility(View.VISIBLE);
        }
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mTittle.setOnClickListener(this);
        mTittle.setText("增加设备");
        mModify.setOnClickListener(this);
        mDeviceGroupId.setOnClickListener(this);
        mDeviceType.setOnClickListener(this);
        mFinish.setOnClickListener(this);
        if (!"modify".equals(mType)) {
            mModifyGroupId.setOnClickListener(this);
            mModifyType.setOnClickListener(this);
        }

        mDeviceLatEdit.setOnLongClickListener(this);
        mDeviceLatText.setOnLongClickListener(this);
        mDeviceLngEdit.setOnLongClickListener(this);
        mDeviceLngText.setOnLongClickListener(this);
    }

    private void processIntent() {
        mType = this.getIntent().getStringExtra("type");
        if ("modify".equals(mType)) {
            mPos = this.getIntent().getIntExtra("pos", 0);
            Bundle bundle = this.getIntent().getBundleExtra("deviceInfo");
            mDeviceInfo = new DeviceInfo();
            mDeviceInfo.setDevId(bundle.getString("id"));
            mDeviceInfo.setAddress(bundle.getString("addr"));
            mDeviceInfo.setAddUserId(bundle.getString("userId"));
            mDeviceInfo.setDevName(bundle.getString("name"));
            mDeviceInfo.setSim(bundle.getString("sim"));
            mDeviceInfo.setLat(bundle.getFloat("lat"));
            mDeviceInfo.setLng(bundle.getFloat("lng"));
            mDeviceInfo.setAddTimestamp(bundle.getLong("lng"));
            mDeviceInfo.setDevType(bundle.getInt("type"));
            mDeviceInfo.setGroupId(bundle.getInt("groupId"));
        }
        else {
            Location location = MyLocation.getLocation(this);
            if (location != null) {
                mDeviceInfo.setLng((float)location.getLongitude());
                mDeviceInfo.setLat((float)location.getLatitude());
            }

            if (location != null) {
                String addr = MyLocation.getAddr(this, location);
                mDeviceInfo.setAddress(addr);
            }

            mDeviceInfo.setGroupId(0);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mModify.getId()) {
            mDeviceIdEdit.setVisibility(View.VISIBLE);
            mDeviceNameEdit.setVisibility(View.VISIBLE);
            mDeviceLngEdit.setVisibility(View.VISIBLE);
            mDeviceLatEdit.setVisibility(View.VISIBLE);
            mDeviceAddrEdit.setVisibility(View.VISIBLE);
            mDeviceSimEdit.setVisibility(View.VISIBLE);
            mDeviceIdText.setVisibility(View.GONE);
            mDeviceNameText.setVisibility(View.GONE);
            mDeviceLngText.setVisibility(View.GONE);
            mDeviceLatText.setVisibility(View.GONE);
            mDeviceAddrText.setVisibility(View.GONE);
            mDeviceSimText.setVisibility(View.GONE);

            mTittle.setText("增加设备");

            mModify.setVisibility(View.GONE);

            mFinish.setVisibility(View.VISIBLE);

            mModifyGroupId.setOnClickListener(this);
            mModifyType.setOnClickListener(this);
        }
        else if (id == mFinish.getId()) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDevId(mDeviceIdEdit.getText().toString());
            deviceInfo.setAddress(mDeviceAddrEdit.getText().toString());
            deviceInfo.setSim(mDeviceSimEdit.getText().toString());
            deviceInfo.setGroupId(GlobeFun.parseInt(mDeviceGroupId.getText().toString()));
            deviceInfo.setDevType(0);
            deviceInfo.setDevName(mDeviceNameEdit.getText().toString());
            deviceInfo.setAddUserId(mDeviceInfo.getAddUserId());
            deviceInfo.setLat(GlobeFun.parseFloat(mDeviceLatEdit.getText().toString()));
            deviceInfo.setLng(GlobeFun.parseFloat(mDeviceLngEdit.getText().toString()));

            if ("modify".equals(mType)) {
                DatabaseUtil.getInstance().updateDevice(deviceInfo);
                NetworkHelper.getInstance().updateDevice(deviceInfo, new IUIDataHelperCallback() {
                    @Override
                    public void onSuccess(Object obj) {

                    }

                    @Override
                    public void onError(int code) {

                    }
                });

                Intent intent = new Intent();
                intent.putExtra("deviceId", deviceInfo.getDevId());
                intent.putExtra("pos", mPos);
                setResult(RESULT_OK, intent);

                finish();
            }
            else {
                DatabaseUtil.getInstance().addDevice(deviceInfo);
                NetworkHelper.getInstance().addDevice(deviceInfo, new IUIDataHelperCallback() {
                    @Override
                    public void onSuccess(Object obj) {

                    }

                    @Override
                    public void onError(int code) {

                    }
                });

                Intent intent = new Intent();
                intent.putExtra("deviceId", deviceInfo.getDevId());
                setResult(RESULT_OK, intent);

                finish();
            }
        }
        else if (id == mModifyGroupId.getId()) {
            ToastUtils.showShort("目前群组编号暂定为0，不支持修改！");
        }
        else if (id == mModifyType.getId()) {
            ToastUtils.showShort("目前设备类型只有电流检测");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        if (id == mDeviceLatEdit.getId()
        || id == mDeviceLatText.getId()
        || id == mDeviceLngEdit.getId()
        || id == mDeviceLngText.getId()) {
            float lat = 0.0f;
            float lng = 0.0f;
            if (mDeviceLatEdit.getVisibility() == View.VISIBLE) {
                lat = GlobeFun.parseFloat(mDeviceLatEdit.getText().toString());
                lng = GlobeFun.parseFloat(mDeviceLngEdit.getText().toString());
            }
            else {
                lat = GlobeFun.parseFloat(mDeviceLatText.getText().toString());
                lng = GlobeFun.parseFloat(mDeviceLngText.getText().toString());
            }
            SPStaticUtils.put("currLat", lat);
            SPStaticUtils.put("currLng", lng);

            ARouter.getInstance().build("/map/baidumap").navigation();
        }

        return true;
    }
}
