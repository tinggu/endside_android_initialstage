package com.guoliang.module.user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.module.user.R;
import com.guoliang.module.user.datahelper.NetworkHelper;
import com.guoliang.module.user.entity.UserGroupInfo;

@Route(path = "/user/createGroup")
public class GroupCreateOrUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "GroupCreateOrUpdateActivity";

    private ImageView mBack;
    private TextView mTittle;
    private EditText mName;
    private TextView mConfirm;

    private boolean mIsCreate;
    private UserGroupInfo mUserGroupInfo;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.group_create_or_update_activity);
        processIntent();
        initViews();
        setOnClickListener();
    }

    private void processIntent() {
        mIsCreate = getIntent().getBooleanExtra("isCreate", true);
        if (!mIsCreate) {
            mUserGroupInfo = GsonUtils.fromJson(getIntent().getStringExtra("userGroupInfo"), UserGroupInfo.class);
        }
    }
    private void initViews() {
        mBack = findViewById(R.id.user_top_back);
        mTittle = findViewById(R.id.user_top_tittle);
        if (mIsCreate) {
            mTittle.setText("创建群组");
        }
        else {
            mTittle.setText("更新群组信息");
        }
        mName = findViewById(R.id.user_group_name);
        if (mUserGroupInfo != null) {
            mName.setText(mUserGroupInfo.getGroupName());
        }
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
            if (mIsCreate) {
                createGroup();
            }
            else {
                updateGroupInfo();
            }
        }
    }

    private void createGroup() {
        NetworkHelper.getInstance().createGroup(mName.getText().toString(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                UserGroupInfo userGroupInfo = (UserGroupInfo)obj;
                Intent intent = new Intent();
                intent.putExtra("userGroupInfo", GsonUtils.toJson(userGroupInfo));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("群组创建失败");
            }
        });
    }

    private void updateGroupInfo() {
        NetworkHelper.getInstance().updateGroupInfo(mUserGroupInfo.getGroupId(), mName.getText().toString(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mUserGroupInfo.setGroupName(mName.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("userGroupInfo", GsonUtils.toJson(mUserGroupInfo));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("更新群组信息失败，请检查网络！");
            }
        });
    }
}
