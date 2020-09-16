package com.ctfww.module.keyevents.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.Consts;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.ctfww.commonlib.fragment.DayCalendarFragment;
import com.ctfww.commonlib.utils.CalendarUtils;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keyevents.datahelper.sp.Const;
import com.ctfww.module.keyevents.fragment.KeyEventListFragment;
import com.haibin.calendarview.Calendar;

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
        long startTime = MyDateTimeUtils.getDayStartTime(calendar.getTimeInMillis());
        long endTime = MyDateTimeUtils.getDayEndTime(calendar.getTimeInMillis());
        mKeyEventListFragment.showEndList(startTime, endTime);

        long timeStamp = calendar.getTimeInMillis();
        long monthStartTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        if (monthStartTime != mLastMonthStartTime) {
            getEveryDayCount(calendar.getTimeInMillis());
            mLastMonthStartTime = monthStartTime;
        }
    }

    @Override
    public void onYearChanged(int year) {

    }

    private void getEveryDayCount(long timeStamp) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = SPStaticUtils.getString(Const.ROLE);
        long startTime = MyDateTimeUtils.getMonthStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getMonthEndTime(timeStamp);
        HashMap<String, Calendar> hashMap = new HashMap<>();
        for (long time = startTime; time < endTime; time += 24l * 3600l * 1000l) {
            long count = "admin".equals(role) ? DBHelper.getInstance().getEndCount(groupId, time, time + 24l * 3600l * 1000l) : DBHelper.getInstance().getEndCount(groupId, userId, time, time + 24l * 3600l * 1000l);
            if (count > 0) {
                Calendar key = CalendarUtils.produceCalendar(time);
                Calendar value = CalendarUtils.produceCalendar(Consts.CALENDAR_END_ABNORMAL, "结");
                hashMap.put(key.toString(), value);
            }
        }

        mDayCalendarFragment.setSchemeData(hashMap);
    }
}
