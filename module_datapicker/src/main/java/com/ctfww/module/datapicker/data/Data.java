package com.ctfww.module.datapicker.data;

import android.content.Context;
import android.util.AttributeSet;

import com.ctfww.module.datapicker.WheelPicker;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 * HourPicker
 * Created by ycuwq on 2018/1/22.
 */
public class Data extends WheelPicker<Integer> {

    private OnDataSelectedListener mOnDataSelectedListener;

    public Data(Context context) {
        this(context, null);
    }

    public Data(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Data(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemMaximumWidthText("00");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);
        setDataFormat(numberFormat);
        List<Integer> initList = Arrays.asList(1, 2);
        setDataList(initList);
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
            @Override
            public void onWheelSelected(Integer item, int position) {
                if (mOnDataSelectedListener != null) {
                    mOnDataSelectedListener.onDataSelected(item, position);
                }
            }
        });
    }

    public void setSelectedPosition(int position) {
        setSelectedPosition(position, true);
    }

    public void setSelectedPosition(int position, boolean smootScroll) {
        setCurrentPosition(position, smootScroll);
    }

    public void setOnDataSelectedListener(OnDataSelectedListener onDataSelectedListener) {
        mOnDataSelectedListener = onDataSelectedListener;
    }

    public void updateData(List<Integer> dataList) {
        setDataList(dataList);
    }

    public interface OnDataSelectedListener {
        void onDataSelected(Integer item, int position);
    }
}
