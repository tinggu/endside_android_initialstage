package com.ctfww.module.user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.ApkUtils;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.UserGroupInfo;

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
            if (TextUtils.isEmpty(mName.getText().toString())) {
                DialogUtils.onlyPrompt("群组名称不能为空！", this);
                return;
            }

            if (mIsCreate) {
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setGroupName(mName.getText().toString());
                groupInfo.setTimeStamp(System.currentTimeMillis());
                groupInfo.setUserId(SPStaticUtils.getString("user_open_id"));
                groupInfo.setAppName(ApkUtils.getAppName(getApplicationContext()));
                groupInfo.setStatus("reserve");
                groupInfo.setSynTag("new");
                groupInfo.combineGroupId();
                DBHelper.getInstance().addGroup(groupInfo);
            }
            else {
                String groupId = mUserGroupInfo.getGroupId();
                GroupInfo groupInfo = DBHelper.getInstance().getGroup(groupId);
                groupInfo.setGroupName(mName.getText().toString());
                groupInfo.setTimeStamp(System.currentTimeMillis());
                groupInfo.setUserId(SPStaticUtils.getString("user_open_id"));
                groupInfo.setSynTag("modify");
                DBHelper.getInstance().updateGroup(groupInfo);
            }

            Airship.getInstance().synGroupInfoToCloud();
        }
    }
}
