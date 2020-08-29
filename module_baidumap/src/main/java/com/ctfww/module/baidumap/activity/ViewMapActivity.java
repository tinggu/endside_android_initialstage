package com.ctfww.module.baidumap.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.baidumap.R;
import com.ctfww.module.baidumap.utils.BaiduMapUtils;

import java.util.List;

@Route(path = "/baidumap/viewMap")
public class ViewMapActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "ViewMapActivity";

    private ImageView mBack;
    private TextView mTittle;
    private BaiduMap mBaiduMap;
    private MapView mMapView;

    private LinearLayout mAddressDescLL;
    private TextView mName;
    private TextView mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_map_activity);

        initViews();

        processIntent();

        setOnClickListener();
    }

    private void processIntent() {
        String type = getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(type)) {
            return;
        }

        if ("center".equals(type)) {
            double lat = getIntent().getDoubleExtra("lat", 22.0);
            double lng = getIntent().getDoubleExtra("lng", 113.0);
            LatLng latLng = BaiduMapUtils.gps2Baidu(new LatLng((double)lat, (double)lng));
            BaiduMapUtils.showMapByPt(latLng, mBaiduMap);
            BaiduMapUtils.drawPt(latLng, BaiduMapUtils.PT_TYPE_CURR, mBaiduMap);
            String name = getIntent().getStringExtra("name");
            String address = getIntent().getStringExtra("address");
            if (TextUtils.isEmpty(name)) {
                mAddressDescLL.setVisibility(View.GONE);
            }
            else {
                mAddressDescLL.setVisibility(View.VISIBLE);
                mName.setText(name);
                if (!TextUtils.isEmpty(address)) {
                    mAddress.setText(address);
                }
            }
        }
        else if ("trace".equals(type)) {
            double[] tracePtArr = getIntent().getDoubleArrayExtra("tracePtArr");
            double[] wayPtArr = getIntent().getDoubleArrayExtra("wayPtArr");
            List<LatLng> traceLatLngList = BaiduMapUtils.gps2Baidu(tracePtArr);
            LogUtils.i(TAG, traceLatLngList.toString());
            BaiduMapUtils.showMapByPtList(traceLatLngList, mMapView, mBaiduMap);
            BaiduMapUtils.drawTrace(traceLatLngList, mBaiduMap);
            BaiduMapUtils.drawPt(traceLatLngList.get(0), BaiduMapUtils.PT_TYPE_START, mBaiduMap);
            BaiduMapUtils.drawPt(traceLatLngList.get(traceLatLngList.size() - 1), BaiduMapUtils.PT_TYPE_END, mBaiduMap);

            List<LatLng> wayPtLatLngList = BaiduMapUtils.gps2Baidu(wayPtArr);
            if (wayPtLatLngList == null || wayPtLatLngList.isEmpty()) {
                return;
            }

            for (int i = 0; i < wayPtLatLngList.size(); ++i) {
                BaiduMapUtils.drawPt(wayPtLatLngList.get(i), BaiduMapUtils.PT_TYPE_WAY, mBaiduMap);
            }
        }
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("查看地图");
        mMapView = findViewById(R.id.baidu_map);
        mBaiduMap = mMapView.getMap();

        mAddressDescLL = findViewById(R.id.address_desc);
        mName = findViewById(R.id.name);
        mAddress = findViewById(R.id.address);
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
}
