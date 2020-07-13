package com.guoliang.module.keyevents.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.module.keyevents.Entity.KeyEvent;
import com.guoliang.module.keyevents.R;
import com.guoliang.module.keyevents.adapter.KeyEventListAdapter;
import com.guoliang.module.keyevents.datahelper.NetworkHelper;
import com.guoliang.module.keyevents.fragment.KeyEventListFragment;

import java.util.ArrayList;
import java.util.List;

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

        mKeyEventListFragment.getNoEndList();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("未处理工单");
        mProcessed = findViewById(R.id.top_addition);
        mProcessed.setText("已处理");
        mProcessed.setVisibility(View.VISIBLE);

        mKeyEventListFragment = (KeyEventListFragment)getSupportFragmentManager().findFragmentById(R.id.keepevent_list_fragment);
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


}