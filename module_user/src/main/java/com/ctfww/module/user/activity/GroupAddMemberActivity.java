package com.ctfww.module.user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.Utils;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.UserInfo;

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
            if (!Utils.isValidMobileNum(mMobile.getText().toString())) {
                DialogUtils.onlyPrompt("请输入有效电话号码！", this);
                return;
            }

            GroupInviteInfo groupInviteInfo = new GroupInviteInfo();
            UserInfo userInfo = DBQuickEntry.getSelfInfo();
            GroupInfo groupInfo = DBQuickEntry.getWorkingGroup();
            groupInviteInfo.setFromUserId(userInfo.getUserId());
            groupInviteInfo.setToUserId(mMobile.getText().toString());
            groupInviteInfo.setGroupId(groupInfo.getGroupId());
            groupInviteInfo.setTimeStamp(System.currentTimeMillis());
            groupInviteInfo.setStatus("send");
            groupInviteInfo.setFromMobile(userInfo.getMobile());
            groupInviteInfo.setFromNickName(userInfo.getNickName());
            groupInviteInfo.setToUserId(mMobile.getText().toString());
            groupInviteInfo.setToNickName("");
            groupInviteInfo.setGroupName(groupInfo.getGroupName());
            groupInviteInfo.setSynTag("new");
            groupInviteInfo.combineInviteId();

            DBHelper.getInstance().addInvite(groupInviteInfo);
            Airship.getInstance().synNoticeInfoToCloud();

            finish();
        }
    }
}
