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
import com.ctfww.module.keepwatch.adapter.KeepWatchSelectDeskListAdapter;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KeepWatchSelectDeskFragment extends Fragment {
    private final static String TAG = "KeepWatchSelectDeskFragment";

    private RecyclerView mDeskListView;
    private KeepWatchSelectDeskListAdapter mDeskListAdapter;
    private View mV;

    private HashMap<String, String> mAssignmentMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_select_desk_list_fragment, container, false);
        initViews();
        setOnClickListener();
        return mV;
    }

    private void initViews() {
        mDeskListView = mV.findViewById(R.id.keepwatch_desk_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mDeskListView.setLayoutManager(layoutManager);
        String groupId = SPStaticUtils.getString("working_group_id");
        List<KeepWatchDesk> deskList = DBHelper.getInstance().getKeepWatchDeskList(groupId);
        mDeskListAdapter = new KeepWatchSelectDeskListAdapter(deskList);
        mDeskListView.setAdapter(mDeskListAdapter);
    }

    private void setOnClickListener() {

    }

    public void setAssignment(HashMap<String, String> assignmentMap) {
        mAssignmentMap = assignmentMap;
        String groupId = SPStaticUtils.getString("working_group_id");
        List<KeepWatchDesk> deskList = DBHelper.getInstance().getKeepWatchDeskList(groupId);
        updateDeskAssignmetStatus(deskList);
        mDeskListAdapter.setList(deskList);
        mDeskListAdapter.notifyDataSetChanged();
    }

    private void updateDeskAssignmetStatus(List<KeepWatchDesk> deskList) {
        for (int i = 0; i < deskList.size(); ++i) {
            String val = mAssignmentMap.get("" + deskList.get(i).getDeskId());
            if (val == null) {
                deskList.get(i).setIsAssignmented(false);
            }
            else {
                deskList.get(i).setIsAssignmented(true);
            }
        }
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
