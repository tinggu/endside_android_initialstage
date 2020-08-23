package com.ctfww.module.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.blankj.utilcode.util.ActivityUtils.finishAllActivities;

@Route(path = "/user/account")
public class AccountActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "AccountActivity";

    private final static int REQUEST_CODE_SET_ONE_VAL = 1;
    private final static int REQUEST_CODE_UPDATE_MOBILE = 2;
    private final static int REQUEST_CODE_UPDATE_PASSWORD = 3;

    private ImageView mBack;
    private TextView mTittle;
    private TextView mUnregister;

    private TextView mMobile;
    private LinearLayout mUpdateMobile;
    private TextView mEmail;
    private LinearLayout mUpdateEmail;
    private LinearLayout mUpdatePassword;
    private TextView mWechat;
    private LinearLayout mUpdateWechat;
    private TextView mBlog;
    private LinearLayout mUpdateBlog;
    private TextView mQq;
    private LinearLayout mUpdateQq;

    private UserInfo mUserInfo;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_account_activity);
        initViews();
        setOnClickListener();
        mUserInfo = DBQuickEntry.getSelfInfo();
        updateUI();

        EventBus.getDefault().register(this);
        Airship.getInstance().synUserInfoFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText("账号管理");

        mUnregister = findViewById(R.id.full_width_btn);
        mUnregister.setText("注销账号");


        mMobile = findViewById(R.id.user_info_mobile);
        mUpdateMobile = findViewById(R.id.user_info_update_mobile);

        mEmail = findViewById(R.id.user_info_email);
        mUpdateEmail = findViewById(R.id.user_info_update_email);

        mUpdatePassword = findViewById(R.id.user_info_update_password);

        mWechat = findViewById(R.id.user_info_wechat);
        mUpdateWechat = findViewById(R.id.user_info_update_wechat);

        mBlog = findViewById(R.id.user_info_blog);
        mUpdateBlog = findViewById(R.id.user_info_update_blog);

        mQq = findViewById(R.id.user_info_qq);
        mUpdateQq = findViewById(R.id.user_info_update_blog);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mUnregister.setOnClickListener(this);
        mUpdateMobile.setOnClickListener(this);
        mUpdateEmail.setOnClickListener(this);
        mUpdatePassword.setOnClickListener(this);
        mUpdateWechat.setOnClickListener(this);
        mUpdateBlog.setOnClickListener(this);
        mUpdateQq.setOnClickListener(this);
    }

    private void updateUI() {
        if (mUserInfo == null) {
            return;
        }

        mMobile.setText(mUserInfo.getMobile());
        mEmail.setText(mUserInfo.getEmail());
        mWechat.setText(mUserInfo.getWechatNum());
        mBlog.setText(mUserInfo.getBlogNum());
        mQq.setText(mUserInfo.getQqNum());
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("finish_user_info_syn".equals(messageEvent.getMessage())) {
            updateUI();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            Airship.getInstance().synUserInfoToCloud();
            finish();
        }
        else if (id == mUpdateMobile.getId()) {
            Intent intent = new Intent(this, UpdateMobileActivity.class);
            startActivity(intent);
        }
        else if (id == mUpdatePassword.getId()) {
            Intent intent = new Intent(this, SetPasswordActivity.class);
            intent.putExtra("mobile", mMobile.getText().toString());
            intent.putExtra("type", "modify");

            startActivityForResult(intent, REQUEST_CODE_UPDATE_PASSWORD);
        }
        else if (id == mUpdateEmail.getId()) {
            Intent intent = new Intent(this, OneValSetActivity.class);
            intent.putExtra("value", mEmail.getText().toString());
            intent.putExtra("type", "email");

            startActivityForResult(intent, REQUEST_CODE_SET_ONE_VAL);
        }
        else if (id == mUpdateWechat.getId()) {
            Intent intent = new Intent(this, OneValSetActivity.class);
            intent.putExtra("value", mWechat.getText().toString());
            intent.putExtra("type", "wechat");

            startActivityForResult(intent, REQUEST_CODE_SET_ONE_VAL);
        }
        else if (id == mUpdateBlog.getId()) {
            Intent intent = new Intent(this, OneValSetActivity.class);
            intent.putExtra("value", mBlog.getText().toString());
            intent.putExtra("type", "blog");

            startActivityForResult(intent, REQUEST_CODE_SET_ONE_VAL);
        }
        else if (id == mUpdateQq.getId()) {
            Intent intent = new Intent(this, OneValSetActivity.class);
            intent.putExtra("value", mQq.getText().toString());
            intent.putExtra("type", "qq");

            startActivityForResult(intent, REQUEST_CODE_SET_ONE_VAL);
        }
        else if (id == mUnregister.getId()) {
            LogUtils.i(TAG, "mUnregister.getId()");
            DialogUtils.selectDialog("注销后，你的个人相关的数据会全部丢失，请确认是否注销？", this, new DialogUtils.Callback() {
                @Override
                public void onConfirm(int radioSelectItem) {
                    unregister();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_PASSWORD) {
            if (resultCode==RESULT_OK) {
                String password = data.getStringExtra("password");

                mUserInfo.setPassword(password);
                mUserInfo.setTimeStamp(System.currentTimeMillis());
                DBHelper.getInstance().updateUser(mUserInfo);
            }
        }
        else if (requestCode == REQUEST_CODE_SET_ONE_VAL) {
            if (resultCode==RESULT_OK) {
                final String key = data.getStringExtra("key");
                final String val = data.getStringExtra("value");
                if ("email".equals(key)) {
                    mEmail.setText(val);
                    mUserInfo.setEmail(val);
                    mUserInfo.setTimeStamp(System.currentTimeMillis());
                    DBHelper.getInstance().updateUser(mUserInfo);
                }
                else if ("wechat".equals(key)) {
                    mWechat.setText(val);
                    mUserInfo.setWechatNum(val);
                    mUserInfo.setTimeStamp(System.currentTimeMillis());
                    DBHelper.getInstance().updateUser(mUserInfo);
                }
                else if ("blog".equals(key)) {
                    mBlog.setText(val);
                    mUserInfo.setBlogNum(val);
                    mUserInfo.setTimeStamp(System.currentTimeMillis());
                    DBHelper.getInstance().updateUser(mUserInfo);
                }
                else if ("qq".equals(key)) {
                    mQq.setText(val);
                    mUserInfo.setQqNum(val);
                    mUserInfo.setTimeStamp(System.currentTimeMillis());
                    DBHelper.getInstance().updateUser(mUserInfo);
                }
            }
        }
    }

    private void unregister() {
        NetworkHelper.getInstance().deleteAccount(SPStaticUtils.getString("user_open_id"), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                SPStaticUtils.clear();
                finishAllActivities();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
