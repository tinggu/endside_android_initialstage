package com.ctfww.module.signin.fragment;

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
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.module.signin.R;
import com.ctfww.module.signin.adapter.SigninListAdapter;
import com.ctfww.module.signin.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.signin.entity.SigninInfo;

import java.util.List;

public class SigninListFragment extends Fragment {
    private final static String TAG = "SigninListFragment";
    private int mMaxCount = 3;

    private LinearLayout mViewAllLL;

    private TextView mNoData;

    private RecyclerView mSigninInfoListView;
    SigninListAdapter mSigninListAdapter;
    private View mV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.signin_list_fragment, container, false);
        initViews();
        setOnClickListener();
        return mV;
    }

    private void initViews() {
        mViewAllLL = mV.findViewById(R.id.review_all_ll);
        mNoData = mV.findViewById(R.id.no_data);
        mSigninInfoListView = mV.findViewById(R.id.list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(mV.getContext());
        mSigninInfoListView.setLayoutManager(layoutManager);
        List<SigninInfo> signinList = DBQuickEntry.getSigninList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        LogUtils.i(TAG, "initViews: signinList.size() = " + signinList.size());
        mSigninListAdapter = new SigninListAdapter(signinList, mMaxCount);
        mSigninInfoListView.setAdapter(mSigninListAdapter);

        viewShowOpt(signinList.size());
    }

    public void setMaxCount(int maxCount) {
        mMaxCount = maxCount;
    }

    private void viewShowOpt(int objectSize) {
        if (mMaxCount < objectSize) {
            mViewAllLL.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
            mSigninInfoListView.setVisibility(View.VISIBLE);
        }
        else {
            if (objectSize == 0) {
                mViewAllLL.setVisibility(View.GONE);
                mNoData.setVisibility(View.VISIBLE);
                mSigninInfoListView.setVisibility(View.GONE);
            }
            else {
                mViewAllLL.setVisibility(View.VISIBLE);
                mNoData.setVisibility(View.GONE);
                mSigninInfoListView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setOnClickListener() {
        mViewAllLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/signinList").navigation();
            }
        });
    }

    public void showTodaySigninList() {
        List<SigninInfo> signinList = DBQuickEntry.getSigninList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        mSigninListAdapter.setList(signinList);
        mSigninListAdapter.notifyDataSetChanged();
    }

    public void showOneDaySigninList(long timeStamp) {
        List<SigninInfo> signinList = DBQuickEntry.getSigninList(MyDateTimeUtils.getDayStartTime(timeStamp), MyDateTimeUtils.getDayEndTime(timeStamp));
        mSigninListAdapter.setList(signinList);
        mSigninListAdapter.notifyDataSetChanged();
    }
}
