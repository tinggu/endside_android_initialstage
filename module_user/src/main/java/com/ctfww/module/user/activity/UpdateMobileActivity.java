package com.ctfww.module.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.UserInfo;

public class UpdateMobileActivity extends AppCompatActivity implements View.OnClickListener, VerifyPasswordDialog.IInputPassword {
    private final static String TAG = "UpdateMobileActivity";

    private final static int REQUEST_CODE = 1;

    private ImageView mBackImg;
    private TextView mTopBarTitlle;
    private TextView mMobileTxt;
    private Button mBlueBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_update_mobile_activity);
        initViews();

         setOnClickListener();
    }

    private void initViews() {
        mBackImg = findViewById(R.id.user_back);
        mTopBarTitlle = findViewById(R.id.user_top_bar_tittle);
        mTopBarTitlle.setText("更换手机号");
        mMobileTxt = findViewById(R.id.user_mobile_desc);
        UserInfo userInfo = DBQuickEntry.getSelfInfo();
        mMobileTxt.setText("绑定的手机号：" + userInfo.getMobile());
        mBlueBtn = findViewById(R.id.user_blue_btn);
        mBlueBtn.setText("更换手机号");
    }

    private void setOnClickListener() {
        mBackImg.setOnClickListener(this);
        mBlueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBlueBtn.getId()) {
            VerifyPasswordDialog dlg = new VerifyPasswordDialog();
            dlg.show(getSupportFragmentManager(), "VerifyPasswordDialog");
        }

        if (id == mBackImg.getId()) {
            finish();
        }
    }

    @Override
    public void onConfirm(String password) {
        if (TextUtils.isEmpty(password)) {
            LogUtils.i(TAG, "密码不能为空");
            return;
        }

        UserInfo userInfo = DBQuickEntry.getSelfInfo();
        if (!password.equals(userInfo.getPassword())) {
            LogUtils.i(TAG, "密码验证不正确");
            return;
        }

        startActivity(new Intent(this, UpdateMobile2Activity.class));
    }
}
