package com.ctfww.module.desk.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.datahelper.airship.Airship;
import com.ctfww.module.desk.datahelper.dbhelper.DBHelper;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;

import java.util.List;


public class RouteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RouteSummary> list;
    private Context mContext;

    public RouteListAdapter(List<RouteSummary> list) {
        this.list = list;
    }

    public void setList(List<RouteSummary> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_one_item, parent, false);
        RouteViewHolder holder = new RouteViewHolder(view);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RouteSummary routeSummary = list.get(position);
        ((RouteViewHolder)holder).name.setText(routeSummary.getRouteName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(RouteViewHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.delete.getVisibility() == View.VISIBLE) {
                    holder.modify.setVisibility(View.GONE);
                    holder.delete.setVisibility(View.GONE);
                    return;
                }

                int position = holder.getAdapterPosition();
                RouteSummary routeSummary = list.get(position);
                List<RouteDesk> routeDeskList = DBHelper.getInstance().getRouteDeskInOneRoute(routeSummary.getGroupId(), routeSummary.getRouteId());
                if (routeDeskList.size() < 2) {
                    ToastUtils.showShort("该路线的形状点不足两个，不能显示路线！");
                    return;
                }

                float[] array = new float[routeDeskList.size() * 2];
                int i = 0;
                for (; i < routeDeskList.size(); ++i) {
                    RouteDesk routeDesk = routeDeskList.get(i);
                    array[2 * i] = (float)routeDesk.getLat();
                    array[2 * i + 1] = (float)routeDesk.getLng();
                }

                ARouter.getInstance().build("/baidumap/viewMap")
                        .withString("type", "trace")
                        .withFloatArray("trace_array", array)
                        .navigation();
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.modify.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.modify.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);
                int position = holder.getAdapterPosition();
                RouteSummary routeSummary = list.get(position);
                routeSummary.setStatus("delete");
                routeSummary.setTimeStamp(System.currentTimeMillis());
                routeSummary.setSynTag("modify");
                DBHelper.getInstance().updateRouteSummary(routeSummary);
                Airship.getInstance().synRouteSummaryToCloud();

                list.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.modify.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);

                int position = holder.getAdapterPosition();
                RouteSummary routeSummary = list.get(position);
                ARouter.getInstance().build("/desk/modifyRoute")
                        .withString("group_id", routeSummary.getGroupId())
                        .withInt("route_id", routeSummary.getRouteId())
                        .navigation();
            }
        });
    }
}

class RouteViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView name;
    public Button modify, delete;

    public RouteViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        name = itemView.findViewById(R.id.route_name);
        modify = itemView.findViewById(R.id.modify);
        delete = itemView.findViewById(R.id.delete);
    }
}
