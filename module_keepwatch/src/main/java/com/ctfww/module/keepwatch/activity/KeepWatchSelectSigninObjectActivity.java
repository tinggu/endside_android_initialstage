package com.ctfww.module.keepwatch.activity;

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
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.keepwatch.datahelper.airship.Airship;
import com.ctfww.module.keepwatch.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;
import com.ctfww.module.keepwatch.fragment.KeepWatchSelectDeskFragment;
import com.ctfww.module.keepwatch.fragment.KeepWatchSelectRouteFragment;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = "/keepwatch/selectRouteAndDesk")
public class KeepWatchSelectSigninObjectActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "KeepWatchSelectSigninObjectActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mConfirm;

    private KeepWatchSelectDeskFragment mDeskFragment;
    private KeepWatchSelectRouteFragment mRouteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepwatch_select_signin_object_activity);
        initViews();
        setOnClickListener();
        EventBus.getDefault().register(this);
        Airship.getInstance().synKeepWatchRouteSummaryFromCloud();
        Airship.getInstance().synKeepWatchDeskFromCloud();
//        getAssignmentList();
        // 记得修改
    }

    private void initViews() {

        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("邀请");
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

        TabLayout tabLayout = findViewById(R.id.keepwatch_select_tab_layout);
        ViewPager viewPager = findViewById(R.id.keepwatch_select_pager);

        final String[] tableTitle = {"选择签到点", "选择签到路线"};

        final List<Fragment> fragmentList = new ArrayList<>();
        mDeskFragment = new KeepWatchSelectDeskFragment();
        fragmentList.add(mDeskFragment);
        mRouteFragment = new KeepWatchSelectRouteFragment();
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
                intent.putExtra("selected_desk", GsonUtils.toJson(deskList));
            }

            if (!routeList.isEmpty()) {
                intent.putExtra("selected_route", GsonUtils.toJson(routeList));
            }

            setResult(RESULT_OK, intent);
        }
    }

    private boolean mIsFinishRouteSyn = false;
    private boolean mIsFinishDeskSyn = false;
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onGetMessage(MessageEvent messageEvent) {
        if ("finish_route_syn".equals(messageEvent.getMessage())) {
            mIsFinishRouteSyn = true;
            getAssignmentList();
        }
        else if ("finish_desk_syn".equals(messageEvent.getMessage())) {
            mIsFinishDeskSyn = true;
            getAssignmentList();
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

    private void getAssignmentList() {
        if (!mIsFinishRouteSyn || !mIsFinishDeskSyn) {
            return;
        }

        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        List<KeepWatchDesk> deskList = DBHelper.getInstance().getKeepWatchDeskList(groupId);
        List<KeepWatchRouteSummary> routeList = DBHelper.getInstance().getKeepWatchRouteSummaryList(groupId);
        if (deskList.isEmpty() && routeList.isEmpty()) {
            DialogUtils.onlyPrompt("没有签到点，也没有签到路线！", this);
            return;
        }

        NetworkHelper.getInstance().getKeepWatchPeriodAssignmentList(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchAssignment> keepWatchAssignmentList = (List<KeepWatchAssignment>)obj;
                onGetAssignmentList(keepWatchAssignmentList);
                LogUtils.i(TAG, "getTodayAssignment success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getTodayAssignment fail: code = " + code);
            }
        });
    }

    private void onGetAssignmentList(List<KeepWatchAssignment> assignmentList) {
        if (assignmentList.isEmpty()) {
            return;
        }

        HashMap<String, String> assignmentHashMap = new HashMap<>();
        for (int i = 0; i < assignmentList.size(); ++i) {
            KeepWatchAssignment assignment = assignmentList.get(i);
            String key = assignment.getDeskId() == 0 ? assignment.getRouteId() : "" + assignment.getDeskId();
            assignmentHashMap.put(key, key);
        }

        mDeskFragment.setAssignment(assignmentHashMap);
        mRouteFragment.setAssignment(assignmentHashMap);
    }
}
