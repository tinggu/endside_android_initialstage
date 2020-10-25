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
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.ColorPromptFragment;
import com.ctfww.commonlib.fragment.DayCalendarFragment;
import com.ctfww.commonlib.utils.CalendarUtils;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByUser;
import com.ctfww.module.keepwatch.fragment.KeepWatchRankingFragment;
import com.haibin.calendarview.Calendar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private long mTimeStamp;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_day_report_activity);
        initViews();
        setOnClickListener();

        mDayCalendarFragment.initData();

        EventBus.getDefault().register(this);

        com.ctfww.module.assignment.datahelper.airship.Airship.getInstance().synAssignmentFromCloud();
        com.ctfww.module.assignment.datahelper.airship.Airship.getInstance().synTodayAssignmentFromCloud();
        com.ctfww.module.keyevents.datahelper.airship.Airship.getInstance().synKeyEventTraceFromCloud();
        com.ctfww.module.keyevents.datahelper.airship.Airship.getInstance().synKeyEventFromCloud();
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
        mTimeStamp = calendar.getTimeInMillis();
        long monthStartTime = MyDateTimeUtils.getMonthStartTime(mTimeStamp);
        if (mMonthStartTime != monthStartTime) {
            showEveryDayStatus(mTimeStamp);
            mMonthStartTime = monthStartTime;
        }

        showAssignment(mTimeStamp);
        showKeyEvent(mTimeStamp);
        mKeepWatchRankingFragment.getThisDayRanking(mTimeStamp);
    }

    /**
     * 年份变化回调
     * @param year 年份
     */
    @Override
    public void onYearChanged(int year) {

    }

    private void showAssignment(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);

        long finishAssignmentCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getFinishCount(startTime);
        long shouldAssignmentCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getTodayAssignmentCount(startTime);
        mAssigmentCompeltion.setText("" + finishAssignmentCount + "/" + shouldAssignmentCount);

        long signinCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getSigninCount(startTime);
        mSigninCount.setText("" + signinCount);
        mMemberCount.setText("" + 0);

//        long todayAssignmentCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getTodayAssignmentCount(startTime);
        long leakCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getLeakCount(startTime);
        mDeskCoverage.setText("" + (finishAssignmentCount - leakCount) + "/" + finishAssignmentCount);
        mLeakCount.setText("" + leakCount);
    }

    private void showKeyEvent(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);

        mAbnormalCount.setText("" + com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getCreateCount(startTime, endTime));
        mEndAbnormalCount.setText("" + com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getEndCount(startTime, endTime));
    }

    private void showEveryDayStatus(long timeStamp) {
        HashMap<String, Calendar> hashMap = com.ctfww.module.keepwatch.datahelper.Utils.getCalendarEveryStatus(timeStamp);
        mDayCalendarFragment.setSchemeData(hashMap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if (com.ctfww.module.keyevents.datahelper.sp.Const.FINISH_KEY_EVENT_SYN.equals(messageEvent.getMessage())
                || com.ctfww.module.keyevents.datahelper.sp.Const.FINISH_KEY_EVENT_TRACE_SYN.equals(messageEvent.getMessage())) {
            showKeyEvent(mTimeStamp);
            mKeepWatchRankingFragment.getThisDayRanking(mTimeStamp);
        }
        else if (com.ctfww.module.assignment.datahelper.sp.Const.FINISH_ASSIGNMENT_SYN.equals(messageEvent.getMessage())
                || com.ctfww.module.assignment.datahelper.sp.Const.FINISH_TODAY_ASSIGNMENT_SYN.equals(messageEvent.getMessage())) {
            showAssignment(mTimeStamp);
            mKeepWatchRankingFragment.getThisDayRanking(mTimeStamp);
        }
    }
}