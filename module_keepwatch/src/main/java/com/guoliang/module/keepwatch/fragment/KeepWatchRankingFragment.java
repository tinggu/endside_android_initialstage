package com.guoliang.module.keepwatch.fragment;

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
import com.blankj.utilcode.util.SPStaticUtils;
import com.bumptech.glide.Glide;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.FileInfo;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.entity.MyDateTimeUtils;
import com.guoliang.commonlib.utils.DialogUtils;
import com.guoliang.commonlib.utils.FileUtils;
import com.guoliang.module.keepwatch.DataHelper.NetworkHelper;
import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.Utils;
import com.guoliang.module.keepwatch.entity.KeepWatchPersonTrends;
import com.guoliang.module.keepwatch.entity.KeepWatchRanking;
import com.guoliang.module.keepwatch.entity.KeepWatchStatisticsByPeriod;
import com.guoliang.module.keepwatch.util.PopupWindowUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class KeepWatchRankingFragment extends Fragment {
    private final static String TAG = "KeepWatchRankingFragment";

    private View mV;

    private TextView mNoData;
    private LinearLayout mLL;
    private ImageView mGoldHead;
    private TextView mGoldPower;
    private TextView mGoldNickName;
    private ImageView mSilverHead;
    private TextView mSilverPower;
    private TextView mSilverNickName;
    private ImageView mBronzeHead;
    private TextView mBronzePower;
    private TextView mBronzeNickName;

    private String mType;
    private long mTimeStamp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_ranking_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mNoData = v.findViewById(R.id.keepwatch_no_data);

        mLL = v.findViewById(R.id.keepwatch_ranking_ll);

        mGoldHead = v.findViewById(R.id.keepwatch_gold_head);
        mGoldPower = v.findViewById(R.id.keepwatch_gold_score);
        mGoldNickName = v.findViewById(R.id.keepwatch_gold_nick_name);
        mSilverHead = v.findViewById(R.id.keepwatch_silver_head);
        mSilverPower = v.findViewById(R.id.keepwatch_silver_score);
        mSilverNickName = v.findViewById(R.id.keepwatch_silver_nick_name);
        mBronzeHead = v.findViewById(R.id.keepwatch_bronze_head);
        mBronzePower = v.findViewById(R.id.keepwatch_bronze_score);
        mBronzeNickName = v.findViewById(R.id.keepwatch_bronze_nick_name);

        mNoData.setVisibility(View.VISIBLE);
        mLL.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        mLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/ranking").navigation();
                EventBus.getDefault().postSticky(new MessageEvent(mType, "" + mTimeStamp));
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

    public void getTodayRanking() {
        getRanking(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        mType = "keepwatch_day_report";
        mTimeStamp = System.currentTimeMillis();
    }

    public void getThisDayRanking(long timeStamp) {
        getRanking(MyDateTimeUtils.getDayStartTime(timeStamp), MyDateTimeUtils.getDayEndTime(timeStamp));
        mType = "keepwatch_day_report";
        mTimeStamp = timeStamp;
    }

    public void getThisWeekRanking(long timeStamp) {
        getRanking(MyDateTimeUtils.getWeekStartTime(timeStamp), MyDateTimeUtils.getWeekEndTime(timeStamp));
        mType = "keepwatch_week_report";
        mTimeStamp = timeStamp;
    }

    public void getThisMonthRanking(long timeStamp) {
        getRanking(MyDateTimeUtils.getMonthStartTime(timeStamp), MyDateTimeUtils.getMonthEndTime(timeStamp));
        mType = "keepwatch_month_report";
        mTimeStamp = timeStamp;
    }

    public void getRanking(long startTime, long endTime) {
        final int count = 1;
        NetworkHelper.getInstance().getKeepWatchRanking(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchRanking> keepWatchRankingList = (List<KeepWatchRanking>)obj;
                updateRankingToUI(keepWatchRankingList);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void updateRankingToUI(List<KeepWatchRanking> keepWatchRankingList) {
        if (keepWatchRankingList.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mLL.setVisibility(View.GONE);
            return;
        }

        KeepWatchRanking keepWatchRanking = keepWatchRankingList.get(0);
        Glide.with(this).load(keepWatchRanking.getHeadUrl()).into(mGoldHead);
//        updateHead(keepWatchRanking.getHeadUrl(), keepWatchRanking.getUserId());
        mGoldNickName.setText(keepWatchRanking.getNickName());
        mGoldPower.setText("综合分 " + keepWatchRanking.getScore());

        if (keepWatchRankingList.size() >= 2) {
            keepWatchRanking = keepWatchRankingList.get(1);
            Glide.with(this).load(keepWatchRanking.getHeadUrl()).into(mSilverHead);
            mSilverNickName.setText(keepWatchRanking.getNickName());
            mSilverPower.setText("综合分 " + keepWatchRanking.getScore());
        }

        if (keepWatchRankingList.size() >= 3) {
            keepWatchRanking = keepWatchRankingList.get(2);
            Glide.with(this).load(keepWatchRanking.getHeadUrl()).into(mBronzeHead);
            mBronzeNickName.setText(keepWatchRanking.getNickName());
            mBronzePower.setText("综合分 " + keepWatchRanking.getScore());
        }

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
            mGoldHead.setImageBitmap(bitmap);
        }
        else {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setUrl(headUrl);
            fileInfo.setPath("head");
            fileInfo.setName(userId + ".jpg");
            fileInfo.setAddition1("ranking");
            fileInfo.setAddition2(headPath);
            MessageEvent messageEvent = new MessageEvent("download_file", GsonUtils.toJson(fileInfo));
            EventBus.getDefault().post(messageEvent);
        }
    }
}
