package com.ctfww.module.keepwatch.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keyevents.fragment.KeyEventSnatchFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class KeepWatchStatisticsFragment extends Fragment {
    private final static String TAG = "KeepWatchFragment";

    private View mV;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mKeepWatchLL;
    private ImageView mCompletionRatePrompt;
    private ImageView mCoverageRatePrompt;
    private TextView mCompletionRate;
    private TextView mCoverageRate;
    private TextView mSigninCount;
    private TextView mDeskCount;

    private ImageView mCompletionDefinition;
    private TextView mTodayAssignment;
    private LinearLayout mTodayCompletionLL;
    private TextView mTodayCompletion;
    private LinearLayout mTodayLeakCountLL;
    private TextView mTodayLeakCount;
    private LinearLayout mAbnormalReportCountLL;
    private TextView mAbnormalReportCount;

    private KeyEventSnatchFragment mKeyEventSnatchFragment;
    private PersonTrendsFragment mPersonTrendsFragment;

    private KeepWatchRankingFragment mKeepWatchRankingFragment;
    private TextView mRankingTittle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_statistics_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
       mSwipeRefreshLayout = v.findViewById(R.id.keepwatch_refresh);
        mKeepWatchLL = v.findViewById(R.id.keepwatch_keep_watch_ll);
        mCompletionRatePrompt = v.findViewById(R.id.keepwatch_completion_rate_prompt);
        mCoverageRatePrompt = v.findViewById(R.id.keepwatch_coverage_rate_prompt);
        mCompletionRate = v.findViewById(R.id.keepwatch_completion_rate);
        mCoverageRate = v.findViewById(R.id.keepwatch_coverage_rate);
        mSigninCount = v.findViewById(R.id.keepwatch_signin_count);
        mDeskCount = v.findViewById(R.id.keepwatch_signin_desk_count);

        mCompletionDefinition = v.findViewById(R.id.keepwatch_completion_definition);
        mTodayAssignment = v.findViewById(R.id.keepwatch_today_assignment);
        mTodayCompletionLL = v.findViewById(R.id.keepwatch_today_completion_ll);
        mTodayCompletion = v.findViewById(R.id.keepwatch_today_completion);
        mTodayLeakCountLL = v.findViewById(R.id.keepwatch_today_leak_count_ll);
        mTodayLeakCount = v.findViewById(R.id.keepwatch_today_leak_count);
        mAbnormalReportCountLL = v.findViewById(R.id.keepwatch_abnormal_report_count_ll);
        mAbnormalReportCount = v.findViewById(R.id.keepwatch_abnormal_report_count);
        long noEndKeyEventCount = com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getNotEndCount();
        mAbnormalReportCount.setText("" + noEndKeyEventCount);

        mKeyEventSnatchFragment = (KeyEventSnatchFragment) getChildFragmentManager().findFragmentById(R.id.keepwatch_snatch_key_event_fragment);

        mPersonTrendsFragment = (PersonTrendsFragment)getChildFragmentManager().findFragmentById(R.id.keepwatch_person_trends_fragment);

        mKeepWatchRankingFragment = (KeepWatchRankingFragment)getChildFragmentManager().findFragmentById(R.id.keepwatch_ranking_fragment);
        mRankingTittle = mKeepWatchRankingFragment.getView().findViewById(R.id.keepwatch_ranking_tittle);
        mRankingTittle.setText("今日排行榜");
    }

    private void setOnClickListener() {
        mKeepWatchLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/statisticsDetail").navigation();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                com.ctfww.module.user.datahelper.airship.Airship.getInstance().synFromCloud();
                com.ctfww.module.desk.datahelper.airship.Airship.getInstance().synFromCloud();
                com.ctfww.module.assignment.datahelper.airship.Airship.getInstance().synFromCloud();
                com.ctfww.module.signin.datahelper.airship.Airship.getInstance().synFromCloud();
                com.ctfww.module.keyevents.datahelper.airship.Airship.getInstance().synFromCloud();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mCompletionRatePrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.onlyPrompt("今日已签到次数/今日总需要签到次数", v.getContext());
            }
        });

        mCoverageRatePrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.onlyPrompt("今日签到点数/今日需要签到的总点数", v.getContext());
            }
        });

        mCompletionDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.onlyPrompt("今日已签到次数/今日需要见到的次数", v.getContext());
            }
        });

        mTodayAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/assignment/todayAssignment").navigation();
            }
        });

        mTodayCompletionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/statisticsDetail").navigation();
            }
        });

        mTodayLeakCountLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/signinLeak").navigation();
            }
        });

        mAbnormalReportCountLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keyevents/noEndList").navigation();
            }
        });
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if ("bind_group".equals(msg)) {
            LogUtils.i(TAG, "bind_group refresh!");
            updateFinishRate();
            updateCoverageRate();
            updateAssignmentStatus();
            updateLeakCount();
            updateAbnormalCount();
            mKeyEventSnatchFragment.refresh();
            mPersonTrendsFragment.showPersonTrends();
            mKeepWatchRankingFragment.getTodayRanking();

            long noEndKeyEventCount = com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getNotEndCount();
            mAbnormalReportCount.setText("" + noEndKeyEventCount);
        }
        else if (com.ctfww.module.assignment.datahelper.sp.Const.FINISH_ASSIGNMENT_SYN.equals(messageEvent.getMessage())) {
            updateFinishRate();
            updateCoverageRate();
            updateAssignmentStatus();
            updateLeakCount();
        }
        else if (com.ctfww.module.signin.datahelper.sp.Const.FINISH_SIGNIN_SYN.equals(messageEvent.getMessage())) {
            updateFinishRate();
            updateCoverageRate();
            updateAssignmentStatus();
            updateLeakCount();

            mPersonTrendsFragment.showPersonTrends();
        }
        else if (com.ctfww.module.keyevents.datahelper.sp.Const.FINISH_KEY_EVENT_TRACE_SYN.equals(messageEvent.getMessage())) {
            mPersonTrendsFragment.showPersonTrends();
        }
        else if (com.ctfww.module.keyevents.datahelper.sp.Const.FINISH_KEY_EVNET_PERSON_SYN.equals(messageEvent.getMessage())) {
            updateAbnormalCount();
        }
//        else if ("im_received_data".equals(msg)) {
//            Head head = GsonUtils.fromJson(messageEvent.getValue(), Head.class);
//            if (head.getMsgType() == 3001) {
//                if (head.getMsgContentType() == 10) {
//                    mKeyEventSnatchFragment.refresh();
//                    mKeepWatchPersonTrendsFragment.getPersonTrends();
//                    if (!mKeyEventSnatchFragment.isDoing()) {
//                        Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
//                        long[] patter = {1000, 2000, 2000, 50};
//                        vibrator.vibrate(patter, -1);
//                    }
//                    String role = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup();
//                    if ("admin".equals(role)) {
//                        getNoEndKeyEventCount();
//                    }
//                }
//                else if (head.getMsgContentType() == 11) {
//                    mKeepWatchPersonTrendsFragment.getPersonTrends();
//                    String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
//                    String role = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup();
//                    if (userId.equals(head.getFromId()) || "admin".equals(role)) {
//                        getNoEndKeyEventCount();
//                    }
//
//                    if (userId.equals(head.getFromId())) {
//                        mKeyEventSnatchFragment.refresh();
//                    }
//                }
//                else if (head.getMsgContentType() == 20) {
//                    mKeepWatchPersonTrendsFragment.getPersonTrends();
//                    String role = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup();
//                    String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
//                    if ("admin".equals(role) || userId.equals(head.getFromId())) {
//                        getTodayKeepWatchStatistics();
//                    }
//                }
//            }
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateWatchInfoToUI();
        mKeyEventSnatchFragment.refresh();
        mPersonTrendsFragment.showPersonTrends();
        mKeepWatchRankingFragment.getTodayRanking();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void updateWatchInfoToUI() {
        updateFinishRate();
        updateCoverageRate();
        updateAssignmentStatus();
        updateLeakCount();
        updateAbnormalCount();
    }



    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    private void updateFinishRate() {
        List<TodayAssignment> todayAssignmentList = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getTodayAssignmentList(MyDateTimeUtils.getTodayStartTime());
        int shouldCount = 0;
        int signinCount = 0;
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            shouldCount += todayAssignment.getFrequency();
            signinCount += Math.min(todayAssignment.getFrequency(), todayAssignment.getSigninCount());
        }

        String str = String.format("%.1f%%", (double)signinCount * 100 / shouldCount);
        mCompletionRate.setText(str);

        String count = "签到数：" + signinCount + "次";
        mSigninCount.setText(count);
    }

    private void updateCoverageRate() {
        long leakCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getLeakCount(MyDateTimeUtils.getTodayStartTime());
        long count = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getTodayAssignmentCount(MyDateTimeUtils.getTodayStartTime());
        long coverageCount = count - leakCount;

        String str = String.format("%.1f%%", (double)coverageCount * 100 / count);
        mCoverageRate.setText(str);

        mDeskCount.setText("点位数：" + count + "个");
    }

    private void updateAssignmentStatus() {
        long finishCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getFinishCount(MyDateTimeUtils.getTodayStartTime());
        long count = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getTodayAssignmentCount(MyDateTimeUtils.getTodayStartTime());
        String completion = "" + finishCount + "/" + count;
        if (completion.length() >= 5) {
            mTodayCompletion.setTextSize(22);
        }
        mTodayCompletion.setText(completion);
    }

    private void updateLeakCount() {
        long leakCount = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getLeakCount(MyDateTimeUtils.getTodayStartTime());
        mTodayLeakCount.setText("" + leakCount);
    }

    private void updateAbnormalCount() {
        long abnormalCount = com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getNotEndCount();
        mAbnormalReportCount.setText("" + abnormalCount);
    }
}
