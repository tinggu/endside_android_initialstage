package com.guoliang.module.keepwatch.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MyDateTimeUtils;
import com.guoliang.module.keepwatch.DataHelper.NetworkHelper;
import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.adapter.KeepWatchDangerListAdapter;
import com.guoliang.module.keepwatch.adapter.KeepWatchRecordAdapter;
import com.guoliang.module.keepwatch.entity.KeepWatchPeriodShowData;
import com.guoliang.module.keepwatch.entity.KeepWatchRanking;
import com.guoliang.module.keepwatch.entity.KeepWatchStatisticsByDesk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KeepWatchDangerListFragment extends Fragment {
    private final static String TAG = "KeepWatchDangerListFragment";

    private View mV;

    private RecyclerView mKeepWatchDangerListView;
    private List<KeepWatchStatisticsByDesk> mKeepWatchStatisticsByDeskList = new ArrayList<>();
    private KeepWatchDangerListAdapter mKeepWatchDangerListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_danger_list_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mKeepWatchDangerListView = v.findViewById(R.id.keepwatch_danger_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(v.getContext());
        mKeepWatchDangerListView.setLayoutManager(layoutManager);
        mKeepWatchDangerListAdapter = new KeepWatchDangerListAdapter(mKeepWatchStatisticsByDeskList);
        mKeepWatchDangerListView.setAdapter(mKeepWatchDangerListAdapter);
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

    public void getDangerList(long startTime, long endTime) {
        LogUtils.i(TAG, "getDangerList: startTime = " + startTime + ", endTime = " + endTime);
        NetworkHelper.getInstance().getKeepWatchSigninStatisticsForDesk(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchStatisticsByDesk> keepWatchStatisticsByDeskList = (List<KeepWatchStatisticsByDesk>)obj;
                LogUtils.i(TAG, "getDangerList: keepWatchStatisticsByDeskList.size() = " + keepWatchStatisticsByDeskList.size());
 //               LogUtils.i(TAG, "getDangerList: keepWatchStatisticsByDeskList");
                List<KeepWatchStatisticsByDesk> dangerList = _getDangerList(keepWatchStatisticsByDeskList);
                mKeepWatchDangerListAdapter.setList(dangerList);
                mKeepWatchDangerListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private  List<KeepWatchStatisticsByDesk> _getDangerList(List<KeepWatchStatisticsByDesk> keepWatchStatisticsByDeskList) {
        for (int i = 0; i < keepWatchStatisticsByDeskList.size(); ++i) {
            keepWatchStatisticsByDeskList.get(i).combineDangerFactor();
        }

        keepWatchStatisticsByDeskList.sort(new Comparator<KeepWatchStatisticsByDesk>() {
            @Override
            public int compare(KeepWatchStatisticsByDesk o1, KeepWatchStatisticsByDesk o2) {
                return (int)(o2.getDangerFactor() - o1.getDangerFactor() + 0.5);
            }
        });

        List<KeepWatchStatisticsByDesk>  ret = new ArrayList<>();
        for (int i = 0; i < keepWatchStatisticsByDeskList.size(); ++i) {
            if (keepWatchStatisticsByDeskList.get(i).getDangerFactor() >= 10.0) {
                ret.add(keepWatchStatisticsByDeskList.get(i));
            }
        }

        return ret;
    }
}
