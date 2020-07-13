package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninStatistics;

import java.util.List;


public class KeepWatchSigninLeakListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeepWatchSigninStatistics> list;
    private Context mContext;

    public KeepWatchSigninLeakListAdapter(List<KeepWatchSigninStatistics> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<KeepWatchSigninStatistics> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_signin_leak_one_item, parent, false);
        return new KeepWatcSigninLeakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeepWatchSigninStatistics keepWatchSigninStatistics = list.get(position);
        ((KeepWatcSigninLeakViewHolder)holder).deskId.setText("【" + keepWatchSigninStatistics.getDeskId() + "】");
        ((KeepWatcSigninLeakViewHolder)holder).deskName.setText(keepWatchSigninStatistics.getDeskName());
        ((KeepWatcSigninLeakViewHolder)holder).frequency.setText("" + keepWatchSigninStatistics.getFrequency() + "次");
        ((KeepWatcSigninLeakViewHolder)holder).nickName.setText(keepWatchSigninStatistics.getNickName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class KeepWatcSigninLeakViewHolder extends RecyclerView.ViewHolder {
    public TextView deskId, deskName, frequency;
    public TextView nickName, mobile;
    public View view;

    public KeepWatcSigninLeakViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskId = itemView.findViewById(R.id.keepwatch_desk_id);
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        frequency = itemView.findViewById(R.id.keepwatch_signin_frequency);
        nickName = itemView.findViewById(R.id.keepwatch_nick_name);
    }
}
