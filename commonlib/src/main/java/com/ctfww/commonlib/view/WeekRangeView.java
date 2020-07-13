package com.ctfww.commonlib.view;

import android.content.Context;
import android.graphics.Canvas;

import com.ctfww.commonlib.entity.MyDateTimeUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

import java.util.ArrayList;
import java.util.List;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class WeekRangeView extends MonthView {

    private int mRadius;

    public WeekRangeView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
 //       mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
//        int cx = x + mItemWidth / 2;
//        int cy = y + mItemHeight / 2;
        int left = 20 + mRadius;
        int top = y + mItemHeight / 2 - mRadius;
        int right = canvas.getWidth() - 20 - mRadius;
        int bottom = top + 2 * mRadius;
        canvas.drawRect(left, top, right, bottom, mSelectedPaint);

        int cx = left;
        int cy = top + mRadius;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);

        cx = right;
        cy = top + mRadius;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);

        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
       float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        java.util.Calendar todayCalendar = java.util.Calendar.getInstance();
        String day = todayCalendar.get(java.util.Calendar.DAY_OF_MONTH) == calendar.getDay() ? "今天" : String.valueOf(calendar.getDay());
        mSupportDataList.add(new SupportData((float)cx, baselineY, day, calendar));
        if (isSelected) {
            mSelectStartTime = MyDateTimeUtils.getWeekStartTime(calendar.getTimeInMillis());
            for (int i = 0; i < mSupportDataList.size(); ++i) {
                SupportData supportData = mSupportDataList.get(i);
                if (supportData.calendar.getTimeInMillis() >= mSelectStartTime && supportData.calendar.getTimeInMillis() < mSelectStartTime + 7l * 24l * 3600l * 1000l) {
                    canvas.drawText(supportData.name,
                            supportData.x,
                            supportData.y,
                            mSelectTextPaint);
                }
            }

            mSupportDataList.clear();
        }
        else {
            if (calendar.getTimeInMillis() >= mSelectStartTime && calendar.getTimeInMillis() < mSelectStartTime + 7l * 24l * 3600l * 1000l) {
                canvas.drawText(day,
                        cx,
                        baselineY,
                        mSelectTextPaint);
                if (calendar.getTimeInMillis() >= mSelectStartTime + 6l * 24l * 3600l * 1000l) {
                    mSupportDataList.clear();
                    mSelectStartTime = 0;
                }
            }
            else {
                canvas.drawText(day, cx, baselineY,
                        calendar.isCurrentDay() ? mCurDayTextPaint :
                                calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            }
        }

//        java.util.Calendar todayCalendar = java.util.Calendar.getInstance();
//        String day = todayCalendar.get(java.util.Calendar.DAY_OF_MONTH) == calendar.getDay() ? "今天" : String.valueOf(calendar.getDay());
//        mPOIList.add(new POI(x, y, day));
//        if (isSelected) {
//            mSelectY = y;
//            drawPrevText(canvas);
//            mPOIList.clear();
//        }
//        else {
//            drawElseText(canvas, day, x, y, calendar);
//            if (mSelectY >= 0 && y != mSelectY) {
//                mPOIList.clear();
//                mSelectY = 0;
//            }
//        }
    }

//    private void drawPrevText(Canvas canvas) {
//        for (int i = 0; i < mPOIList.size(); ++i) {
//            if (mSelectY == mPOIList.get(i).getY()) {
//                int cx = mPOIList.get(i).getX() + mItemWidth / 2;
//                float baselineY = mTextBaseLine + mPOIList.get(i).getY();
//                canvas.drawText(mPOIList.get(i).getName(), mPOIList.get(i).getX(), baselineY, mSelectedPaint);
//            }
//        }
//    }
//
//    private void drawElseText(Canvas canvas, String day, int x, int y, Calendar calendar) {
//        int cx = x + mItemWidth / 2;
//        float baselineY = mTextBaseLine + y;
//        if (y == mSelectY) {
//            canvas.drawText(day, x, baselineY, mSelectedPaint);
//        }
//        else {
//            canvas.drawText(day, x, baselineY,
//                    calendar.isCurrentDay() ? mCurDayTextPaint :
//                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
//        }
//    }

    private List<SupportData> mSupportDataList = new ArrayList<>();
    private long mSelectStartTime = 0;

    class SupportData {
        public float x;
        public float y;
        public String name;
        public Calendar calendar;

        public SupportData(float x, float y, String name, Calendar calendar) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.calendar = calendar;
        }


    }
}
