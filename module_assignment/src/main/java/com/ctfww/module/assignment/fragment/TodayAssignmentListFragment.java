package com.ctfww.module.assignment.fragment;

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

import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.adapter.AssignmentListAdapter;
import com.ctfww.module.assignment.datahelper.airship.AirshipConst;
import com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.assignment.entity.AssignmentInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TodayAssignmentListFragment extends Fragment {
    private final static String TAG = "TodayAssignmentListFragment";

    TextView mNoAssignmentPrompt;
    RecyclerView mAssignmentListView;
    AssignmentListAdapter mAssignmentListAdapter;

    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.today_assignment_list_fragment, container, false);
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
        List<AssignmentInfo> assignmentList = DBQuickEntry.getTodayWorkingAssignmentList();
        mAssignmentListAdapter = new AssignmentListAdapter(assignmentList, "today");
        mAssignmentListView.setAdapter(mAssignmentListAdapter);
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        if (AirshipConst.FINISH_ASSIGNMENT_SYN.equals(messageEvent.getMessage())) {
            List<AssignmentInfo> assignmentList = DBQuickEntry.getTodayWorkingAssignmentList();
            mAssignmentListAdapter.setList(assignmentList);
            mAssignmentListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
