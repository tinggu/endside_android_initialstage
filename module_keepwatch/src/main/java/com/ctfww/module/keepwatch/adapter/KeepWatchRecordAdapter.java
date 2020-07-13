package com.ctfww.module.keepwatch.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.activity.ViewMapActivity;
import com.ctfww.module.keepwatch.entity.KeepWatchPeriodShowData;

import java.util.Calendar;
import java.util.List;


public class KeepWatchRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeepWatchPeriodShowData> list;

    public KeepWatchRecordAdapter(List<KeepWatchPeriodShowData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_one_item, parent, false);
        final KeepWatchRecordViewHolder holder = new KeepWatchRecordViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(holder.view.getContext(), ViewMapActivity.class);
                intent.putExtra("type", "trace_in_period");
                intent.putExtra("period_id", list.get(position).getPeriodId());
                holder.view.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(list.get(position).getStartTime());
        LogUtils.i("current", "onBindViewHolder: startTime = " + list.get(position).getStartTime() + ", hour = " + calendar.get(Calendar.HOUR_OF_DAY) + ", minute = " + calendar.get(Calendar.MINUTE));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(list.get(position).getEndTime());
        LogUtils.i("current", "onBindViewHolder: endTime = " + list.get(position).getEndTime() + ", hour = " + calendar2.get(Calendar.HOUR_OF_DAY) + ", minute = " + calendar2.get(Calendar.MINUTE));
        if (0 == position) {

            ((KeepWatchRecordViewHolder) holder).yearMonth.setText("" + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
            ((KeepWatchRecordViewHolder) holder).monthDay.setText("" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            ((KeepWatchRecordViewHolder) holder).startEndTime.setText("巡检时间 " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " - " + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE));
            ((KeepWatchRecordViewHolder) holder).signinDeskName.setText(list.get(position).getDeskName());
            return;
        }

        Calendar calendarPrev = Calendar.getInstance();
        calendarPrev.setTimeInMillis(list.get(position - 1).getStartTime());
        if (calendarPrev.get(Calendar.YEAR) != calendar.get(Calendar.YEAR) || calendarPrev.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
            ((KeepWatchRecordViewHolder) holder).yearMonth.setText("" + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
            ((KeepWatchRecordViewHolder) holder).monthDay.setText("" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            ((KeepWatchRecordViewHolder) holder).startEndTime.setText("巡检时间 " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " - " + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE));
            ((KeepWatchRecordViewHolder) holder).signinDeskName.setText(list.get(position).getDeskName());
        }
        else if (calendarPrev.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH)) {
            ((KeepWatchRecordViewHolder) holder).yearMonth.setVisibility(View.GONE);
            ((KeepWatchRecordViewHolder) holder).monthDay.setText("" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            ((KeepWatchRecordViewHolder) holder).startEndTime.setText("巡检时间 " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " - " + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE));
            ((KeepWatchRecordViewHolder) holder).signinDeskName.setText(list.get(position).getDeskName());
        }
        else if (list.get(position).getStartTime() != list.get(position - 1).getStartTime()) {
            ((KeepWatchRecordViewHolder) holder).yearMonth.setVisibility(View.GONE);
            ((KeepWatchRecordViewHolder) holder).monthDay.setVisibility(View.GONE);
            ((KeepWatchRecordViewHolder) holder).startEndTime.setText("巡检时间 " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " - " + calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE));
            ((KeepWatchRecordViewHolder) holder).signinDeskName.setText(list.get(position).getDeskName());
        }
        else {
            ((KeepWatchRecordViewHolder) holder).yearMonth.setVisibility(View.GONE);
            ((KeepWatchRecordViewHolder) holder).monthDay.setVisibility(View.GONE);
            ((KeepWatchRecordViewHolder) holder).startEndTime.setVisibility(View.GONE);
            ((KeepWatchRecordViewHolder) holder).signinDeskName.setText(list.get(position).getDeskName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<KeepWatchPeriodShowData> keepWatchProcessShowDataList) {
        list = keepWatchProcessShowDataList;
    }
}

class KeepWatchRecordViewHolder extends RecyclerView.ViewHolder {
    public TextView yearMonth, monthDay;
    public LinearLayout startEndTimeLinearLayout, signinDeskNameLinearLayout;
    public TextView startEndTime, signinDeskName;
    public View view;

    public KeepWatchRecordViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        yearMonth = itemView.findViewById(R.id.keepwatch_year_month);
        monthDay = itemView.findViewById(R.id.keepwatch_month_day);
        startEndTimeLinearLayout = itemView.findViewById(R.id.keepwatch_signin_start_end_time_linear_layout);
        signinDeskNameLinearLayout = itemView.findViewById(R.id.keepwatch_signin_desk_name_linear_layout);
        startEndTime = itemView.findViewById(R.id.keepwatch_signin_start_end_time);
        signinDeskName = itemView.findViewById(R.id.keepwatch_signin_desk_name);
    }
}
