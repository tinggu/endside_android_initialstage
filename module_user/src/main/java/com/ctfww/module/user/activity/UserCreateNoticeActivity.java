package com.ctfww.module.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;
import com.ctfww.module.user.entity.UserInfo;

@Route(path = "/user/createNotice")
public class UserCreateNoticeActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UserCreateNoticeActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mRelease;
    private EditText mNoticeTittle;
    private EditText mNoticeDesc;

    private UserInfo mSelfInfo;
    private String mGroupId;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_create_notice_activity);
        mGroupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        mSelfInfo = DBQuickEntry.getSelfInfo();
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("创建通知");
        mRelease = findViewById(R.id.top_addition);
        mRelease.setText("发布");
        mRelease.setVisibility(View.VISIBLE);
        mNoticeTittle = findViewById(R.id.user_notice_tittle);
        mNoticeDesc = findViewById(R.id.user_notice_desc);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mRelease.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mRelease.getId()) {
            if (TextUtils.isEmpty(mGroupId) || mSelfInfo == null) {
                return;
            }

            NoticeInfo noticeInfo = new NoticeInfo();
            noticeInfo.setGroupId(SPStaticUtils.getString(Const.WORKING_GROUP_ID));
            noticeInfo.setUserId(mSelfInfo.getUserId());
            noticeInfo.setTittle(mNoticeTittle.getText().toString());
            noticeInfo.setContent(mNoticeDesc.getText().toString());
            noticeInfo.setType(0);
            noticeInfo.setTimeStamp(System.currentTimeMillis());
            noticeInfo.setSynTag("new");
            noticeInfo.combineNoticeId();
            DBHelper.getInstance().addNotice(noticeInfo);
            Airship.getInstance().synNoticeInfoToCloud();

            NoticeReadStatus readStatus = new NoticeReadStatus();
            readStatus.setGroupId(mGroupId);
            readStatus.setNoticeId(noticeInfo.getNoticeId());
            readStatus.setUserId(mSelfInfo.getUserId());
            readStatus.setTimeStamp(noticeInfo.getTimeStamp());
            readStatus.setFlag(2);
            readStatus.combieId();
            readStatus.setSynTag("new");
            DBHelper.getInstance().addNoticeReadStatus(readStatus);
            Airship.getInstance().synNoticeReadStatusToCloud();

            finish();
        }
    }
}
