package com.guoliang.module.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.guoliang.commonlib.base.BaseActivity;
import com.guoliang.commonlib.location.MyLocation;
import com.guoliang.commonlib.utils.PermissionUtils;
import com.guoliang.module.main.R;
import com.guoliang.module.main.view.DeviceMonitorLineChart;

@Route(path = "/main/main")
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private ProgressBar mProgressBarNormal;
    private ProgressBar mProgressBarRepair;
    private ProgressBar mProgressBarAlarm;
    private ProgressBar mProgressBarClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_main);

        PermissionUtils.requestLocationPermission(this);

        setNickname();

        initViews();

        initClickListener();

        setLineChartData();
    }

    private void setLineChartData() {
        DeviceMonitorLineChart lineChart = findViewById(R.id.main_page_line_chart);
        lineChart.initLineChart();
        lineChart.setData();
    }

    private void setNickname() {
        TextView nickNameTxt = findViewById(R.id.main_user_nickName);
        if (getIntent().getExtras() != null) {
            nickNameTxt.setText(getIntent().getExtras().getString("nickName"));
        }
    }

    private void initClickListener() {
        findViewById(R.id.main_setting_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/setting/setting").navigation();
            }
        });

        findViewById(R.id.main_add_device_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/device/info").navigation();
            }
        });

        mProgressBarNormal.setOnClickListener(this);
        mProgressBarRepair.setOnClickListener(this);
        mProgressBarAlarm.setOnClickListener(this);
        mProgressBarClose.setOnClickListener(this);
    }

    private void initViews() {
        mProgressBarNormal = findViewById(R.id.main_page_circle_progress_bar_normal);
        mProgressBarRepair = findViewById(R.id.main_page_circle_progress_bar_repair);
        mProgressBarAlarm = findViewById(R.id.main_page_circle_progress_bar_alarm);
        mProgressBarClose = findViewById(R.id.main_page_circle_progress_bar_close);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.main_page_circle_progress_bar_normal) {
            startActivity(new Intent(this, StatusDeviceListActivity.class));
        }

        if (id == R.id.main_page_circle_progress_bar_repair) {
            startActivity(new Intent(this, StatusDeviceListActivity.class));
        }

        if (id == R.id.main_page_circle_progress_bar_alarm) {
            startActivity(new Intent(this, StatusDeviceListActivity.class));
        }

        if (id == R.id.main_page_circle_progress_bar_close) {
            startActivity(new Intent(this, StatusDeviceListActivity.class));
        }
    }
}
