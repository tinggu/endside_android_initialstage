package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.WeekCalendarFragment;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.ctfww.module.keepwatch.fragment.KeepWatchRankingFragment;
import com.haibin.calendarview.Calendar;

@Route(path = "/keepwatch/weekReport")
public class KeepWatchWeekReportActivity extends AppCompatActivity implements View.OnClickListener, WeekCalendarFragment.OnWeekCalendarFragmentInteractionListener {
    private final static String TAG = "KeepWatchWeekReportActivity";

    private ImageView mBack;
    private TextView mTittle;

    private WeekCalendarFragment mWeekCalendarFragment;

    private LinearLayout mSigninCountLL;
    private TextView mSigninCount;
    private LinearLayout mLeakCountLL;
    private TextView mLeakCount;
    private LinearLayout mAbnormalCountLL;
    private TextView mAbnormalCount;
    private LinearLayout mEndAbnormalCountLL;
    private TextView mEndAbnormalCount;

    private KeepWatchRankingFragment mKeepWatchRankingFragment;
    private TextView mRankingTittle;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_week_report_activity);
        initViews();
        setOnClickListener();

        mWeekCalendarFragment.initData();
        mKeepWatchRankingFragment.getThisWeekRanking(System.currentTimeMillis());
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("周报");

        mSigninCountLL = findViewById(R.id.keepwatch_signin_count_ll);
        mSigninCount = findViewById(R.id.keepwatch_signin_count);
        mLeakCountLL = findViewById(R.id.keepwatch_leak_count_ll);
        mLeakCount = findViewById(R.id.keepwatch_leak_count);
        mAbnormalCountLL = findViewById(R.id.keepwatch_abnormal_count_ll);
        mAbnormalCount = findViewById(R.id.keepwatch_abnormal_count);
        mEndAbnormalCountLL = findViewById(R.id.keepwatch_end_abnormal_count_ll);
        mEndAbnormalCount = findViewById(R.id.keepwatch_end_abnormal_count);

        mWeekCalendarFragment = (WeekCalendarFragment)getSupportFragmentManager().findFragmentById(R.id.week_calendar_fragment);

        mKeepWatchRankingFragment = (KeepWatchRankingFragment)getSupportFragmentManager().findFragmentById(R.id.keepwatch_ranking_fragment);
        mRankingTittle = mKeepWatchRankingFragment.getView().findViewById(R.id.keepwatch_ranking_tittle);
        mRankingTittle.setText("本周排行榜");
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mSigninCountLL.setOnClickListener(this);
        mLeakCountLL.setOnClickListener(this);
        mAbnormalCountLL.setOnClickListener(this);
        mEndAbnormalCountLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
    }

    /**
     * 日历变换回调
     * @param calendar 日历对象
     */
    @Override
    public void onWeekCalendarSelect(Calendar calendar) {
        LogUtils.i(TAG, "onDayCalendarSelect");
        long timeStamp = calendar.getTimeInMillis();
        showStatistics(timeStamp);
        showWeek(timeStamp);
        mKeepWatchRankingFragment.getThisWeekRanking(timeStamp);
    }

    /**
     * 年份变化回调
     * @param year 年份
     */
    @Override
    public void onYearChanged(int year) {

    }

    private void showStatistics(long timeStamp) {
        mSigninCount.setText("" + com.ctfww.module.signin.datahelper.dbhelper.DBQuickEntry.getSigninCount(MyDateTimeUtils.getDayStartTime(timeStamp), MyDateTimeUtils.getDayEndTime(timeStamp)));
        mLeakCount.setText("" + com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getLeakCount(MyDateTimeUtils.getDayStartTime(timeStamp)));
        mAbnormalCount.setText("" + com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getCreateCount(MyDateTimeUtils.getDayStartTime(timeStamp)));
        mEndAbnormalCount.setText("" + com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getEndCount(MyDateTimeUtils.getDayStartTime(timeStamp)));
    }

    private void showWeek(long timeStamp) {
//        long startTime = MyDateTimeUtils.getWeekStartTime(timeStamp);
//        HashMap<String, Calendar> hashMap = new HashMap<>();
//        for (int i = 0; i < 7; ++i) {
//            if (startTime == MyDateTimeUtils.getTodayStartTime()) {
//                Calendar key = CalendarUtils.produceCalendar(startTime);
//                Calendar value = CalendarUtils.produceCalendar(0xFF108cd4, "今");
//                hashMap.put(key.toString(), value);
//            }
//            else {
//                Calendar key = CalendarUtils.produceCalendar(startTime);
//                Calendar value = CalendarUtils.produceCalendar(0xFF108cd4, "√");
//                hashMap.put(key.toString(), value);
//            }
//
//            startTime += 24l * 3600l * 1000l;
//        }
//
//        mDayCalendarFragment.setSchemeData(hashMap);
    }
}