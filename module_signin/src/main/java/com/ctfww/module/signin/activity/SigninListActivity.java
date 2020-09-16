package com.ctfww.module.signin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.DayCalendarFragment;
import com.ctfww.module.signin.R;
import com.ctfww.module.signin.fragment.SigninListFragment;
import com.haibin.calendarview.Calendar;

@Route(path = "/keepwatch/signinList")
public class SigninListActivity extends AppCompatActivity implements View.OnClickListener,
        DayCalendarFragment.OnDayCalendarFragmentInteractionListener {
    private final static String TAG = "SigninListActivity";

    private ImageView mBack;
    private TextView mTittle;
    private ImageView mPopCalendar;

    private SigninListFragment mSigninListFragment;
    private DayCalendarFragment mDayCalendarFragment;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.signin_list_activity);
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

        mSigninListFragment = (SigninListFragment)getSupportFragmentManager().findFragmentById(R.id.signin_list_fragment);
        mSigninListFragment.getView().findViewById(R.id.list_tittle_ll).setVisibility(View.GONE);

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
        mSigninListFragment.showOneDaySigninList(timeStamp);
    }

    @Override
    public void onYearChanged(int year) {

    }
}