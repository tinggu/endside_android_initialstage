package com.ctfww.module.desk.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.entity.RouteDesk;

import java.util.List;


public class RouteDeskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RouteDesk> list;
    private Context mContext;

    public RouteDeskListAdapter(List<RouteDesk> list) {
        this.list = list;
    }

    public void setList(List<RouteDesk> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_desk_one_item, parent, false);
        return new RouteDeskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RouteDesk routeDesk = list.get(position);
        ((RouteDeskViewHolder)holder).deskId.setText("" + routeDesk.getDeskId());
        if (position == 0) {
            ((RouteDeskViewHolder)holder).dist.setText("" + 0.0 + "m");
        }
        else {
            ((RouteDeskViewHolder)holder).dist.setText(String.format("%.1fm", calcDist(position)));
        }

        if (TextUtils.isEmpty(routeDesk.getTag())) {
            ((RouteDeskViewHolder)holder).tag.setText("经由点");
        }
        else {
            ((RouteDeskViewHolder)holder).tag.setText("标志点：" + routeDesk.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private double calcDist(int position) {
        if (position == 0) {
            return 0.0;
        }

        double dist = 0.0;
        for (int i = 0; i < position; ++i) {
            dist += GlobeFun.calcLocationDist(list.get(i).getLat(), list.get(i).getLng(), list.get(i + 1).getLat(), list.get(i + 1).getLng());
        }

        return dist;
    }
}

class RouteDeskViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView deskId, dist;
    public TextView tag;

    public RouteDeskViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskId = itemView.findViewById(R.id.desk_id);
        dist = itemView.findViewById(R.id.dist);
        tag = itemView.findViewById(R.id.tag);
    }
}
