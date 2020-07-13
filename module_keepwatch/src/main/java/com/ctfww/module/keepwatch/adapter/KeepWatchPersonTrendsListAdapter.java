package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchPersonTrends;

import java.util.Calendar;
import java.util.List;


public class KeepWatchPersonTrendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeepWatchPersonTrends> list;
    private Context mContext;

    public KeepWatchPersonTrendsListAdapter(List<KeepWatchPersonTrends> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<KeepWatchPersonTrends> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_person_trends_one_item, parent, false);
        return new KeepWatchPersonTrendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeepWatchPersonTrends keepWatchPersonTrends = list.get(position);
        Glide.with(((KeepWatchPersonTrendsViewHolder)holder).view).load(keepWatchPersonTrends.getHeadUrl()).into(((KeepWatchPersonTrendsViewHolder)holder).head);
        ((KeepWatchPersonTrendsViewHolder)holder).nickName.setText(keepWatchPersonTrends.getNickName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(keepWatchPersonTrends.getTimeStamp());
        ((KeepWatchPersonTrendsViewHolder)holder).dateTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        ((KeepWatchPersonTrendsViewHolder)holder).deskName.setText("[" + keepWatchPersonTrends.getDeskId() + "]" + "  " + keepWatchPersonTrends.getDeskName());
        ((KeepWatchPersonTrendsViewHolder)holder).status.setText(keepWatchPersonTrends.getStatusChinese());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class KeepWatchPersonTrendsViewHolder extends RecyclerView.ViewHolder {
    public ImageView head;
    public TextView nickName, dateTime;
    public TextView deskName, status;
    public View view;

    public KeepWatchPersonTrendsViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        head = itemView.findViewById(R.id.keepwatch_head);
        nickName = itemView.findViewById(R.id.keepwatch_nick_name);
        dateTime = itemView.findViewById(R.id.keepwatch_date_time);
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        status = itemView.findViewById(R.id.keepwatch_status);
    }
}
