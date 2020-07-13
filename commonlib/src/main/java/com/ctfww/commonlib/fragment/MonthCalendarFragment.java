package com.ctfww.commonlib.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ctfww.commonlib.R;

import java.util.Calendar;

public class MonthCalendarFragment extends Fragment {

    private static final String TAG = "MonthCalendarFragment";

    private ImageView mLastYear;

    private ImageView mNextYear;

    private TextView mYearNum;

    private TextView mMonth01;
    private TextView mMonth02;
    private TextView mMonth03;
    private TextView mMonth04;
    private TextView mMonth05;
    private TextView mMonth06;
    private TextView mMonth07;
    private TextView mMonth08;
    private TextView mMonth09;
    private TextView mMonth10;
    private TextView mMonth11;
    private TextView mMonth12;

    private Context mContext;

    private OnMonthCalendarFragmentInteractionListener mListener;

    private Calendar mCalendar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.month_calendar_fragment, container, false);

        initViews(view);
        setOnClickListeners();

        mCalendar = Calendar.getInstance();
        mYearNum.setText("" + mCalendar.get(Calendar.YEAR));
        showMonth();

        return view;
    }

    private void initViews(View view) {
        mLastYear = view.findViewById(R.id.calendar_last_year);
        mNextYear = view.findViewById(R.id.calendar_next_year);
        mYearNum = view.findViewById(R.id.calendar_year_num);

        mMonth01 = view.findViewById(R.id.calendar_month_1);
        mMonth02 = view.findViewById(R.id.calendar_month_2);
        mMonth03 = view.findViewById(R.id.calendar_month_3);
        mMonth04 = view.findViewById(R.id.calendar_month_4);
        mMonth05 = view.findViewById(R.id.calendar_month_5);
        mMonth06 = view.findViewById(R.id.calendar_month_6);
        mMonth07 = view.findViewById(R.id.calendar_month_7);
        mMonth08 = view.findViewById(R.id.calendar_month_8);
        mMonth09 = view.findViewById(R.id.calendar_month_9);
        mMonth10 = view.findViewById(R.id.calendar_month_10);
        mMonth11 = view.findViewById(R.id.calendar_month_11);
        mMonth12 = view.findViewById(R.id.calendar_month_12);
    }

    private void setOnClickListeners() {
        mLastYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCalenderByYear(mCalendar.get(Calendar.YEAR) - 1);
                mYearNum.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));
                selectMonth(mCalendar.get(Calendar.MONTH) + 1);
            }
        });

        mNextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCalenderByYear(mCalendar.get(Calendar.YEAR) + 1);
                mYearNum.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));
                selectMonth(mCalendar.get(Calendar.MONTH) + 1);
            }
        });

        mMonth01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(1);
            }
        });

        mMonth02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(2);
            }
        });

        mMonth03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(3);
            }
        });

        mMonth04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(4);
            }
        });

        mMonth05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(5);
            }
        });

        mMonth06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(6);
            }
        });

        mMonth07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(7);
            }
        });

        mMonth08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(8);
            }
        });

        mMonth09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(9);
            }
        });

        mMonth10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(10);
            }
        });

        mMonth11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(11);
            }
        });

        mMonth12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth(12);
            }
        });

    }

    private void selectMonth(int month) {
        refreshCalendarByMonth(month);
        mListener.onMonthSelected(mCalendar);
        showMonth();
    }

    private void showMonth() {
        resetMonth();
        switch (mCalendar.get(Calendar.MONTH) + 1) {
            case 1:
                mMonth01.setTextColor(Color.rgb(255, 255, 255));
                mMonth01.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 2:
                mMonth02.setTextColor(Color.rgb(255, 255, 255));
                mMonth02.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 3:
                mMonth03.setTextColor(Color.rgb(255, 255, 255));
                mMonth03.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 4:
                mMonth04.setTextColor(Color.rgb(255, 255, 255));
                mMonth04.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 5:
                mMonth05.setTextColor(Color.rgb(255, 255, 255));
                mMonth05.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 6:
                mMonth06.setTextColor(Color.rgb(255, 255, 255));
                mMonth06.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 7:
                mMonth07.setTextColor(Color.rgb(255, 255, 255));
                mMonth07.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 8:
                mMonth08.setTextColor(Color.rgb(255, 255, 255));
                mMonth08.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 9:
                mMonth09.setTextColor(Color.rgb(255, 255, 255));
                mMonth09.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 10:
                mMonth10.setTextColor(Color.rgb(255, 255, 255));
                mMonth10.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 11:
                mMonth11.setTextColor(Color.rgb(255, 255, 255));
                mMonth11.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
            case 12:
                mMonth12.setTextColor(Color.rgb(255, 255, 255));
                mMonth12.setBackground(mContext.getDrawable(R.drawable.shape_blue_15));
                break;
        }
    }

    private void resetMonth() {

        mMonth01.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth02.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth03.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth04.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth05.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth06.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth07.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth08.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth09.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth10.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth11.setTextColor(mContext.getResources().getColor(R.color.color_black_50));
        mMonth12.setTextColor(mContext.getResources().getColor(R.color.color_black_50));

        mMonth01.setBackground(null);
        mMonth02.setBackground(null);
        mMonth03.setBackground(null);
        mMonth04.setBackground(null);
        mMonth05.setBackground(null);
        mMonth06.setBackground(null);
        mMonth07.setBackground(null);
        mMonth08.setBackground(null);
        mMonth09.setBackground(null);
        mMonth10.setBackground(null);
        mMonth11.setBackground(null);
        mMonth12.setBackground(null);
    }

    private void refreshCalenderByYear(int year) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == year) {
            mCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        else {
            mCalendar.set(year, 0, 1);
        }
    }

    private void refreshCalendarByMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == mCalendar.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == month - 1) {
            mCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        else {
            mCalendar.set(mCalendar.get(Calendar.YEAR), month - 1, 1);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;

        if (context instanceof OnMonthCalendarFragmentInteractionListener) {
            mListener = (OnMonthCalendarFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public void initData() {
        mListener.onMonthSelected(mCalendar);
    }

    public long getTimeStamp() {
        return mCalendar.getTimeInMillis();
    }

    public interface OnMonthCalendarFragmentInteractionListener {
        void onMonthSelected(Calendar calendar);
    }
}
