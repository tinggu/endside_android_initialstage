package com.ctfww.module.assignment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.fragment.PeriodDeskAssignmentListFragment;
import com.ctfww.module.assignment.fragment.PeriodRouteAssignmentListFragment;
import com.google.android.material.tabs.TabLayout;
import com.ctfww.commonlib.entity.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/assignment/list")
public class PeriodAssignmentListActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "PeriodAssignmentListActivity";

    private ImageView mBack;
    private TextView mTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_list_activity);
        initViews();
        setOnClickListener();
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("周期任务");

        TabLayout tabLayout = findViewById(R.id.assignment_tab_layout);
        ViewPager viewPager = findViewById(R.id.assignment_view_pager);

        String[] tableTitle = {"巡检点", "巡检路线"};

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PeriodDeskAssignmentListFragment());
        fragmentList.add(new PeriodRouteAssignmentListFragment());

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

    // 处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onGetMessage(MessageEvent messageEvent) {

    }
}