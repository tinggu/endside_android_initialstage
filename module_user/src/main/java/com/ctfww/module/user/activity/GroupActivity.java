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
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.User2GroupAdapter;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.GroupUserInfo;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.finishAllActivities;

@Route(path = "/user/groupUser")
public class GroupActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "GroupActivity";

    private final static int REQUEST_CODE_ADD_MEMBER = 1;

    private ImageView mBack;
    private TextView mTittle;
    private TextView mAddMember;
    private LinearLayout mAdminAction;
    private TextView mWithdrawManage;
    private TextView mDeleteGroup;
    private TextView mWithdrawGroup;

    private RecyclerView mMemberListView;
    User2GroupAdapter mUser2GroupAdapter;

    GroupActivityData mGroupActivityData = new GroupActivityData();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_2_group_activity);
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_top_back);
        mTittle = findViewById(R.id.user_top_tittle);
        String groupName = SPStaticUtils.getString("working_group_name");
        if (!TextUtils.isEmpty(groupName)) {
            mTittle.setText(groupName);
        }

        mAddMember = findViewById(R.id.user_top_addition);
        mAddMember.setText("邀请");
        String role = SPStaticUtils.getString("role");
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

        mUser2GroupAdapter = new User2GroupAdapter(new ArrayList<GroupUserInfo>(), this);
        mMemberListView.setAdapter(mUser2GroupAdapter);

        getGroupUserInfoList();
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mAddMember.setOnClickListener(this);
        mWithdrawGroup.setOnClickListener(this);
        mWithdrawManage.setOnClickListener(this);
        mDeleteGroup.setOnClickListener(this);
    }

    private void getGroupUserInfoList() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        NetworkHelper.getInstance().getGroupUserInfo(groupId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mGroupActivityData.setGroupUserInfoList((List<GroupUserInfo>)obj);
                mUser2GroupAdapter.setList(mGroupActivityData.getGroupUserInfoList());
                mUser2GroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    public void showGroupUserInfoActivity(GroupUserInfo groupUserInfo) {
        Intent intent = new Intent(this, GroupUserInfoActivity.class);
        intent.putExtra("groupUserInfo", GsonUtils.toJson(groupUserInfo));
        String role = SPStaticUtils.getString("role");
        boolean isCanUpdate = "admin".equals(role) && !"admin".equals(groupUserInfo.getRole()) ? true : false;
        intent.putExtra("isCanUpdate", isCanUpdate);
        startActivityForResult(intent, 2);
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
            withDrawManage();
        }
        else if (id == mDeleteGroup.getId()) {
            deleteGroup();
        }
        else if (id == mWithdrawGroup.getId()) {
            withdrawGroup();
        }
    }

    private void onlyPrompt(final String prompt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(prompt);
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        dialog.show();
    }

    private void withDrawManage() {
        if (mGroupActivityData.getRoleCnt()== 1) {
            onlyPrompt("你是唯一管理员，不能退出管理！");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("退出管理后，你将不能查看群组内其他人信息，确认要退出管理？");
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _withDrawManage();
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

    private void _withDrawManage() {
        final String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        final String userId = SPStaticUtils.getString("user_open_id");
        final String role = "member";
        NetworkHelper.getInstance().updateGroupUserRole(userId, role, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                GroupUserInfo groupUserInfo = new GroupUserInfo();
                groupUserInfo.setUserId(userId);
                groupUserInfo.setRole(role);
                mGroupActivityData.updateOneItem(groupUserInfo);
                mUser2GroupAdapter.setList(mGroupActivityData.getGroupUserInfoList());
                mUser2GroupAdapter.notifyDataSetChanged();
                SPStaticUtils.put("role", role);
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("退出失败，请确认网络是否正常！");
            }
        });
    }

    public void deleteGroup() {
        if (mGroupActivityData.getRoleCnt() >= 2) {
            onlyPrompt("还有其他管理员，你不能私自解散此群组，除非其他管理员退出管理！");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("解散后，此群组将不存在，请确认！");
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _deleteGroup();
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

    private void _deleteGroup() {
        final String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        NetworkHelper.getInstance().deleteGroup(groupId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                SPStaticUtils.remove("working_group_id");
                SPStaticUtils.remove("role");
                SPStaticUtils.remove("working_group_name");
                finish();
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("解散群组失败，请确认网络是否正常！");
            }
        });
    }

    private void withdrawGroup() {
        String role = SPStaticUtils.getString("role");
        if (mGroupActivityData.getRoleCnt() == 1 && "admin".equals(role)) {
            onlyPrompt("你是唯一管理员，不能退出，除非你将某成员的角色修改为管理员！");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("退出后，你将不属于此群，请确认！");
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _withdrawGroup();
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

    private void _withdrawGroup() {
        final String groupId = SPStaticUtils.getString("working_group_id");
        final String userId = SPStaticUtils.getString("user_open_id");
        NetworkHelper.getInstance().withdrawGroup(userId, groupId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                finish();
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("退出群组失败，请确认网络是否正常！");
            }
        });
    }

    public void deleteMember(final String userId) {
        final String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        NetworkHelper.getInstance().withdrawGroup(userId, groupId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mGroupActivityData.deleteMember(userId);
                mUser2GroupAdapter.setList(mGroupActivityData.getGroupUserInfoList());
                mUser2GroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("退出群组失败，请确认网络是否正常！");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode != RESULT_OK) {
                return;
            }

            String groupUserInfoStr = data.getStringExtra("groupUserInfo");
            if (groupUserInfoStr != null) {
                GroupUserInfo groupUserInfo = GsonUtils.fromJson(groupUserInfoStr, GroupUserInfo.class);
                mGroupActivityData.updateOneItem(groupUserInfo);
                mUser2GroupAdapter.setList(mGroupActivityData.getGroupUserInfoList());
                mUser2GroupAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateRole(final String userId, final String role) {
        NetworkHelper.getInstance().updateGroupUserRole(userId, role, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                GroupUserInfo groupUserInfo = new GroupUserInfo();
                groupUserInfo.setUserId(userId);
                groupUserInfo.setRole(role);
                mGroupActivityData.updateOneItem(groupUserInfo);
                mUser2GroupAdapter.setList(mGroupActivityData.getGroupUserInfoList());
                mUser2GroupAdapter.notifyDataSetChanged();
                if (userId.equals(SPStaticUtils.getString("user_open_id"))) {
                    SPStaticUtils.put("role", role);
                }
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("退出失败，请确认网络是否正常！");
            }
        });
    }
}

class GroupActivityData {
    List<GroupUserInfo> groupUserInfoList;
    public GroupActivityData() {
        groupUserInfoList = new ArrayList<>();
    }

    public List<GroupUserInfo> getGroupUserInfoList() {
        return groupUserInfoList;
    }

    public void setGroupUserInfoList(List<GroupUserInfo> groupUserInfoList) {
        this.groupUserInfoList = groupUserInfoList;
    }

    public void addOrUpdateGroupUserInfo(GroupUserInfo groupUserInfo) {
        int i = 0;
        for (; i < groupUserInfoList.size(); ++i) {
            if (groupUserInfo.getUserId().equals(groupUserInfoList.get(i).getUserId())) {
                groupUserInfoList.get(i).setRole(groupUserInfo.getRole());
                break;
            }
        }

        if (i == groupUserInfoList.size()) {
            groupUserInfoList.add(groupUserInfo);
        }
    }

    public void deleteMember(int position) {
        groupUserInfoList.remove(position);
    }

    public void deleteMember(String userId) {
        for (int i = 0; i < groupUserInfoList.size(); ++i) {
            if (userId.equals(groupUserInfoList.get(i).getUserId())) {
                groupUserInfoList.remove(i);
                return;
            }
        }
    }

    public int getRoleCnt() {
        int ret = 0;
        for (int i = 0; i < groupUserInfoList.size(); ++i) {
            if ("admin".equals(groupUserInfoList.get(i).getRole())) {
                ++ret;
            }
        }

        return ret;
    }

    public void updateOneItem(GroupUserInfo groupUserInfo) {
        for (int i = 0; i < groupUserInfoList.size(); ++i) {
            if (groupUserInfoList.get(i).getUserId().equals(groupUserInfo.getUserId())) {
                groupUserInfoList.get(i).setRole(groupUserInfo.getRole());
                break;
            }
        }
    }
}