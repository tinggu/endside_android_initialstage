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
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.datahelper.sp.Const;
import com.ctfww.module.assignment.fragment.TodayDeskAssignmentListFragment;
import com.ctfww.module.assignment.fragment.TodayRouteAssignmentListFragment;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/assignment/todayAssignment")
public class TodayAssignmentListActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "TodayAssignmentListActivity";

    private ImageView mBack;
    private TextView mTittle;

    private TodayDeskAssignmentListFragment mDeskFragment;
    private TodayRouteAssignmentListFragment mRouteFragment;

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
        mTittle.setText("今日任务");

        TabLayout tabLayout = findViewById(R.id.assignment_tab_layout);
        ViewPager viewPager = findViewById(R.id.assignment_view_pager);

        String[] tableTitle = {"巡检点", "巡检路线"};

        List<Fragment> fragmentList = new ArrayList<>();
        mDeskFragment = new TodayDeskAssignmentListFragment();
        fragmentList.add(mDeskFragment);
        mRouteFragment = new TodayRouteAssignmentListFragment();
        fragmentList.add(mRouteFragment);

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
        if (Const.FINISH_DESK_ASSIGNMENT_SYN.equals(messageEvent.getMessage())) {
            mDeskFragment.update();
        }
        else if (Const.FINISH_ROUTE_ASSIGNMENT_SYN.equals(messageEvent.getMessage())) {
            mRouteFragment.update();
        }
        else if (Const.ADD_DESK.equals(messageEvent.getMessage()) || Const.MODIFY_DESK.equals(messageEvent.getMessage())) {
            mDeskFragment.update();
        }
        else if (Const.ADD_ROUTE.equals(messageEvent.getMessage()) || Const.MODIFY_ROUTE.equals(messageEvent.getMessage())) {
            mRouteFragment.update();
        }
    }
}