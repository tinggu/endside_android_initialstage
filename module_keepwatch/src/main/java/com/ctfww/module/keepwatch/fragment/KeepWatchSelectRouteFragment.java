package com.ctfww.module.keepwatch.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.KeepWatchSelectRouteListAdapter;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KeepWatchSelectRouteFragment extends Fragment {
    private final static String TAG = "KeepWatchSelectRouteFragment";

    private RecyclerView mRouteListView;
    private List<KeepWatchRouteSummary> mRouteList = new ArrayList<>();
    private KeepWatchSelectRouteListAdapter mRouteListAdapter;
    private View mV;

    private HashMap<String, String> mAssignmentMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_select_route_list_fragment, container, false);
        initViews();
        setOnClickListener();
        return mV;
    }

    private void initViews() {
        mRouteListView = mV.findViewById(R.id.keepwatch_route_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRouteListView.setLayoutManager(layoutManager);
        String groupId = SPStaticUtils.getString("working_group_id");
        mRouteList = DBHelper.getInstance().getKeepWatchRouteSummaryList(groupId);
        mRouteListAdapter = new KeepWatchSelectRouteListAdapter(mRouteList);
        mRouteListView.setAdapter(mRouteListAdapter);
    }

    private void setOnClickListener() {

    }

    public void setAssignment(HashMap<String, String> assignmentMap) {
        mAssignmentMap = assignmentMap;
        updateDeskAssignmetStatus();
        mRouteListAdapter.setList(mRouteList);
        mRouteListAdapter.notifyDataSetChanged();
    }

    private void updateDeskAssignmetStatus() {
        for (int i = 0; i < mRouteList.size(); ++i) {
            String val = mAssignmentMap.get(mRouteList.get(i).getRouteId());
            if (val == null) {
                mRouteList.get(i).setIsAssignmented(false);
            }
            else {
                mRouteList.get(i).setIsAssignmented(true);
            }
        }
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
