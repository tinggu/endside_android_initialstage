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
import com.ctfww.module.user.datahelper.DataHelper;
import com.ctfww.module.user.entity.UserInfo;

public class UpdateMobileActivity extends AppCompatActivity implements View.OnClickListener, VerifyPasswordDialog.IInputPassword {
    private final static String TAG = "UpdateMobileActivity";

    private final static int REQUEST_CODE = 1;

    private ImageView mBackImg;
    private TextView mTopBarTitlle;
    private TextView mMobileTxt;
    private Button mBlueBtn;

    private UserInfo mUserInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_update_mobile_activity);
        mUserInfo = DataHelper.getInstance().getUserInfo();
        initViews();

         setOnClickListener();
    }

    private void initViews() {
        mBackImg = findViewById(R.id.user_back);
        mTopBarTitlle = findViewById(R.id.user_top_bar_tittle);
        mTopBarTitlle.setText("更换手机号");
        mMobileTxt = findViewById(R.id.user_mobile_desc);
        mMobileTxt.setText("绑定的手机号：" + mUserInfo.getMobile());
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

        if (!password.equals(mUserInfo.getPassword())) {
            LogUtils.i(TAG, "密码验证不正确");
            return;
        }

        startActivityForResult(new Intent(this, UpdateMobile2Activity.class), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String mobileTemp = data.getStringExtra("mobile");
                if (mUserInfo.getMobile().equals(mobileTemp)) {
                    ToastUtils.showShort("修改的手机号码与原来的相同，因此没有修改！");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("mobile", mobileTemp);
                    setResult(RESULT_OK, intent);
                }

                finish();
            }
        }
    }
}
