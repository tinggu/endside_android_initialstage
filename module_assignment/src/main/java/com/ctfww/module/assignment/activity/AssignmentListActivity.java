package com.ctfww.module.assignment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.adapter.AssignmentListAdapter;
import com.ctfww.module.assignment.datahelper.airship.Airship;
import com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.assignment.datahelper.sp.Const;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.commonlib.entity.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/assignment/list")
public class AssignmentListActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "AssignmentListActivity";

    private ImageView mBack;
    private TextView mTittle;

    TextView mNoAssignmentPrompt;
    RecyclerView mAssignmentListView;
    AssignmentListAdapter mAssignmentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_list_activity);
        initViews();
        setOnClickListener();
        EventBus.getDefault().register(this);

        Airship.getInstance().synAssignmentFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("任务计划");

        mNoAssignmentPrompt = findViewById(R.id.prompt_no_assignment);
        mAssignmentListView = findViewById(R.id.assignment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAssignmentListView.setLayoutManager(layoutManager);
        List<AssignmentInfo> assignmentList = DBQuickEntry.getAssignmentList();
        mAssignmentListAdapter = new AssignmentListAdapter(assignmentList);
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

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onGetMessage(MessageEvent messageEvent) {
        if (Const.FINISH_ASSIGNMENT_SYN.equals(messageEvent.getMessage())) {
            List<AssignmentInfo> assignmentList = DBQuickEntry.getAssignmentList();
            mAssignmentListAdapter = new AssignmentListAdapter(assignmentList);
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
}