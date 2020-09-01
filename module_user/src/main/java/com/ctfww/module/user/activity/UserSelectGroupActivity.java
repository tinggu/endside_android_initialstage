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
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.UserSelectGroupListAdapter;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.GroupUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@Route(path = "/user/selectGroup")
public class UserSelectGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UserSelectGroupActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mConfirm;
    private TextView mNoGroupPrompt;
    private RecyclerView mUserSelectGroupListView;
    private UserSelectGroupListAdapter mUserSelectGroupListAdapter;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_select_group_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);
        Airship.getInstance().synGroupUserInfoFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText("群组管理");
        mConfirm = findViewById(R.id.user_top_bar_right_btn);
        mConfirm.setText("确定");
        mNoGroupPrompt = findViewById(R.id.user_prompt_no_create_group);
        mUserSelectGroupListView = findViewById(R.id.user_group_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mUserSelectGroupListView.setLayoutManager(layoutManager);
        List<GroupUserInfo> groupUserInfoList = DBQuickEntry.getSelfGroupUserList();
        mUserSelectGroupListAdapter = new UserSelectGroupListAdapter(groupUserInfoList);
        mUserSelectGroupListView.setAdapter(mUserSelectGroupListAdapter);
        if (groupUserInfoList.isEmpty()) {
            mNoGroupPrompt.setVisibility(View.VISIBLE);
            mUserSelectGroupListView.setVisibility(View.GONE);
        }
        else {
            mNoGroupPrompt.setVisibility(View.GONE);
            mUserSelectGroupListView.setVisibility(View.VISIBLE);
        }
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("finish_group_user_syn".equals(messageEvent.getMessage())) {
            List<GroupUserInfo> infoList = DBQuickEntry.getSelfGroupUserList();
            mUserSelectGroupListAdapter.setList(infoList);
            mUserSelectGroupListAdapter.notifyDataSetChanged();
            if (infoList.isEmpty()) {
                mNoGroupPrompt.setVisibility(View.VISIBLE);
                mUserSelectGroupListView.setVisibility(View.GONE);
            }
            else {
                mNoGroupPrompt.setVisibility(View.GONE);
                mUserSelectGroupListView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        } else if (id == mConfirm.getId()) {
            String groupId = mUserSelectGroupListAdapter.getGroupId();
            if (TextUtils.isEmpty(groupId) || groupId.equals(SPStaticUtils.getString(Const.WORKING_GROUP_ID))) {
                return;
            }

            SPStaticUtils.put(Const.WORKING_GROUP_ID, groupId);
            EventBus.getDefault().post(new MessageEvent("bind_group"));

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
