package com.ctfww.module.keyevents.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.fragment.KeyEventReportFragment;

@Route(path = "/keyevents/reportKeyEvent")
public class ReportKeyEventActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "ReportKeyEventActivity";
    

    private int mType = 0; // 暂不区分关键事件类型
    private ImageView mBack;
    private TextView mTittle;
    private TextView mReport;
    private KeyEventReportFragment mReportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyevents_report_activity);
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("上报事件");
        mReport = findViewById(R.id.top_addition);
        mReport.setText("上报");
        mReport.setVisibility(View.VISIBLE);

        mReportFragment = (KeyEventReportFragment)(getSupportFragmentManager().findFragmentById(R.id.keyevent_report_fragment));
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mReport.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: enter");
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mReport.getId()) { // 上报事件
            mReportFragment.reportKeyEvent();
        }
    }
}
