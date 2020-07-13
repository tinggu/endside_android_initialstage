package com.guoliang.module.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.gson.reflect.TypeToken;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.module.user.R;
import com.guoliang.module.user.adapter.UserGroupMgtAdapter;
import com.guoliang.module.user.adapter.UserSelectGroupListAdapter;
import com.guoliang.module.user.datahelper.NetworkHelper;
import com.guoliang.module.user.entity.UserGroupInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Route(path = "/user/selectGroup")
public class UserSelectGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UserSelectGroupActivity";

    ImageView mBack;
    TextView mTittle;
    TextView mConfirm;
    TextView mNoGroupPrompt;
    RecyclerView mGroupListView;

    UserSelectGroupListAdapter mUserSelectGroupListAdapter;

    UserSelectGroupActivityData mUserSelectGroupActivityData = new UserSelectGroupActivityData();


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_select_group_activity);
        initViews();
        setOnClickListener();
        getGroupList();
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText("群组管理");
        mConfirm = findViewById(R.id.user_top_bar_right_btn);
        mConfirm.setText("确定");
        mNoGroupPrompt = findViewById(R.id.user_prompt_no_create_group);
        mNoGroupPrompt.setVisibility(View.GONE);
        mGroupListView = findViewById(R.id.user_group_list);
        mGroupListView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mGroupListView.setLayoutManager(layoutManager);
        mGroupId = SPStaticUtils.getString("working_group_id");
        mUserSelectGroupListAdapter = new UserSelectGroupListAdapter(mUserSelectGroupActivityData.getUserGroupInfoList(), mGroupId);
        mGroupListView.setAdapter(mUserSelectGroupListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    private void getGroupList() {
        String userId = SPStaticUtils.getString("user_open_id");
        NetworkHelper.getInstance().getUserGroupInfo(userId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mUserSelectGroupActivityData.setUserGroupInfoList((ArrayList<UserGroupInfo>) obj);
                mUserSelectGroupListAdapter.setList(mUserSelectGroupActivityData.getUserGroupInfoList(), mGroupId);
                mUserSelectGroupListAdapter.notifyDataSetChanged();
                mGroupListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "no group");
                mNoGroupPrompt.setVisibility(View.VISIBLE);
            }
        });
    }

    private String mGroupId;
    @Subscribe(threadMode= ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("select_group".equals(messageEvent.getMessage())) {
            LogUtils.i(TAG, "onGetMessage: messageEvent = " + messageEvent.toString());
            mGroupId = messageEvent.getValue();
            mUserSelectGroupListAdapter.setList(mUserSelectGroupActivityData.getUserGroupInfoList(), mGroupId);
            mUserSelectGroupListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        } else if (id == mConfirm.getId()) {
            UserGroupInfo userGroupInfo = mUserSelectGroupActivityData.getGroup(mGroupId);
            if (userGroupInfo != null) {
                SPStaticUtils.put("working_group_id", userGroupInfo.getGroupId());
                SPStaticUtils.put("role", userGroupInfo.getRole());
                SPStaticUtils.put("working_group_name", userGroupInfo.getGroupName());
                EventBus.getDefault().post(new MessageEvent("bind_group"));
            }

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

class UserSelectGroupActivityData {
    List<UserGroupInfo> userGroupInfoList;
    public UserSelectGroupActivityData() {
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

    public UserGroupInfo getGroup(int position) {
        if (position == -1 || position >= userGroupInfoList.size()) {
            return null;
        }

        return userGroupInfoList.get(position);
    }

    public UserGroupInfo getGroup(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }

        String oldGroupId = SPStaticUtils.getString("working_group_id");
        if (groupId.equals(oldGroupId)) {
            return null;
        }

        for (int i = 0; i < userGroupInfoList.size(); ++i) {
            UserGroupInfo userGroupInfo = userGroupInfoList.get(i);
            if (groupId.equals(userGroupInfo.getGroupId())) {
                return userGroupInfo;
            }
        }

        return null;
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
