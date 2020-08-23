package com.ctfww.module.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.UserSendInviteListAdapter;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.GroupInviteInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class UserSendInviteListFragment extends Fragment {
    private final static String TAG = "UserInviteListFragment";

    TextView mNoInvitePrompt;
    RecyclerView mUserSendInviteListView;
    UserSendInviteListAdapter mUserSendInviteListAdapter;

    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.user_invite_list_fragment, container, false);
        initViews(mV);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return mV;
    }

    private void initViews(View v) {
        mNoInvitePrompt = v.findViewById(R.id.group_prompt_no_invite);
        mUserSendInviteListView = v.findViewById(R.id.group_invite_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mUserSendInviteListView.setLayoutManager(layoutManager);
        List<GroupInviteInfo> inviteInfoList = DBQuickEntry.getSelfSendInviteList();
        mUserSendInviteListAdapter = new UserSendInviteListAdapter(inviteInfoList);
        mUserSendInviteListView.setAdapter(mUserSendInviteListAdapter);
        if (inviteInfoList.isEmpty()) {
            mNoInvitePrompt.setVisibility(View.VISIBLE);
            mUserSendInviteListView.setVisibility(View.GONE);
        }
        else {
            mNoInvitePrompt.setVisibility(View.GONE);
            mUserSendInviteListView.setVisibility(View.VISIBLE);
        }
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("finish_invite_syn".equals(messageEvent.getMessage())) {
            List<GroupInviteInfo> inviteInfoList = DBQuickEntry.getSelfSendInviteList();
            mUserSendInviteListAdapter.setList(inviteInfoList);
            mUserSendInviteListAdapter.notifyDataSetChanged();
            if (inviteInfoList.isEmpty()) {
                mNoInvitePrompt.setVisibility(View.VISIBLE);
                mUserSendInviteListView.setVisibility(View.GONE);
            }
            else {
                mNoInvitePrompt.setVisibility(View.GONE);
                mUserSendInviteListView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}

class UserSendInviteListFragmentData {
    List<GroupInviteInfo> groupInviteInfoList = new ArrayList<>();
    public UserSendInviteListFragmentData() {

    }

    public void setGroupInviteInfoList(List<GroupInviteInfo> groupInviteInfoList) {
        this.groupInviteInfoList = groupInviteInfoList;
    }

    public List<GroupInviteInfo> getGroupInviteInfoList() {
        return groupInviteInfoList;
    }

    public void delete(int position) {
        if (groupInviteInfoList.size() > position) {
            groupInviteInfoList.remove(position);
        }
    }
}
