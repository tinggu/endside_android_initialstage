package com.ctfww.module.keyevents.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.module.keyevents.Entity.KeyEventStatisticsByUserUnit;
import com.ctfww.module.keyevents.R;

import java.util.List;

public class KeyEventEndStatisticsByUserUnitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeyEventStatisticsByUserUnit> list;
    private Context mContext;

    public KeyEventEndStatisticsByUserUnitAdapter(List<KeyEventStatisticsByUserUnit> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<KeyEventStatisticsByUserUnit> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keyevent_end_statistics_by_user_unit_one_item, parent, false);
        return new KeyEventEndStatisticsByUserUnitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeyEventStatisticsByUserUnit keyEventStatisticsByUserUnit = list.get(position);
        int ranking = position + 1;
        for (int i = position; i > 0; --i) {
            if (list.get(position).getEndCount() != list.get(position - 1).getEndCount()) {
                break;
            }

            --ranking;
        }

        ((KeyEventEndStatisticsByUserUnitViewHolder)holder).position.setText("" + ranking);
        ((KeyEventEndStatisticsByUserUnitViewHolder)holder).nickName.setText(keyEventStatisticsByUserUnit.getNickName());
        ((KeyEventEndStatisticsByUserUnitViewHolder)holder).count.setText("" + keyEventStatisticsByUserUnit.getEndCount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class KeyEventEndStatisticsByUserUnitViewHolder extends RecyclerView.ViewHolder {
    public TextView position, nickName;
    public TextView count;
    public View view;

    public KeyEventEndStatisticsByUserUnitViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        position = itemView.findViewById(R.id.keyevent_position);
        nickName = itemView.findViewById(R.id.keyevent_nick_name);
        count = itemView.findViewById(R.id.keyevent_count);
    }
}
