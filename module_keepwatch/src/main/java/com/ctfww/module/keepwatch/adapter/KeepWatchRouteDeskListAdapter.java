package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteDesk;

import java.util.List;


public class KeepWatchRouteDeskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeepWatchRouteDesk> list;
    private Context mContext;

    public KeepWatchRouteDeskListAdapter(List<KeepWatchRouteDesk> list) {
        this.list = list;
    }

    public void setList(List<KeepWatchRouteDesk> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_route_desk_one_item, parent, false);
        return new KeepWatchRouteDeskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeepWatchRouteDesk keepWatchRouteDesk = list.get(position);
        ((KeepWatchRouteDeskViewHolder)holder).deskId.setText("" + keepWatchRouteDesk.getDeskId());
        if (position == 0) {
            ((KeepWatchRouteDeskViewHolder)holder).dist.setText("" + 0.0 + "m");
        }
        else {
            ((KeepWatchRouteDeskViewHolder)holder).dist.setText(String.format("%.1fm", calcDist(position)));
        }

        if (TextUtils.isEmpty(keepWatchRouteDesk.getTag())) {
            ((KeepWatchRouteDeskViewHolder)holder).tag.setText("经由点");
        }
        else {
            ((KeepWatchRouteDeskViewHolder)holder).tag.setText("标志点：" + keepWatchRouteDesk.getTag());
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

class KeepWatchRouteDeskViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView deskId, dist;
    public TextView tag;

    public KeepWatchRouteDeskViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskId = itemView.findViewById(R.id.keepwatch_desk_id);
        dist = itemView.findViewById(R.id.keepwatch_dist);
        tag = itemView.findViewById(R.id.keepwatch_tag);
    }
}
