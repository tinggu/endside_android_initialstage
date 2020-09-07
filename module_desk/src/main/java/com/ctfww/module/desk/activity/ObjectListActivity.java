package com.ctfww.module.desk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.GsonUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.datahelper.airship.Airship;
import com.ctfww.module.desk.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.fragment.DeskListFragment;
import com.ctfww.module.desk.fragment.RouteListFragment;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Route(path = "/desk/list")
public class ObjectListActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "ObjectListActivity";

    private ImageView mBack;
    private TextView mTittle;

    private DeskListFragment mDeskListFragment;
    private RouteListFragment mRouteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list_activity);
        initViews();
        setOnClickListener();
        EventBus.getDefault().register(this);
        Airship.getInstance().synRouteSummaryFromCloud();
        Airship.getInstance().synRouteDeskFromCloud();
    }

    private void initViews() {

        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("巡检目标管理");

//        Toolbar toolbar = findViewById(R.id.user_invite_toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        toolbar.setTitle("邀请");

        TabLayout tabLayout = findViewById(R.id.object_list_tab_layout);
        ViewPager viewPager = findViewById(R.id.object_list_pager);

        final String[] tableTitle = {"巡检点", "巡检路线"};

        final List<Fragment> fragmentList = new ArrayList<>();
        mDeskListFragment = new DeskListFragment();
        fragmentList.add(mDeskListFragment);
        mRouteListFragment = new RouteListFragment();
        fragmentList.add(mRouteListFragment);

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

//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                changeTabTextView(tab, true);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                changeTabTextView(tab, false);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });

        tabLayout.setupWithViewPager(viewPager);

//        changeTabTextView(tabLayout.getTabAt(0), true);
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

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("finish_route_syn".equals(messageEvent.getMessage())) {
            mRouteListFragment.updateRouteList();
        }
        else if ("finish_desk_syn".equals(messageEvent.getMessage())) {
            mDeskListFragment.updateDeskList();
        }
        else if ("modify_desk".equals(messageEvent.getMessage()) || "add_desk".equals(messageEvent.getMessage())) {
            mDeskListFragment.updateDeskList();
        }
        else if ("modify_route".equals(messageEvent.getMessage()) || "add_route".equals(messageEvent.getMessage())) {
            mRouteListFragment.updateRouteList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }




//    public void changeTabTextView(TabLayout.Tab tab, boolean isSelected) {
//        View view = tab.getCustomView();
//        TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
//        if (isSelected) {
//            textView.setTextAppearance(this, R.style.TabLayoutSelectTextStyle);
//        } else {
//            textView.setTextAppearance(this, R.style.TabLayoutNormalTextStyle);
//        }
//    }
}
