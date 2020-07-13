package com.ctfww.module.keepwatch.fragment;

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
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.KeepWatchAssignmentListAdapter;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class KeepWatchPeriodAssignmentListFragment extends Fragment {
    private final static String TAG = "KeepWatchPeriodAssignmentListFragment";

    TextView mNoAssignmentPrompt;
    RecyclerView mAssignmentListView;
    KeepWatchAssignmentListAdapter mKeepWatchAssignmentListAdapter;
    List<KeepWatchAssignment> mKeepWatchAssignmentList = new ArrayList<>();

    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_period_assignment_list_fragment, container, false);
        initViews(mV);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return mV;
    }

    private void initViews(View v) {
        mNoAssignmentPrompt = v.findViewById(R.id.keepwatch_prompt_no_assignment);
        mNoAssignmentPrompt.setVisibility(View.GONE);
        mAssignmentListView = v.findViewById(R.id.keepwatch_assignment_list);
        mAssignmentListView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAssignmentListView.setLayoutManager(layoutManager);
        mKeepWatchAssignmentListAdapter = new KeepWatchAssignmentListAdapter(mKeepWatchAssignmentList, "period");
        mAssignmentListView.setAdapter(mKeepWatchAssignmentListAdapter);
    }

    private void getAssignmentList() {
        NetworkHelper.getInstance().getKeepWatchPeriodAssignmentList(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeepWatchAssignmentList = (List<KeepWatchAssignment>)obj;
                mKeepWatchAssignmentListAdapter.setList(mKeepWatchAssignmentList);
                mKeepWatchAssignmentListAdapter.notifyDataSetChanged();
                if (mKeepWatchAssignmentList.isEmpty()) {
                    mNoAssignmentPrompt.setVisibility(View.VISIBLE);
                }
                else {
                    mAssignmentListView.setVisibility(View.VISIBLE);
                }
                LogUtils.i(TAG, "getAssignment success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getAssignment fail: code = " + code);
                if (mKeepWatchAssignmentList.isEmpty()) {
                    mNoAssignmentPrompt.setVisibility(View.VISIBLE);
                }
                else {
                    mAssignmentListView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if ("keepwatch_has_delete_assignment".equals(msg)) {
            KeepWatchAssignment keepWatchAssignment = GsonUtils.fromJson(messageEvent.getValue(), KeepWatchAssignment.class);
            for (int i = 0; i < mKeepWatchAssignmentList.size(); ++i) {
                KeepWatchAssignment keepWatchAssignmentThis = mKeepWatchAssignmentList.get(i);
                if (keepWatchAssignment.getDeskId() == keepWatchAssignmentThis.getDeskId() && keepWatchAssignment.getUserId() == keepWatchAssignmentThis.getUserId()) {
                    mKeepWatchAssignmentList.remove(i);
                    break;
                }
            }

            mKeepWatchAssignmentListAdapter.setList(mKeepWatchAssignmentList);
            mKeepWatchAssignmentListAdapter.notifyDataSetChanged();
        }
        else if ("transfer_assignment".equals(msg)) {
            getAssignmentList();
        }
        else if ("take_back_assignment".equals(msg)) {
            getAssignmentList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAssignmentList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
