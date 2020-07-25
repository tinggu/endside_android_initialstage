package com.ctfww.module.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ctfww.module.user.fragment.UserNoticeListFragment;
import com.google.android.material.tabs.TabLayout;
import com.ctfww.module.user.R;
import com.ctfww.module.user.fragment.UserReceiveInviteListFragment;
import com.ctfww.module.user.fragment.UserSendInviteListFragment;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/user/invite")
public class UserInviteActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "KeepWatchAssignmentActivity";

    private ImageView mBack;
    private TextView mTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_invite_activity);
        initViews();
        setOnClickListener();
    }

    private void initViews() {

        mBack = findViewById(R.id.user_top_back);
        mTittle = findViewById(R.id.user_top_tittle);
        mTittle.setText("邀请");
//        Toolbar toolbar = findViewById(R.id.user_invite_toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        toolbar.setTitle("邀请");

        TabLayout tabLayout = findViewById(R.id.user_invite_tab_layout);
        ViewPager viewPager = findViewById(R.id.user_invite_view_pager);

        final String[] tableTitle = {"通知", "收到的邀请", "发出的邀请"};

        final List<Fragment> fragmentList = new ArrayList<>();
        UserNoticeListFragment userNoticeListFragment = new UserNoticeListFragment();
        fragmentList.add(userNoticeListFragment);
        UserReceiveInviteListFragment userReceiveInviteListFragment = new UserReceiveInviteListFragment();
        fragmentList.add(userReceiveInviteListFragment);
        UserSendInviteListFragment userSendInviteListFragment = new UserSendInviteListFragment();
        fragmentList.add(userSendInviteListFragment);

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
