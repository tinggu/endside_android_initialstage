package com.ctfww.module.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.network.NetworkConst;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.Utils;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.UserInfo;

import static com.ctfww.commonlib.Consts.REST_FAIL;

@Route(path = "/user/login")
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private TextView mTittleInPage;
    private EditText mPhoneNumEt;

    private Button mSmsLoginBtn;
    private Button mPasswordLoginBtn;

    private TextView mServiceStatement;


    // 短信验证码登录
    private LinearLayout mSmsLoginLayout;

    private EditText mSmsNumEt;

    private TextView mGetSmsNum;

    private TextView mSwitchToPasswordLogin;


    // 密码登录
    private LinearLayout mPasswordLoginLayout;

    private EditText mPasswordEt;

    private TextView mForgetPassword;

    private TextView mSwitchToSmsNumLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utils.isLogined()) {
            ARouter.getInstance().build(getNavigationName()).navigation();
            finish();
            return;
        }

        setContentView(R.layout.user_login_activity);

        initViews();

        processBundle();

        setOnClickListener();

        setOnEditListener();

        setServiceStatementStyle();
    }

    private void setOnEditListener() {
        mPhoneNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setGetSmsNumEnable(Utils.isValidMobileNum(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSmsNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSmsLoginBtnEnable(Utils.isValidSMS(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setPasswordLoginBtnEnable(Utils.isValidPassword(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setServiceStatementStyle() {
        SpannableString spannableString = new SpannableString(getString(R.string.user_login_privacy_statement_entrance));
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#50A2FF"));
        spannableString.setSpan(colorSpan, spannableString.length() - 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mServiceStatement.setText(spannableString);
    }

    private void setOnClickListener() {
        mSmsLoginBtn.setOnClickListener(this);
        mPasswordLoginBtn.setOnClickListener(this);
//        mServiceStatement.setOnClickListener(this);

        mGetSmsNum.setOnClickListener(this);
        mSwitchToPasswordLogin.setOnClickListener(this);

        mForgetPassword.setOnClickListener(this);
        mSwitchToSmsNumLogin.setOnClickListener(this);
    }

    private void initViews() {
        mTittleInPage = findViewById(R.id.user_tittle_in_page);
        String navigationName = getNavigationName();
        if (navigationName.indexOf("keepwatch") != -1) {
            mTittleInPage.setText("你好，\n欢迎使用慧创未来巡检系统");
        }

        mPhoneNumEt = findViewById(R.id.user_mobile_edit);
        mSmsLoginBtn = findViewById(R.id.user_sms_login_btn);
        mPasswordLoginBtn = findViewById(R.id.user_password_login_btn);
        mServiceStatement = findViewById(R.id.user_app_service_statement);

        mSmsLoginLayout = findViewById(R.id.user_login_with_sms_layout_all);
        mSmsNumEt = findViewById(R.id.user_sms_edit);
        mGetSmsNum = findViewById(R.id.user_get_sms);
        mSwitchToPasswordLogin = findViewById(R.id.user_login_switch_to_password);

        mPasswordLoginLayout = findViewById(R.id.user_login_with_password_layout_all);
        mPasswordEt = findViewById(R.id.user_password_edit);
        mForgetPassword = findViewById(R.id.user_forget_password);
        mSwitchToSmsNumLogin = findViewById(R.id.user_login_switch_to_sms);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.user_sms_login_btn) { // 验证码登录
            loginBySMS();
        }
        if (i == R.id.user_password_login_btn) { // 密码登录
            loginByPassword();
        }
        else if (i == R.id.user_get_sms) { // 获取验证码
            setPhoneNumEtEnable(false);
            setGetSmsNumEnable(false);
            sendSmsNum();
        }
        else if (i == R.id.user_login_switch_to_password) { // 密码登录
            mSmsLoginLayout.setVisibility(View.GONE);
            mPasswordLoginLayout.setVisibility(View.VISIBLE);
        }
        else if (i == R.id.user_login_switch_to_sms) { // 短信验证码登录
            mSmsLoginLayout.setVisibility(View.VISIBLE);
            mPasswordLoginLayout.setVisibility(View.GONE);
        }
        else if (i == R.id.user_forget_password) { // 忘记密码
            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("mobile", mPhoneNumEt.getText().toString());
            intent.putExtras(bundle);

            startActivity(intent);
            finish();
        }
    }

    CountDownTimer timer = new CountDownTimer(90*1000, 1000) {
        @Override
        public void onTick(long l) {
            mGetSmsNum.setText(getResources().getString(R.string.user_login_sms_input_count_down, l/1000));
        }

        @Override
        public void onFinish() {
            setPhoneNumEtEnable(true);
            mGetSmsNum.setText(getResources().getString(R.string.user_login_sms_num_send_again));
            setGetSmsNumEnable(true);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void loginBySMS() {
        if (!Utils.isValidMobileNum(mPhoneNumEt.getText().toString())|| !Utils.isValidSMS(mSmsNumEt.getText().toString())) {
            ToastUtils.showShort(R.string.user_toast_valid_mobile_or_sms);
            return;
        }

        KeyboardUtils.hideSoftInput(this);

        NetworkHelper.getInstance().loginBySMS(mPhoneNumEt.getText().toString(),
                mSmsNumEt.getText().toString(),
                new IUIDataHelperCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        UserInfo userInfo = (UserInfo)obj;
                        userInfo.setSynTag("cloud");
                        DBHelper.getInstance().addUser(userInfo);

                        SPStaticUtils.put(Const.USER_OPEN_ID, userInfo.getUserId());

                        ToastUtils.showShort(R.string.user_toast_success_login);
                        ARouter.getInstance().build(getNavigationName()).navigation();
                        finish();
                    }

                    @Override
                    public void onError(int code) {
                        switch (code) {
                            case NetworkConst.ERR_CODE_NETWORK_FIAL:
                                ToastUtils.showShort(R.string.user_toast_fail_login_by_network);
                                break;
                            default:
                                ToastUtils.showShort(R.string.user_toast_fail_login_by_sms);
                                break;
                        }
                    }
                });

    }

    private void loginByPassword() {
        if (!Utils.isValidMobileNum(mPhoneNumEt.getText().toString())|| !Utils.isValidPassword(mPasswordEt.getText().toString())) {
            ToastUtils.showShort(R.string.user_toast_valid_mobile_or_password);
            return;
        }

        KeyboardUtils.hideSoftInput(this);
        NetworkHelper.getInstance().loginByPassword(mPhoneNumEt.getText().toString(),
                mPasswordEt.getText().toString()
                , new IUIDataHelperCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        UserInfo userInfo = (UserInfo)obj;
                        userInfo.setSynTag("cloud");
                        DBHelper.getInstance().addUser(userInfo);

                        SPStaticUtils.put(Const.USER_OPEN_ID, userInfo.getUserId());

                        ToastUtils.showShort("登录成功");
                        ARouter.getInstance().build(getNavigationName()).navigation();
                        finish();
                    }

                    @Override
                    public void onError(int code) {
                        switch (code) {
                            case NetworkConst.ERR_CODE_NETWORK_FIAL:
                                ToastUtils.showShort("登录失败，请检查网络");
                                setPhoneNumEtEnable(true);
                                break;
                            default:
                                ToastUtils.showShort("账号或密码错误，请使用验证码登录");
                                setPhoneNumEtEnable(true);
                                break;
                        }
                    }
                });
    }

    private void sendSmsNum() {
        KeyboardUtils.hideSoftInput(this);
        NetworkHelper.getInstance().sendSmsNum(mPhoneNumEt.getText().toString(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                ToastUtils.showShort("验证码已发送");

                mGetSmsNum.setText(getResources().getString(R.string.user_login_sms_input_count_down, 90));
                mGetSmsNum.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_edit_hint));

                // 开启倒计时
                timer.start();
            }

            @Override
            public void onError(int code) {
                setPhoneNumEtEnable(true);
                setGetSmsNumEnable(true);
                switch (code) {
                    case NetworkConst.ERR_CODE_NETWORK_FIAL:
                        ToastUtils.showShort("请检查网络");
                        break;
                    case REST_FAIL:
                        ToastUtils.showShort("不能频繁获取短信：一天只有10次短信机会，一小时只有5次短信机会。因此，请注意设置密码，采用账号密码登录。");
                        break;
                    default:
                        ToastUtils.showShort("服务器出错了");
                        break;
                }

            }
        });

    }

    private void setSmsLoginBtnEnable(boolean b) {
        if (b) {
            mSmsLoginBtn.setBackground(getDrawable(R.drawable.user_login_submit_btn_bg_100));
        } else {
            mSmsLoginBtn.setBackground(getDrawable(R.drawable.user_login_submit_btn_bg_30));
        }
    }

    private void setPasswordLoginBtnEnable(boolean b) {
        if (b) {
            mPasswordLoginBtn.setBackground(getDrawable(R.drawable.user_login_submit_btn_bg_100));
        } else {
            mPasswordLoginBtn.setBackground(getDrawable(R.drawable.user_login_submit_btn_bg_30));
        }
    }

    private void setPhoneNumEtEnable(boolean b) {
        if (b) {
            mPhoneNumEt.setEnabled(true);
        }
        else {
            mPhoneNumEt.setEnabled(false);
        }
    }

    private void setGetSmsNumEnable(boolean b) {
        if (b) {
            mGetSmsNum.setTextColor(ContextCompat.getColor(this, R.color.color_btn_bg_01_100));
            mGetSmsNum.setEnabled(true);
        }
        else {
            mGetSmsNum.setTextColor(ContextCompat.getColor(this, R.color.color_edit_hint));
            mGetSmsNum.setEnabled(false);
        }
    }

    private void processBundle() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        String mobile = bundle.getString("mobile");
        if (!TextUtils.isEmpty(mobile)) {
            mPhoneNumEt.setText(mobile);
        }

        String isSmsLogin = bundle.getString("isSmsLogin");
        if ("true".equals(isSmsLogin)) {
            mSmsLoginLayout.setVisibility(View.VISIBLE);
            mPasswordLoginLayout.setVisibility(View.GONE);
        }
        else {
            mSmsLoginLayout.setVisibility(View.GONE);
            mPasswordLoginLayout.setVisibility(View.VISIBLE);
        }
    }

    private String getNavigationName() {
        String appPackageName = getApplication().getPackageName();
        SPStaticUtils.put("app_package_name", appPackageName);
        int index = appPackageName.lastIndexOf('.');
        String name = index == -1 ? appPackageName : appPackageName.substring(index + 1);

        return "/" + name + "/" + name;
    }
}
