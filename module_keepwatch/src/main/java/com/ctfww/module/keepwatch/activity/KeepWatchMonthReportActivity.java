package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.MonthCalendarFragment;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.fragment.KeepWatchDangerListFragment;
import com.ctfww.module.keepwatch.fragment.KeepWatchRankingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

@Route(path = "/keepwatch/monthReport")
public class KeepWatchMonthReportActivity extends AppCompatActivity implements View.OnClickListener, MonthCalendarFragment.OnMonthCalendarFragmentInteractionListener {
    private final static String TAG = "KeepWatchMonthReportActivity";

    private ImageView mBack;
    private TextView mTittle;

    private MonthCalendarFragment mMonthCalendarFragment;

    private LinearLayout mAssigmentCompeltionLL;
    private TextView mAssigmentCompeltion;
    private LinearLayout mDeskCoverageLL;
    private TextView mDeskCoverage;
    private LinearLayout mSigninCountLL;
    private TextView mSigninCount;
    private LinearLayout mLeakCountLL;
    private TextView mLeakCount;
    private LinearLayout mCreateAbnormalCountLL;
    private TextView mCreateAbnormalCount;
    private LinearLayout mEndAbnormalCountLL;
    private TextView mEndAbnormalCount;
    private LinearLayout mMemberCountLL;
    private TextView mMemberCount;

    private KeepWatchDangerListFragment mKeepWatchDangerListFragment;
    private KeepWatchRankingFragment mKeepWatchRankingFragment;
    private TextView mRankingTittle;

    private long mTimeStamp;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_month_report_activity);
        initViews();
        setOnClickListener();

 //       showCurrentMonthStatus(System.currentTimeMillis());

        mMonthCalendarFragment.initData();

        EventBus.getDefault().register(this);

        com.ctfww.module.assignment.datahelper.airship.Airship.getInstance().synAssignmentFromCloud();
        com.ctfww.module.assignment.datahelper.airship.Airship.getInstance().synTodayAssignmentFromCloud();
        com.ctfww.module.keyevents.datahelper.airship.Airship.getInstance().synKeyEventTraceFromCloud();
        com.ctfww.module.keyevents.datahelper.airship.Airship.getInstance().synKeyEventFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("月报");

        mAssigmentCompeltionLL = findViewById(R.id.keepwatch_completion_ll);
        mAssigmentCompeltion = findViewById(R.id.keepwatch_completion);
        mDeskCoverageLL = findViewById(R.id.keepwatch_desk_coverage_ll);
        mDeskCoverage = findViewById(R.id.keepwatch_desk_coverage);
        mSigninCountLL = findViewById(R.id.keepwatch_signin_count_ll);
        mSigninCount = findViewById(R.id.keepwatch_signin_count);
        mLeakCountLL = findViewById(R.id.keepwatch_leak_count_ll);
        mLeakCount = findViewById(R.id.keepwatch_leak_count);
        mCreateAbnormalCountLL = findViewById(R.id.keepwatch_create_abnormal_count_ll);
        mCreateAbnormalCount = findViewById(R.id.keepwatch_create_abnormal_count);
        mEndAbnormalCountLL = findViewById(R.id.keepwatch_end_abnormal_count_ll);
        mEndAbnormalCount = findViewById(R.id.keepwatch_end_abnormal_count);
        mMemberCountLL = findViewById(R.id.keepwatch_member_count_ll);
        mMemberCount = findViewById(R.id.keepwatch_member_count);

        mMonthCalendarFragment = (MonthCalendarFragment)getSupportFragmentManager().findFragmentById(R.id.month_calendar_fragment);
        mKeepWatchDangerListFragment = (KeepWatchDangerListFragment)getSupportFragmentManager().findFragmentById(R.id.keepwatch_danger_list_fragment);
        mKeepWatchRankingFragment = (KeepWatchRankingFragment)getSupportFragmentManager().findFragmentById(R.id.keepwatch_ranking_fragment);
        mRankingTittle = mKeepWatchRankingFragment.getView().findViewById(R.id.keepwatch_ranking_tittle);
        mRankingTittle.setText("本月排行榜");

    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mAssigmentCompeltionLL.setOnClickListener(this);
        mSigninCountLL.setOnClickListener(this);
        mMemberCountLL.setOnClickListener(this);
        mDeskCoverageLL.setOnClickListener(this);
        mLeakCountLL.setOnClickListener(this);
        mCreateAbnormalCountLL.setOnClickListener(this);
        mEndAbnormalCountLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mEndAbnormalCountLL.getId()) {
            ARouter.getInstance().build("/keyevent/keyEventEndStatisticsByUserUnit").navigation();
            EventBus.getDefault().postSticky(new MessageEvent("keyevent_month_report", "" + mMonthCalendarFragment.getTimeStamp()));
        }
    }
// getEveryOneEndKeyEventStatistics
    @Override
    public void onMonthSelected(Calendar calendar) {
        LogUtils.i(TAG, "onMonthSelected");
        mTimeStamp = calendar.getTimeInMillis();
        LogUtils.i(TAG, "onMonthSelected: mTimeStamp = " + mTimeStamp);
        showAssignment(mTimeStamp);
        showKeyEvent(mTimeStamp);
        mKeepWatchRankingFragment.getThisMonthRanking(mTimeStamp);
        mKeepWatchDangerListFragment.getThisMonthDangerList(mTimeStamp);
    }

    private void showAssignment(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);

        long finishAssignmentCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getFinishCount(startTime, endTime);
        long shouldAssignmentCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getTodayAssignmentCount(startTime, endTime);
        mAssigmentCompeltion.setText("" + finishAssignmentCount + "/" + shouldAssignmentCount);

        mSigninCount.setText("" + com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getSigninCount(startTime, endTime));

        mMemberCount.setText("" + 0);

//        long todayAssignmentCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getTodayAssignmentCount(startTime);
        long leakCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getLeakCount(startTime, endTime);
        mDeskCoverage.setText("" + (finishAssignmentCount - leakCount) + "/" + finishAssignmentCount);
        mLeakCount.setText("" + leakCount);
    }

    private void showKeyEvent(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);

        mCreateAbnormalCount.setText("" + com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getCreateCount(startTime, endTime));
        mEndAbnormalCount.setText("" + com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getEndCount(startTime, endTime));
    }

    private void getDangerPointList(long timeStamp) {
//        NetworkHelper.getInstance()
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
            mKeepWatchRankingFragment.getThisMonthRanking(mTimeStamp);
            mKeepWatchDangerListFragment.getThisMonthDangerList(mTimeStamp);
        }
        else if (com.ctfww.module.assignment.datahelper.sp.Const.FINISH_ASSIGNMENT_SYN.equals(messageEvent.getMessage())
                || com.ctfww.module.assignment.datahelper.sp.Const.FINISH_TODAY_ASSIGNMENT_SYN.equals(messageEvent.getMessage())) {
            showAssignment(mTimeStamp);
            mKeepWatchRankingFragment.getThisMonthRanking(mTimeStamp);
            mKeepWatchDangerListFragment.getThisMonthDangerList(mTimeStamp);
        }
    }
}