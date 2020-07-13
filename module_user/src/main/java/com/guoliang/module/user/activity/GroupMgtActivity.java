package com.guoliang.module.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.module.user.R;
import com.guoliang.module.user.adapter.UserGroupMgtAdapter;
import com.guoliang.module.user.bean.GroupUserBean;
import com.guoliang.module.user.datahelper.NetworkHelper;
import com.guoliang.module.user.entity.UserGroupInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.finishAllActivities;

@Route(path = "/user/group")
public class GroupMgtActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "GroupMgtActivity";

    private final static int REQUEST_CODE_CREATE_OR_UPDATE_GROUP = 1;

    ImageView mBack;
    TextView mTittle;
    TextView mCreate;
    TextView mNoGroupPrompt;
    RecyclerView mGroupListView;

    Button mSendInvite;
    Button mReceiveInvite;

    UserGroupMgtAdapter mGroupMgtAdapter;

    GroupMgtActivityData mGroupMgtActivityData = new GroupMgtActivityData();


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_group_mgt_activity);
        initViews();
        setOnClickListener();
        getGroupList();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText("群组管理");
        mCreate = findViewById(R.id.user_top_bar_right_btn);
        mCreate.setText("创建");
        mNoGroupPrompt = findViewById(R.id.user_prompt_no_create_group);
        mNoGroupPrompt.setVisibility(View.GONE);
        mGroupListView = findViewById(R.id.user_group_list);
        mGroupListView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mGroupListView.setLayoutManager(layoutManager);
        mGroupMgtAdapter = new UserGroupMgtAdapter(mGroupMgtActivityData.getUserGroupInfoList(), this);
        mGroupListView.setAdapter(mGroupMgtAdapter);
        mSendInvite = findViewById(R.id.group_send_invite);
        mReceiveInvite = findViewById(R.id.group_invite);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mCreate.setOnClickListener(this);
        mSendInvite.setOnClickListener(this);
        mReceiveInvite.setOnClickListener(this);
    }

    private void getGroupList() {
        String userId = SPStaticUtils.getString("user_open_id");
        NetworkHelper.getInstance().getUserGroupInfo(userId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mGroupMgtActivityData.setUserGroupInfoList((ArrayList<UserGroupInfo>)obj);
                mGroupMgtAdapter.setList(mGroupMgtActivityData.getUserGroupInfoList());
                mGroupMgtAdapter.notifyDataSetChanged();
                mGroupListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "no group");
                mNoGroupPrompt.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mCreate.getId()) {
            Intent intent = new Intent(this, GroupCreateOrUpdateActivity.class);
            intent.putExtra("isCreate", true);
            startActivityForResult(intent, REQUEST_CODE_CREATE_OR_UPDATE_GROUP);
        }
        else if (id == mSendInvite.getId()) {

        }
        else if (id == mReceiveInvite.getId()) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult: requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == 1) { // 创建群组界面
            if (resultCode==RESULT_OK) {
                String userGroupInfoStr = data.getStringExtra("userGroupInfo");
                UserGroupInfo userGroupInfo = GsonUtils.fromJson(userGroupInfoStr, UserGroupInfo.class);
                mGroupMgtActivityData.addOrUpdateGroup(userGroupInfo);
                mGroupMgtAdapter.setList(mGroupMgtActivityData.getUserGroupInfoList());
                mGroupMgtAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == 2) { // 显示某群组界面
            if (resultCode==RESULT_OK) {
                String groupChangeStatus = data.getStringExtra("groupChangeStatus");
                LogUtils.i(TAG, "onActivityResult: groupChangeStatus = " + groupChangeStatus);
                if ("update".equals(groupChangeStatus)) {
                    String userGroupInfoStr = data.getStringExtra("userGroupInfo");
                    UserGroupInfo userGroupInfo = GsonUtils.fromJson(userGroupInfoStr, UserGroupInfo.class);
                    mGroupMgtActivityData.addOrUpdateGroup(userGroupInfo);
                    mGroupMgtAdapter.setList(mGroupMgtActivityData.getUserGroupInfoList());
                    mGroupMgtAdapter.notifyDataSetChanged();
                }
                else  if ("delete".equals(groupChangeStatus)) {
                    String groupId = data.getStringExtra("groupId");
                    mGroupMgtActivityData.deleteGroup(groupId);
                    mGroupMgtAdapter.setList(mGroupMgtActivityData.getUserGroupInfoList());
                    mGroupMgtAdapter.notifyDataSetChanged();
                    LogUtils.i(TAG, "onActivityResult: groupId = " + groupId);
                }
            }
        }
        else if (requestCode == 3) { // 邀请接受界面，可能会加入多个群组
            if (resultCode==RESULT_OK) {
                String userGroupInfoListStr = data.getStringExtra("userGroupInfoList");
                Type type = new TypeToken<List<UserGroupInfo>>() {}.getType();
                List<UserGroupInfo> userGroupInfoList = GsonUtils.fromJson(userGroupInfoListStr, type);
                for (int i = 0; i < userGroupInfoList.size(); ++i) {
                    mGroupMgtActivityData.addOrUpdateGroup(userGroupInfoList.get(i));
                }
                mGroupMgtAdapter.setList(mGroupMgtActivityData.getUserGroupInfoList());
                mGroupMgtAdapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteGroup(final int position, final String groupId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupMgtActivity.this);
//        builder.setTitle("提示：");
        builder.setMessage("解除群组，你将不能再看到群组的任何信息。确认解除？");
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(true);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
                NetworkHelper.getInstance().deleteGroup(groupId, new IUIDataHelperCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        mGroupMgtActivityData.deleteGroup(position);
                        mGroupMgtAdapter.setList(mGroupMgtActivityData.getUserGroupInfoList());
                        mGroupMgtAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int code) {
                        ToastUtils.showShort("删除失败，请确认网络是否正常！");
                    }
                });
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        //设置中立按钮
//        builder.setNeutralButton("保密", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "你选择了中立", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });


        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        //对话框显示的监听事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
//                Log.e(TAG, "对话框显示了");
            }
        });
        //对话框消失的监听事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                Log.e(TAG, "对话框消失了");
            }
        });
        dialog.show();                              //显示对话框
    }
}

class GroupMgtActivityData {
    List<UserGroupInfo> userGroupInfoList;
    public GroupMgtActivityData() {
        userGroupInfoList = new ArrayList<>();
    }

    public List<UserGroupInfo> getUserGroupInfoList() {
        return userGroupInfoList;
    }

    public void setUserGroupInfoList(List<UserGroupInfo> userGroupInfoList) {
        this.userGroupInfoList = userGroupInfoList;
    }

    public void addOrUpdateGroup(UserGroupInfo userGroupInfo) {
        int i = 0;
        for (; i < userGroupInfoList.size(); ++i) {
            if (userGroupInfo.getGroupId().equals(userGroupInfoList.get(i).getGroupId())) {
                userGroupInfoList.get(i).setGroupName(userGroupInfo.getGroupName());
                userGroupInfoList.get(i).setRole(userGroupInfo.getRole());
                break;
            }
        }

        if (i == userGroupInfoList.size()) {
            userGroupInfoList.add(userGroupInfo);
        }
    }

    public void deleteGroup(int position) {
        userGroupInfoList.remove(position);
    }

    public void deleteGroup(String groupId) {
       for (int i = 0; i < userGroupInfoList.size(); ++i) {
           if (userGroupInfoList.get(i).getGroupId().equals(groupId)) {
               userGroupInfoList.remove(i);
               return;
           }
       }
    }
}
