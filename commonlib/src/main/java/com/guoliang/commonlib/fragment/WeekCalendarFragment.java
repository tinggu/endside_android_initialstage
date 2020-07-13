package com.guoliang.commonlib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.guoliang.commonlib.R;
import com.guoliang.commonlib.entity.MyDateTimeUtils;
import com.guoliang.commonlib.utils.CalendarUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.Map;

public class WeekCalendarFragment extends Fragment implements
        CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener{

    private static final String TAG = "WeekCalendarFragment";

    private OnWeekCalendarFragmentInteractionListener mListener;

    private LinearLayout mCalendarLL;
    private ImageView mLeft;
    private TextView mYearMonth;
    private ImageView mRight;
    private TextView mPrompt;
    private CalendarView mCalendarView;


    public WeekCalendarFragment() {
    }

    public static WeekCalendarFragment newInstance() {
        return new WeekCalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.week_calendar_fragment, container, false);

        initViews(view);
        setOnClickListener();

        return view;
    }

    private void setOnClickListener() {
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToPre(true);
            }
        });

        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToNext(true);
            }
        });

        mPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initViews(View view) {
        mCalendarLL = view.findViewById(R.id.calendar_ll);
        mLeft = view.findViewById(R.id.calendar_left);
        mYearMonth = view.findViewById(R.id.calendar_year_month);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String yearMonth = String.format("%04d-%02d", calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH) + 1);
        mYearMonth.setText(yearMonth);
        mRight = view.findViewById(R.id.calendar_right);
        mPrompt = view.findViewById(R.id.calendar_prompt);
        mCalendarView = view.findViewById(R.id.haibin_calendar_view);
        mCalendarView.scrollToCurrent();
//        onDayCalendarSelect(mCalendarView.getSelectedCalendar());
        LogUtils.i(TAG, "initViews");
//        mCalendarView.setVisibility(View.GONE);
    }

    // 设置多个标记
    public void setSchemeData(long timeStamp) {
//        long startTime = MyDateTimeUtils.getWeekStartTime(timeStamp);
//        HashMap<String, Calendar> hashMap = new HashMap<>();
//        for (int i = 0; i < 7; ++i) {
//            if (startTime == MyDateTimeUtils.getTodayStartTime()) {
//                Calendar key = CalendarUtils.produceCalendar(startTime);
//                Calendar value = CalendarUtils.produceCalendar(0xFF108cd4, "今天");
//                hashMap.put(key.toString(), value);
//            }
//            else {
//                Calendar key = CalendarUtils.produceCalendar(startTime);
//                Calendar value = CalendarUtils.produceCalendar(0xFF108cd4, "√");
//                hashMap.put(key.toString(), value);
//            }
//
//            startTime += 24l * 3600l * 1000l;
//        }
//        mCalendarView.setSchemeDate(map);
    }

    // 获取Calendar对象
    public Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        //如果单独标记颜色、则会使用这个颜色
        calendar.setSchemeColor(color);
        calendar.setScheme(text);

        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        String yearMonth = String.format("%04d-%02d", calendar.getYear(), calendar.getMonth());
        mYearMonth.setText(yearMonth);
        onDayCalendarSelect(calendar);
    }

    @Override
    public void onYearChange(int year) {
        onYearChanged(year);
    }

    private void onYearChanged(int year) {
        if (mListener != null) {
            mListener.onYearChanged(year);
        }
    }

    private void onDayCalendarSelect(Calendar calendar) {
        LogUtils.i(TAG, "onDayCalendarSelect");
        if (mListener != null) {
            LogUtils.i(TAG, "onDayCalendarSelect: mListener != null");
            mListener.onWeekCalendarSelect(calendar);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnWeekCalendarFragmentInteractionListener) {
            mListener = (OnWeekCalendarFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setVisibility(int type) {
        mCalendarLL.setVisibility(type);
    }

    public int getVisibility() {
        return mCalendarLL.getVisibility();
    }

    public void initData() {
        onDayCalendarSelect(mCalendarView.getSelectedCalendar());
    }

    public interface OnWeekCalendarFragmentInteractionListener {
        void onWeekCalendarSelect(Calendar calendar);
        void onYearChanged(int year);
    }
}
