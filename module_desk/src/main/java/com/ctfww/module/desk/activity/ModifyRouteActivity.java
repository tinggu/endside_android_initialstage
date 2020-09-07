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
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.LocationGson;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.location.GPSLocationManager;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.datahelper.airship.Airship;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.datahelper.sp.Const;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@Route(path = "/desk/modifyRoute")
public class ModifyRouteActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ModifyRouteActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mConfirm;
    private EditText mRouteName;
    private LinearLayout mDistLL;
    private TextView mDist;

    private String mRouteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_route_activity);

        initViews();

        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("修改巡检路线");
        mConfirm = findViewById(R.id.top_addition);
        mConfirm.setText("确定");
        mConfirm.setVisibility(View.VISIBLE);
        mRouteName = findViewById(R.id.route_name);

        mDistLL = findViewById(R.id.dist_ll);
        mDist = findViewById(R.id.dist);

        updateUI();
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mDistLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mConfirm.getId()) {
            modifyRouteSummary();
        }
        else if (id == mDistLL.getId()) {
            viewRoute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void modifyRouteSummary() {
        if (TextUtils.isEmpty(mRouteId)) {
            finish();
            return;
        }

        RouteSummary routeSummary  = DBHelper.getInstance().getRouteSummary(mRouteId);
        if (routeSummary == null) {
            finish();
            return;
        }

        String routeName = mRouteName.getText().toString();
        if (TextUtils.isEmpty(routeName))  {
            DialogUtils.onlyPrompt("名称不能为空！", this);
            return;
        }

        if (routeName.equals(routeSummary.getRouteName())) {
            finish();
            return;
        }

        routeSummary.setRouteName(routeName);
        routeSummary.setTimeStamp(System.currentTimeMillis());
        routeSummary.setSynTag("modify");

        DBHelper.getInstance().updateRouteSummary(routeSummary);
        Airship.getInstance().synRouteSummaryToCloud();

        EventBus.getDefault().post(new MessageEvent("modify_route"));
    }

    private void viewRoute() {
        if (TextUtils.isEmpty(mRouteId)) {
            return;
        }

        List<RouteDesk> routeDeskList = DBHelper.getInstance().getRouteDeskInOneRoute(mRouteId);
        if (routeDeskList.size() <= 1) {
            return;
        }

        float[] array = new float[routeDeskList.size() * 2];
        for (int i = 0; i < routeDeskList.size(); ++i) {
            RouteDesk routeDesk = routeDeskList.get(i);
            array[2 * i] = (float) routeDesk.getLat();
            array[2 * i + 1]  = (float)routeDesk.getLng();
        }

        ARouter.getInstance().build("/baidumap/viewMap")
                .withString("type", "trace")
                .withFloatArray("trace_array", array)
                .navigation();
    }

    private void updateUI() {
        mRouteId = getIntent().getStringExtra("route_id");
        if (!TextUtils.isEmpty(mRouteId)) {
            RouteSummary routeSummary = DBHelper.getInstance().getRouteSummary(mRouteId);
            if (routeSummary != null) {
                mRouteName.setText(routeSummary.getRouteName());
            }

            List<RouteDesk> routeDeskList = DBHelper.getInstance().getRouteDeskInOneRoute(mRouteId);
            double sum = 0.0;
            for (int i = 0; i < routeDeskList.size() - 1; ++i) {
                RouteDesk routeDesk1 = routeDeskList.get(i);
                RouteDesk routeDesk2 = routeDeskList.get(i + 1);
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
}
