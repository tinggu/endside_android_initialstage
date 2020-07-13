package com.guoliang.module.attend.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.guoliang.module.attend.R;
import com.guoliang.module.attend.entity.AttendPersonTrends;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

public class AttendPersonTrendsFragment extends Fragment {
    private final static String TAG = "KeepWatchPersonTrendsFragment";

    private View mV;

    private TextView mNoData;
    private LinearLayout mLL;
    private ImageView mHead;
    private TextView mNickName;
    private TextView mTime;
    private TextView mDeskName;
    private TextView mStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.attend_person_trends_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mNoData = v.findViewById(R.id.attend_no_data);
        mLL = v.findViewById(R.id.attend_person_trends);
        mHead = v.findViewById(R.id.attend_person_trends_head);
        mNickName = v.findViewById(R.id.attend_person_trends_nick_name);
        mTime = v.findViewById(R.id.attend_person_trends_time);
        mDeskName = v.findViewById(R.id.attend_person_trends_desk_name);
        mStatus = v.findViewById(R.id.attend_person_trends_status);

        mNoData.setVisibility(View.VISIBLE);
        mLL.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        mLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/personTrends").navigation();
            }
        });
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

    public void getPersonTrends() {
        final int count = 1;
//        NetworkHelper.getInstance().getPersonTrends(count, new IUIDataHelperCallback() {
//            @Override
//            public void onSuccess(Object obj) {
//                List<KeepWatchPersonTrends> keepWatchPersonTrendsList = (List<KeepWatchPersonTrends>)obj;
//                updatePersonTrendsToUI(keepWatchPersonTrendsList);
//                LogUtils.i(TAG, "getPersonTrends success: keepWatchPersonTrendsList.size() = " + keepWatchPersonTrendsList.size());
//            }
//
//            @Override
//            public void onError(int code) {
//                LogUtils.i(TAG, "getPersonTrends fail: code = " + code);
//            }
//        });
    }

    private void updatePersonTrendsToUI(List<AttendPersonTrends> attendPersonTrendsList) {
        if (attendPersonTrendsList.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mLL.setVisibility(View.GONE);
            return;
        }

        AttendPersonTrends attendPersonTrends = attendPersonTrendsList.get(0);
        if (!TextUtils.isEmpty(attendPersonTrends.getHeadUrl())) {
            Glide.with(this).load(attendPersonTrends.getHeadUrl()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mHead);
        }

        mNickName.setText(attendPersonTrends.getNickName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(attendPersonTrends.getTimeStamp());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = String.format("%02d:%02d", hour, minute);
        mTime.setText(time);
//        mPersonTrendsDeskType.setImageBitmap();
        mDeskName.setText("[" + attendPersonTrends.getDeskId() + "]" + "  " + attendPersonTrends.getDeskName());
        mStatus.setText(attendPersonTrends.getStatusChinese());

        mNoData.setVisibility(View.GONE);
        mLL.setVisibility(View.VISIBLE);
    }
}
