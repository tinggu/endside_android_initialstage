package com.ctfww.module.desk.adapter;

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

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.activity.QrActivity;
import com.ctfww.commonlib.entity.Qr;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.Utils;
import com.ctfww.module.desk.datahelper.airship.Airship;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.entity.DeskInfo;

import java.util.List;


public class DeskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DeskInfo> list;
    private Context mContext;

    public DeskListAdapter(List<DeskInfo> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<DeskInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.desk_one_item, parent, false);
        final DeskViewHolder holder = new DeskViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (holder.delete.getVisibility() == View.VISIBLE) {
                    holder.delete.setVisibility(View.GONE);
                    return;
                }

                DeskInfo deskInfo = list.get(position);
                ARouter.getInstance().build("/common/viewMap")
                        .withString("type", "center")
                        .withDouble("lat", deskInfo.getLat())
                        .withDouble("lng", deskInfo.getLng())
                        .withString("name", deskInfo.getDeskName())
                        .withString("address", deskInfo.getDeskAddress())
                        .navigation();
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
                DeskInfo deskInfo = list.get(position);
                String url = Utils.getDeskQrUrl(deskInfo.getDeskId());
                Qr qr = new Qr(url, "" + deskInfo.getDeskId(), deskInfo.getDeskName());
                ARouter.getInstance().build("/common/qr").withString("qr", GsonUtils.toJson(qr)).navigation();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.delete.setVisibility(View.GONE);

                int position = holder.getAdapterPosition();
                DeskInfo desk = list.get(position);
                desk.setStatus("delete");
                desk.setTimeStamp(System.currentTimeMillis());
                desk.setSynTag("modify");
                DBHelper.getInstance().updateDesk(desk);
                list.remove(position);

                notifyDataSetChanged();
                Airship.getInstance().synDeskToCloud();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeskInfo desk = list.get(position);
        ((DeskViewHolder)holder).deskId.setText("" + desk.getDeskId());
        ((DeskViewHolder)holder).deskName.setText(desk.getDeskName());
        ((DeskViewHolder)holder).address.setText(desk.getDeskAddress());
        if (desk.hasWifi()) {
            ((DeskViewHolder)holder).wifi.setChecked(true);
        }
        if (desk.hasGps()) {
            ((DeskViewHolder)holder).gps.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class DeskViewHolder extends RecyclerView.ViewHolder {
    public CheckBox wifi, gps;
    public TextView deskId;
    public TextView deskName;
    public ImageView qr;
    public TextView address;
    public Button delete;
    public View view;

    public DeskViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        wifi = itemView.findViewById(R.id.wifi);
        gps = itemView.findViewById(R.id.gps);
        deskId = itemView.findViewById(R.id.desk_id);
        deskName = itemView.findViewById(R.id.desk_name);
        qr = itemView.findViewById(R.id.qr);
        address = itemView.findViewById(R.id.desk_address);
        delete = itemView.findViewById(R.id.desk_delete);
    }
}
