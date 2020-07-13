package com.ctfww.module.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.DBHelper;
import com.ctfww.module.user.datahelper.DataHelper;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.UserInfo;

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


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_account_activity);
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText("账号管理");

        mUnregister = findViewById(R.id.full_width_btn);
        mUnregister.setText("注销账号");

        UserInfo userInfo = DataHelper.getInstance().getUserInfo();
        mMobile = findViewById(R.id.user_info_mobile);
        mUpdateMobile = findViewById(R.id.user_info_update_mobile);
        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            mMobile.setText(userInfo.getMobile());
        }

        mEmail = findViewById(R.id.user_info_email);
        mUpdateEmail = findViewById(R.id.user_info_update_email);
         if (!TextUtils.isEmpty(userInfo.getEmail()) || !"null".equals(userInfo.getEmail())) {
             mEmail.setText(userInfo.getEmail());
        }

        mUpdatePassword = findViewById(R.id.user_info_update_password);

        mWechat = findViewById(R.id.user_info_wechat);
        mUpdateWechat = findViewById(R.id.user_info_update_wechat);
        if (!TextUtils.isEmpty(userInfo.getWechatNum()) && !"null".equals(userInfo.getWechatNum())) {
            mWechat.setText(userInfo.getWechatNum());
        }

        mBlog = findViewById(R.id.user_info_blog);
        mUpdateBlog = findViewById(R.id.user_info_update_blog);
        if (!TextUtils.isEmpty(userInfo.getBlogNum()) && !"null".equals(userInfo.getBlogNum())) {
            mBlog.setText(userInfo.getBlogNum());
        }
        mQq = findViewById(R.id.user_info_qq);
        mUpdateQq = findViewById(R.id.user_info_update_blog);
        if (!TextUtils.isEmpty(userInfo.getQqNum()) && !"null".equals(userInfo.getQqNum())) {
            mQq.setText(userInfo.getQqNum());
        }
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mUpdateMobile.getId()) {
            Intent intent = new Intent(this, UpdateMobileActivity.class);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_MOBILE);
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
            unregister();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_MOBILE) {
            if (resultCode==RESULT_OK) {
                String mobile = data.getStringExtra("mobile");

                UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                userInfo.setMobile(mobile);
                userInfo.setModifyTimestamp(System.currentTimeMillis());
                updateUserInfo(userInfo, "mobile");
            }
        }
        else if (requestCode == REQUEST_CODE_UPDATE_PASSWORD) {
            if (resultCode==RESULT_OK) {
                String password = data.getStringExtra("password");

                UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                userInfo.setPassword(password);
                userInfo.setModifyTimestamp(System.currentTimeMillis());
                updateUserInfo(userInfo, password);
            }
        }
        else if (requestCode == REQUEST_CODE_SET_ONE_VAL) {
            if (resultCode==RESULT_OK) {
                final String key = data.getStringExtra("key");
                final String val = data.getStringExtra("value");
                if ("email".equals(key)) {
                    UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                    userInfo.setEmail(val);
                    userInfo.setModifyTimestamp(System.currentTimeMillis());
                    updateUserInfo(userInfo, "email");
                }
                else if ("wechat".equals(key)) {
                    UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                    userInfo.setWechatNum(val);
                    userInfo.setModifyTimestamp(System.currentTimeMillis());
                    updateUserInfo(userInfo, "wechat");
                }
                else if ("blog".equals(key)) {
                    UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                    userInfo.setBlogNum(val);
                    userInfo.setModifyTimestamp(System.currentTimeMillis());
                    updateUserInfo(userInfo, "blog");
                }
                else if ("qq".equals(key)) {
                    UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                    userInfo.setQqNum(val);
                    userInfo.setModifyTimestamp(System.currentTimeMillis());
                    updateUserInfo(userInfo, "qq");
                }
            }
        }
    }

    private void updateUserInfo(final UserInfo userInfo, final String type) {
        NetworkHelper.getInstance().updateUserInfo(userInfo,
                new IUIDataHelperCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        userInfo.setSynTag("cloud");
                        DBHelper.getInstance().updateUser(userInfo);
                        if ("mobile".equals(type)) {
                            mMobile.setText(userInfo.getMobile());
                            LogUtils.i(TAG, "updateUserInfo: 电话号码更新成功");
                        }
                        else if ("password".equals(type)){
                            LogUtils.i(TAG, "updateUserInfo: 密码已经更新成功");
                        }
                        else if ("email".equals(type)){
                            mEmail.setText(userInfo.getEmail());
                            LogUtils.i(TAG, "updateUserInfo: 邮箱已经更新成功");
                        }
                        else if ("wechat".equals(type)){
                            mWechat.setText(userInfo.getWechatNum());
                            LogUtils.i(TAG, "updateUserInfo: 微信已经更新成功");
                        }
                        else if ("blog".equals(type)){
                            mBlog.setText(userInfo.getBlogNum());
                            LogUtils.i(TAG, "updateUserInfo: 微博已经更新成功");
                        }
                        else if ("qq".equals(type)){
                            mQq.setText(userInfo.getQqNum());
                            LogUtils.i(TAG, "updateUserInfo: QQ已经更新成功");
                        }
                    }

                    @Override
                    public void onError(int code) {
                        userInfo.setSynTag("modify");
                        DBHelper.getInstance().updateUser(userInfo);
                        ToastUtils.showShort("updateUserInfo: 更新失败，请确认网络是否正常！");
                    }
                });
    }

    private void unregister() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
        builder.setMessage("注销后，你的个人相关的数据会全部丢失，请确认是否注销？");
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _unregister();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void _unregister() {
        NetworkHelper.getInstance().unregister(SPStaticUtils.getString("user_open_id"), new IUIDataHelperCallback() {
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
}
