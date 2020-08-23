package com.ctfww.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.UserNoticeReadStatusListAdapter;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoticeDescActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "NoticeDescActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mNoticeTittle;
    private TextView mNoticeContent;
    private TextView mReleaseNickName;
    private TextView mReleaseDateTime;

    private RecyclerView mNoticeReadStatusListView;
    private UserNoticeReadStatusListAdapter mNoticeReadStatusListAdapter;

    private NoticeInfo mNoticeInfo;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_notice_desc_activity);
        processIntent();
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);
        Airship.getInstance().synNoticeReadStatusFromCloud();
    }

    private void processIntent() {
        String noticeInfoStr = getIntent().getStringExtra("notice_info");
        mNoticeInfo = GsonUtils.fromJson(noticeInfoStr, NoticeInfo.class);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("消息详情");

        mNoticeTittle = findViewById(R.id.user_notice_tittle);
        mNoticeTittle.setText(mNoticeInfo.getTittle());
        mNoticeContent = findViewById(R.id.user_notice_desc);
        mNoticeContent.setText(mNoticeInfo.getContent());
        mReleaseNickName = findViewById(R.id.user_release_nick_name);
        mReleaseNickName.setText(mNoticeInfo.getNickName());
        mReleaseDateTime = findViewById(R.id.user_release_date_time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mNoticeInfo.getTimeStamp());
        String dateTime = String.format("%04d-%02d-%02d %02d:%02d:%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        mReleaseDateTime.setText(dateTime);

        mNoticeReadStatusListView = findViewById(R.id.user_notice_read_status_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mNoticeReadStatusListView.setLayoutManager(layoutManager);

        List<NoticeReadStatus> noticeReadStatusList = DBHelper.getInstance().getNoticeReadStatusList(mNoticeInfo.getNoticeId());
        mNoticeReadStatusListAdapter = new UserNoticeReadStatusListAdapter(noticeReadStatusList);
        mNoticeReadStatusListView.setAdapter(mNoticeReadStatusListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("finish_notice_read_status_syn".equals(messageEvent.getMessage())) {
            List<NoticeReadStatus> noticeReadStatusList = DBHelper.getInstance().getNoticeReadStatusList(mNoticeInfo.getNoticeId());
            mNoticeReadStatusListAdapter.setList(noticeReadStatusList);
            mNoticeReadStatusListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}