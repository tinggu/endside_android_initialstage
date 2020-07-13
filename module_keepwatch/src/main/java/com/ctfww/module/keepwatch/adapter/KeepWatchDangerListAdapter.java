package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchStatisticsByDesk;

import java.util.List;


public class KeepWatchDangerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeepWatchStatisticsByDesk> list;
    private Context mContext;

    public KeepWatchDangerListAdapter(List<KeepWatchStatisticsByDesk> list) {
        this.list = list;
    }

    public void setList(List<KeepWatchStatisticsByDesk> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_danger_one_item, parent, false);
        return new KeepWatchDangerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeepWatchStatisticsByDesk keepWatchStatisticsByDesk = list.get(position);
        ((KeepWatchDangerViewHolder)holder).deskId.setText("" + keepWatchStatisticsByDesk.getDeskId());
        ((KeepWatchDangerViewHolder)holder).deskName.setText(keepWatchStatisticsByDesk.getDeskName());
        ((KeepWatchDangerViewHolder)holder).shouldCount.setText("本月应巡检：" + keepWatchStatisticsByDesk.getFrequency() + "次");
        ((KeepWatchDangerViewHolder)holder).signinCount.setText("本月实际巡检：" + keepWatchStatisticsByDesk.getSigninCount() + "次");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class KeepWatchDangerViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView deskId, deskName;
    public TextView shouldCount, signinCount;

    public KeepWatchDangerViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskId = itemView.findViewById(R.id.keepwatch_desk_id);
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        shouldCount = itemView.findViewById(R.id.keepwatch_should_count);
        signinCount = itemView.findViewById(R.id.keepwatch_signin_count);
    }
}
