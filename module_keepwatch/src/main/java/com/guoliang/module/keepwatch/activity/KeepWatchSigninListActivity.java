package com.guoliang.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MyDateTimeUtils;
import com.guoliang.commonlib.fragment.DayCalendarFragment;
import com.guoliang.module.keepwatch.DataHelper.NetworkHelper;
import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.adapter.KeepWatchSigninListAdapter;
import com.guoliang.module.keepwatch.fragment.KeepWatchSigninListFragment;
import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/keepwatch/signinList")
public class KeepWatchSigninListActivity extends AppCompatActivity implements View.OnClickListener,
        DayCalendarFragment.OnDayCalendarFragmentInteractionListener {
    private final static String TAG = "KeepWatchSigninListActivity";

    private ImageView mBack;
    private TextView mTittle;
    private ImageView mPopCalendar;

    private KeepWatchSigninListFragment mKeepWatchSigninListFragment;
    private DayCalendarFragment mDayCalendarFragment;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_signin_list_activity);
        initViews();
        setOnClickListener();

        mDayCalendarFragment.initData();

    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("签到信息列表");
        mPopCalendar = findViewById(R.id.top_addition_img);
        mPopCalendar.setImageResource(R.drawable.pop_calendar);
        mPopCalendar.setVisibility(View.VISIBLE);

        mKeepWatchSigninListFragment = (KeepWatchSigninListFragment)getSupportFragmentManager().findFragmentById(R.id.keepwatch_signin_list_fragment);
        mKeepWatchSigninListFragment.getView().findViewById(R.id.keepwatch_signin_list_tittle_ll).setVisibility(View.GONE);

        mDayCalendarFragment = (DayCalendarFragment)getSupportFragmentManager().findFragmentById(R.id.day_calendar_fragment);
        mDayCalendarFragment.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mPopCalendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mPopCalendar.getId()) {
            if (mDayCalendarFragment.getVisibility() == View.VISIBLE) {
                mDayCalendarFragment.setVisibility(View.GONE);
            }
            else {
                mDayCalendarFragment.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDayCalendarSelect(Calendar calendar) {
        long timeStamp = calendar.getTimeInMillis();
        mKeepWatchSigninListFragment.getSigninList(MyDateTimeUtils.getDayStartTime(timeStamp), MyDateTimeUtils.getDayEndTime(timeStamp));
    }

    @Override
    public void onYearChanged(int year) {

    }
}