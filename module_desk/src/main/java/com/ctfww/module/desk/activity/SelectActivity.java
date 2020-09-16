package com.ctfww.module.desk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ctfww.module.desk.fragment.SelectDeskFragment;
import com.ctfww.module.desk.fragment.SelectRouteFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Route(path = "/desk/selectSignObject")
public class SelectActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "SelectActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mConfirm;

    private SelectDeskFragment mDeskFragment;
    private SelectRouteFragment mRouteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
        initViews();
        setOnClickListener();
        EventBus.getDefault().register(this);
        Airship.getInstance().synRouteSummaryFromCloud();
        Airship.getInstance().synRouteDeskFromCloud();
//        getAssignmentList();
        // 记得修改
    }

    private void initViews() {

        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("选择签到对象");
        mConfirm = findViewById(R.id.top_addition);
        mConfirm.setText("确定");
        mConfirm.setVisibility(View.VISIBLE);

//        Toolbar toolbar = findViewById(R.id.user_invite_toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        toolbar.setTitle("邀请");

        TabLayout tabLayout = findViewById(R.id.select_tab_layout);
        ViewPager viewPager = findViewById(R.id.select_pager);

        final String[] tableTitle = {"选择签到点", "选择签到路线"};

        final List<Fragment> fragmentList = new ArrayList<>();
        mDeskFragment = new SelectDeskFragment();


        ArrayList<Integer> deskIdList = getIntent().getIntegerArrayListExtra("selected_desk_id_list");
        if (deskIdList != null && !deskIdList.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("selected_desk_id_list", deskIdList);
            mDeskFragment.setArguments(bundle);
        }
        fragmentList.add(mDeskFragment);

        mRouteFragment = new SelectRouteFragment();
        ArrayList<Integer> routeIdList = getIntent().getIntegerArrayListExtra("selected_route_id_list");
        if (routeIdList != null && !routeIdList.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("selected_route_id_list", routeIdList);
            mRouteFragment.setArguments(bundle);
        }
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
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mConfirm.getId()) {
            List<String> deskList = mDeskFragment.getSelectedList();
            List<String> routeList = mRouteFragment.getSelectedList();
            if (deskList.isEmpty() && routeList.isEmpty()) {
                DialogUtils.onlyPrompt("你没有选择任何签到点或签到路线！", this);
                return;
            }

            Intent intent = new Intent();
            if (!deskList.isEmpty()) {
                EventBus.getDefault().post(new MessageEvent("selected_desk", GsonUtils.toJson(deskList)));
            }

            if (!routeList.isEmpty()) {
                EventBus.getDefault().post(new MessageEvent("selected_route", GsonUtils.toJson(routeList)));
            }

            finish();
        }
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("finish_route_syn".equals(messageEvent.getMessage())) {
            mRouteFragment.updateRouteList();
        }
        else if ("finish_desk_syn".equals(messageEvent.getMessage())) {
            mDeskFragment.updateDeskList();
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
