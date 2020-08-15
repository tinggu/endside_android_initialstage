package com.ctfww.module.keepwatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;

import java.util.HashMap;
import java.util.List;


public class KeepWatchSelectRouteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "KeepWatchSelectRouteListAdapter";

    private List<KeepWatchRouteSummary> list;
    private HashMap<String, String> selectedMap = new HashMap<>();

    public KeepWatchSelectRouteListAdapter(List<KeepWatchRouteSummary> list) {
        this.list = list;
        selectedMap.clear();
    }

    public void setList(List<KeepWatchRouteSummary> list) {
        this.list = list;
        selectedMap.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_select_route_one_item, parent, false);
        KeepWatchSelectRouteViewHolder holder = new KeepWatchSelectRouteViewHolder(view);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogUtils.i(TAG, "onBindViewHolder = " + position);

        final KeepWatchRouteSummary route = list.get(position);
        ((KeepWatchSelectRouteViewHolder) holder).routeName.setText(route.getRouteName());
        if (route.getIsAssignmented()) {
            ((KeepWatchSelectRouteViewHolder) holder).routeSelect.setVisibility(View.GONE);
            ((KeepWatchSelectRouteViewHolder) holder).prompt.setVisibility(View.VISIBLE);
        }
        else {
            ((KeepWatchSelectRouteViewHolder) holder).routeSelect.setVisibility(View.VISIBLE);
            ((KeepWatchSelectRouteViewHolder) holder).prompt.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(KeepWatchSelectRouteViewHolder holder) {
        holder.routeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                KeepWatchRouteSummary route = list.get(position);
                if (holder.routeSelect.isChecked()) {
                    selectedMap.put(route.getRouteId(), route.getRouteId());
                }
                else {
                    selectedMap.remove(route.getRouteId());
                }
            }
        });
    }

    public HashMap<String, String> getSelectedMap() {
        return selectedMap;
    }
}

class KeepWatchSelectRouteViewHolder extends RecyclerView.ViewHolder {
    private final static String TAG = "KeepWatchSelectRouteViewHolder";

    View view;
    TextView routeName;
    CheckBox routeSelect;
    TextView prompt;

    KeepWatchSelectRouteViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        routeName = itemView.findViewById(R.id.keepwatch_route_name);
        routeSelect = itemView.findViewById(R.id.keepwatch_select);
        prompt = itemView.findViewById(R.id.keepwatch_selected_prompt);
    }
}
