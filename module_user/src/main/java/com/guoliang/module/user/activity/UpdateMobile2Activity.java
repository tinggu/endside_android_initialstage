package com.guoliang.module.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.module.user.R;
import com.guoliang.module.user.datahelper.NetworkHelper;

public class UpdateMobile2Activity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UpdateMobile2Activity";

    private ImageView mBack;
    private TextView mTopBarTitlle;
    private EditText mMobile;
    private EditText mSms;
    private TextView mGetSms;
    private Button mBlueBtn;

    private String sms;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_update_mobile2_activity);
        initViews();

        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTopBarTitlle = findViewById(R.id.user_top_bar_tittle);
        mTopBarTitlle.setText("更换手机号");
        mMobile = findViewById(R.id.user_mobile_edit);
        mSms = findViewById(R.id.user_sms_edit);
        mGetSms = findViewById(R.id.user_get_sms);
        mBlueBtn = findViewById(R.id.user_blue_btn);
        mBlueBtn.setText("完成");
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mGetSms.setOnClickListener(this);
        mBlueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mGetSms.getId()) {
            timer.start();
            NetworkHelper.getInstance().sendSmsNum(mMobile.getText().toString(), new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    sms = obj == null ? "" : (String)obj;
                }

                @Override
                public void onError(int code) {

                }
            });
        }
        else if (id == mBlueBtn.getId()) {
            if (TextUtils.isEmpty(sms) || TextUtils.isEmpty(mSms.getText().toString())) {
                ToastUtils.showShort("验证码不能为空！");
                return;
            }

            if (!sms.equals(mSms.getText().toString())) {
                ToastUtils.showShort("验证码不对应！");
                return;
            }

            Intent intent = new Intent();
            intent.putExtra("mobile", mMobile.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    CountDownTimer timer = new CountDownTimer(90*1000, 1000) {
        @Override
        public void onTick(long l) {
            mGetSms.setText(getResources().getString(R.string.user_login_sms_input_count_down, l/1000));
        }

        @Override
        public void onFinish() {
            setPhoneNumEtEnable(true);
            mGetSms.setText(getResources().getString(R.string.user_login_sms_num_send_again));
            setGetSmsNumEnable(true);
        }
    };

    private void setPhoneNumEtEnable(boolean b) {
        if (b) {
            mMobile.setEnabled(true);
        }
        else {
            mMobile.setEnabled(false);
        }
    }

    private void setGetSmsNumEnable(boolean b) {
        if (b) {
            mGetSms.setTextColor(ContextCompat.getColor(this, R.color.color_btn_bg_01_100));
            mGetSms.setEnabled(true);
        }
        else {
            mGetSms.setTextColor(ContextCompat.getColor(this, R.color.color_edit_hint));
            mGetSms.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
