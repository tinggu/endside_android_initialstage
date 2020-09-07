package com.ctfww.module.desk.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.Qr;
import com.ctfww.commonlib.location.GPSLocationManager;
import com.ctfww.commonlib.location.MyLocation;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.Utils;
import com.ctfww.module.desk.datahelper.airship.Airship;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.datahelper.sp.Const;
import com.ctfww.module.desk.entity.DeskInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = "/desk/modifyDesk")
public class ModifyDeskActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ModifyDeskActivity";

    private ImageView mBack;
    private TextView mTittle;
    private EditText mDeskName;
    private TextView mDeskAddress;
    private TextView mConfirm;

    private DeskInfo mDeskInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.modify_desk_activity);

        initViews();

        setOnClickListener();

       String deskInfoStr = getIntent().getStringExtra("desk_info");
       if (!TextUtils.isEmpty(deskInfoStr)) {
           mDeskInfo = GsonUtils.fromJson(deskInfoStr, DeskInfo.class);
           mDeskName.setText(mDeskInfo.getDeskName());
           mDeskAddress.setText(mDeskInfo.getDeskAddress());
       }
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("修改签到点信息");
        mDeskName = findViewById(R.id.desk_name);
        mDeskAddress = findViewById(R.id.desk_address);
        mConfirm = findViewById(R.id.top_addition);
        mConfirm.setText("确定");
        mConfirm.setVisibility(View.VISIBLE);
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
            modifyDesk();
        }
    }

    private void modifyDesk() {
        if (mDeskInfo == null) {
            return;
        }

        String deskName = mDeskName.getText().toString();
        String deskAddress = mDeskAddress.getText().toString();
        if (TextUtils.isEmpty(deskName) || TextUtils.isEmpty(deskAddress)) {
            DialogUtils.onlyPrompt("名称与地址都不能为空！", this);
            return;
        }

        mDeskInfo.setDeskName(deskName);
        mDeskInfo.setDeskAddress(deskAddress);
        mDeskInfo.setTimeStamp(System.currentTimeMillis());
        mDeskInfo.setSynTag("modify");

        DBHelper.getInstance().updateDesk(mDeskInfo);
        Airship.getInstance().synDeskToCloud();

        EventBus.getDefault().post(new MessageEvent("modify_desk"));

        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
