package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.activity.KeepWatchDeskListActivity;
import com.ctfww.module.keepwatch.activity.KeepWatchQrActivity;
import com.ctfww.module.keepwatch.activity.KeepWatchViewSigninDeskActivity;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;

import java.util.List;


public class KeepWatchDeskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeepWatchDesk> list;
    private Context mContext;

    public KeepWatchDeskListAdapter(List<KeepWatchDesk> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<KeepWatchDesk> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_desk_one_item, parent, false);
        final KeepWatchDeskViewHolder holder = new KeepWatchDeskViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (holder.delete.getVisibility() == View.VISIBLE) {
                    holder.delete.setVisibility(View.GONE);
                    return;
                }

                Intent intent = new Intent(mContext, KeepWatchViewSigninDeskActivity.class);
                intent.putExtra("desk_id", list.get(position).getDeskId());
                mContext.startActivity(intent);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!"admin".equals(SPStaticUtils.getString("role"))) {
                    return true;
                }

                holder.delete.setVisibility(View.VISIBLE);

                return true;
            }
        });

        holder.qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                KeepWatchDesk desk = list.get(position);
                Intent intent = new Intent(mContext, KeepWatchQrActivity.class);
                intent.putExtra("desk_id", desk.getDeskId());
                intent.putExtra("desk_name", desk.getDeskName());
                mContext.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                KeepWatchDesk desk = list.get(position);
                ((KeepWatchDeskListActivity)mContext).deleteSigninDesk(desk.getDeskId());
                holder.delete.setVisibility(View.GONE);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeepWatchDesk desk = list.get(position);
        ((KeepWatchDeskViewHolder)holder).deskId.setText("" + desk.getDeskId());
        ((KeepWatchDeskViewHolder)holder).deskName.setText(desk.getDeskName());
        ((KeepWatchDeskViewHolder)holder).address.setText(desk.getDeskAddress());
        if (desk.hasWifi()) {
            ((KeepWatchDeskViewHolder)holder).wifi.setChecked(true);
        }
        if (desk.hasGps()) {
            ((KeepWatchDeskViewHolder)holder).gps.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class KeepWatchDeskViewHolder extends RecyclerView.ViewHolder {
    public CheckBox wifi, gps;
    public TextView deskId;
    public TextView deskName;
    public ImageView qr;
    public TextView address;
    public Button delete;
    public View view;

    public KeepWatchDeskViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        wifi = itemView.findViewById(R.id.keepwatch_wifi);
        gps = itemView.findViewById(R.id.keepwatch_gps);
        deskId = itemView.findViewById(R.id.keepwatch_desk_id);
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        qr = itemView.findViewById(R.id.keepwatch_desk_qr);
        address = itemView.findViewById(R.id.keepwatch_desk_address);
        delete = itemView.findViewById(R.id.keepwatch_desk_delete);
    }
}
