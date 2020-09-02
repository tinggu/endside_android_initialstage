package com.ctfww.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.UserNoticeListAdapter;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.NoticeInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@Route(path = "/user/notice")
public class NoticeListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "NoticeListActivity";

    private final static int REQUEST_CODE_ADD_MEMBER = 1;

    private ImageView mBack;
    private TextView mTittle;

    private RecyclerView mNoticeListView;
    private UserNoticeListAdapter mNoticeListAdapter;

    private TextView mNoNoticePrompt;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_notice_list_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);
        Airship.getInstance().synNoticeInfoFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("消息管理");

        mNoNoticePrompt = findViewById(R.id.user_prompt_no_notice);

        mNoticeListView = findViewById(R.id.user_notice_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mNoticeListView.setLayoutManager(layoutManager);

        List<NoticeInfo> noticeInfoList = DBQuickEntry.getWorkingNoticeList();
        mNoticeListAdapter = new UserNoticeListAdapter(noticeInfoList, this);
        mNoticeListView.setAdapter(mNoticeListAdapter);

        if (noticeInfoList.isEmpty()) {
            mNoNoticePrompt.setVisibility(View.VISIBLE);
            mNoticeListView.setVisibility(View.GONE);
        }
        else {
            mNoNoticePrompt.setVisibility(View.GONE);
            mNoticeListView.setVisibility(View.VISIBLE);
        }
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            Airship.getInstance().synNoticeInfoToCloud();
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageEvent messageEvent) {
        if ("finish_notice_syn".equals(messageEvent.getMessage())) {
            List<NoticeInfo> noticeInfoList = DBQuickEntry.getWorkingNoticeList();
            mNoticeListAdapter.setList(noticeInfoList);
            mNoticeListAdapter.notifyDataSetChanged();

            if (noticeInfoList.isEmpty()) {
                mNoNoticePrompt.setVisibility(View.VISIBLE);
                mNoticeListView.setVisibility(View.GONE);
            }
            else {
                mNoNoticePrompt.setVisibility(View.GONE);
                mNoticeListView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}