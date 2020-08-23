package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.tabs.TabLayout;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.fragment.KeepWatchPeriodAssignmentListFragment;
import com.ctfww.module.keepwatch.fragment.KeepWatchTodayAssignmentListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/keepwatch/assignmentList")
public class KeepWatchAssignmentListActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "KeepWatchAssignmentListActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mTransfer;
    private TextView mTakeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepwatch_assignment_list_activity);
        initViews();
        setOnClickListener();
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("任务");

        TabLayout tabLayout = findViewById(R.id.keepwatch_assignment_tab_layout);
        ViewPager viewPager = findViewById(R.id.keepwatch_assignment_view_pager);

        String[] tableTitle = {"当天任务", "周期任务"};

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new KeepWatchTodayAssignmentListFragment());
        fragmentList.add(new KeepWatchPeriodAssignmentListFragment());

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tableTitle[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        mTransfer = findViewById(R.id.keepwatch_transfer);
        mTakeBack = findViewById(R.id.keepwatch_take_back);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mTransfer.setOnClickListener(this);
        mTakeBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mTransfer.getId()) {
            ARouter.getInstance().build("/user/selectUser").navigation();
        }
        else if (id == mTakeBack.getId()) {
            takeBack();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("selected_user".equals(messageEvent.getMessage())) {
            String userId = messageEvent.getValue();
            transfer(userId);
            LogUtils.i(TAG, "onGetMessage: selected_user");
        }
    }

    private void transfer(String toUserId) {
        NetworkHelper.getInstance().transferKeepWatchAssignment(toUserId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                EventBus.getDefault().post(new MessageEvent("transfer_assignment"));
                LogUtils.i(TAG, "transfer success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "transfer fail: code = " + code);
            }
        });
    }

    private void takeBack() {
        NetworkHelper.getInstance().takeBackKeepWatchAssignment(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                EventBus.getDefault().post(new MessageEvent("take_back_assignment"));
                LogUtils.i(TAG, "takeBack success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "takeBack fail: code = " + code);
            }
        });
    }
}