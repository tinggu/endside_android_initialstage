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
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.adapter.KeyEventListAdapter;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;

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

    public void getEndList(long timeStamp) {
        long startTime = MyDateTimeUtils.getDayStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getDayEndTime(timeStamp);
        NetworkHelper.getInstance().getEndKeyEventList(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeyEventList = (List<KeyEvent>)obj;
                showList("");
                LogUtils.i(TAG, "getEndList success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getEndList fail: code = " + code);
            }
        });
    }

    public void getNoEndList() {
        NetworkHelper.getInstance().getNoEndKeyEventList(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeyEventList = (List<KeyEvent>)obj;
                showList("");
                LogUtils.i(TAG, "getNoEndList success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getNoEndList fail: code = " + code);
            }
        });
    }

    public void showList(String userId) {
        List<KeyEvent> keyEventListThis;
        if (TextUtils.isEmpty(userId)) {
            keyEventListThis = mKeyEventList;
        }
        else {
            keyEventListThis = new ArrayList<>();
            for (int i = 0; i < mKeyEventList.size(); ++i) {
                KeyEvent keyEvent = mKeyEventList.get(i);
                if (userId.equals(keyEvent.getUserId())) {
                    keyEventListThis.add(keyEvent);
                }
            }
        }

        if (keyEventListThis.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mKeyEventListView.setVisibility(View.GONE);
        }
        else {
            mKeyEventListAdapter.setList(keyEventListThis);
            mKeyEventListAdapter.notifyDataSetChanged();
            mNoData.setVisibility(View.GONE);
            mKeyEventListView.setVisibility(View.VISIBLE);
        }
    }
}
