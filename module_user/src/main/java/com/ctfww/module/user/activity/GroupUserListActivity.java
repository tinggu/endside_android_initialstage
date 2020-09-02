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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.GroupUserListAdapter;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.datahelper.sp.SPQuickEntry;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@Route(path = "/user/groupUserList")
public class GroupUserListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "GroupUserListActivity";

    private final static int REQUEST_CODE_ADD_MEMBER = 1;

    private ImageView mBack;
    private TextView mTittle;
    private TextView mAddMember;
    private LinearLayout mAdminAction;
    private TextView mWithdrawManage;
    private TextView mDeleteGroup;
    private TextView mWithdrawGroup;

    private RecyclerView mMemberListView;
    GroupUserListAdapter mGroupUserListAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.group_user_list_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);
        Airship.getInstance().synGroupUserInfoFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        GroupInfo groupInfo = DBQuickEntry.getWorkingGroup();
        if (groupInfo != null) {
            mTittle.setText(groupInfo.getGroupName());
        }

        mAddMember = findViewById(R.id.top_addition);
        mAddMember.setText("邀请");
        String role = DBQuickEntry.getRoleInWorkingGroup();
        if ("admin".equals(role)) {
            mAddMember.setVisibility(View.VISIBLE);
        }
        else {
            mAddMember.setVisibility(View.GONE);
        }

        mAdminAction = findViewById(R.id.group_admin_action_linear_layout);
        mWithdrawManage = findViewById(R.id.group_withdraw_manage);
        mDeleteGroup = findViewById(R.id.group_delete_group);
        if (!"admin".equals(role)) {
            mAdminAction.setVisibility(View.GONE);
        }

        mWithdrawGroup = findViewById(R.id.group_withdraw_group);
        mWithdrawGroup.setText("退出群组");

        mMemberListView = findViewById(R.id.user_group_member_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mMemberListView.setLayoutManager(layoutManager);

        List<GroupUserInfo> groupUserInfoList = DBQuickEntry.getWorkingGroupUserList();
        mGroupUserListAdapter = new GroupUserListAdapter(groupUserInfoList, this);
        mMemberListView.setAdapter(mGroupUserListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mAddMember.setOnClickListener(this);
        mWithdrawGroup.setOnClickListener(this);
        mWithdrawManage.setOnClickListener(this);
        mDeleteGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mAddMember.getId()) {
            Intent intent = new Intent(this, GroupAddMemberActivity.class);
            startActivity(intent);
        }
        else if (id == mWithdrawManage.getId()) {
            DialogUtils.selectDialog("退出管理后，你将不能查看群组内其他人信息，确认要退出管理？", this, new DialogUtils.Callback() {
                @Override
                public void onConfirm(int radioSelectItem) {
                    withDrawManage();
                }

                @Override
                public void onCancel() {

                }
            });
        }
        else if (id == mDeleteGroup.getId()) {
            DialogUtils.selectDialog("解散后，此群组将不存在，请确认！", this, new DialogUtils.Callback() {
                @Override
                public void onConfirm(int radioSelectItem) {
                    deleteGroup();
                }

                @Override
                public void onCancel() {

                }
            });
        }
        else if (id == mWithdrawGroup.getId()) {
            GroupUserInfo info = DBQuickEntry.getWorkingGroupUser("");
            if (info == null) {
                return;
            }

            DialogUtils.selectDialog("退出后，你将不属于此群，请确认！", this, new DialogUtils.Callback() {
                @Override
                public void onConfirm(int radioSelectItem) {
                    withdrawGroup();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    private void withDrawManage() {
        if (!SPQuickEntry.isAdmin()) {
            return;
        }

        if (DBQuickEntry.getAdminCountInWorkingGroup() <= 1) {
            DialogUtils.onlyPrompt("你是唯一的管理员，不能退出管理！", this);
            return;
        }

        GroupUserInfo groupUserInfo = DBQuickEntry.getWorkingGroupUser("");
        if (groupUserInfo == null) {
            return;
        }

        groupUserInfo.setRole("member");
        groupUserInfo.setTimeStamp(System.currentTimeMillis());
        String synTag = "new".equals(groupUserInfo.getSynTag()) ? "new" : "modify";
        groupUserInfo.setSynTag(synTag);
        DBHelper.getInstance().updateGroupUser(groupUserInfo);

        Airship.getInstance().synToCloud();

        SPStaticUtils.put(Const.ROLE, Const.ROLE_MEMBER);
    }

    private void deleteGroup() {
        if (!SPQuickEntry.isAdmin()) {
            return;
        }

        if (DBQuickEntry.getAdminCountInWorkingGroup() != 1) {
            DialogUtils.onlyPrompt("你不是唯一的管理员，不能解散群组！", this);
            return;
        }

        GroupInfo groupInfo = DBQuickEntry.getWorkingGroup();
        if (groupInfo == null) {
            return;
        }

        LogUtils.i(TAG, "deleteGroup: groupInfo = " + groupInfo.toString());

        groupInfo.setStatus("delete");
        groupInfo.setTimeStamp(System.currentTimeMillis());
        groupInfo.setSynTag("modify");
        DBHelper.getInstance().updateGroup(groupInfo);

        Airship.getInstance().synGroupInfoToCloud();

        List<GroupUserInfo> groupUserInfoList = DBHelper.getInstance().getGroupUserList(groupInfo.getGroupId());
        for (int i = 0; i < groupUserInfoList.size(); ++i) {
            GroupUserInfo groupUserInfo = groupUserInfoList.get(i);
            groupUserInfo.setStatus("delete");
            groupUserInfo.setTimeStamp(System.currentTimeMillis());
            groupUserInfo.setSynTag("modify");
            DBHelper.getInstance().updateGroupUser(groupUserInfo);
        }

        Airship.getInstance().synGroupUserInfoToCloud();

        SPStaticUtils.remove(Const.WORKING_GROUP_ID);
        SPStaticUtils.remove(Const.ROLE);
        EventBus.getDefault().post(new MessageEvent("unbind_group"));

        finish();
    }

    private void withdrawGroup() {
        if (SPQuickEntry.isAdmin() && DBQuickEntry.getAdminCountInWorkingGroup() <= 1) {
            DialogUtils.onlyPrompt("你是唯一的管理员，不能退出群组！", this);
            return;
        }

        GroupUserInfo groupUserInfo = DBQuickEntry.getWorkingGroupUser("");
        groupUserInfo.setStatus("delete");
        groupUserInfo.setTimeStamp(System.currentTimeMillis());
        String synTag = "new".equals(groupUserInfo.getSynTag()) ? "new" : "modify";
        groupUserInfo.setSynTag(synTag);
        DBHelper.getInstance().updateGroupUser(groupUserInfo);
        Airship.getInstance().synGroupUserInfoToCloud();

        SPStaticUtils.remove(Const.WORKING_GROUP_ID);
        SPStaticUtils.remove(Const.ROLE);
        EventBus.getDefault().post(new MessageEvent("unbind_group"));

        finish();
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("finish_group_user_syn".equals(messageEvent.getMessage())) {
            List<GroupUserInfo> groupUserInfoList = DBQuickEntry.getWorkingGroupUserList();
            mGroupUserListAdapter.setList(groupUserInfoList);
            mGroupUserListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}