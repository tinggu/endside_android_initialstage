package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.Consts;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.ColorPromptFragment;
import com.ctfww.commonlib.fragment.DayCalendarFragment;
import com.ctfww.commonlib.utils.CalendarUtils;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByUser;
import com.ctfww.module.keepwatch.fragment.KeepWatchRankingFragment;
import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = "/keepwatch/dayReport")
public class KeepWatchDayReportActivity extends AppCompatActivity implements View.OnClickListener, DayCalendarFragment.OnDayCalendarFragmentInteractionListener {
    private final static String TAG = "KeepWatchDayReportActivity";

    private ImageView mBack;
    private TextView mTittle;

    private DayCalendarFragment mDayCalendarFragment;
    private ColorPromptFragment mColorPromptFragment;

    private LinearLayout mAssigmentCompeltionLL;
    private TextView mAssigmentCompeltion;
    private LinearLayout mSigninCountLL;
    private TextView mSigninCount;
    private LinearLayout mMemberCountLL;
    private TextView mMemberCount;
    private LinearLayout mDeskCoverageLL;
    private TextView mDeskCoverage;
    private LinearLayout mLeakCountLL;
    private TextView mLeakCount;
    private LinearLayout mAbnormalCountLL;
    private TextView mAbnormalCount;
    private LinearLayout mEndAbnormalCountLL;
    private TextView mEndAbnormalCount;

    private KeepWatchRankingFragment mKeepWatchRankingFragment;
    private TextView mRankingTittle;

    List<KeepWatchStatisticsByUser> mKeepWatchStatisticsByUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_day_report_activity);
        initViews();
        setOnClickListener();

        mDayCalendarFragment.initData();
        mKeepWatchRankingFragment.getTodayRanking();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("日报");

        mAssigmentCompeltionLL = findViewById(R.id.keepwatch_completion_ll);
        mAssigmentCompeltion = findViewById(R.id.keepwatch_completion);
        mSigninCountLL = findViewById(R.id.keepwatch_signin_count_ll);
        mSigninCount = findViewById(R.id.keepwatch_signin_count);
        mMemberCountLL = findViewById(R.id.keepwatch_member_count_ll);
        mMemberCount = findViewById(R.id.keepwatch_member_count);
        mDeskCoverageLL = findViewById(R.id.keepwatch_desk_coverage_ll);
        mDeskCoverage = findViewById(R.id.keepwatch_desk_coverage);
        mLeakCountLL = findViewById(R.id.keepwatch_leak_count_ll);
        mLeakCount = findViewById(R.id.keepwatch_leak_count);
        mAbnormalCountLL = findViewById(R.id.keepwatch_abnormal_count_ll);
        mAbnormalCount = findViewById(R.id.keepwatch_abnormal_count);
        mEndAbnormalCountLL = findViewById(R.id.keepwatch_end_abnormal_count_ll);
        mEndAbnormalCount = findViewById(R.id.keepwatch_end_abnormal_count);

        mDayCalendarFragment = (DayCalendarFragment)getSupportFragmentManager().findFragmentById(R.id.day_calendar_fragment);

        mColorPromptFragment = (ColorPromptFragment) getSupportFragmentManager().findFragmentById(R.id.color_prompt_fragment);
        mColorPromptFragment.setVisibility(View.VISIBLE);

        mKeepWatchRankingFragment = (KeepWatchRankingFragment)getSupportFragmentManager().findFragmentById(R.id.keepwatch_ranking_fragment);
        mRankingTittle = mKeepWatchRankingFragment.getView().findViewById(R.id.keepwatch_ranking_tittle);
        mRankingTittle.setText("本日排行榜");
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mAssigmentCompeltionLL.setOnClickListener(this);
        mSigninCountLL.setOnClickListener(this);
        mMemberCountLL.setOnClickListener(this);
        mDeskCoverageLL.setOnClickListener(this);
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

    private long mMonthStartTime = 0;

    /**
     * 日历变换回调
     * @param calendar 日历对象
     */
    @Override
    public void onDayCalendarSelect(Calendar calendar) {
        LogUtils.i(TAG, "onDayCalendarSelect");
        long timeStamp = calendar.getTimeInMillis();
        long monthStartTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        if (mMonthStartTime != monthStartTime) {
            getHistoryEveryDayStatistics(timeStamp);
            mMonthStartTime = monthStartTime;
        }
        getStatistics(timeStamp);
        mKeepWatchRankingFragment.getThisDayRanking(timeStamp);
    }

    /**
     * 年份变化回调
     * @param year 年份
     */
    @Override
    public void onYearChanged(int year) {

    }

    private void getStatistics(long timeStamp) {
        long startTime = MyDateTimeUtils.getDayStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getDayEndTime(timeStamp);
        NetworkHelper.getInstance().getKeepWatchStatistics(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                KeepWatchStatisticsByPeriod keepWatchStatisticsByPeriod = (KeepWatchStatisticsByPeriod)obj;
                showStatistics(keepWatchStatisticsByPeriod);
                LogUtils.i(TAG, "getStatistics success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getStatistics fail: code= " + code);
            }
        });
    }

    private void showStatistics(KeepWatchStatisticsByPeriod keepWatchStatisticsByPeriod) {
        mAssigmentCompeltion.setText("" + keepWatchStatisticsByPeriod.getAssignmentCount() + "/" + keepWatchStatisticsByPeriod.getShouldAssignmentCount());
        mSigninCount.setText("" + keepWatchStatisticsByPeriod.getSigninCount());
        mMemberCount.setText("" + keepWatchStatisticsByPeriod.getMemberCount());
        mDeskCoverage.setText("" + keepWatchStatisticsByPeriod.getDeskCount() + "/" + keepWatchStatisticsByPeriod.getShouldDeskCount());
        mLeakCount.setText("" + (keepWatchStatisticsByPeriod.getShouldCount() - keepWatchStatisticsByPeriod.getSigninCount()));
        mAbnormalCount.setText("" + keepWatchStatisticsByPeriod.getAbnormalCount());
        mEndAbnormalCount.setText("" + keepWatchStatisticsByPeriod.getEndCount());
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