package com.ctfww.module.keyevents.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.adapter.PersonPerformanceAdapter;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.entity.PersonPerformance;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventStatisticsByUserUnit;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.adapter.KeyEventListAdapter;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/keyevent/keyEventEndStatisticsByUserUnit")
public class KeyEventEndStatisticsByUserUnitActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "KeyEventEndStatisticsByUserUnitActivity";

    private ImageView mBack;
    private TextView mTittle;

    private RecyclerView mPersonPerformanceListView;
    private PersonPerformanceAdapter mPersonPerformanceAdapter;
    private List<PersonPerformance> mPersonPerformanceList = new ArrayList<>();

    private RecyclerView mEndKeyEventListView;
    private KeyEventListAdapter mKeyEventListAdapter;
    private List<KeyEvent> mKeyEventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keyevent_end_statistics_by_user_unit_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("处理异常排行榜");

        mPersonPerformanceListView = findViewById(R.id.keyevent_person_performance_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mPersonPerformanceListView.setLayoutManager(layoutManager);

        mPersonPerformanceAdapter = new PersonPerformanceAdapter(mPersonPerformanceList);
        mPersonPerformanceListView.setAdapter(mPersonPerformanceAdapter);

        mEndKeyEventListView = findViewById(R.id.keyevent_end_key_event_list);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        mEndKeyEventListView.setLayoutManager(layoutManager2);

        mKeyEventListAdapter = new KeyEventListAdapter(mKeyEventList);
        mEndKeyEventListView.setAdapter(mKeyEventListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    private void getEndPersonPerformace(long startTime, long endTime) {
        NetworkHelper.getInstance().getEveryOneEndKeyEventStatistics(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeyEventStatisticsByUserUnit> keyEventStatisticsByUserUnitList = (List<KeyEventStatisticsByUserUnit>)obj;
                mPersonPerformanceList.clear();
                for (int i = 0; i < keyEventStatisticsByUserUnitList.size(); ++i) {
                    KeyEventStatisticsByUserUnit keyEventStatisticsByUserUnit = keyEventStatisticsByUserUnitList.get(i);
                    PersonPerformance personPerformance = new PersonPerformance();
                    personPerformance.setUserId(keyEventStatisticsByUserUnit.getUserId());
                    personPerformance.setNickName(keyEventStatisticsByUserUnit.getNickName());
                    personPerformance.setHeadUrl(keyEventStatisticsByUserUnit.getHeadUrl());
                    personPerformance.setScore(keyEventStatisticsByUserUnit.getEndCount());
                    mPersonPerformanceList.add(personPerformance);
                }
                mPersonPerformanceAdapter.setList(mPersonPerformanceList);
                mPersonPerformanceAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getEndPersonPerformace success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getEndPersonPerformace fail: code = " + code);
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

    private long mStartTime = 0;
    private long mEndTime = 0;
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyMessageEvent(MessageEvent messageEvent) {
        if ("keyevent_day_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            mStartTime = MyDateTimeUtils.getDayStartTime(timeStamp);
            mEndTime = MyDateTimeUtils.getDayEndTime(timeStamp);
            getEndPersonPerformace(mStartTime, mEndTime);
        }
        else if ("keyevent_week_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            mStartTime = MyDateTimeUtils.getWeekStartTime(timeStamp);
            mEndTime = MyDateTimeUtils.getWeekEndTime(timeStamp);
            getEndPersonPerformace(mStartTime, mEndTime);
        }
        else if ("keyevent_month_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            mStartTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
            mEndTime = MyDateTimeUtils.getMonthEndTime(timeStamp);
            getEndPersonPerformace(mStartTime, mEndTime);
        }
        else if ("select_person_formance".equals(messageEvent.getMessage())) {
            getEndKeyEventList(messageEvent.getValue());
        }

//        EventBus.getDefault().removeStickyEvent(messageEvent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getEndKeyEventList(String userId) {
        NetworkHelper.getInstance().getEndKeyEventList(mStartTime, mEndTime, userId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeyEventList = (List<KeyEvent>)obj;
                mKeyEventListAdapter.setList(mKeyEventList);
                mKeyEventListAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getEndList success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getEndList fail: code = " + code);
            }
        });
    }
}