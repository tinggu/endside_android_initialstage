package com.ctfww.commonlib.fragment;

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
import com.ctfww.commonlib.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.Map;

public class DayCalendarFragment extends Fragment implements
        CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener{

    private static final String TAG = "DayCalendarFragment";

    private OnDayCalendarFragmentInteractionListener mListener;

    private LinearLayout mCalendarLL;
    private ImageView mLeft;
    private TextView mYearMonth;
    private ImageView mRight;
    private TextView mPrompt;
    private CalendarView mCalendarView;


    public DayCalendarFragment() {
    }

    public static DayCalendarFragment newInstance() {
        return new DayCalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.day_calendar_fragment, container, false);

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
    public void setSchemeData(Map<String, Calendar> map) {
        mCalendarView.setSchemeDate(map);
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
        String yearMonth = String.format("%02d-%02d", calendar.getYear(), calendar.getMonth());
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
            mListener.onDayCalendarSelect(calendar);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDayCalendarFragmentInteractionListener) {
            mListener = (OnDayCalendarFragmentInteractionListener) context;
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

    public interface OnDayCalendarFragmentInteractionListener {
        void onDayCalendarSelect(Calendar calendar);
        void onYearChanged(int year);
    }
}
