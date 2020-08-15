package com.ctfww.module.keepwatch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.DataHelper.airship.Airship;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.KeepWatchDeskListAdapter;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/keepwatch/deskList")
public class KeepWatchDeskListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "KeepWatchDeskListActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mAddDesk;

    private RecyclerView mDeskListView;
    private KeepWatchDeskListAdapter mKeepWatchDeskListAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_desk_list_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);

        Airship.getInstance().synKeepWatchDeskFromCloud();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("签到点列表");
        mAddDesk = findViewById(R.id.top_addition);
        mAddDesk.setText("添加");
        if ("admin".equals(SPStaticUtils.getString("role"))) {
            mAddDesk.setVisibility(View.VISIBLE);
        }

        mDeskListView = findViewById(R.id.keepwatch_desk_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mDeskListView.setLayoutManager(layoutManager);

        mKeepWatchDeskListAdapter = new KeepWatchDeskListAdapter(new ArrayList<KeepWatchDesk>(), this);
        mDeskListView.setAdapter(mKeepWatchDeskListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mAddDesk.setOnClickListener(this);
    }

    private void getDeskList() {
        String groupId = SPStaticUtils.getString("working_group_id");
        List<KeepWatchDesk> deskList = DBHelper.getInstance().getKeepWatchDeskList(groupId);
        LogUtils.i(TAG, "getDeskList: deskList.size() = " + deskList.size());
        mKeepWatchDeskListAdapter.setList(deskList);
        mKeepWatchDeskListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mAddDesk.getId()) {
            Intent intent = new Intent(this, KeepWatchAddDeskActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("finish_desk_syn".equals(messageEvent.getMessage())) {
            getDeskList();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                return;
            }

            getDeskList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}