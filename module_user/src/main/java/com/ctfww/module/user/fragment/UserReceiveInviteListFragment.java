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
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
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

    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.user_invite_list_fragment, container, false);
        initViews(mV);

        return mV;
    }

    private void initViews(View v) {
        mNoInvitePrompt = v.findViewById(R.id.group_prompt_no_invite);

        mInviteListView = v.findViewById(R.id.group_invite_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mInviteListView.setLayoutManager(layoutManager);
        List<GroupInviteInfo> groupInviteInfoList = DBQuickEntry.getSelfReceivedInviteList();
        mUserReceiveInviteListAdapter = new UserReceiveInviteListAdapter(groupInviteInfoList);
        mInviteListView.setAdapter(mUserReceiveInviteListAdapter);
        if (groupInviteInfoList.isEmpty()) {
            mNoInvitePrompt.setVisibility(View.VISIBLE);
            mInviteListView.setVisibility(View.GONE);
        }
        else {
            mNoInvitePrompt.setVisibility(View.GONE);
            mInviteListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onFinshInviteSyn() {
        List<GroupInviteInfo> groupInviteInfoList = DBQuickEntry.getSelfReceivedInviteList();
        mUserReceiveInviteListAdapter.setList(groupInviteInfoList);
        mUserReceiveInviteListAdapter.notifyDataSetChanged();
        if (groupInviteInfoList.isEmpty()) {
            mNoInvitePrompt.setVisibility(View.VISIBLE);
            mInviteListView.setVisibility(View.GONE);
        }
        else {
            mNoInvitePrompt.setVisibility(View.GONE);
            mInviteListView.setVisibility(View.VISIBLE);
        }
    }
}
