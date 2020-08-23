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
import com.ctfww.commonlib.Consts;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.DayCalendarFragment;
import com.ctfww.commonlib.utils.CalendarUtils;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.KeepWatchStatisticsByDeskListAdapter;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByUser;
import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = "/keepwatch/signinLeak")
public class KeepWatchLeakListActivity extends AppCompatActivity implements View.OnClickListener,
        DayCalendarFragment.OnDayCalendarFragmentInteractionListener {
    private final static String TAG = "KeepWatchLeakListActivity";

    private ImageView mBack;
    private TextView mTittle;
    private ImageView mPopCalendar;

    private DayCalendarFragment mDayCalendarFragment;

    private RecyclerView mLeakListView;
    private KeepWatchStatisticsByDeskListAdapter mKeepWatchStatisticsByDeskListAdapter;
    private List<KeepWatchStatisticsByDesk> mKeepWatchStatisticsByDeskList = new ArrayList<>();

    List<KeepWatchStatisticsByUser> mKeepWatchStatisticsByUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_leak_list_activity);
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

        mLeakListView = findViewById(R.id.keepwatch_signin_leak_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mLeakListView.setLayoutManager(layoutManager);
        mKeepWatchStatisticsByDeskListAdapter = new KeepWatchStatisticsByDeskListAdapter(mKeepWatchStatisticsByDeskList, this);
        mLeakListView.setAdapter(mKeepWatchStatisticsByDeskListAdapter);

        mDayCalendarFragment = (DayCalendarFragment)getSupportFragmentManager().findFragmentById(R.id.day_calendar_fragment);
        mDayCalendarFragment.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mPopCalendar.setOnClickListener(this);
    }

    private void getLeakList(long timeStamp) {
        long startTime = MyDateTimeUtils.getDayStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getDayEndTime(timeStamp);
        NetworkHelper.getInstance().getKeepWatchLeakStatisticsForDesk(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeepWatchStatisticsByDeskList = (List<KeepWatchStatisticsByDesk>)obj;
                mKeepWatchStatisticsByDeskListAdapter.setList(mKeepWatchStatisticsByDeskList);
                mKeepWatchStatisticsByDeskListAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getLeakList success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getLeakList fail: code = " + code);
            }
        });
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
//            getStatisticsByPeriodByDayUnit(calendar.getTimeInMillis());
            getHistoryEveryDayStatistics(calendar.getTimeInMillis());
            mLastMonthStartTime = monthStartTime;
        }

        getLeakList(calendar.getTimeInMillis());
    }

    /**
     * 年份变化回调
     * @param year 年份
     */
    @Override
    public void onYearChanged(int year) {

    }

    private void getStatisticsByPeriodByDayUnit(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);
        NetworkHelper.getInstance().getKeepWatchStatisticsByPeriodByDayUnit(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchStatisticsByPeriod> keepWatchStatisticsByPeriodList = (List<KeepWatchStatisticsByPeriod>)obj;
                LogUtils.i(TAG, "getStatisticsByPeriodByDayUnit: keepWatchStatisticsByPeriodList.size() = " + keepWatchStatisticsByPeriodList.size());

                HashMap<String, Calendar> hashMap = new HashMap<>();
                for (int i = 0; i < keepWatchStatisticsByPeriodList.size(); ++i) {
                    KeepWatchStatisticsByPeriod keepWatchStatisticsByPeriod = keepWatchStatisticsByPeriodList.get(i);
                    if (keepWatchStatisticsByPeriod.getLeakCount() > 0) {
                        Calendar key = CalendarUtils.produceCalendar(keepWatchStatisticsByPeriod.getTimeStamp());
                        Calendar value = CalendarUtils.produceCalendar(0xFFbc13f0, "漏");
                        hashMap.put(key.toString(), value);
                    }
                }

                mDayCalendarFragment.setSchemeData(hashMap);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getStatisticsByPeriodByDayUnit: code = " + code);
            }
        });
    }

    private void getHistoryEveryDayStatistics(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);
        NetworkHelper.getInstance().getHistoryEveryDayStatistics(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeepWatchStatisticsByUserList = (List<KeepWatchStatisticsByUser>)obj;
                LogUtils.i(TAG, "getHistoryEveryDayStatistics: mKeepWatchStatisticsByUserList.size() = " + mKeepWatchStatisticsByUserList.size());

                HashMap<String, Calendar> hashMap = new HashMap<>();
                for (int i = 0; i < mKeepWatchStatisticsByUserList.size(); ++i) {
                    KeepWatchStatisticsByUser keepWatchStatisticsByUser = mKeepWatchStatisticsByUserList.get(i);
                    if (keepWatchStatisticsByUser.getShouldAssignmentCount() - keepWatchStatisticsByUser.getDeskCount() > 0) {
                        Calendar key = CalendarUtils.produceCalendar(keepWatchStatisticsByUser.getStartTimeStamp());
                        Calendar value = CalendarUtils.produceCalendar(Consts.CALENDAR_LEAK_DESK, "漏");
                        hashMap.put(key.toString(), value);
                    }
                }

                mDayCalendarFragment.setSchemeData(hashMap);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getHistoryEveryDayStatistics: code = " + code);
            }
        });
    }
}