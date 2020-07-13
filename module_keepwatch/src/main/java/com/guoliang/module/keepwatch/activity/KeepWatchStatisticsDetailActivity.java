package com.guoliang.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.guoliang.commonlib.Consts;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MyDateTimeUtils;
import com.guoliang.commonlib.fragment.ColorPromptFragment;
import com.guoliang.commonlib.fragment.DayCalendarFragment;
import com.guoliang.commonlib.utils.CalendarUtils;
import com.guoliang.module.keepwatch.DataHelper.NetworkHelper;
import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.adapter.KeepWatchStatisticsByDeskListAdapter;
import com.guoliang.module.keepwatch.entity.KeepWatchStatisticsByDesk;
import com.guoliang.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.guoliang.module.keepwatch.entity.KeepWatchStatisticsByUser;
import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = "/keepwatch/statisticsDetail")
public class KeepWatchStatisticsDetailActivity extends AppCompatActivity implements View.OnClickListener,
        DayCalendarFragment.OnDayCalendarFragmentInteractionListener {
    private final static String TAG = "KeepWatchStatisticsDetailActivity";

    private ImageView mBack;
    private TextView mTittle;
    private ImageView mPopCalendar;

    private DayCalendarFragment mDayCalendarFragment;
    private ColorPromptFragment mColorPromptFragment;

    private RecyclerView mStatisticsListView;
    private KeepWatchStatisticsByDeskListAdapter mKeepWatchStatisticsByDeskListAdapter;
    private List<KeepWatchStatisticsByDesk> mKeepWatchStatisticsByDeskList = new ArrayList<>();

    List<KeepWatchStatisticsByUser> mKeepWatchStatisticsByUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_statistics_detail_activity);
        initViews();
        setOnClickListener();

        HashMap hashMap = CalendarUtils.getTodayFill();
        mDayCalendarFragment.setSchemeData(hashMap);

        mDayCalendarFragment.initData();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("任务完成现状");
        mPopCalendar = findViewById(R.id.top_addition_img);
        mPopCalendar.setImageResource(R.drawable.pop_calendar);
        mPopCalendar.setVisibility(View.VISIBLE);

        mStatisticsListView = findViewById(R.id.keepwatch_signin_statistics_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mStatisticsListView.setLayoutManager(layoutManager);

        mKeepWatchStatisticsByDeskListAdapter = new KeepWatchStatisticsByDeskListAdapter(mKeepWatchStatisticsByDeskList, this);
        mStatisticsListView.setAdapter(mKeepWatchStatisticsByDeskListAdapter);

        mDayCalendarFragment = (DayCalendarFragment)getSupportFragmentManager().findFragmentById(R.id.day_calendar_fragment);
        mDayCalendarFragment.setVisibility(View.GONE);

        mColorPromptFragment = (ColorPromptFragment) getSupportFragmentManager().findFragmentById(R.id.color_prompt_fragment);
        mColorPromptFragment.setVisibility(View.GONE);
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
                mColorPromptFragment.setVisibility(View.GONE);
            }
            else {
                mDayCalendarFragment.setVisibility(View.VISIBLE);
                mColorPromptFragment.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getAssignmentAndSigninStatisticsForDesk(long timeStamp) {
        long startTime = MyDateTimeUtils.getDayStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getDayEndTime(timeStamp);

        NetworkHelper.getInstance().getKeepWatchAssignmentAndSigninStatisticsForDesk(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeepWatchStatisticsByDeskList = (List<KeepWatchStatisticsByDesk>)obj;
                mKeepWatchStatisticsByDeskListAdapter.setList(mKeepWatchStatisticsByDeskList);
                mKeepWatchStatisticsByDeskListAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getAssignmentAndSigninStatisticsForDesk success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getAssignmentAndSigninStatisticsForDesk fail: code = " + code);
            }
        });
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
            getHistoryEveryDayStatistics(calendar.getTimeInMillis());
            mLastMonthStartTime = monthStartTime;
        }

        if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
            mKeepWatchStatisticsByDeskListAdapter.setList(new ArrayList<KeepWatchStatisticsByDesk>());
            mKeepWatchStatisticsByDeskListAdapter.notifyDataSetChanged();
            return;
        }

        getAssignmentAndSigninStatisticsForDesk(calendar.getTimeInMillis());
    }

    /**
     * 年份变化回调
     * @param year 年份
     */
    @Override
    public void onYearChanged(int year) {

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
                    if (keepWatchStatisticsByUser.getAbnormalCount() > 0) {
                        Calendar key = CalendarUtils.produceCalendar(keepWatchStatisticsByUser.getStartTimeStamp());
                        Calendar value = CalendarUtils.produceCalendar(Consts.CALENDAR_REPORT_ABNORMAL, "异");
                        hashMap.put(key.toString(), value);
                    }
                    else if (keepWatchStatisticsByUser.getShouldAssignmentCount() - keepWatchStatisticsByUser.getDeskCount() > 0) {
                        Calendar key = CalendarUtils.produceCalendar(keepWatchStatisticsByUser.getStartTimeStamp());
                        Calendar value = CalendarUtils.produceCalendar(Consts.CALENDAR_LEAK_DESK, "漏");
                        hashMap.put(key.toString(), value);
                    }
                    else if (keepWatchStatisticsByUser.getSigninCount() < keepWatchStatisticsByUser.getShouldCount()) {
                        Calendar key = CalendarUtils.produceCalendar(keepWatchStatisticsByUser.getStartTimeStamp());
                        Calendar value = CalendarUtils.produceCalendar(Consts.CALENDAR_LEAK_SIGNIN, "少");
                        hashMap.put(key.toString(), value);
                    }
                    else if (keepWatchStatisticsByUser.getEndCount() > 0) {
                        Calendar key = CalendarUtils.produceCalendar(keepWatchStatisticsByUser.getStartTimeStamp());
                        Calendar value = CalendarUtils.produceCalendar(Consts.CALENDAR_END_ABNORMAL, "结");
                        hashMap.put(key.toString(), value);
                    }
                    else if (keepWatchStatisticsByUser.getSigninCount() > 0) {
                        Calendar key = CalendarUtils.produceCalendar(keepWatchStatisticsByUser.getStartTimeStamp());
                        Calendar value = CalendarUtils.produceCalendar(Consts.CALENDAR_SIGNIN, "签");
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