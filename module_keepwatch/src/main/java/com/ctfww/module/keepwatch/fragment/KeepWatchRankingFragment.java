package com.ctfww.module.keepwatch.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.bumptech.glide.Glide;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.datahelper.sp.Const;
import com.ctfww.module.keepwatch.entity.Ranking;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KeepWatchRankingFragment extends Fragment {
    private final static String TAG = "KeepWatchRankingFragment";

    private View mV;

    private TextView mNoData;
    private LinearLayout mLL;
    private ImageView mGoldHead;
    private TextView mGoldPower;
    private TextView mGoldNickName;
    private ImageView mSilverHead;
    private TextView mSilverPower;
    private TextView mSilverNickName;
    private ImageView mBronzeHead;
    private TextView mBronzePower;
    private TextView mBronzeNickName;

    private String mType;
    private long mTimeStamp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_ranking_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mNoData = v.findViewById(R.id.keepwatch_no_data);

        mLL = v.findViewById(R.id.keepwatch_ranking_ll);

        mGoldHead = v.findViewById(R.id.keepwatch_gold_head);
        mGoldPower = v.findViewById(R.id.keepwatch_gold_score);
        mGoldNickName = v.findViewById(R.id.keepwatch_gold_nick_name);
        mSilverHead = v.findViewById(R.id.keepwatch_silver_head);
        mSilverPower = v.findViewById(R.id.keepwatch_silver_score);
        mSilverNickName = v.findViewById(R.id.keepwatch_silver_nick_name);
        mBronzeHead = v.findViewById(R.id.keepwatch_bronze_head);
        mBronzePower = v.findViewById(R.id.keepwatch_bronze_score);
        mBronzeNickName = v.findViewById(R.id.keepwatch_bronze_nick_name);

        mNoData.setVisibility(View.VISIBLE);
        mLL.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        mLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/ranking").navigation();
                EventBus.getDefault().postSticky(new MessageEvent(mType, "" + mTimeStamp));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getTodayRanking() {
        List<Ranking> rankingList = getRankingList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        mType = "keepwatch_day_report";
        mTimeStamp = System.currentTimeMillis();
        updateRankingToUI(rankingList);
    }

    public void getThisDayRanking(long timeStamp) {
        List<Ranking> rankingList = getRankingList(MyDateTimeUtils.getDayStartTime(timeStamp), MyDateTimeUtils.getDayEndTime(timeStamp));
        mType = "keepwatch_day_report";
        mTimeStamp = timeStamp;
        updateRankingToUI(rankingList);
    }

    public void getThisWeekRanking(long timeStamp) {
        List<Ranking> rankingList = getRankingList(MyDateTimeUtils.getWeekStartTime(timeStamp), MyDateTimeUtils.getWeekEndTime(timeStamp));
        mType = "keepwatch_week_report";
        mTimeStamp = timeStamp;
        updateRankingToUI(rankingList);
    }

    public void getThisMonthRanking(long timeStamp) {
        List<Ranking> rankingList = getRankingList(MyDateTimeUtils.getMonthStartTime(timeStamp), MyDateTimeUtils.getMonthEndTime(timeStamp));
        mType = "keepwatch_month_report";
        mTimeStamp = timeStamp;
        updateRankingToUI(rankingList);
    }

    private void updateRankingToUI(List<Ranking> keepWatchRankingList) {
        if (keepWatchRankingList.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mLL.setVisibility(View.GONE);
            return;
        }

        Ranking keepWatchRanking = keepWatchRankingList.get(0);
        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keepWatchRanking.getUserId());
        if (userInfo != null) {
            Glide.with(this).load(userInfo.getHeadUrl()).into(mGoldHead);
            mGoldNickName.setText(userInfo.getNickName());
        }
        mGoldPower.setText("综合分 " + keepWatchRanking.getScore());

        if (keepWatchRankingList.size() >= 2) {
            keepWatchRanking = keepWatchRankingList.get(1);
            userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keepWatchRanking.getUserId());
            if (userInfo != null) {
                Glide.with(this).load(userInfo.getHeadUrl()).into(mSilverHead);
                mSilverNickName.setText(userInfo.getNickName());
            }
            mSilverPower.setText("综合分 " + keepWatchRanking.getScore());
        }

        if (keepWatchRankingList.size() >= 3) {
            keepWatchRanking = keepWatchRankingList.get(2);
            userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keepWatchRanking.getUserId());
            if (userInfo != null) {
                Glide.with(this).load(userInfo.getHeadUrl()).into(mBronzeHead);
                mBronzeNickName.setText(userInfo.getNickName());
            }
            mBronzePower.setText("综合分 " + keepWatchRanking.getScore());
        }

        mNoData.setVisibility(View.GONE);
        mLL.setVisibility(View.VISIBLE);

    }

    public List<Ranking> getRankingList(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<Ranking>();
        }

        List<GroupUserInfo> groupUserInfoList = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getGroupUserList();
        List<Ranking> rankingList = new ArrayList<>();
        for (int i = 0; i < groupUserInfoList.size(); ++i) {
            Ranking ranking = new Ranking();
            String userId = groupUserInfoList.get(i).getUserId();
            ranking.setUserId(userId);

            List<TodayAssignment> todayAssignmentList = com.ctfww.module.assignment.datahelper.dbhelper.DBHelper.getInstance().getFinishList(groupId, userId, startTime, endTime);
            int score = 0;
            for (int j = 0; j < todayAssignmentList.size(); ++j) {
                TodayAssignment todayAssignment = todayAssignmentList.get(j);
                score += todayAssignment.getScore();
            }

            List<KeyEvent> keyEventList = com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper.getInstance().getEndList(groupId, userId, startTime, endTime);
            for (int j = 0; j < keyEventList.size(); ++j) {
                KeyEvent keyEvent = keyEventList.get(j);
                score += keyEvent.getScore();
            }

            ranking.setScore(score);

            rankingList.add(ranking);
        }

        rankingList.sort(new Comparator<Ranking>() {
            @Override
            public int compare(Ranking o1, Ranking o2) {
                Integer val1 = o1.getScore();
                Integer val2 = o2.getScore();
                return val2.compareTo(val1);
            }
        });

        return rankingList;
    }
}
