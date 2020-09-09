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

import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.adapter.RouteAssignmentListAdapter;
import com.ctfww.module.assignment.adapter.RouteTodayAssignmentListAdapter;
import com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.assignment.entity.RouteAssignment;

import java.util.List;

public class PeriodRouteAssignmentListFragment extends Fragment {
    private final static String TAG = "PeriodRouteAssignmentListFragment";

    TextView mNoAssignmentPrompt;
    RecyclerView mAssignmentListView;
    RouteAssignmentListAdapter mAssignmentListAdapter;

    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.period_assignment_list_fragment, container, false);
        initViews(mV);

        return mV;
    }

    private void initViews(View v) {
        mNoAssignmentPrompt = v.findViewById(R.id.prompt_no_assignment);
        mAssignmentListView = v.findViewById(R.id.assignment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAssignmentListView.setLayoutManager(layoutManager);
        List<RouteAssignment> assignmentList = DBQuickEntry.getWorkingRouteAssignmentList();
        mAssignmentListAdapter = new RouteAssignmentListAdapter(assignmentList);
        mAssignmentListView.setAdapter(mAssignmentListAdapter);
        if (assignmentList.isEmpty()) {
            mNoAssignmentPrompt.setVisibility(View.VISIBLE);
            mAssignmentListView.setVisibility(View.GONE);
        }
        else {
            mNoAssignmentPrompt.setVisibility(View.GONE);
            mAssignmentListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void update() {
        List<RouteAssignment> assignmentList = DBQuickEntry.getWorkingRouteAssignmentList();
        mAssignmentListAdapter = new RouteAssignmentListAdapter(assignmentList);
        mAssignmentListAdapter.notifyDataSetChanged();

        if (assignmentList.isEmpty()) {
            mNoAssignmentPrompt.setVisibility(View.VISIBLE);
            mAssignmentListView.setVisibility(View.GONE);
        }
        else {
            mNoAssignmentPrompt.setVisibility(View.GONE);
            mAssignmentListView.setVisibility(View.VISIBLE);
        }
    }
}
