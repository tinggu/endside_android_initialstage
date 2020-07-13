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
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.MonthCalendarFragment;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.ctfww.module.keepwatch.fragment.KeepWatchDangerListFragment;
import com.ctfww.module.keepwatch.fragment.KeepWatchRankingFragment;

import org.greenrobot.eventbus.EventBus;

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

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_month_report_activity);
        initViews();
        setOnClickListener();

        long startTime = MyDateTimeUtils.getPresentMonthStartTime();
        long endTime = MyDateTimeUtils.getPresentMonthEndTime();
        getStatistics(System.currentTimeMillis());

        mMonthCalendarFragment.initData();
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
        long timeStamp = calendar.getTimeInMillis();
        LogUtils.i(TAG, "onMonthSelected: timeStamp = " + timeStamp);
        getStatistics(timeStamp);
        mKeepWatchRankingFragment.getThisMonthRanking(timeStamp);
        mKeepWatchDangerListFragment.getThisMonthDangerList(timeStamp);
    }


    private void getStatistics(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);
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
        LogUtils.i(TAG, "showStatistics: keepWatchStatisticsByPeriod = " + keepWatchStatisticsByPeriod.toString());
        mAssigmentCompeltion.setText("" + keepWatchStatisticsByPeriod.getAssignmentCount() + "/" + keepWatchStatisticsByPeriod.getShouldAssignmentCount());
        mSigninCount.setText("" + keepWatchStatisticsByPeriod.getSigninCount());
        mMemberCount.setText("" + keepWatchStatisticsByPeriod.getMemberCount());
        mDeskCoverage.setText("" + keepWatchStatisticsByPeriod.getDeskCount() + "/" + keepWatchStatisticsByPeriod.getShouldDeskCount());
        mLeakCount.setText("" + (keepWatchStatisticsByPeriod.getShouldCount() - keepWatchStatisticsByPeriod.getSigninCount()));
        mCreateAbnormalCount.setText("" + keepWatchStatisticsByPeriod.getAbnormalCount());
        mEndAbnormalCount.setText("" + keepWatchStatisticsByPeriod.getEndCount());
    }

    private void getDangerPointList(long timeStamp) {
//        NetworkHelper.getInstance()
    }
}