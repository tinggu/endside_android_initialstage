package com.ctfww.module.keepwatch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDesk;

import org.greenrobot.eventbus.EventBus;

public class KeepWatchAddRouteTagActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "KeepWatchAddRouteTagActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mConfirm;
    private EditText mTag;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_add_route_tag_activity);
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("增加路线标签");
        mConfirm = findViewById(R.id.top_addition);
        mConfirm.setText("确定");
        mConfirm.setVisibility(View.VISIBLE);
        mTag = findViewById(R.id.keepwatch_route_desk_tag);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mConfirm.getId()) {
            if (TextUtils.isEmpty(mTag.getText().toString())) {
                DialogUtils.onlyPrompt("请填写标签！", this);
                return;
            }

            Intent intent = new Intent();
            intent.putExtra("route_desk_tag", mTag.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
