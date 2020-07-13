package com.ctfww.module.keepwatch.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.ctfww.module.keepwatch.R;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapHelper {





    public static LatLng gps2Baidu(LatLng latLng) {
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);

        converter.coord(latLng);
        return converter.convert();
    }

    public static List<LatLng> gps2Baidu(List<LatLng> gpsList) {
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);

        List<LatLng> baiduList = new ArrayList<>();
        for (int i = 0; i < gpsList.size(); ++i) {
            converter.coord(gpsList.get(i));
            LatLng latLng = converter.convert();
            baiduList.add(latLng);

        }

        return baiduList;
    }

    public static List<LatLng> gps2Baidu(double[] ptArr) {
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);

        List<LatLng> baiduList = new ArrayList<>();
        for (int i = 0; i < ptArr.length / 2; ++i) {
            converter.coord(new LatLng(ptArr[2 * i], ptArr[2 * i + 1]));
            LatLng latLng = converter.convert();
            baiduList.add(latLng);

        }

        return baiduList;
    }

    public static void showMapByPt(LatLng latLng, BaiduMap baiduMap) {
        float zoom = 20.0f;

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(zoom);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        baiduMap.setMapStatus(mapStatusUpdate);
    }

    public static void showMapByPtList(List<LatLng> latLngList, MapView mapView, BaiduMap baiduMap) {
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (LatLng p : latLngList) {
//            builder = builder.include(p);
//        }
//        LatLngBounds latlngBounds = builder.build();
//        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(latlngBounds,mapView.getWidth(),mapView.getHeight());
//        baiduMap.animateMapStatus(mapStatusUpdate);
        if (latLngList == null || latLngList.isEmpty()) {
            showMapByPt(new LatLng(22.0, 113.0), baiduMap);
            return;
        }

        double lat = 0.0;
        double lng = 0.0;
        for (int i = 0; i < latLngList.size(); ++i) {
            lat += latLngList.get(i).latitude;
            lng += latLngList.get(i).longitude;
        }

        showMapByPt(new LatLng(lat / latLngList.size(), lng / latLngList.size()), baiduMap);
    }

    public static Overlay drawPt(LatLng latLng, int type, BaiduMap baiduMap) {
        int bmpId = getPtBmpId(type);

        BitmapDescriptor ptBmp = BitmapDescriptorFactory.fromResource(bmpId);
        MarkerOptions wayPtOptions = new MarkerOptions().position(latLng).icon(ptBmp);
        return baiduMap.addOverlay(wayPtOptions);
    }

    public static void drawTrace(List<LatLng> latLngList,  BaiduMap baiduMap) {
        OverlayOptions overlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(latLngList);

        Overlay polyline = baiduMap.addOverlay(overlayOptions);
    }

    public static void drawTraceTail(List<LatLng> tracePtList,  BaiduMap baiduMap) {
        if (tracePtList.size() < 2) {
            return;
        }

        List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(tracePtList.get(tracePtList.size() - 2));
        latLngList.add(tracePtList.get(tracePtList.size() - 1));
        OverlayOptions overlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(latLngList);

        Overlay polyline = baiduMap.addOverlay(overlayOptions);
    }

    public static void drawLine(LatLng startLatLng, LatLng endLatLng,  BaiduMap baiduMap) {
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(startLatLng);
        latLngList.add(endLatLng);
        OverlayOptions overlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(latLngList);

        Overlay polyline = baiduMap.addOverlay(overlayOptions);
    }

    public final static int PT_TYPE_START = 1;
    public final static int PT_TYPE_END = 2;
    public final static int PT_TYPE_WAY = 3;
    public final static int PT_TYPE_NO_WAY = 4;
    public final static int PT_TYPE_CURR = 5;

    private static int getPtBmpId(int type) {
        int id = R.drawable.keepwatch_icon_center;
        switch (type) {
            case PT_TYPE_START:
                id = R.drawable.icon_start;
                break;
            case PT_TYPE_END:
                id = R.drawable.icon_end;
                break;
            case PT_TYPE_WAY:
                id = R.drawable.signined_way_point;
                break;
            case PT_TYPE_NO_WAY:
                id = R.drawable.unsignined_way_point;
                break;
            case PT_TYPE_CURR:
                id = R.drawable.keepwatch_icon_center;
                break;
            default:
                id = R.drawable.keepwatch_icon_center;
                break;
        }

        return id;
    }

}
