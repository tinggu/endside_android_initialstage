package com.guoliang.module.main.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.guoliang.module.main.R;

import java.util.ArrayList;

public class DeviceMonitorLineChart extends LineChart {

    private static final String TAG = "DeviceMonitorLineChart";

    public DeviceMonitorLineChart(Context context) {
        super(context);
    }

    public DeviceMonitorLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeviceMonitorLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initLineChart() {
        LogUtils.i(TAG, "initLineChart: ");
        // 图表属性
        setBackgroundColor(Color.TRANSPARENT);
        setNoDataText(Utils.getApp().getString(R.string.main_had_no_device_on_monitor_now));
        setContentDescription("");
        setTouchEnabled(true);
        setDragEnabled(true);
        setScaleXEnabled(true);
        setScaleYEnabled(false);

        Legend legend = getLegend();
        legend.setEnabled(false);

        Description description = getDescription();
        description.setEnabled(false);
        setDescription(description);


        // 图表X轴
        XAxis xAxis = getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        xAxis.setTextColor(Color.rgb(171,182,212));
        xAxis.setTextSize(9f);
        xAxis.setLabelCount(5);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + ":00";
            }
        });

        // 图表左边Y轴
        YAxis leftYAxis = getAxisLeft();
        leftYAxis.setEnabled(true);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.setDrawAxisLine(false);
        leftYAxis.setTextColor(Color.rgb(171,182,212));
        leftYAxis.setTextSize(9f);
        leftYAxis.setGridLineWidth(1f);
        leftYAxis.setGridColor(R.color.main_color_main_page_bg);
        leftYAxis.setAxisMaximum(100);
        leftYAxis.setAxisMinimum(0);
        leftYAxis.setLabelCount(5);
        leftYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        // 图表右边Y轴
        YAxis rightYAxis = getAxisRight();
        rightYAxis.setEnabled(false);
    }

    public void setData() {
        ArrayList<Entry> alarmValues = new ArrayList<>();
        alarmValues.add(new Entry(0, 10));
        alarmValues.add(new Entry(1, 12));
        alarmValues.add(new Entry(2, 9));
        alarmValues.add(new Entry(3, 14));
        alarmValues.add(new Entry(4, 12));
        alarmValues.add(new Entry(5, 10));
        alarmValues.add(new Entry(6, 9));
        alarmValues.add(new Entry(7, 7));
        alarmValues.add(new Entry(8, 6));
        alarmValues.add(new Entry(9, 7));

        ArrayList<Entry> normalValues = new ArrayList<>();
        normalValues.add(new Entry(0, 85));
        normalValues.add(new Entry(1, 82));
        normalValues.add(new Entry(2, 84));
        normalValues.add(new Entry(3, 80));
        normalValues.add(new Entry(4, 78));
        normalValues.add(new Entry(5, 76));
        normalValues.add(new Entry(6, 75));
        normalValues.add(new Entry(7, 79));
        normalValues.add(new Entry(8, 83));
        normalValues.add(new Entry(9, 82));

        ArrayList<Entry> repairValues = new ArrayList<>();
        repairValues.add(new Entry(0, 25));
        repairValues.add(new Entry(1, 23));
        repairValues.add(new Entry(2, 21));
        repairValues.add(new Entry(3, 24));
        repairValues.add(new Entry(4, 20));
        repairValues.add(new Entry(5, 18));
        repairValues.add(new Entry(6, 23));
        repairValues.add(new Entry(7, 25));
        repairValues.add(new Entry(8, 27));
        repairValues.add(new Entry(9, 30));

        ArrayList<Entry> closeValues = new ArrayList<>();
        closeValues.add(new Entry(0, 5));
        closeValues.add(new Entry(1, 6));
        closeValues.add(new Entry(2, 9));
        closeValues.add(new Entry(3, 7));
        closeValues.add(new Entry(4, 8));
        closeValues.add(new Entry(5, 10));
        closeValues.add(new Entry(6, 12));
        closeValues.add(new Entry(7, 9));
        closeValues.add(new Entry(8, 4));
        closeValues.add(new Entry(9, 2));

        if (getData() != null && getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) getData().getDataSetByIndex(0);
            lineDataSet.setValues(alarmValues);
            getData().notifyDataChanged();
            notifyDataSetChanged();
        } else {
            ArrayList<ILineDataSet> allLinesList = new ArrayList<>();

            LineDataSet alarmLineDataSet = new LineDataSet(alarmValues, "告警");
            alarmLineDataSet.setLineWidth(1.5f);
            alarmLineDataSet.setColor(Color.rgb(236, 74,20));
            alarmLineDataSet.setHighLightColor(getResources().getColor(R.color.color_white_30));
            alarmLineDataSet.setDrawCircles(false);
            alarmLineDataSet.setDrawValues(false);

            LineDataSet normalLineDataSet = new LineDataSet(normalValues, "正常");
            normalLineDataSet.setLineWidth(1f);
            normalLineDataSet.setColor(Color.rgb(81,235,175));
            normalLineDataSet.setHighLightColor(getResources().getColor(R.color.color_white_30));
            normalLineDataSet.setDrawCircles(false);
            normalLineDataSet.setDrawValues(false);

            LineDataSet repairLineDataSet = new LineDataSet(repairValues, "维修");
            repairLineDataSet.setLineWidth(1f);
            repairLineDataSet.setColor(Color.rgb(87,195,255));
            repairLineDataSet.setHighLightColor(getResources().getColor(R.color.color_white_30));
            repairLineDataSet.setDrawCircles(false);
            repairLineDataSet.setDrawValues(false);

            LineDataSet closeLineDataSet = new LineDataSet(closeValues, "关闭");
            closeLineDataSet.setLineWidth(1f);
            closeLineDataSet.setColor(Color.rgb(170,170,170));
            closeLineDataSet.setHighLightColor(getResources().getColor(R.color.color_white_30));
            closeLineDataSet.setDrawCircles(false);
            closeLineDataSet.setDrawValues(false);

            allLinesList.add(alarmLineDataSet);
            allLinesList.add(normalLineDataSet);
            allLinesList.add(repairLineDataSet);
            allLinesList.add(closeLineDataSet);

            LineData lineData = new LineData(allLinesList);
            setData(lineData);
        }

        setVisibleXRangeMaximum(5);
        moveViewToX(alarmValues.size());

        animateX(1000);
        invalidate();
    }
}
