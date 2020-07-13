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
import com.ctfww.module.user.entity.GroupInviteInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class UserSendInviteListFragment extends Fragment {
    private final static String TAG = "UserInviteListFragment";

    TextView mNoInvitePrompt;
    RecyclerView mInviteListView;
    UserSendInviteListAdapter mUserSendInviteListAdapter;
    UserSendInviteListFragmentData mUserSendInviteListFragmentData = new UserSendInviteListFragmentData();

    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.user_invite_list_fragment, container, false);
        initViews(mV);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        getInviteList();
        return mV;
    }

    private void initViews(View v) {
        mNoInvitePrompt = v.findViewById(R.id.group_prompt_no_invite);
        mNoInvitePrompt.setVisibility(View.GONE);
        mInviteListView = v.findViewById(R.id.group_invite_list);
        mInviteListView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mInviteListView.setLayoutManager(layoutManager);
        mUserSendInviteListAdapter = new UserSendInviteListAdapter(mUserSendInviteListFragmentData.getGroupInviteInfoList());
        mInviteListView.setAdapter(mUserSendInviteListAdapter);
    }

    private void getInviteList() {
        NetworkHelper.getInstance().getSendInvite(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<GroupInviteInfo> groupInviteInfoList = (ArrayList<GroupInviteInfo>)obj;
                mUserSendInviteListFragmentData.setGroupInviteInfoList(groupInviteInfoList);
                mUserSendInviteListAdapter.setList(mUserSendInviteListFragmentData.getGroupInviteInfoList());
                mUserSendInviteListAdapter.notifyDataSetChanged();
                if (groupInviteInfoList.isEmpty()) {
                    mInviteListView.setVisibility(View.GONE);
                    mNoInvitePrompt.setVisibility(View.VISIBLE);
                }
                else {
                    mInviteListView.setVisibility(View.VISIBLE);
                    mNoInvitePrompt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getInviteList: code = " + code);
                if (mUserSendInviteListFragmentData.getGroupInviteInfoList().isEmpty()) {
                    mInviteListView.setVisibility(View.GONE);
                    mNoInvitePrompt.setVisibility(View.VISIBLE);
                }
                else {
                    mInviteListView.setVisibility(View.VISIBLE);
                    mNoInvitePrompt.setVisibility(View.GONE);
                }
            }
        });
    }

    public void deleteData(int position) {
        mUserSendInviteListFragmentData.delete(position);
        mUserSendInviteListAdapter.setList(mUserSendInviteListFragmentData.getGroupInviteInfoList());
        mUserSendInviteListAdapter.notifyDataSetChanged();
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if ("user_delete_send_invite".equals(msg)) {
            deleteData(GlobeFun.parseInt(messageEvent.getValue()));
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
