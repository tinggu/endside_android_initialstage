package com.ctfww.module.datapicker.data;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ctfww.module.datapicker.R;

import java.util.List;

/**
 * HourAndMinutePicker
 * Created by ycuwq on 2018/1/22.
 */
public class DataPicker extends LinearLayout implements
        Data.OnDataSelectedListener {

    private Data mData;
    private OnDataSelectedListener mOnDataSelectedListener;

    public DataPicker(Context context) {
        this(context, null);
    }

    public DataPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_data, this);
        initChild();
        initAttrs(context, attrs);
        mData.setBackgroundDrawable(getBackground());
    }

    @Override
    public void onDataSelected(Integer data, int position) {
        if (mOnDataSelectedListener != null) {
            mOnDataSelectedListener.onDataSelected(data, position);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DataPicker);
        int textSize = a.getDimensionPixelSize(R.styleable.DataPicker_itemTextSize, getResources().getDimensionPixelSize(R.dimen.WheelItemTextSize));
        int textColor = a.getColor(R.styleable.DataPicker_itemTextColor, Color.BLACK);
        boolean isTextGradual = a.getBoolean(R.styleable.DataPicker_textGradual, true);
        boolean isCyclic = a.getBoolean(R.styleable.DataPicker_wheelCyclic, true);
        int halfVisibleItemCount = a.getInteger(R.styleable.DataPicker_halfVisibleItemCount, 1);
        int selectedItemTextColor = a.getColor(R.styleable.DataPicker_selectedTextColor, Color.BLACK); // getResources().getColor(R.color.com_ycuwq_datepicker_selectedTextColor)
        int selectedItemTextSize = a.getDimensionPixelSize(R.styleable.HourAndMinutePicker_selectedTextSize, getResources().getDimensionPixelSize(R.dimen.WheelSelectedItemTextSize));
        int itemWidthSpace = a.getDimensionPixelSize(R.styleable.HourAndMinutePicker_itemWidthSpace,
                getResources().getDimensionPixelOffset(R.dimen.WheelItemWidthSpace));
        int itemHeightSpace = a.getDimensionPixelSize(R.styleable.HourAndMinutePicker_itemHeightSpace,
                getResources().getDimensionPixelOffset(R.dimen.WheelItemHeightSpace));
        boolean isZoomInSelectedItem = a.getBoolean(R.styleable.HourAndMinutePicker_zoomInSelectedItem, true);
        boolean isShowCurtain = a.getBoolean(R.styleable.HourAndMinutePicker_wheelCurtain, true);
        int curtainColor = a.getColor(R.styleable.HourAndMinutePicker_wheelCurtainColor, Color.WHITE);
        boolean isShowCurtainBorder = a.getBoolean(R.styleable.HourAndMinutePicker_wheelCurtainBorder, true);
        int curtainBorderColor = a.getColor(R.styleable.HourAndMinutePicker_wheelCurtainBorderColor, getResources().getColor(R.color.com_ycuwq_datepicker_divider));
        a.recycle();

        setTextSize(textSize);
        setTextColor(textColor);
        setTextGradual(isTextGradual);
        setCyclic(isCyclic);
        setHalfVisibleItemCount(halfVisibleItemCount);
        setSelectedItemTextColor(selectedItemTextColor);
        setSelectedItemTextSize(selectedItemTextSize);
        setItemWidthSpace(itemWidthSpace);
        setItemHeightSpace(itemHeightSpace);
        setZoomInSelectedItem(isZoomInSelectedItem);
        setShowCurtain(isShowCurtain);
        setCurtainColor(curtainColor);
        setShowCurtainBorder(isShowCurtainBorder);
        setCurtainBorderColor(curtainBorderColor);
    }
    private void initChild() {
        mData = findViewById(R.id.data_layout_data);
        mData.setOnDataSelectedListener(this);
    }

    public void updateData(List<Integer> dataList) {
        mData.updateData(dataList);
    }

    /**
     * @param position
     */
    public void setPosition(int position) {
        setPosition(position, true);
    }

    /**
     * @param position
     * @param smoothScroll the smooth scroll
     */
    public void setPosition(int position, boolean smoothScroll) {
        mData.setSelectedPosition(position, smoothScroll);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (mData != null) {
            mData.setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (mData != null) {
            mData.setBackgroundResource(resid);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (mData != null) {
            mData.setBackgroundDrawable(background);
        }
    }

    /**
     * 一般列表的文本颜色
     *
     * @param textColor 文本颜色
     */
    public void setTextColor(int textColor) {
        mData.setTextColor(textColor);
    }

    /**
     * 一般列表的文本大小
     *
     * @param textSize 文字大小
     */
    public void setTextSize(int textSize) {
        mData.setTextSize(textSize);
    }

    /**
     * 设置被选中时候的文本颜色
     *
     * @param selectedItemTextColor 文本颜色
     */
    public void setSelectedItemTextColor(int selectedItemTextColor) {
        mData.setSelectedItemTextColor(selectedItemTextColor);
    }

    /**
     * 设置被选中时候的文本大小
     *
     * @param selectedItemTextSize 文字大小
     */
    public void setSelectedItemTextSize(int selectedItemTextSize) {
        mData.setSelectedItemTextSize(selectedItemTextSize);
    }


    /**
     * 设置显示数据量的个数的一半。
     * 为保证总显示个数为奇数,这里将总数拆分，itemCount = mHalfVisibleItemCount * 2 + 1
     *
     * @param halfVisibleItemCount 总数量的一半
     */
    public void setHalfVisibleItemCount(int halfVisibleItemCount) {
        mData.setHalfVisibleItemCount(halfVisibleItemCount);
    }

    /**
     * Sets item width space.
     *
     * @param itemWidthSpace the item width space
     */
    public void setItemWidthSpace(int itemWidthSpace) {
        mData.setItemWidthSpace(itemWidthSpace);
    }

    /**
     * 设置两个Item之间的间隔
     *
     * @param itemHeightSpace 间隔值
     */
    public void setItemHeightSpace(int itemHeightSpace) {
        mData.setItemHeightSpace(itemHeightSpace);
    }


    /**
     * Set zoom in center item.
     *
     * @param zoomInSelectedItem the zoom in center item
     */
    public void setZoomInSelectedItem(boolean zoomInSelectedItem) {
        mData.setZoomInSelectedItem(zoomInSelectedItem);
    }

    /**
     * 设置是否循环滚动。
     * set wheel cyclic
     * @param cyclic 上下边界是否相邻
     */
    public void setCyclic(boolean cyclic) {
        mData.setCyclic(cyclic);
    }

    /**
     * 设置文字渐变，离中心越远越淡。
     * Set the text color gradient
     * @param textGradual 是否渐变
     */
    public void setTextGradual(boolean textGradual) {
        mData.setTextGradual(textGradual);
    }


    /**
     * 设置中心Item是否有幕布遮盖
     * set the center item curtain cover
     * @param showCurtain 是否有幕布
     */
    public void setShowCurtain(boolean showCurtain) {
        mData.setShowCurtain(showCurtain);
    }

    /**
     * 设置幕布颜色
     * set curtain color
     * @param curtainColor 幕布颜色
     */
    public void setCurtainColor(int curtainColor) {
        mData.setCurtainColor(curtainColor);
    }

    /**
     * 设置幕布是否显示边框
     * set curtain border
     * @param showCurtainBorder 是否有幕布边框
     */
    public void setShowCurtainBorder(boolean showCurtainBorder) {
        mData.setShowCurtainBorder(showCurtainBorder);
    }

    /**
     * 幕布边框的颜色
     * curtain border color
     * @param curtainBorderColor 幕布边框颜色
     */
    public void setCurtainBorderColor(int curtainBorderColor) {
        mData.setCurtainBorderColor(curtainBorderColor);
    }

    /**
     * 设置选择器的指示器文本
     * set indicator text
     * @param hourText  小时指示器文本
     * @param minuteText 分钟指示器文本

     */
    public void setIndicatorText(String hourText, String minuteText) {
        mData.setIndicatorText(hourText);
    }

    /**
     * 设置指示器文字的颜色
     * set indicator text color
     * @param textColor 文本颜色
     */
    public void setIndicatorTextColor(int textColor) {
        mData.setIndicatorTextColor(textColor);
    }

    /**
     * 设置指示器文字的大小
     *  indicator text size
     * @param textSize 文本大小
     */
    public void setIndicatorTextSize(int textSize) {
        mData.setTextSize(textSize);
    }

    public int getCurrentData() {
        int position = mData.getCurrentPosition();
        return mData.getDataList().get(position);
    }

    /**
     * The interface On date selected listener.
     */
    public interface OnDataSelectedListener {
        /**
         * On time selected.
         *
         * @param data
         */
        void onDataSelected(Integer data, int position);
    }
}
