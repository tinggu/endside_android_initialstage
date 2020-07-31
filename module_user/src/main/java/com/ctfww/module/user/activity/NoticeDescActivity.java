package com.ctfww.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.UserNoticeListAdapter;
import com.ctfww.module.user.adapter.UserNoticeReadStatusListAdapter;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;

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
    private List<NoticeReadStatus> mNoticeReadStatusList = new ArrayList<>();

    private NoticeInfo mNoticeInfo;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_notice_desc_activity);
        processIntent();
        initViews();
        setOnClickListener();
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

        mNoticeReadStatusListAdapter = new UserNoticeReadStatusListAdapter(mNoticeReadStatusList);
        mNoticeReadStatusListView.setAdapter(mNoticeReadStatusListAdapter);

        getNoticeReadStatus();
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

    private void getNoticeReadStatus() {
        NetworkHelper.getInstance().getNoticeReadStatus(mNoticeInfo.getNoticeId(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mNoticeReadStatusList = (ArrayList<NoticeReadStatus>)obj;
                mNoticeReadStatusListAdapter.setList(mNoticeReadStatusList);
                mNoticeReadStatusListAdapter.notifyDataSetChanged();
                mNoticeReadStatusListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getNoticeReadStatus fail: code = " + code);
            }
        });
    }
}