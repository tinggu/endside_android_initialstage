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

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.adapter.SelectDeskListAdapter;
import com.ctfww.module.desk.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SelectDeskFragment extends Fragment {
    private final static String TAG = "SelectDeskFragment";

    private RecyclerView mDeskListView;
    private SelectDeskListAdapter mDeskListAdapter;
    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.select_desk_list_fragment, container, false);
        initViews();
        setOnClickListener();
        return mV;
    }

    private void initViews() {
        mDeskListView = mV.findViewById(R.id.desk_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mDeskListView.setLayoutManager(layoutManager);
        List<DeskInfo> deskList = DBQuickEntry.getWorkingDeskList();
        Bundle bundle = getArguments();
        List<Integer> selectedDeskId = bundle == null ? new ArrayList<>() : bundle.getIntegerArrayList("selected_desk_id_list");
        mDeskListAdapter = new SelectDeskListAdapter(deskList, selectedDeskId);
        mDeskListView.setAdapter(mDeskListAdapter);
    }

    private void setOnClickListener() {

    }

    public void setSelectedDeskIdList(List<Integer> deskIdList) {
        mDeskListAdapter.setSelectedDeskIdList(deskIdList);
        mDeskListAdapter.notifyDataSetChanged();
    }

    public void updateDeskList() {
        List<DeskInfo> deskList = DBQuickEntry.getWorkingDeskList();
        mDeskListAdapter.setList(deskList);
        mDeskListAdapter.notifyDataSetChanged();
    }

    public List<String> getSelectedList() {
        HashMap map = mDeskListAdapter.getSelectedMap();
        List<String> ret = new ArrayList<>();
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
            ret.add(entry.getKey());
        }

        return ret;
    }
}
