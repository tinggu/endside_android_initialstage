package com.ctfww.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;

@Route(path = "/user/createNotice")
public class UserCreateNoticeActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UserCreateNoticeActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mRelease;
    private EditText mNoticeTittle;
    private EditText mNoticeDesc;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_create_notice_activity);
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
            UserInfo userInfo = DBQuickEntry.getSelfInfo();
            if (userInfo == null) {
                return;
            }

            NoticeInfo noticeInfo = new NoticeInfo();
            noticeInfo.setGroupId(SPStaticUtils.getString("working_group_id"));
            noticeInfo.setUserId(userInfo.getUserId());
            noticeInfo.setNickName(userInfo.getNickName());
            noticeInfo.setTittle(mNoticeTittle.getText().toString());
            noticeInfo.setContent(mNoticeDesc.getText().toString());
            noticeInfo.setType(0);
            noticeInfo.setTimeStamp(System.currentTimeMillis());
            noticeInfo.setFlag(2);
            noticeInfo.setSynTag("new");

            noticeInfo.combineNoticeId();

            DBHelper.getInstance().addNotice(noticeInfo);

            Airship.getInstance().synNoticeInfoToCloud();

            finish();
        }
    }
}
