package com.guoliang.module.keepwatch.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.entity.KeepWatchSigninStatistics;
import com.guoliang.module.keepwatch.entity.KeepWatchStatisticsByDesk;

import java.util.List;


public class KeepWatchStatisticsByDeskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeepWatchStatisticsByDesk> list;
    private Context mContext;

    public KeepWatchStatisticsByDeskListAdapter(List<KeepWatchStatisticsByDesk> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<KeepWatchStatisticsByDesk> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_signin_statistics_one_item, parent, false);
        return new KeepWatcStatisticsByDeskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeepWatchStatisticsByDesk keepWatchStatisticsByDesk = list.get(position);
        ((KeepWatcStatisticsByDeskViewHolder)holder).deskName.setText("[" + keepWatchStatisticsByDesk.getDeskId() + "]" + "  " + keepWatchStatisticsByDesk.getDeskName());
        ((KeepWatcStatisticsByDeskViewHolder)holder).statistics.setText("" + keepWatchStatisticsByDesk.getSigninCount() + "/" + keepWatchStatisticsByDesk.getFrequency());
        if (keepWatchStatisticsByDesk.getSigninCount() == 0) {
            ((KeepWatcStatisticsByDeskViewHolder)holder).statistics.setTextColor(0xFFF65066);
        }
        else if (keepWatchStatisticsByDesk.getSigninCount() < keepWatchStatisticsByDesk.getFrequency()) {
            ((KeepWatcStatisticsByDeskViewHolder)holder).statistics.setTextColor(0xFFEDC00E);
        }
        else {
            ((KeepWatcStatisticsByDeskViewHolder)holder).statistics.setTextColor(0xFF7ED321);
        }

        ((KeepWatcStatisticsByDeskViewHolder)holder).nickName.setText(keepWatchStatisticsByDesk.getNickName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class KeepWatcStatisticsByDeskViewHolder extends RecyclerView.ViewHolder {
    public TextView deskName, statistics;
    public TextView nickName, mobile;
    public View view;

    public KeepWatcStatisticsByDeskViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        statistics = itemView.findViewById(R.id.keepwatch_signin_statistics);
        nickName = itemView.findViewById(R.id.keepwatch_nick_name);
    }
}
