package com.ctfww.module.assignment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.DayCalendarFragment;
import com.ctfww.commonlib.utils.CalendarUtils;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.adapter.LeakListAdapter;
import com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.haibin.calendarview.Calendar;

import java.util.HashMap;
import java.util.List;

@Route(path = "/assignment/leak")
public class LeakListActivity extends AppCompatActivity implements View.OnClickListener,
        DayCalendarFragment.OnDayCalendarFragmentInteractionListener {
    private final static String TAG = "LeakListActivity";

    private ImageView mBack;
    private TextView mTittle;
    private ImageView mPopCalendar;

    private DayCalendarFragment mDayCalendarFragment;

    private RecyclerView mLeakListView;
    private LeakListAdapter mLeakListAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.leak_list_activity);
        initViews();
        setOnClickListener();

        HashMap hashMap = CalendarUtils.getTodayFill();
        mDayCalendarFragment.setSchemeData(hashMap);

        mDayCalendarFragment.initData();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("漏检点");
        mPopCalendar = findViewById(R.id.top_addition_img);
        mPopCalendar.setImageResource(R.drawable.pop_calendar);
        mPopCalendar.setVisibility(View.VISIBLE);

        mLeakListView = findViewById(R.id.leak_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mLeakListView.setLayoutManager(layoutManager);
        List<TodayAssignment> todayAssignmentList = DBQuickEntry.getLeakList(MyDateTimeUtils.getTodayStartTime());
        mLeakListAdapter = new LeakListAdapter(todayAssignmentList);
        mLeakListView.setAdapter(mLeakListAdapter);

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

    private long mLastMonthStartTime = 0;
    /**
     * 日历变换回调
     * @param calendar 日历对象
     */
    @Override
    public void onDayCalendarSelect(Calendar calendar) {
        long timeStamp = calendar.getTimeInMillis();
        long monthStartTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        if (monthStartTime != mLastMonthStartTime) {
            showLeakOnCalendar(timeStamp);
            mLastMonthStartTime = monthStartTime;
        }

        List<TodayAssignment> todayAssignmentList = DBQuickEntry.getLeakList(MyDateTimeUtils.getDayStartTime(timeStamp));
        mLeakListAdapter.setList(todayAssignmentList);
        mLeakListAdapter.notifyDataSetChanged();
    }

    /**
     * 年份变化回调
     * @param year 年份
     */
    @Override
    public void onYearChanged(int year) {

    }

    private void showLeakOnCalendar(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);

        HashMap<String, Calendar> hashMap = new HashMap<>();
        for (long time = startTime; time < endTime; time += 24l * 3600l * 1000l) {
            long count = DBQuickEntry.getLeakCount(MyDateTimeUtils.getDayStartTime(time));
            if (count > 0) {
                Calendar key = CalendarUtils.produceCalendar(time);
                Calendar value = CalendarUtils.produceCalendar(0xFFbc13f0, "漏");
                hashMap.put(key.toString(), value);
            }
        }

        mDayCalendarFragment.setSchemeData(hashMap);
    }
}