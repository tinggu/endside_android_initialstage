package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.KeepWatchRankingListAdapter;
import com.ctfww.module.keepwatch.datahelper.sp.Const;
import com.ctfww.module.keepwatch.entity.Ranking;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.user.entity.GroupUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Route(path = "/keepwatch/ranking")
public class KeepWatchRankingListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "KeepWatchRankingListActivity";

    private ImageView mBack;
    private TextView mTittle;

    private RecyclerView mRankingListView;
    private KeepWatchRankingListAdapter mKeepWatchRankingListAdapter;
    private List<Ranking> mKeepWatchRankingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_ranking_list_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("排行榜");

        mRankingListView = findViewById(R.id.keepwatch_ranking_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mRankingListView.setLayoutManager(layoutManager);

        mKeepWatchRankingListAdapter = new KeepWatchRankingListAdapter(mKeepWatchRankingList, this);
        mRankingListView.setAdapter(mKeepWatchRankingListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    private List<Ranking> getRankingList(long startTime, long endTime) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return new ArrayList<Ranking>();
        }

        List<GroupUserInfo> groupUserInfoList = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getGroupUserList();
        List<Ranking> rankingList = new ArrayList<>();
        for (int i = 0; i < groupUserInfoList.size(); ++i) {
            Ranking ranking  = new Ranking();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyMessageEvent(MessageEvent messageEvent) {
        if ("keepwatch_day_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            long startTime = MyDateTimeUtils.getDayStartTime(timeStamp);
            long endTime = MyDateTimeUtils.getDayEndTime(timeStamp);
            getRankingList(startTime, endTime);
        }
        else if ("keepwatch_week_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            long startTime = MyDateTimeUtils.getWeekStartTime(timeStamp);
            long endTime = MyDateTimeUtils.getWeekEndTime(timeStamp);
            getRankingList(startTime, endTime);
        }
        else if ("keepwatch_month_report".equals(messageEvent.getMessage())) {
            long timeStamp = GlobeFun.parseLong(messageEvent.getValue());
            long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
            long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);
            getRankingList(startTime, endTime);
        }

//        EventBus.getDefault().removeStickyEvent(messageEvent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}