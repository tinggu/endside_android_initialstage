package com.ctfww.module.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.R;
import com.ctfww.module.user.adapter.User2GroupAdapter;
import com.ctfww.module.user.adapter.UserNoticeListAdapter;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.NoticeInfo;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/user/notice")
public class NoticeActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "NoticeActivity";

    private final static int REQUEST_CODE_ADD_MEMBER = 1;

    private ImageView mBack;
    private TextView mTittle;

    private RecyclerView mNoticeListView;
    private UserNoticeListAdapter mNoticeListAdapter;
    private List<NoticeInfo> mNoticeInfoList = new ArrayList<>();

    private TextView mNoNoticePrompt;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_notice_activity);
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("消息管理");

        mNoNoticePrompt = findViewById(R.id.user_prompt_no_notice);

        mNoticeListView = findViewById(R.id.user_notice_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mNoticeListView.setLayoutManager(layoutManager);

        mNoticeListAdapter = new UserNoticeListAdapter(mNoticeInfoList, this);
        mNoticeListView.setAdapter(mNoticeListAdapter);

        mNoticeListView.setVisibility(View.GONE);

        LogUtils.i(TAG, "aaaaaaaaaaaaaaaaaaaaaaa");
        getNotice();
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

    private void getNotice() {
        NetworkHelper.getInstance().getNotice(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mNoticeInfoList = (ArrayList<NoticeInfo>)obj;
                if (mNoticeInfoList.isEmpty()) {
                    mNoticeListView.setVisibility(View.GONE);
                    mNoNoticePrompt.setVisibility(View.VISIBLE);
                    return;
                }

                mNoticeListAdapter.setList(mNoticeInfoList);
                mNoticeListAdapter.notifyDataSetChanged();
                mNoticeListView.setVisibility(View.VISIBLE);
                mNoNoticePrompt.setVisibility(View.GONE);

                addNoticeReadStatus();
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getNotice fail: code = " + code);
            }
        });
    }

    private void addNoticeReadStatus() {
        boolean isChange = false;
        for (int i = 0; i < mNoticeInfoList.size(); ++i) {
            final NoticeInfo noticeInfo = mNoticeInfoList.get(i);
            if (noticeInfo.getFlag() == 0) {
                continue;
            }

            NetworkHelper.getInstance().addNoticeReadStatus(noticeInfo.getNoticeId(), 1, new IUIDataHelperCallback() {
                @Override
                public void onSuccess(Object obj) {
                    noticeInfo.setFlag(1);
                    mNoticeListAdapter.setList(mNoticeInfoList);
                    mNoticeListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(int code) {
                    LogUtils.i(TAG, "addNoticeReadStatus fail: code = " + code);
                }
            });
        }
    }
}