package com.guoliang.module.keepwatch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.utils.DialogUtils;
import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.module.keepwatch.R;

import static com.blankj.utilcode.util.ActivityUtils.finishAllActivities;

@Route(path = "/keepwatch/setting")
public class KeepWatchSettingActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "KeepWatchSettingActivity";

    private LinearLayout mUserInfo;
    private LinearLayout mAccount;
    private LinearLayout mVersion;
    private TextView mCurrVersion;
    private LinearLayout mAbout;
    private ImageView mBack;
    private TextView mTittle;
    private TextView mQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepwatch_setting_activity);

        initViews();

        setOnClickListener();
    }

    private void initViews() {
        mUserInfo = findViewById(R.id.keepwatch_setting_my_info);
        mAccount = findViewById(R.id.keepwatch_setting_account);
        mVersion = findViewById(R.id.setting_update_version);
        mCurrVersion = findViewById(R.id.keepwatch_setting_curr_version);
        mCurrVersion.setText(GlobeFun.getAppVersion(this));
        mAbout = findViewById(R.id.setting_about);
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("设置");
        mQuit = findViewById(R.id.keepwatch_setting_full_width_btn);
        mQuit.setText("退出");
    }

    private void setOnClickListener() {
        mUserInfo.setOnClickListener(this);
        mAccount.setOnClickListener(this);
        mVersion.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mQuit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mUserInfo.getId()) {
            ARouter.getInstance().build("/user/info").navigation();
        }
        else if (id == mAccount.getId()) {
            ARouter.getInstance().build("/user/account").navigation();
        }
        else if (id == mAccount.getId()) {
            ARouter.getInstance().build("/user/account").navigation();
        }
        else if (id == mVersion.getId()) {
            processVersion();
        }
        else if (id == mAbout.getId()) {
            Intent intent = new Intent(this, KeepWatchAboutAppActivity.class);
            startActivity(intent);
        }
        else if (id == mQuit.getId()) {
            DialogUtils.selectDialog("退出后再进App，需要重新进行登录，请确认是否要退出？", this, new DialogUtils.Callback() {
                @Override
                public void onConfirm(int radioSelectItem) {
                    SPStaticUtils.remove("user_had_login_flag");
                    finishAllActivities();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    private void processVersion() {
 //       String currVersion = GlobeFun.getAppVersion(this);
        ToastUtils.showShort("已经是最新版本了！");
    }
}
