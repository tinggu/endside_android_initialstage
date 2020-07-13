package com.guoliang.module.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.network.NetworkConst;
import com.guoliang.module.user.R;
import com.guoliang.module.user.datahelper.DataHelper;
import com.guoliang.module.user.datahelper.NetworkHelper;
import com.guoliang.module.user.datahelper.DBHelper;
import com.guoliang.module.user.entity.UserInfo;

public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "SetPasswordActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mGetSms;
    private EditText mSms;
    private EditText mPassword;
    private Button mFinish;
    private TextView mMobile;

    private String mType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_set_password_activity);
        initViews();

        mType = this.getIntent().getStringExtra("type");
        String mobile = this.getIntent().getStringExtra("mobile");
        mMobile.setText(mobile);

        sendSmsNum();

        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText("设置密码");
        mGetSms = findViewById(R.id.user_get_sms);
        mSms = findViewById(R.id.user_sms_edit);
        mPassword = findViewById(R.id.user_password_edit);
        mFinish = findViewById(R.id.user_blue_btn);
        mMobile = findViewById(R.id.user_mobile_num_text);

    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mGetSms.setOnClickListener(this);
        mFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == mBack.getId()) {
            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("mobile", mMobile.getText().toString());
            intent.putExtras(bundle);

            startActivity(intent);

            finish();
        }
        else if (i == mGetSms.getId()) { // 获取短信验证码
            sendSmsNum();
        }
        else if (i == mFinish.getId()) { // 完成
            KeyboardUtils.hideSoftInput(this);

            if (!DataHelper.getInstance().isValidSMS(mSms.getText().toString()) || !DataHelper.getInstance().isValidPassword(mPassword.getText().toString())) {
                ToastUtils.showShort(R.string.user_toast_valid_mobile_or_sms);
                return;
            }

            LogUtils.i(TAG, "type = " + mType);
            if ("login".equals(mType)){
                loginAndSetPassword();
            }
            else if ("modify".equals(mType)) {
                Intent intent = new Intent();
                intent.putExtra("password", mPassword.getText().toString());
                LogUtils.i(TAG, "password = " + mPassword.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    CountDownTimer timer = new CountDownTimer(90*1000, 1000) {
        @Override
        public void onTick(long l) {
            mGetSms.setText(getResources().getString(R.string.user_login_sms_input_count_down, l/1000));
        }

        @Override
        public void onFinish() {
            mGetSms.setText(getResources().getString(R.string.user_login_sms_num_send_again));
            setGetSmsNumEnable(true);
        }
    };

    private void sendSmsNum() {
        KeyboardUtils.hideSoftInput(this);
        NetworkHelper.getInstance().sendSmsNum(mMobile.getText().toString(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                ToastUtils.showShort(R.string.user_toast_have_send_sms);

                mGetSms.setText(getResources().getString(R.string.user_login_sms_input_count_down, 90));
                mGetSms.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_edit_hint));

                // 开启倒计时
                timer.start();
            }

            @Override
            public void onError(int code) {
                setGetSmsNumEnable(true);
                switch (code) {
                    case NetworkConst.ERR_CODE_NETWORK_FIAL:
                        ToastUtils.showShort(R.string.user_toast_check_network);
                        break;
                    default:
                        ToastUtils.showShort(R.string.user_toast_cloud_error);
                        break;
                }

            }
        });

    }

    private void loginAndSetPassword() {
        NetworkHelper.getInstance().loginBySMS(mMobile.getText().toString(), mSms.getText().toString(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                userInfo.setPassword(mPassword.getText().toString());
                userInfo.setModifyTimestamp(System.currentTimeMillis());
                userInfo.setSynTag("modify");
                DBHelper.getInstance().updateUser(userInfo);
                ARouter.getInstance().build(getNavigationName()).navigation();
            }

            @Override
            public void onError(int code) {
                switch (code) {
                    case NetworkConst.ERR_CODE_NETWORK_FIAL:
                        ToastUtils.showShort(R.string.user_toast_check_network);
                        break;
                    default:
                        ToastUtils.showShort(R.string.user_toast_valid_mobile_or_sms);
                        break;
                }
            }
        });
    }

    private String getNavigationName() {
        String appPackageName = getApplication().getPackageName();
        SPStaticUtils.put("app_package_name", appPackageName);
        int index = appPackageName.lastIndexOf('.');
        String name = index == -1 ? appPackageName : appPackageName.substring(index + 1);

        return "/" + name + "/" + name;
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
