package com.ctfww.module.baidumap.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.module.baidumap.R;
import com.ctfww.module.baidumap.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础地图类型
 */
@Route(path = "/map/baidumap")
public class BaiduMapActivity extends AppCompatActivity {

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private boolean isPermissionRequested;

    private TextView mAddrAppend;
    private TextView mAddrDesc;
    private LinearLayout mAddrStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.baidumap_map_activity);

        mAddrAppend = findViewById(R.id.address_append);
        mAddrDesc = findViewById(R.id.address_desc);
        mAddrStr = findViewById(R.id.addr_string);

        mMapView = (MapView) findViewById(R.id.baidu_map);
        mBaiduMap = mMapView.getMap();

        // 构建地图状态
        MapStatus.Builder builder = new MapStatus.Builder();
        // 默认 天安门
//        LatLng center = new LatLng(39.915071, 116.403907);
        // 默认 11级
//        float zoom = 11.0f;

        // 该Intent是OfflineDemo中查看离线地图调起的
//        Intent intent = getIntent();
//        if (null != intent) {
//            center = new LatLng(intent.getDoubleExtra("y", 39.915071),
//                    intent.getDoubleExtra("x", 116.403907));
//            zoom = intent.getFloatExtra("level", 11.0f);
//        }
        double lat = SPStaticUtils.getDouble("currLat");
        double lng = SPStaticUtils.getDouble("currLng");
        if (lat < 0.1 || lat > 89.9f {
            lat = 39.915071;
        }

        if (lng < 0.1 || lng > 179.9) {
            lng = 116.403907;
        }

        Utils.Gps gps = Utils.gps84_To_Gcj02(lat, lng);
        gps = Utils.gcj02_To_Bd09(gps.getLat(), gps.getLng());

        LatLng center = new LatLng(gps.getLat(), gps.getLng());
        double zoom = 18.0;

        builder.target(center).zoom(zoom);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());

        // 设置地图状态
        mBaiduMap.setMapStatus(mapStatusUpdate);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             *
             * @param point 点击的地理坐标
             */
            @Override
            public void onMapClick(LatLng point) {
                if (mAddrStr.getVisibility() == View.VISIBLE) {
                    mAddrStr.setVisibility(View.GONE);
                    return;
                }

                mAddrStr.setVisibility(View.VISIBLE);
                
                latlngToAddress(point);
                mBaiduMap.clear();
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(point).icon(bitmap);
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(options);

                //构建折线点坐标
                LatLng p1 = new LatLng(22.6156, 114.0645);
                LatLng p2 = new LatLng(22.6150, 114.0640);
                LatLng p3 = new LatLng(22.5142, 114.0643);
                List<LatLng> points = new ArrayList<LatLng>();
                points.add(p1);
                points.add(p2);
                points.add(p3);

//设置折线的属性
                OverlayOptions mOverlayOptions = new PolylineOptions()
                        .width(10)
                        .color(0xAAFF0000)
                        .points(points);
//在地图上绘制折线
//mPloyline 折线对象
                Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
            }

            /**
             * 地图内 Poi 单击事件回调函数
             *
             * @param mapPoi 点击的 poi 信息
             */
            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                latlngToAddress(mapPoi.getPosition());
            }
        });
    }

    /**
     * 清除地图缓存数据，支持清除普通地图和卫星图缓存，再次进入地图页面生效。
     */
    public void cleanMapCache(View view) {
        if (mBaiduMap == null){
            return;
        }
        int mapType = mBaiduMap.getMapType();
        if (mapType == BaiduMap.MAP_TYPE_NORMAL) {
            // // 清除地图缓存数据
            mBaiduMap.cleanCache(BaiduMap.MAP_TYPE_NORMAL);
        } else if (mapType == BaiduMap.MAP_TYPE_SATELLITE) {
            // 清除地图缓存数据
            mBaiduMap.cleanCache(BaiduMap.MAP_TYPE_SATELLITE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }

    // 百度地图通过坐标获取地址，（ 要签名打包才能得到地址）
    private void latlngToAddress(LatLng latlng) {
        GeoCoder geoCoder = GeoCoder.newInstance();

        // 设置地址或经纬度反编译后的监听,这里有两个回调方法
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    ToastUtils.showShort("找不到该地址!");
                } else {
                    ToastUtils.showShort(result.getAddress());
//                    result.getPoiList()
                    mAddrAppend.setText(result.getAddress());
                    mAddrDesc.setText(result.getAddressDetail().province + "-" + result.getAddressDetail().city);
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                // 详细地址转换在经纬度
                ToastUtils.showShort(result.getAddress());
                mAddrAppend.setText(result.getAddress());
 //               mAddrDesc.setText(result.getAddressDetail().province + "-" + result.getAddressDetail().city);

            }
        });

        // 设置反地理经纬度坐标,请求位置时,需要一个经纬度
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));

    }
}
