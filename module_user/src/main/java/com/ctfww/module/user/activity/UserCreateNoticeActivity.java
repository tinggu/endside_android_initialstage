package com.ctfww.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.NetworkHelper;

@Route(path = "/user/createNotice")
public class UserCreateNoticeActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UserCreateNoticeActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mRelease;
    private EditText mNoticeTittle;
    private EditText mNoticeDesc;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_create_notice_activity);
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("增加成员");
        mRelease = findViewById(R.id.top_addition);
        mRelease.setText("发布");
        mRelease.setVisibility(View.VISIBLE);
        mNoticeTittle = findViewById(R.id.user_notice_tittle);
        mNoticeDesc = findViewById(R.id.user_notice_desc);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mRelease.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mRelease.getId()) {
            NetworkHelper.getInstance().addNotice(mNoticeTittle.getText().toString(), mNoticeDesc.getText().toString(), new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    ToastUtils.showShort("通知发布成功");
                    finish();
                }

                @Override
                public void onError(int code) {
                    ToastUtils.showShort("通知发布失败，请确认网络是否正常！");
                }
            });
        }
    }
}
