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

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.UserReceiveInviteListAdapter;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.GroupInviteInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class UserReceiveInviteListFragment extends Fragment {
    private final static String TAG = "UserReceiveInviteListFragment";

    TextView mNoInvitePrompt;
    RecyclerView mInviteListView;
    UserReceiveInviteListAdapter mUserReceiveInviteListAdapter;
    UserReceiveInviteListFragmentData mUserReceiveInviteListFragmentData = new UserReceiveInviteListFragmentData();

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
        mUserReceiveInviteListAdapter = new UserReceiveInviteListAdapter(mUserReceiveInviteListFragmentData.getGroupInviteInfoList());
        mInviteListView.setAdapter(mUserReceiveInviteListAdapter);
    }

    private void getInviteList() {
        NetworkHelper.getInstance().getInvite(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mUserReceiveInviteListFragmentData.setGroupInviteInfoList((ArrayList<GroupInviteInfo>)obj);
                mUserReceiveInviteListAdapter.setList(mUserReceiveInviteListFragmentData.getGroupInviteInfoList());
                mUserReceiveInviteListAdapter.notifyDataSetChanged();
                mInviteListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "no receive invite");
                mNoInvitePrompt.setVisibility(View.VISIBLE);
            }
        });
    }

    public void updateData(GroupInviteInfo groupInviteInfo) {
        mUserReceiveInviteListFragmentData.update(groupInviteInfo);
        mUserReceiveInviteListAdapter.setList(mUserReceiveInviteListFragmentData.getGroupInviteInfoList());
        mUserReceiveInviteListAdapter.notifyDataSetChanged();
    }

    public void deleteData(int position) {
        mUserReceiveInviteListFragmentData.delete(position);
        mUserReceiveInviteListAdapter.setList(mUserReceiveInviteListFragmentData.getGroupInviteInfoList());
        mUserReceiveInviteListAdapter.notifyDataSetChanged();
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if ("user_update_receive_invite".equals(msg)) {
            GroupInviteInfo groupInviteInfo = GsonUtils.fromJson(messageEvent.getValue(), GroupInviteInfo.class);
            updateData(groupInviteInfo);
            mUserReceiveInviteListAdapter.setList(mUserReceiveInviteListFragmentData.getGroupInviteInfoList());
            mUserReceiveInviteListAdapter.notifyDataSetChanged();
        }
        else if ("user_delete_receive_invite".equals(msg)){
            mUserReceiveInviteListFragmentData.delete(GlobeFun.parseInt(messageEvent.getValue()));
            mUserReceiveInviteListAdapter.setList(mUserReceiveInviteListFragmentData.getGroupInviteInfoList());
            mUserReceiveInviteListAdapter.notifyDataSetChanged();
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

class UserReceiveInviteListFragmentData {
    List<GroupInviteInfo> groupInviteInfoList = new ArrayList<>();
    public UserReceiveInviteListFragmentData() {

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

    public void update(GroupInviteInfo groupInviteInfo) {
        for (int i = 0; i < groupInviteInfoList.size(); ++i) {
            if (groupInviteInfo.getInviteId().equals(groupInviteInfoList.get(i).getInviteId())) {
                groupInviteInfoList.get(i).setStatus(groupInviteInfo.getStatus());
                break;
            }
        }
    }
}
