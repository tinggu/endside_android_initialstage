package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.KeepWatchPersonTrendsListAdapter;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/keepwatch/personTrends")
public class KeepWatchPersonTrendsListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "KeepWatchPersonTrendsListActivity";

    private ImageView mBack;
    private TextView mTittle;

    private RecyclerView mPersonTrendsListView;
    private KeepWatchPersonTrendsListAdapter mKeepWatchPersonTrendsListAdapter;
    private List<KeepWatchPersonTrends> mKeepWatchPersonTrendsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_person_trends_list_activity);
        initViews();
        setOnClickListener();

        getPersonTrendsList();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("人员动态");

        mPersonTrendsListView = findViewById(R.id.keepwatch_person_trends_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mPersonTrendsListView.setLayoutManager(layoutManager);

        mKeepWatchPersonTrendsListAdapter = new KeepWatchPersonTrendsListAdapter(mKeepWatchPersonTrendsList, this);
        mPersonTrendsListView.setAdapter(mKeepWatchPersonTrendsListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    private void getPersonTrendsList() {
        long startTime = MyDateTimeUtils.getTodayStartTime();
        long endTime = MyDateTimeUtils.getTodayEndTime();
        NetworkHelper.getInstance().getPersonTrends(1000, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeepWatchPersonTrendsList = (List<KeepWatchPersonTrends>)obj;
                mKeepWatchPersonTrendsListAdapter.setList(mKeepWatchPersonTrendsList);
                mKeepWatchPersonTrendsListAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getPersonTrendsList success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getPersonTrendsList fail: code = " + code);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
    }
}