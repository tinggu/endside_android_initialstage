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
import com.ctfww.module.user.adapter.UserNoticeListAdapter;
import com.ctfww.module.user.adapter.UserReceiveInviteListAdapter;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.NoticeInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class UserNoticeListFragment extends Fragment {
    private final static String TAG = "UserNoticeListFragment";

    TextView mNoNoticePrompt;
    RecyclerView mNoticeListView;
    UserNoticeListAdapter mNoticeListAdapter;
    List<NoticeInfo> mNoticeInfoList;

    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.user_notice_list_fragment, container, false);
        initViews(mV);

        getNotice();

        return mV;
    }

    private void initViews(View v) {
        mNoNoticePrompt = v.findViewById(R.id.user_prompt_no_notice);
        mNoticeListView = v.findViewById(R.id.user_notice_list);
        mNoticeListView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mNoticeListView.setLayoutManager(layoutManager);
        mNoticeInfoList = new ArrayList<>();
        mNoticeListAdapter = new UserNoticeListAdapter(mNoticeInfoList);
        mNoticeListView.setAdapter(mNoticeListAdapter);
    }

    private void getNotice() {
        NetworkHelper.getInstance().getNotice(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mNoticeInfoList = (ArrayList<NoticeInfo>)obj;
                if (mNoticeInfoList.isEmpty()) {
                    mNoticeListView.setVisibility(View.GONE);
                    mNoNoticePrompt.setVisibility(View.VISIBLE);
                    return;
                }

                mNoticeListAdapter.setList(mNoticeInfoList);
                mNoticeListAdapter.notifyDataSetChanged();
                mNoticeListView.setVisibility(View.VISIBLE);
                mNoNoticePrompt.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getNotice fail: code = " + code);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
