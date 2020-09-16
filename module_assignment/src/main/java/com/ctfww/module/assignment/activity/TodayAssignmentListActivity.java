package com.ctfww.module.assignment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.adapter.TodayAssignmentListAdapter;
import com.ctfww.module.assignment.datahelper.airship.Airship;
import com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.assignment.datahelper.sp.Const;
import com.ctfww.module.assignment.entity.TodayAssignment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/assignment/todayAssignment")
public class TodayAssignmentListActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "TodayAssignmentListActivity";

    private ImageView mBack;
    private TextView mTittle;

    TextView mNoAssignmentPrompt;
    RecyclerView mAssignmentListView;
    TodayAssignmentListAdapter mTodayAssignmentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_list_activity);
        initViews();
        setOnClickListener();
        EventBus.getDefault().register(this);

        Airship.getInstance().synAssignmentFromCloud();
        Airship.getInstance().synTodayAssignmentFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("今日任务");

        mNoAssignmentPrompt = findViewById(R.id.prompt_no_assignment);
        mAssignmentListView = findViewById(R.id.assignment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAssignmentListView.setLayoutManager(layoutManager);
        List<TodayAssignment> assignmentList = DBQuickEntry.getTodayAssignmentList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        mTodayAssignmentListAdapter = new TodayAssignmentListAdapter(assignmentList);
        mAssignmentListView.setAdapter(mTodayAssignmentListAdapter);
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
        if (Const.FINISH_ASSIGNMENT_SYN.equals(messageEvent.getMessage())
        || Const.FINISH_TODAY_ASSIGNMENT_SYN.equals(messageEvent.getMessage())) {
            List<TodayAssignment> assignmentList = DBQuickEntry.getTodayAssignmentList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
            mTodayAssignmentListAdapter = new TodayAssignmentListAdapter(assignmentList);
            mTodayAssignmentListAdapter.notifyDataSetChanged();
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