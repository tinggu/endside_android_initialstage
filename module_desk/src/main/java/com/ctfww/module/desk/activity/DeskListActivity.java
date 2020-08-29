package com.ctfww.module.desk.activity;

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
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.adapter.DeskListAdapter;
import com.ctfww.module.desk.datahelper.airship.Airship;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.desk.entity.DeskInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/keepwatch/deskList")
public class DeskListActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "DeskListActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mAddDesk;

    private RecyclerView mDeskListView;
    private DeskListAdapter mDeskListAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.desk_list_activity);
        initViews();
        setOnClickListener();

        EventBus.getDefault().register(this);

        Airship.getInstance().synDeskFromCloud();
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

        mDeskListView = findViewById(R.id.desk_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mDeskListView.setLayoutManager(layoutManager);

        List<DeskInfo> deskList = DBQuickEntry.getWorkingDeskList();
        mDeskListAdapter = new DeskListAdapter(deskList, this);
        mDeskListView.setAdapter(mDeskListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mAddDesk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mAddDesk.getId()) {
            Intent intent = new Intent(this, AddDeskActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("finish_desk_syn".equals(messageEvent.getMessage())) {
            List<DeskInfo> deskList = DBQuickEntry.getWorkingDeskList();
            mDeskListAdapter.setList(deskList);
            mDeskListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                return;
            }

            List<DeskInfo> deskList = DBQuickEntry.getWorkingDeskList();
            mDeskListAdapter.setList(deskList);
            mDeskListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}