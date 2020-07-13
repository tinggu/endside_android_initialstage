package com.guoliang.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.module.keepwatch.R;

public class KeepWatchAboutAppActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "KeepWatchAboutAppActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepwatch_about_app_activity);

        initViews();

        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("关于小松巡检");
        mVersion = findViewById(R.id.setting_curr_version);
        mVersion.setText("KeepWatch " + GlobeFun.getAppVersion(this));
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
    }
}
