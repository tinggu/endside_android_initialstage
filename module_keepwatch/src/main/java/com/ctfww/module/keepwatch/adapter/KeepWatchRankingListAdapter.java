package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchRanking;

import java.util.List;


public class KeepWatchRankingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeepWatchRanking> list;
    private Context mContext;

    public KeepWatchRankingListAdapter(List<KeepWatchRanking> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<KeepWatchRanking> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_ranking_one_item, parent, false);
        return new KeepWatchRankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeepWatchRanking keepWatchRanking = list.get(position);
        ((KeepWatchRankingViewHolder)holder).position.setText("" + (position + 1));
        ((KeepWatchRankingViewHolder)holder).nickName.setText(keepWatchRanking.getNickName());
        ((KeepWatchRankingViewHolder)holder).score.setText("" + keepWatchRanking.getScore());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class KeepWatchRankingViewHolder extends RecyclerView.ViewHolder {
    public TextView position, nickName;
    public TextView score;
    public View view;

    public KeepWatchRankingViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        position = itemView.findViewById(R.id.keepwatch_position);
        nickName = itemView.findViewById(R.id.keepwatch_nick_name);
        score = itemView.findViewById(R.id.keepwatch_score);
    }
}
