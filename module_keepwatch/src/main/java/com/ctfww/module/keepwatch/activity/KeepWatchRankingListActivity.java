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
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.KeepWatchRankingListAdapter;
import com.ctfww.module.keepwatch.entity.Ranking;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/keepwatch/ranking")
public class KeepWatchRankingListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "KeepWatchRankingListActivity";

    private ImageView mBack;
    private TextView mTittle;

    private RecyclerView mRankingListView;
    private KeepWatchRankingListAdapter mKeepWatchRankingListAdapter;
    private List<Ranking> mKeepWatchRankingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_ranking_list_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("排行榜");

        mRankingListView = findViewById(R.id.keepwatch_ranking_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mRankingListView.setLayoutManager(layoutManager);

        mKeepWatchRankingListAdapter = new KeepWatchRankingListAdapter(mKeepWatchRankingList, this);
        mRankingListView.setAdapter(mKeepWatchRankingListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    private void getRankingList(long startTime, long endTime) {
        NetworkHelper.getInstance().getKeepWatchRanking(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeepWatchRankingList = (List<Ranking>)obj;
                mKeepWatchRankingListAdapter.setList(mKeepWatchRankingList);
                mKeepWatchRankingListAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getRankingList success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getRankingList fail: code = " + code);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyMessageEvent(MessageEvent messageEvent) {
        if ("keepwatch_day_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            long startTime = MyDateTimeUtils.getDayStartTime(timeStamp);
            long endTime = MyDateTimeUtils.getDayEndTime(timeStamp);
            getRankingList(startTime, endTime);
        }
        else if ("keepwatch_week_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            long startTime = MyDateTimeUtils.getWeekStartTime(timeStamp);
            long endTime = MyDateTimeUtils.getWeekEndTime(timeStamp);
            getRankingList(startTime, endTime);
        }
        else if ("keepwatch_month_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
            long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);
            getRankingList(startTime, endTime);
        }

//        EventBus.getDefault().removeStickyEvent(messageEvent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}