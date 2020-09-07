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
import com.ctfww.module.desk.adapter.DeskListAdapter;
import com.ctfww.module.desk.adapter.SelectDeskListAdapter;
import com.ctfww.module.desk.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.desk.entity.DeskInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DeskListFragment extends Fragment {
    private final static String TAG = "DeskListFragment";

    private RecyclerView mDeskListView;
    private DeskListAdapter mDeskListAdapter;
    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.desk_list_fragment, container, false);
        initViews();
        setOnClickListener();
        return mV;
    }

    private void initViews() {
        mDeskListView = mV.findViewById(R.id.desk_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mDeskListView.setLayoutManager(layoutManager);
        List<DeskInfo> deskList = DBQuickEntry.getWorkingDeskList();
        mDeskListAdapter = new DeskListAdapter(deskList, getContext());
        mDeskListView.setAdapter(mDeskListAdapter);
    }

    private void setOnClickListener() {

    }

    public void setDeskIdList(List<DeskInfo> deskInfoList) {
        mDeskListAdapter.setList(deskInfoList);
        mDeskListAdapter.notifyDataSetChanged();
    }

    public void updateDeskList() {
        List<DeskInfo> deskList = DBQuickEntry.getWorkingDeskList();
        mDeskListAdapter.setList(deskList);
        mDeskListAdapter.notifyDataSetChanged();
    }
}
