package com.ctfww.module.desk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.adapter.RouteListAdapter;
import com.ctfww.module.desk.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.desk.entity.RouteSummary;

import java.util.List;

public class RouteListFragment extends Fragment {
    private final static String TAG = "RouteListFragment";

    private RecyclerView mRouteListView;
    private RouteListAdapter mRouteListAdapter;
    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.route_list_fragment, container, false);
        initViews();
        setOnClickListener();
        return mV;
    }

    private void initViews() {
        mRouteListView = mV.findViewById(R.id.route_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRouteListView.setLayoutManager(layoutManager);
        List<RouteSummary> routeList = DBQuickEntry.getRouteSummaryList();
        LogUtils.i(TAG, "initViews: routeList = " + routeList.toString());
        mRouteListAdapter = new RouteListAdapter(routeList);
        mRouteListView.setAdapter(mRouteListAdapter);
    }

    private void setOnClickListener() {

    }

    public void setRouteList(List<RouteSummary> routeSummaryList) {
        mRouteListAdapter.setList(routeSummaryList);
        mRouteListAdapter.notifyDataSetChanged();
    }

    public void updateRouteList() {
        List<RouteSummary> routeList = DBQuickEntry.getRouteSummaryList();
        mRouteListAdapter.setList(routeList);
        mRouteListAdapter.notifyDataSetChanged();
    }
}
