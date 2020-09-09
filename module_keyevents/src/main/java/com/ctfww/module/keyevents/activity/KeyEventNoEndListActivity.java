package com.ctfww.module.keyevents.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.datahelper.airship.Airship;
import com.ctfww.module.keyevents.datahelper.sp.Const;
import com.ctfww.module.keyevents.fragment.KeyEventListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = "/keyevents/noEndList")
public class KeyEventNoEndListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "KeyEventNoEndListActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mProcessed;

    KeyEventListFragment mKeyEventListFragment;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keyevent_no_end_list_activity);
        initViews();
        setOnClickListener();

        mKeyEventListFragment.showNoEndList();

        EventBus.getDefault().register(this);

        Airship.getInstance().synKeyEventFromCloud();
        Airship.getInstance().synKeyEventPersonFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("未处理工单");
        mProcessed = findViewById(R.id.top_addition);
        mProcessed.setText("已处理");
        mProcessed.setVisibility(View.VISIBLE);

        mKeyEventListFragment = (KeyEventListFragment)getSupportFragmentManager().findFragmentById(R.id.keepevent_list_fragment);
        mKeyEventListFragment.showNoEndList();
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mProcessed.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mProcessed.getId()) {
            ARouter.getInstance().build("/keyevents/endList").navigation();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if (Const.FINISH_KEY_EVNET_PERSON_SYN.equals(messageEvent.getMessage())) {
            mKeyEventListFragment.showNoEndList();
        }
        else if (Const.FINISH_KEY_EVENT_SYN.equals(messageEvent.getMessage())) {
            mKeyEventListFragment.showNoEndList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}