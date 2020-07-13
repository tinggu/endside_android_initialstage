package com.ctfww.module.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.UserSelectUserListAdapter;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.GroupUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/user/selectUser")
public class UserSelectUserActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UserSelectUserActivity";

    ImageView mBack;
    TextView mTittle;
    TextView mConfirm;
    RecyclerView mUserListView;

    UserSelectUserListAdapter mUserSelectUserListAdapter;

    UserSelectUserActivityData mUserSelectUserActivityData = new UserSelectUserActivityData();


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_select_user_activity);
        initViews();
        setOnClickListener();
        getUserList();
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.user_top_back);
        mTittle = findViewById(R.id.user_top_tittle);
        mTittle.setText("选择成员");
        mConfirm = findViewById(R.id.user_top_addition);
        mConfirm.setText("确定");
        mConfirm.setVisibility(View.VISIBLE);
        mUserListView = findViewById(R.id.user_group_user_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mUserListView.setLayoutManager(layoutManager);
        mUserSelectUserListAdapter = new UserSelectUserListAdapter(mUserSelectUserActivityData.getGroupUserInfoList());
        mUserListView.setAdapter(mUserSelectUserListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    private void getUserList() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        NetworkHelper.getInstance().getGroupUserInfo(groupId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mUserSelectUserActivityData.setGroupUserInfoList((ArrayList<GroupUserInfo>) obj);
                mUserSelectUserListAdapter.setList(mUserSelectUserActivityData.getGroupUserInfoList());
                mUserSelectUserListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "no group");
            }
        });
    }

    private String mGroupUserInfoStr;
    @Subscribe(threadMode= ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("select_user".equals(messageEvent.getMessage())) {
            LogUtils.i(TAG, "onGetMessage: messageEvent = " + messageEvent.toString());
            mGroupUserInfoStr = messageEvent.getValue();
            mUserSelectUserListAdapter.setList(mUserSelectUserActivityData.getGroupUserInfoList());
            mUserSelectUserListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mConfirm.getId()) {
            if (TextUtils.isEmpty(mGroupUserInfoStr)) {
                return;
            }

            EventBus.getDefault().post(new MessageEvent("selected_user", mGroupUserInfoStr));

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

class UserSelectUserActivityData {
    List<GroupUserInfo> groupUserInfoList;
    public UserSelectUserActivityData() {
        groupUserInfoList = new ArrayList<>();
    }

    public List<GroupUserInfo> getGroupUserInfoList() {
        return groupUserInfoList;
    }

    public void setGroupUserInfoList(List<GroupUserInfo> groupUserInfoList) {
        this.groupUserInfoList = groupUserInfoList;
    }

    public GroupUserInfo getUser(int position) {
        if (position == -1 || position >= groupUserInfoList.size()) {
            return null;
        }

        return groupUserInfoList.get(position);
    }

    public GroupUserInfo getUser(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }

        for (int i = 0; i < groupUserInfoList.size(); ++i) {
            GroupUserInfo groupUserInfo = groupUserInfoList.get(i);
            if (userId.equals(groupUserInfo.getUserId())) {
                return groupUserInfo;
            }
        }

        return null;
    }
}
