package com.ctfww.module.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.Utils;
import com.ctfww.module.user.datahelper.airship.Airship;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBack;
    private TextView mTittle;
    private EditText mMobile;
    private Button mGetSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_forget_password_activity);
        initViews();

        Bundle bundle = this.getIntent().getExtras();
        String mobile = bundle.getString("mobile");
        mMobile.setText(mobile);

        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText("忘记密码");
        mMobile = findViewById(R.id.user_mobile_edit);
        mGetSms = findViewById(R.id.user_blue_btn);
        mGetSms.setText("获取验证码");
    }

    private void setOnClickListener() {
        mGetSms.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == mGetSms.getId()) { // 验证码登录
            if (!Utils.isValidMobileNum(mMobile.getText().toString())) {
                ToastUtils.showShort(R.string.user_toast_valid_mobile);
                return;
            }

            Intent intent = new Intent(this, SetPasswordActivity.class);
            intent.putExtra("mobile", mMobile.getText().toString());
            intent.putExtra("type", "login");

            startActivity(intent);

            finish();
        }
        else if (i == mBack.getId()) {
            Intent intent = new Intent(this, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("mobile", mMobile.getText().toString());
            bundle.putString("isSMSLogin", "false");
            intent.putExtras(bundle);

            startActivity(intent);

            finish();
        }
    }
}
