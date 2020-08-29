package com.ctfww.module.desk.activity;

import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.LocationGson;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.location.GPSLocationManager;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.adapter.RouteDeskListAdapter;
import com.ctfww.module.desk.datahelper.airship.Airship;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/keepwatch/addRoute")
public class CreateRouteActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "CreateRouteActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mStartEnd;
    private EditText mRouteName;
    private EditText mTag;
    private TextView mAdd;

    private int mWorkStatus;
    private LocationGson mLastLocation;
    private String mRouteId;
    private RecyclerView mRouteDeskListView;
    private RouteDeskListAdapter mRouteDeskListAdapter;

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
        mTag = findViewById(R.id.tag);
        mAdd = findViewById(R.id.add);

        mWorkStatus = 0;

        mRouteId = getIntent().getStringExtra("route_id");
        if (TextUtils.isEmpty(mRouteId)) {
            mRouteId = GlobeFun.getSHA(SPStaticUtils.getString("user_open_id") + System.currentTimeMillis());
            mType = "new";
        }
        else {
            RouteSummary routeSummary = DBHelper.getInstance().getRouteSummary(mRouteId);
            if (routeSummary != null) {
                mRouteName.setText(routeSummary.getRouteName());
                mType = "continue";
            }
        }

        mRouteDeskListView = findViewById(R.id.route_desk_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mRouteDeskListView.setLayoutManager(layoutManager);

        mDeskList = DBHelper.getInstance().getRouteDeskInOneRoute(mRouteId);
        mRouteDeskListAdapter = new RouteDeskListAdapter(mDeskList);
        mRouteDeskListView.setAdapter(mRouteDeskListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mStartEnd.setOnClickListener(this);
        mAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            if (mWorkStatus == 0 || mWorkStatus == 2) {
                finish();
            }
            else if (mWorkStatus == 1) {
                DialogUtils.selectDialog("如果退出，则该路线的数据会全部丢失！", this, new DialogUtils.Callback() {
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
                Location location = GPSLocationManager.getInstances(this).getCurrLocation();
                if (location != null) {
                    LocationGson locationGson = new LocationGson(location);
                    mLastLocation = locationGson;
                    addDesk(locationGson, "起点");
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
                        addDesk(locationGson, "终点");
                    }
                    else {
                        addDesk(mLastLocation, "终点");
                    }

                    DBHelper.getInstance().newRoute(mRouteId);
                    Airship.getInstance().synRouteSummaryToCloud();
                    Airship.getInstance().synRouteDeskToCloud();
                }


            }
        }
        else if (id == mAdd.getId()) {
            if (TextUtils.isEmpty(mTag.getText().toString())) {
                DialogUtils.onlyPrompt("请填写标签", this);
                return;
            }

            Location location = GPSLocationManager.getInstances(this).getCurrLocation();
            LocationGson locationGson = new LocationGson(location);
            addDesk(locationGson, mTag.getText().toString());
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
                addDesk(location, "起点");
            }
            else {
                addDesk(location, "");
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void addRoute() {
        String groupId = SPStaticUtils.getString("working_group_id");
        String routeName = mRouteName.getText().toString();
        RouteSummary route = new RouteSummary();
        route.setGroupId(groupId);
        route.setRouteId(mRouteId);
        route.setRouteName(routeName);
        route.setTimeStamp(System.currentTimeMillis());

        route.setSynTag("0");

        DBHelper.getInstance().addRouteSummary(route);
    }

    private void addDesk(LocationGson location, String tag) {
        RouteDesk routeDesk = new RouteDesk();
        if (mDeskList.isEmpty()) {
            routeDesk.setDeskId(1);
        }
        else {
            if (TextUtils.isEmpty(tag) && GlobeFun.calcLocationDist(mLastLocation, location) < 30.0) {
                return;
            }

            routeDesk.setDeskId(mDeskList.get(mDeskList.size() - 1).getDeskId() + 1);
        }

        mLastLocation = location;
        routeDesk.setRouteId(mRouteId);
        routeDesk.setLat(location.getLat());
        routeDesk.setLng(location.getLng());
        routeDesk.setTag(tag);
        routeDesk.setTimeStamp(System.currentTimeMillis());
        mDeskList.add(routeDesk);
        mRouteDeskListAdapter.setList(mDeskList);
        mRouteDeskListAdapter.notifyDataSetChanged();

        routeDesk.setSynTag("0");
        DBHelper.getInstance().addRouteDesk(routeDesk);
    }
}
