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

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.DangerListAdapter;
import com.ctfww.module.keepwatch.entity.temp.DangerObject;
import com.ctfww.module.keyevents.Entity.KeyEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeepWatchDangerListFragment extends Fragment {
    private final static String TAG = "KeepWatchDangerListFragment";

    private View mV;

    private RecyclerView mDangerListView;
    private DangerListAdapter mDangerListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.danger_list_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mDangerListView = v.findViewById(R.id.danger_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(v.getContext());
        mDangerListView.setLayoutManager(layoutManager);
        List<DangerObject> dangerObjectList = getDangerList(MyDateTimeUtils.getPresentMonthStartTime(), MyDateTimeUtils.getPresentMonthEndTime());
        mDangerListAdapter = new DangerListAdapter(dangerObjectList);
        mDangerListView.setAdapter(mDangerListAdapter);
    }

    private void setOnClickListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getThisMonthDangerList(long timeStamp) {
        getDangerList(MyDateTimeUtils.getMonthStartTime(timeStamp), MyDateTimeUtils.getMonthEndTime(timeStamp));
    }

    public List<DangerObject> getDangerList(long startTime, long endTime) {
        HashMap<String, DangerObject> dangerObjectHashMap = new HashMap<>();
        List<TodayAssignment> todayAssignmentList = com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry.getTodayAssignmentList(startTime, endTime);
        for (int i = 0; i < todayAssignmentList.size(); ++i) {
            TodayAssignment todayAssignment = todayAssignmentList.get(i);
            String key = todayAssignment.getGroupId() + todayAssignment.getObjectId() + todayAssignment.getType();
            DangerObject dangerObject = dangerObjectHashMap.get(key);
            if (dangerObject == null) {
                dangerObject = new DangerObject();
                dangerObject.setGroupId(todayAssignment.getGroupId());
                dangerObject.setObjectId(todayAssignment.getObjectId());
                dangerObject.setType(todayAssignment.getType());
                dangerObjectHashMap.put(key, dangerObject);
            }

            dangerObject.setSigninCount(dangerObject.getSigninCount() + todayAssignment.getSigninCount());
            dangerObject.setShouldCount(dangerObject.getShouldCount() + todayAssignment.getFrequency());

            dangerObject.setSigninScore(dangerObject.getSigninScore() + (todayAssignment.getSigninCount() == todayAssignment.getFrequency() ? todayAssignment.getScore() : 0));
            dangerObject.setShouldScore(dangerObject.getShouldScore() + todayAssignment.getScore());
        }

        List<KeyEvent> keyEventList = com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getNotEndList();
        for (int i = 0; i < keyEventList.size(); ++i) {
            KeyEvent keyEvent = keyEventList.get(i);
            if (keyEvent.getObjectId() == 0) {
                continue;
            }

            String type = keyEvent.getType() < 100 ? "desk" : "route";
            String key = keyEvent.getGroupId() + keyEvent.getObjectId() + type;
            DangerObject dangerObject = dangerObjectHashMap.get(key);
            if (dangerObject == null) {
                dangerObject = new DangerObject();
                dangerObject.setGroupId(keyEvent.getGroupId());
                dangerObject.setObjectId(keyEvent.getObjectId());
                dangerObject.setType(type);
                dangerObjectHashMap.put(key, dangerObject);
            }

            dangerObject.setKeyeventCount(dangerObject.getKeyeventCount() + 1);

            dangerObject.setKeyeventScore(dangerObject.getSigninScore() + keyEvent.getScore());
        }

        List<DangerObject> dangerObjectList = new ArrayList<>();
        for (Map.Entry<String, DangerObject> entry : dangerObjectHashMap.entrySet()) {
            dangerObjectList.add(entry.getValue());
        }

        dangerObjectList.sort(new Comparator<DangerObject>() {
            @Override
            public int compare(DangerObject o1, DangerObject o2) {
                Integer val1 = o1.getKeyeventScore() + o1.getShouldScore() - o1.getSigninScore();
                Integer val2 = o2.getKeyeventScore() + o2.getShouldScore() - o2.getSigninScore();
                return val2.compareTo(val1);
            }
        });

        return dangerObjectList;
    }
}
