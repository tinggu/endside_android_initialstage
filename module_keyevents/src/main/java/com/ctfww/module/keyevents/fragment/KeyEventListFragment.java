package com.ctfww.module.keyevents.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventPerson;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.adapter.KeyEventListAdapter;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry;

import java.util.ArrayList;
import java.util.List;

public class KeyEventListFragment extends Fragment{

    private static final String TAG = "KeyEventListFragment";

    private TextView mNoData;
    private RecyclerView mKeyEventListView;
    private List<KeyEvent> mKeyEventList = new ArrayList<>();
    KeyEventListAdapter mKeyEventListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.keyevent_list_fragment, container, false);

        initViews(view);
        setOnClickListener();

        return view;
    }


    private void setOnClickListener() {

    }

    private void initViews(View view) {
        mNoData = view.findViewById(R.id.keyevent_no_data);

        mKeyEventListView = view.findViewById(R.id.keyevent_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mKeyEventListView.setLayoutManager(linearLayoutManager);
        mKeyEventListAdapter = new KeyEventListAdapter(mKeyEventList);
        mKeyEventListView.setAdapter(mKeyEventListAdapter);
    }

    public void showNoEndList() {
        List<KeyEvent> keyEventList = DBQuickEntry.getNoEndKeyEventList();
        if (keyEventList.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mKeyEventListView.setVisibility(View.GONE);
        }
        else {
            mKeyEventListAdapter.setList(keyEventList);
            mKeyEventListAdapter.notifyDataSetChanged();
            mNoData.setVisibility(View.GONE);
            mKeyEventListView.setVisibility(View.VISIBLE);
        }
    }

    public void showEndList(long startTime, long endTime) {
        List<KeyEvent> keyEventList = DBQuickEntry.getEndKeyEventList(startTime, endTime);
        if (keyEventList.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mKeyEventListView.setVisibility(View.GONE);
        }
        else {
            mKeyEventListAdapter.setList(keyEventList);
            mKeyEventListAdapter.notifyDataSetChanged();
            mNoData.setVisibility(View.GONE);
            mKeyEventListView.setVisibility(View.VISIBLE);
        }
    }
}
