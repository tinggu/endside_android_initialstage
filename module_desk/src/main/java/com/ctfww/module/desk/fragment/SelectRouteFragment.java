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

import com.ctfww.module.desk.R;
import com.ctfww.module.desk.adapter.SelectRouteListAdapter;
import com.ctfww.module.desk.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.desk.entity.RouteSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SelectRouteFragment extends Fragment {
    private final static String TAG = "SelectRouteFragment";

    private RecyclerView mRouteListView;
    private SelectRouteListAdapter mRouteListAdapter;
    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.select_route_list_fragment, container, false);
        initViews();
        setOnClickListener();
        return mV;
    }

    private void initViews() {
        mRouteListView = mV.findViewById(R.id.route_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRouteListView.setLayoutManager(layoutManager);
        List<RouteSummary> routeList = DBQuickEntry.getRouteSummaryList();
        Bundle bundle = getArguments();
        List<Integer> selectedRouteId = bundle == null ? new ArrayList<>() : bundle.getIntegerArrayList("selected_route_id_list");
        mRouteListAdapter = new SelectRouteListAdapter(routeList, selectedRouteId);
        mRouteListView.setAdapter(mRouteListAdapter);
    }

    private void setOnClickListener() {

    }

    public void setSelectedRouteIdList(List<Integer> routeIdList) {
        mRouteListAdapter.setSelectedRouteIdList(routeIdList);
        mRouteListAdapter.notifyDataSetChanged();
    }

    public void updateRouteList() {
        List<RouteSummary> routeList = DBQuickEntry.getRouteSummaryList();
        mRouteListAdapter.setList(routeList);
        mRouteListAdapter.notifyDataSetChanged();
    }

    public List<String> getSelectedList() {
        HashMap map = mRouteListAdapter.getSelectedMap();
        List<String> ret = new ArrayList<>();
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
            ret.add(entry.getKey());
        }

        return ret;
    }
}
