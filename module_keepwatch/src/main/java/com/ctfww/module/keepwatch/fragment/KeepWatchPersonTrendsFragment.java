package com.ctfww.module.keepwatch.fragment;

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
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.FileInfo;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

public class KeepWatchPersonTrendsFragment extends Fragment {
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
        mV = inflater.inflate(R.layout.keepwatch_person_trends_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mNoData = v.findViewById(R.id.keepwatch_no_data);
        mLL = v.findViewById(R.id.keepwatch_person_trends);
        mHead = v.findViewById(R.id.keepwatch_person_trends_head);
        mNickName = v.findViewById(R.id.keepwatch_person_trends_nick_name);
        mTime = v.findViewById(R.id.keepwatch_person_trends_time);
        mDeskName = v.findViewById(R.id.keepwatch_person_trends_desk_name);
        mStatus = v.findViewById(R.id.keepwatch_person_trends_status);

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
        NetworkHelper.getInstance().getPersonTrends(count, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchPersonTrends> keepWatchPersonTrendsList = (List<KeepWatchPersonTrends>)obj;
                updatePersonTrendsToUI(keepWatchPersonTrendsList);
                LogUtils.i(TAG, "getPersonTrends success: keepWatchPersonTrendsList.size() = " + keepWatchPersonTrendsList.size());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getPersonTrends fail: code = " + code);
            }
        });
    }

    private void updatePersonTrendsToUI(List<KeepWatchPersonTrends> keepWatchPersonTrendsList) {
        if (keepWatchPersonTrendsList.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mLL.setVisibility(View.GONE);
            return;
        }

        KeepWatchPersonTrends keepWatchPersonTrends = keepWatchPersonTrendsList.get(0);

        if (!TextUtils.isEmpty(keepWatchPersonTrends.getHeadUrl())) {
            Glide.with(this).load(keepWatchPersonTrends.getHeadUrl()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mHead);
        }
//        updateHead(keepWatchPersonTrends.getHeadUrl(), keepWatchPersonTrends.getUserId());

        mNickName.setText(keepWatchPersonTrends.getNickName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(keepWatchPersonTrends.getTimeStamp());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = String.format("%02d:%02d", hour, minute);
        mTime.setText(time);
//        mPersonTrendsDeskType.setImageBitmap();
        mDeskName.setText("[" + keepWatchPersonTrends.getDeskId() + "]" + "  " + keepWatchPersonTrends.getDeskName());
        mStatus.setText(keepWatchPersonTrends.getStatusChinese());

        mNoData.setVisibility(View.GONE);
        mLL.setVisibility(View.VISIBLE);
    }

    private void updateHead(String headUrl, String userId) {
        if (TextUtils.isEmpty(headUrl)) {
            return;
        }

        String headPath = mV.getContext().getExternalFilesDir("") + "/" + "head" + "/" + userId + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(headPath);
        if (bitmap != null) {
            mHead.setImageBitmap(bitmap);
        }
        else {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setUrl(headUrl);
            fileInfo.setPath("head");
            fileInfo.setName(userId + ".jpg");
            fileInfo.setAddition1("person_trends");
            fileInfo.setAddition2(headPath);
            MessageEvent messageEvent = new MessageEvent("download_file", GsonUtils.toJson(fileInfo));
            EventBus.getDefault().post(messageEvent);
        }
    }
}