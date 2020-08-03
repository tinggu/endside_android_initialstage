package com.ctfww.module.attend.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.attend.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AttendStatisticsFragment extends Fragment {
    private final static String TAG = "AttendStatisticsFragment";

    private View mV;

    private RelativeLayout mInformationRL;
    private TextView mUnreadCount;
    private LinearLayout mGroupLL;
    private ImageView mGroupSelect;
    private TextView mGroupName;
    private ImageView mAddPop;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mKeepWatchLL;
    private ImageView mCompletionRatePrompt;
    private ImageView mCoverageRatePrompt;
    private TextView mCompletionRate;
    private TextView mCoverageRate;
    private TextView mSigninCount;
    private TextView mDeskCount;

    private AttendPersonTrendsFragment mAttendPersonTrendsFragment;

    private TextView mRankingTittle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.attend_statistics_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mInformationRL = v.findViewById(R.id.top_information_rl);
        mUnreadCount = v.findViewById(R.id.information_unread_count);
        mGroupLL = v.findViewById(R.id.top_tittle_ll);
        mGroupSelect = v.findViewById(R.id.top_select);
        mGroupSelect.setVisibility(View.VISIBLE);
        mGroupName = v.findViewById(R.id.top_tittle);
        String groupName = SPStaticUtils.getString("working_group_name");
        if (TextUtils.isEmpty(groupName)) {
            mGroupName.setText("请选择群组");
        }
        else {
            mGroupName.setText(groupName);
        }
        mAddPop = v.findViewById(R.id.top_addition);
        mAddPop.setImageResource(R.drawable.add_pop);

        mSwipeRefreshLayout = v.findViewById(R.id.attend_refresh);
        mKeepWatchLL = v.findViewById(R.id.keepwatch_keep_watch_ll);
        mCompletionRatePrompt = v.findViewById(R.id.keepwatch_completion_rate_prompt);
        mCoverageRatePrompt = v.findViewById(R.id.keepwatch_coverage_rate_prompt);
        mCompletionRate = v.findViewById(R.id.keepwatch_completion_rate);
        mCoverageRate = v.findViewById(R.id.keepwatch_coverage_rate);
        mSigninCount = v.findViewById(R.id.keepwatch_signin_count);
        mDeskCount = v.findViewById(R.id.keepwatch_signin_desk_count);

        mCompletionDefinition = v.findViewById(R.id.keepwatch_completion_definition);
        mTodayCompletionLL = v.findViewById(R.id.keepwatch_today_completion_ll);
        mTodayCompletion = v.findViewById(R.id.keepwatch_today_completion);
        mTodayLeakCountLL = v.findViewById(R.id.keepwatch_today_leak_count_ll);
        mTodayLeakCount = v.findViewById(R.id.keepwatch_today_leak_count);
        mAbnormalReportCountLL = v.findViewById(R.id.keepwatch_abnormal_report_count_ll);
        mAbnormalReportCount = v.findViewById(R.id.keepwatch_abnormal_report_count);

        mKeyEventSnatchFragment = (KeyEventSnatchFragment) getChildFragmentManager().findFragmentById(R.id.keepwatch_snatch_key_event_fragment);

        mKeepWatchPersonTrendsFragment = (KeepWatchPersonTrendsFragment)getChildFragmentManager().findFragmentById(R.id.keepwatch_person_trends_fragment);

        mKeepWatchRankingFragment = (KeepWatchRankingFragment)getChildFragmentManager().findFragmentById(R.id.keepwatch_ranking_fragment);
        mRankingTittle = mKeepWatchRankingFragment.getView().findViewById(R.id.keepwatch_ranking_tittle);
        mRankingTittle.setText("今日排行榜");
    }

    private void setOnClickListener() {
        mInformationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/invite").navigation();
            }
        });

        mKeepWatchLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/statisticsDetail").navigation();
            }
        });

        mAddPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.showKeepWatchAddPopupWindow(getActivity(), mAddPop, -320);
            }
        });

        mGroupLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/selectGroup").navigation();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTodayKeepWatchStatistics();
                getNoEndKeyEventCount();
                mKeyEventSnatchFragment.getDoingKeyEvent();
                mKeepWatchPersonTrendsFragment.getPersonTrends();
                mKeepWatchRankingFragment.getTodayRanking();
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
        if ("tms_first_token".equals(msg)) {
            LogUtils.i(TAG, "tms_first_token refresh!");
            getTodayKeepWatchStatistics();
            getNoEndKeyEventCount();
            mKeyEventSnatchFragment.getDoingKeyEvent();
            mKeepWatchPersonTrendsFragment.getPersonTrends();
            mKeepWatchRankingFragment.getTodayRanking();
        }
        else if ("bind_group".equals(msg)) {
            LogUtils.i(TAG, "bind_group refresh!");
            getTodayKeepWatchStatistics();
            getNoEndKeyEventCount();
            mKeyEventSnatchFragment.getDoingKeyEvent();
            mKeepWatchPersonTrendsFragment.getPersonTrends();
            mKeepWatchRankingFragment.getTodayRanking();
            String groupName= SPStaticUtils.getString("working_group_name");
            LogUtils.i(TAG, "onGetMessage: groupName = " + groupName);
            mGroupName.setText(SPStaticUtils.getString("working_group_name"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isFirstToken()) {
            LogUtils.i(TAG, "onResume refresh!");
            getTodayKeepWatchStatistics();
            getNoEndKeyEventCount();
            mKeyEventSnatchFragment.getDoingKeyEvent();
            mKeepWatchPersonTrendsFragment.getPersonTrends();
            mKeepWatchRankingFragment.getTodayRanking();
        }
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

    private void getTodayKeepWatchStatistics() {
        long startTime = MyDateTimeUtils.getTodayStartTime();
        long endTime = MyDateTimeUtils.getTodayEndTime();
        NetworkHelper.getInstance().getKeepWatchStatistics(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                KeepWatchStatisticsByPeriod keepWatchStatisticsByPeriod = (KeepWatchStatisticsByPeriod)obj;
                updateWatchInfoToUI(keepWatchStatisticsByPeriod);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getKeepWatchInfo fail: code = " + code);
            }
        });
    }

    private void getNoEndKeyEventCount() {
        com.ctfww.module.keyevents.datahelper.NetworkHelper.getInstance().getNoEndKeyEventCount(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                int count = (int)obj;
                mAbnormalReportCount.setText("" + count);
                LogUtils.i(TAG, "getNoEndKeyEventCount success: count = " + count);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getNoEndKeyEventCount fail: code = " + code);
            }
        });
    }

    private void updateWatchInfoToUI(KeepWatchStatisticsByPeriod keepWatchStatisticsByPeriod) {
        String rate = keepWatchStatisticsByPeriod.getCompletionRatePercent();
        mCompletionRate.setText(rate);

        rate = keepWatchStatisticsByPeriod.getCoverageRatePercent();
        mCoverageRate.setText(rate);

        String count = "签到数：" + keepWatchStatisticsByPeriod.getSigninCount() + "次";
        mSigninCount.setText(count);

        count = "点位数：" + keepWatchStatisticsByPeriod.getDeskCount() + "个";
        mDeskCount.setText(count);

        String completion = "" + keepWatchStatisticsByPeriod.getAssignmentCount() + "/" + keepWatchStatisticsByPeriod.getShouldAssignmentCount();
        if (completion.length() >= 5) {
            mTodayCompletion.setTextSize(22);
        }
        mTodayCompletion.setText(completion);

        mTodayLeakCount.setText("" + (keepWatchStatisticsByPeriod.getShouldDeskCount() - keepWatchStatisticsByPeriod.getDeskCount()));

//        mAbnormalReportCount.setText("" + keepWatchInfo.getAbnormalReportCount());
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
}
