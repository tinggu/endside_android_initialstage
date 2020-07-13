package com.ctfww.module.user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.NetworkHelper;

@Route(path = "/user/inviteMember")
public class GroupAddMemberActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "GroupAddMemberActivity";

    private ImageView mBack;
    private TextView mTittle;
    private EditText mMobile;
    private TextView mConfirm;
    private String mGroupId;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.group_add_member_activity);
        mGroupId = SPStaticUtils.getString("working_group_id");
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_top_back);
        mTittle = findViewById(R.id.user_top_tittle);
        mTittle.setText("增加成员");
        mMobile = findViewById(R.id.keepwatch_desk_id);
        mConfirm = findViewById(R.id.user_confirm);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mConfirm.getId()) {
            NetworkHelper.getInstance().addInvite(mGroupId, mMobile.getText().toString(), new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    ToastUtils.showShort("成功发送邀请，等待对方接受");
                    finish();
                }

                @Override
                public void onError(int code) {
                    ToastUtils.showShort("邀请失败，请确认网络是否正常");
                }
            });
        }
    }
}
