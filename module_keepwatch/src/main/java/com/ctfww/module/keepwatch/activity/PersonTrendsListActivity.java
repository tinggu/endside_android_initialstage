package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.PersonTrendsListAdapter;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.signin.entity.SigninInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@Route(path = "/keepwatch/personTrends")
public class PersonTrendsListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "PersonTrendsListActivity";

    private ImageView mBack;
    private TextView mTittle;

    private RecyclerView mPersonTrendsListView;
    private PersonTrendsListAdapter mPersonTrendsListAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_person_trends_list_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);

        com.ctfww.module.signin.datahelper.airship.Airship.getInstance().synSigninFromCloud();
        com.ctfww.module.keyevents.datahelper.airship.Airship.getInstance().synKeyEventTraceFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("人员动态");

        mPersonTrendsListView = findViewById(R.id.keepwatch_person_trends_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mPersonTrendsListView.setLayoutManager(layoutManager);

        List<SigninInfo> signinList = com.ctfww.module.signin.datahelper.dbhelper.DBQuickEntry.getSigninList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        List<KeyEventTrace> keyEventTraceList = com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getKeyEventTraceListForGroup(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        mPersonTrendsListAdapter = new PersonTrendsListAdapter(signinList, keyEventTraceList, this);
        mPersonTrendsListView.setAdapter(mPersonTrendsListAdapter);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if (com.ctfww.module.signin.datahelper.sp.Const.FINISH_SIGNIN_SYN.equals(messageEvent.getMessage())
        || com.ctfww.module.keyevents.datahelper.sp.Const.FINISH_KEY_EVENT_TRACE_SYN.equals(messageEvent.getMessage())) {
            List<SigninInfo> signinList = com.ctfww.module.signin.datahelper.dbhelper.DBQuickEntry.getSigninList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
            List<KeyEventTrace> keyEventTraceList = com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getKeyEventTraceListForGroup(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
            mPersonTrendsListAdapter = new PersonTrendsListAdapter(signinList, keyEventTraceList, this);
            mPersonTrendsListView.setAdapter(mPersonTrendsListAdapter);
        }
    }
}