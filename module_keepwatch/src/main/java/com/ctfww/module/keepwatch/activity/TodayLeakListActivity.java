package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.FinishStatusListAdapter;

import java.util.List;

@Route(path = "/keepwatch/todayLeakList")
public class TodayLeakListActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "TodayLeakListActivity";

    private ImageView mBack;
    private TextView mTittle;

    private RecyclerView mStatisticsListView;
    private FinishStatusListAdapter mFinishStatusListAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.today_leak_list_activity);
        initViews();
        setOnClickListener();

        com.ctfww.module.assignment.datahelper.airship.Airship.getInstance().synFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("今日漏检点");

        mStatisticsListView = findViewById(R.id.finish_status_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mStatisticsListView.setLayoutManager(layoutManager);

        List<TodayAssignment> todayAssignmentList = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getLeakList(MyDateTimeUtils.getTodayStartTime());
        mFinishStatusListAdapter = new FinishStatusListAdapter(todayAssignmentList, this);
        mStatisticsListView.setAdapter(mFinishStatusListAdapter);
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
}