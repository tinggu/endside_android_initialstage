package com.ctfww.module.keepwatch.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.keepwatch.datahelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.adapter.KeepWatchSigninListAdapter;
import com.ctfww.module.keepwatch.entity.SigninInfo;

import java.util.ArrayList;
import java.util.List;

public class KeepWatchSigninListFragment extends Fragment {
    private final static String TAG = "KeepWatchSigninListFragment";

    private LinearLayout mViewDataLL;

    private TextView mNoData;

    private RecyclerView mKeepWatchSigninInfoListView;
    private List<SigninInfo> mKeepWatchSigninInfoList = new ArrayList<>();
    KeepWatchSigninListAdapter mKeepWatchSigninInfoListAdapter;
    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_signin_list_fragment, container, false);
        initViews();
        setOnClickListener();
        return mV;
    }

    private void initViews() {
        mViewDataLL = mV.findViewById(R.id.keepwatch_review_signin_ll);
        mNoData = mV.findViewById(R.id.keepwatch_no_data);
        mKeepWatchSigninInfoListView = mV.findViewById(R.id.keepwatch_signin_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(mV.getContext());
        mKeepWatchSigninInfoListView.setLayoutManager(layoutManager);
        mKeepWatchSigninInfoListAdapter = new KeepWatchSigninListAdapter(mKeepWatchSigninInfoList);
        mKeepWatchSigninInfoListView.setAdapter(mKeepWatchSigninInfoListAdapter);

        mViewDataLL.setVisibility(View.GONE);
        mNoData.setVisibility(View.VISIBLE);
        mKeepWatchSigninInfoListView.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        mViewDataLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/signinList").navigation();
            }
        });
    }

    public void getTodaySigninList() {
        getSigninList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
    }

    public void getThisDaySigninList(long timeStamp) {
        getSigninList(MyDateTimeUtils.getDayStartTime(timeStamp), MyDateTimeUtils.getDayEndTime(timeStamp));
    }

    public void getSigninList(long startTime, long endTime) {
        NetworkHelper.getInstance().getKeepWatchSigninList(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeepWatchSigninInfoList = (List<SigninInfo>)obj;
                List<SigninInfo> list = _getSigninList(mKeepWatchSigninInfoList);
                mKeepWatchSigninInfoListAdapter.setList(list);
                mKeepWatchSigninInfoListAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getSigninList: mKeepWatchSigninInfoList.size() =  " + mKeepWatchSigninInfoList.size());
                if (mKeepWatchSigninInfoList.isEmpty()) {
                    mViewDataLL.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                    mKeepWatchSigninInfoListView.setVisibility(View.GONE);
                }
                else {
                    mViewDataLL.setVisibility(View.VISIBLE);
                    mNoData.setVisibility(View.GONE);
                    mKeepWatchSigninInfoListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getSigninList fail: code = " + code);
            }
        });
    }

    private int mMaxtCount = 0;
    public void setMaxCount(int maxCount) {
        this.mMaxtCount = maxCount;
    }

    private List<SigninInfo> _getSigninList(List<SigninInfo> keepWatchSigninInfoList) {
        if (mMaxtCount <= 0 || mMaxtCount >= keepWatchSigninInfoList.size()) {
            return keepWatchSigninInfoList;
        }

        return keepWatchSigninInfoList.subList(0, mMaxtCount);
    }
}
