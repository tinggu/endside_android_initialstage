package com.ctfww.module.keepwatch.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.keepwatch.DataHelper.DBHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.map.BaiduMapHelper;

public class KeepWatchViewSigninDeskActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "KeepWatchViewSigninDeskActivity";

    private ImageView mBack;
    private TextView mTittle;
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private TextView mAddr;

    private int mDeskId;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_view_signin_desk_activity);

        processIntent();

        initViews();
        setOnClickListener();

        getOneSigninDesk();
    }

    private void processIntent() {
        mDeskId = getIntent().getIntExtra("desk_id", 0);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mMapView = findViewById(R.id.keepwatch_baidu_map);
        mBaiduMap = mMapView.getMap();
        mAddr = findViewById(R.id.keepwatch_signin_desk_addr);
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

    private void getOneSigninDesk() {
        if (mDeskId == 0) {
            return;
        }

        String groupId = SPStaticUtils.getString("working_group_id");
        KeepWatchDesk desk = DBHelper.getInstance().getDesk(groupId, mDeskId);
        if (desk == null) {
            return;
        }

        String address = TextUtils.isEmpty(desk.getDeskAddress()) ? "未注明" : desk.getDeskAddress();
        mAddr.setText(address);
        String deskName = TextUtils.isEmpty(desk.getDeskName()) ? "无名称" : desk.getDeskName();
        mTittle.setText(deskName);

        LatLng latLng = BaiduMapHelper.gps2Baidu(new LatLng(desk.getLat(), desk.getLng()));
        BaiduMapHelper.showMapByPt(latLng, mBaiduMap);
        BaiduMapHelper.drawPt(latLng, BaiduMapHelper.PT_TYPE_CURR, mBaiduMap);
    }
}
