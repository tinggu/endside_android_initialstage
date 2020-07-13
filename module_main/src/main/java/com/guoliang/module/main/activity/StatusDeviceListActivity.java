package com.guoliang.module.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.module.main.R;
import com.guoliang.module.main.adapter.DeviceElectricAdapter;
import com.guoliang.module.main.storage.table.DeviceElectricInfo;

import java.util.ArrayList;
import java.util.List;

public class StatusDeviceListActivity extends AppCompatActivity {

    private static final String TAG = "StatusDeviceListActivit";

    private RecyclerView mDeviceRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_device_list);

        initViews();

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mDeviceRecyclerView.setLayoutManager(layoutManager);

        DeviceElectricAdapter deviceElectricAdapter = new DeviceElectricAdapter(getRecyclerViewData(), new DeviceElectricAdapter.DeviceElectricCallback() {
            @Override
            public void onItemClick() {
                startActivity(new Intent(StatusDeviceListActivity.this, DeviceElectricInfoActivity.class));
            }
        });

        mDeviceRecyclerView.setAdapter(deviceElectricAdapter);
    }

    private List<DeviceElectricInfo> getRecyclerViewData() {
        List<DeviceElectricInfo> deviceElectricInfoList = new ArrayList<>();

        deviceElectricInfoList.add(new DeviceElectricInfo("设备ID: 01010211", "名称: C0Y6V6TH", 1,2,4,6,0.4f));
        deviceElectricInfoList.add(new DeviceElectricInfo("设备ID: 01010212", "名称: C0E6V6TH", 1,0.7f,4,6,0.4f));
        deviceElectricInfoList.add(new DeviceElectricInfo("设备ID: 01010213", "名称: C0Y6V6TH", 1.5f,2,4,6,0.4f));
        deviceElectricInfoList.add(new DeviceElectricInfo("设备ID: 01010214", "名称: C0Y4V6TH", 1,3.4f,4,6,0.4f));
        deviceElectricInfoList.add(new DeviceElectricInfo("设备ID: 01010215", "名称: C0Y6V6TH", 1,2,4,2.1f,0.4f));
        deviceElectricInfoList.add(new DeviceElectricInfo("设备ID: 01010216", "名称: C0Y6V8TH", 1,1.6f,4,6,0.4f));
        deviceElectricInfoList.add(new DeviceElectricInfo("设备ID: 01010217", "名称: C0Y6W6TH", 1,2,2.4f,6,0.5f));
        deviceElectricInfoList.add(new DeviceElectricInfo("设备ID: 01010218", "名称: A0Y6V6TH", 1.9f,2,4,6,0.7f));

        return deviceElectricInfoList;
    }

    private void initViews() {
        findViewById(R.id.main_status_device_list_back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView title = findViewById(R.id.main_status_device_list_title);
        title.setText(getString(R.string.status_device_list_page_title));

        mDeviceRecyclerView = findViewById(R.id.status_device_list);
    }
}
