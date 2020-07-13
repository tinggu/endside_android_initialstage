package com.ctfww.module.device.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.device.R;
import com.ctfww.module.device.adapter.DeviceAdapter;
import com.ctfww.module.device.storage.DatabaseUtil;
import com.ctfww.module.device.storage.table.DeviceInfo;

import java.util.List;

@Route(path = "/device/list")
public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "DeviceListActivity";

    private final static int REQUEST_CODE_DEVICE_INFO = 1;

    private ImageView mBack;
    private TextView mTittle;
    private TextView mAdd;
    private RecyclerView mDeviceListView;
    private List<DeviceInfo> mDeviceList;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.device_info_list_activity);

        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.device_back);
        mTittle = findViewById(R.id.device_top_bar_tittle);
        mTittle.setText("设备列表");
        mAdd = findViewById(R.id.device_top_bar_right_btn);
        mAdd.setText("增加");
        mDeviceListView = findViewById(R.id.device_list);
        if (mDeviceListView == null) {
            LogUtils.i(TAG, "mDeviceList = null");
        }

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mDeviceListView.setLayoutManager(layoutManager);

        mDeviceList = DatabaseUtil.getInstance().queryAllDevice();
        DeviceAdapter deviceAdapter = new DeviceAdapter(mDeviceList, new DeviceAdapter.IDeviceInfoActivityCallback() {
            @Override
            public void start(int pos) {
                DeviceInfo deviceInfo = mDeviceList.get(pos);
                startDeviceInfoActivity(pos, deviceInfo);
            }
        });
        mDeviceListView.setAdapter(deviceAdapter);
    }

    private void startDeviceInfoActivity(int pos, DeviceInfo deviceInfo) {
        Intent intent = new Intent(this, DeviceInfoActivity.class);
        intent.putExtra("type", "modify");
        intent.putExtra("pos", pos);

        Bundle bundle = new Bundle();
        bundle.putString("id", deviceInfo.getDevId());
        bundle.putString("addr", deviceInfo.getAddress());
        bundle.putString("userId", deviceInfo.getAddUserId());
        bundle.putString("name", deviceInfo.getDevName());
        bundle.putString("sim", deviceInfo.getSim());
        bundle.putDouble("lat", deviceInfo.getLat());
        bundle.putDouble("lng", deviceInfo.getLng());
        bundle.putLong("utcTime", deviceInfo.getAddTimestamp());
        bundle.putInt("type", deviceInfo.getDevType());
        bundle.putInt("groupId", deviceInfo.getGroupId());

        intent.putExtra("deviceInfo", bundle);

        startActivityForResult(intent, REQUEST_CODE_DEVICE_INFO);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mAdd.getId()) {
            Intent intent = new Intent(this, DeviceInfoActivity.class);
            startActivityForResult(intent, REQUEST_CODE_DEVICE_INFO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DEVICE_INFO) {
            if (resultCode != RESULT_OK) {
                return;
            }

            String deviceId = data.getStringExtra("deviceId");
            if (TextUtils.isEmpty(deviceId)) {
                return;
            }

            DeviceInfo deviceInfo = DatabaseUtil.getInstance().queryDeviceByUserId(deviceId);
            if (deviceInfo == null) {
                return;
            }

            int pos = data.getIntExtra("pos", -1);
            if (pos == -1) {
                mDeviceList.add(deviceInfo);
            }
            else {
                mDeviceList.set(pos, deviceInfo);
            }

            mDeviceListView.getAdapter().notifyDataSetChanged();
        }
    }
}
