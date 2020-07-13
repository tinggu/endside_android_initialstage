package com.guoliang.module.keyevents.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.guoliang.commonlib.Consts;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MyDateTimeUtils;
import com.guoliang.commonlib.fragment.DayCalendarFragment;
import com.guoliang.commonlib.utils.CalendarUtils;
import com.guoliang.module.keyevents.adapter.KeyEventListAdapter;
import com.guoliang.module.keyevents.Entity.KeyEvent;
import com.guoliang.module.keyevents.R;
import com.guoliang.module.keyevents.bean.KeyEventStatisticsByUser;
import com.guoliang.module.keyevents.datahelper.NetworkHelper;
import com.guoliang.module.keyevents.fragment.KeyEventListFragment;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = "/keyevents/endList")
public class KeyEventEndListActivity extends AppCompatActivity implements View.OnClickListener,
        DayCalendarFragment.OnDayCalendarFragmentInteractionListener {
    private final static String TAG = "KeyEventEndListActivity";

    private final static int REQUEST_CODE_DEVICE_INFO = 1;

    private ImageView mBack;
    private TextView mTittle;
    private ImageView mPopCalendar;

    DayCalendarFragment mDayCalendarFragment;

    KeyEventListFragment mKeyEventListFragment;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keyevent_end_list_activity);

        initViews();
        setOnClickListener();

        mDayCalendarFragment.initData();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("已处理事件列表");
        mPopCalendar = findViewById(R.id.top_addition_img);
        mPopCalendar.setImageResource(R.drawable.pop_calendar);
        mPopCalendar.setVisibility(View.VISIBLE);

        mKeyEventListFragment = (KeyEventListFragment)getSupportFragmentManager().findFragmentById(R.id.keepevent_list_fragment) ;

        mDayCalendarFragment = (DayCalendarFragment)getSupportFragmentManager().findFragmentById(R.id.day_calendar_fragment);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mPopCalendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mPopCalendar.getId()) {
            if (mDayCalendarFragment.getVisibility() == View.VISIBLE) {
                mDayCalendarFragment.setVisibility(View.GONE);
            }
            else {
                mDayCalendarFragment.setVisibility(View.VISIBLE);
            }
        }
    }

    private long mLastMonthStartTime = 0;
    @Override
    public void onDayCalendarSelect(Calendar calendar) {
        mKeyEventListFragment.getEndList(calendar.getTimeInMillis());
        long timeStamp = calendar.getTimeInMillis();
        long monthStartTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        if (monthStartTime != mLastMonthStartTime) {
            getHistoryEveryDayKeyEventStatistics(calendar.getTimeInMillis());
            mLastMonthStartTime = monthStartTime;
        }
    }

    @Override
    public void onYearChanged(int year) {

    }

    private void getHistoryEveryDayKeyEventStatistics(long timeStamp) {
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);
        NetworkHelper.getInstance().getHistoryEveryDayKeyEventStatistics(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeyEventStatisticsByUser> keyEventStatisticsByUserList = (List<KeyEventStatisticsByUser>)obj;
                LogUtils.i(TAG, "getHistoryEveryDayKeyEventStatistics: keyEventStatisticsByUserList.size() = " + keyEventStatisticsByUserList.size());

                HashMap<String, Calendar> hashMap = new HashMap<>();
                for (int i = 0; i < keyEventStatisticsByUserList.size(); ++i) {
                    KeyEventStatisticsByUser keyEventStatisticsByUser = keyEventStatisticsByUserList.get(i);
                    if (keyEventStatisticsByUser.getEndCount() > 0){
                        Calendar key = CalendarUtils.produceCalendar(keyEventStatisticsByUser.getTimeStamp());
                        Calendar value = CalendarUtils.produceCalendar(Consts.CALENDAR_END_ABNORMAL, "结");
                        hashMap.put(key.toString(), value);
                    }
                }

                mDayCalendarFragment.setSchemeData(hashMap);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getHistoryEveryDayKeyEventStatistics: code = " + code);
            }
        });
    }
}
