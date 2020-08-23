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
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.UserSelectUserListAdapter;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
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
    RecyclerView mUserSelectUserListView;
    UserSelectUserListAdapter mUserSelectUserListAdapter;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_select_user_activity);
        initViews();
        setOnClickListener();
        EventBus.getDefault().register(this);
        Airship.getInstance().synGroupUserInfoFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_top_back);
        mTittle = findViewById(R.id.user_top_tittle);
        mTittle.setText("选择成员");
        mConfirm = findViewById(R.id.user_top_addition);
        mConfirm.setText("确定");
        mConfirm.setVisibility(View.VISIBLE);
        mUserSelectUserListView = findViewById(R.id.user_group_user_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mUserSelectUserListView.setLayoutManager(layoutManager);
        List<GroupUserInfo> groupUserInfoList = DBQuickEntry.getWorkingGroupUserList();
        mUserSelectUserListAdapter = new UserSelectUserListAdapter(groupUserInfoList);
        mUserSelectUserListView.setAdapter(mUserSelectUserListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("finish_group_user_syn".equals(messageEvent.getMessage())) {
            List<GroupUserInfo> groupUserInfoList = DBQuickEntry.getWorkingGroupUserList();
            mUserSelectUserListAdapter.setList(groupUserInfoList);
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
            String userId = mUserSelectUserListAdapter.getUserId();
            if (TextUtils.isEmpty(userId)) {
                DialogUtils.onlyPrompt("请选择成员！", this);
                return;
            }

            EventBus.getDefault().post(new MessageEvent("selected_user", userId));

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}