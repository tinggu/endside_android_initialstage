package com.guoliang.module.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.guoliang.module.main.R;
import com.guoliang.module.main.adapter.ElectricInfoViewPagerAdapter;
import com.guoliang.module.main.fragment.BaseElectricFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceElectricInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_electric_info);

        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.electric_info_toolbar);
        TabLayout tabLayout = findViewById(R.id.electric_info_tab_layout);
        ViewPager viewPager = findViewById(R.id.electric_info_view_pager);

        String[] tableTitle = {"A相电流值", "B相电流值","C相电流值", "E相电流值", "N相电流值"};

        toolbar.setNavigationIcon(R.mipmap.ic_back);

        List<Fragment> list_fragment = new ArrayList<>();
        for(int i=0;i<5;i++) {
            list_fragment.add(new BaseElectricFragment());
        }

        ElectricInfoViewPagerAdapter viewPagerAdapter = new ElectricInfoViewPagerAdapter
                (getSupportFragmentManager(),list_fragment, Arrays.asList(tableTitle));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
