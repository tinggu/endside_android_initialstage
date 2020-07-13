package com.guoliang.module.setting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.module.setting.R;

public class AboutAppActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "AboutAppActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app_activity);

        initViews();

        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.setting_back);
        mTittle = findViewById(R.id.setting_top_bar_tittle);
        mTittle.setText("关于控机宝");
        mVersion = findViewById(R.id.setting_curr_version);
        mVersion.setText("KGB " + GlobeFun.getAppVersion(this));
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
