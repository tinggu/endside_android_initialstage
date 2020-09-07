package com.ctfww.module.desk.activity;

import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.LocationGson;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.location.GPSLocationManager;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.datahelper.airship.Airship;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.desk.datahelper.sp.Const;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/dest/createRoute")
public class CreateRouteActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "CreateRouteActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mStartEnd;
    private EditText mRouteName;
    private LinearLayout mDistLL;
    private TextView mDist;

    private int mWorkStatus;
    private LocationGson mLastLocation;
    private String mRouteId;

    private List<RouteDesk> mDeskList;
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_route_activity);

        initViews();

        setOnClickListener();

        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("创建巡检路线");
        mStartEnd = findViewById(R.id.top_addition);
        mStartEnd.setText("开始");
        mStartEnd.setVisibility(View.VISIBLE);
        mRouteName = findViewById(R.id.route_name);
        mDistLL = findViewById(R.id.dist_ll);
        mDist = findViewById(R.id.dist);

        mWorkStatus = 0;

        mRouteId = getIntent().getStringExtra("route_id");
        if (TextUtils.isEmpty(mRouteId)) {
            mRouteId = GlobeFun.getSHA(SPStaticUtils.getString("user_open_id") + System.currentTimeMillis());
            mType = "new";
            mDeskList = new ArrayList<>();
        }
        else {
            RouteSummary routeSummary = DBHelper.getInstance().getRouteSummary(mRouteId);
            if (routeSummary != null) {
                mRouteName.setText(routeSummary.getRouteName());
                mType = "continue";
                mDeskList = DBHelper.getInstance().getRouteDeskInOneRoute(mRouteId);
            }
        }

        updateUI();
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mStartEnd.setOnClickListener(this);
        mDistLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mStartEnd.getId()) {
            if (mWorkStatus == 0) {
                if (TextUtils.isEmpty(mRouteName.getText().toString())) {
                    DialogUtils.onlyPrompt("请输入路线名称！", this);
                    return;
                }

                GlobeFun.setEditTextReadOnly(mRouteName);
                mWorkStatus = 1;
                mStartEnd.setText("结束");
                addRoute();
                if (mDeskList.isEmpty()) {
                    Location location = GPSLocationManager.getInstances(this).getCurrLocation();
                    if (location != null) {
                        LocationGson locationGson = new LocationGson(location);
                        mLastLocation = locationGson;
                    }
                }
                else {
                    mLastLocation = new LocationGson();
                    RouteDesk routeDesk = mDeskList.get(mDeskList.size() - 1);
                    mLastLocation.setLat(routeDesk.getLat());
                    mLastLocation.setLat(routeDesk.getLng());
                    mLastLocation.setTimeStamp(routeDesk.getTimeStamp());
                }
            }
            else if (mWorkStatus == 1) {
                mWorkStatus = 2;
                mStartEnd.setText("已完成");
                if (mDeskList.isEmpty()) {
                    DialogUtils.selectDialog("还没有采集任何控制点，确实不继续采集？", this, new DialogUtils.Callback() {
                        @Override
                        public void onConfirm(int radioSelectItem) {
                            DBHelper.getInstance().deleteKeepWatchRoute(mRouteId);
                            finish();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                else {
                    Location location = GPSLocationManager.getInstances(this).getCurrLocation();
                    if (location != null) {
                        LocationGson locationGson = new LocationGson(location);
                        mLastLocation = locationGson;
                        addDesk(locationGson);
                    }
                    else {
                        addDesk(mLastLocation);
                    }

                    DBHelper.getInstance().newRoute(mRouteId);
                    Airship.getInstance().synRouteSummaryToCloud();
                    Airship.getInstance().synRouteDeskToCloud();
                }


            }
        }
        else if (id == mDistLL.getId()) {
            viewRoute();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("gps_location_updated".equals(messageEvent.getMessage())) {
            if (mWorkStatus != 1) {
                return;
            }

            LocationGson location = GsonUtils.fromJson(messageEvent.getValue(), LocationGson.class);
            if (mDeskList.isEmpty()) {
                addDesk(location);
            }
            else {
                addDesk(location);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void addRoute() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String routeName = mRouteName.getText().toString();
        RouteSummary route = new RouteSummary();
        route.setGroupId(groupId);
        route.setRouteId(mRouteId);
        route.setRouteName(routeName);
        route.setStartTimeStamp(System.currentTimeMillis());
        route.setTimeStamp(System.currentTimeMillis());
        route.setStatus("reserve");

        route.setSynTag("0");

        DBHelper.getInstance().addRouteSummary(route);
    }

    private void addDesk(LocationGson location) {
        if (mLastLocation != null && GlobeFun.calcLocationDist(mLastLocation, location) < 10.0) {
            return;
        }

        RouteDesk routeDesk = new RouteDesk();
        mLastLocation = location;
        routeDesk.setRouteId(mRouteId);
        routeDesk.setLat(location.getLat());
        routeDesk.setLng(location.getLng());
        routeDesk.setTimeStamp(System.currentTimeMillis());
        routeDesk.setDeskId(mDeskList.size() + 1);
        mDeskList.add(routeDesk);

        routeDesk.setSynTag("0");
        DBHelper.getInstance().addRouteDesk(routeDesk);
    }

    private void viewRoute() {
        if (mDeskList.size() <= 1) {
            return;
        }

        float[] array = new float[mDeskList.size() * 2];
        for (int i = 0; i < mDeskList.size(); ++i) {
            RouteDesk routeDesk = mDeskList.get(i);
            array[2 * i] = (float) routeDesk.getLat();
            array[2 * i + 1]  = (float)routeDesk.getLng();
        }

        ARouter.getInstance().build("/baidumap/viewMap")
                .withString("type", "trace")
                .withFloatArray("trace_array", array)
                .navigation();
    }

    private void updateUI() {
        double sum = 0.0;
        for (int i = 0; i < mDeskList.size() - 1; ++i) {
            RouteDesk routeDesk1 = mDeskList.get(i);
            RouteDesk routeDesk2 = mDeskList.get(i + 1);
            sum += GlobeFun.calcLocationDist(routeDesk1.getLat(), routeDesk1.getLng(), routeDesk2.getLat(), routeDesk2.getLng());
        }

        if (sum < 1000.0) {
            mDist.setText("" + (int)sum + "米");
        }
        else if (sum >= 1000.0 && sum <= 100000.0){
            mDist.setText(String.format("%.1f", sum) + "公里");
        }
        else {
            mDist.setText("" + (int)(sum / 1000.0) + "公里");
        }
    }
}
