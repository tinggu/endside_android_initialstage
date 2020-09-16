package com.ctfww.module.desk.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.entity.RouteSummary;

import java.util.HashMap;
import java.util.List;


public class SelectRouteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "SelectRouteListAdapter";

    private List<RouteSummary> list;
    private HashMap<Integer, Integer> selectedMapOld = new HashMap<>();
    private HashMap<Integer, Integer> selectedMapNew = new HashMap<>();

    public SelectRouteListAdapter(List<RouteSummary> list, List<Integer> selectedRouteIdList) {
        this.list = list;
        setSelectedRouteIdList(selectedRouteIdList);
    }

    public void setList(List<RouteSummary> list) {
        this.list = list;
    }

    public void setSelectedRouteIdList(List<Integer> routeIdList) {
        for (int i = 0; i < routeIdList.size(); ++i) {
            int routeId = routeIdList.get(i);
            selectedMapOld.put(routeId, routeId);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_route_one_item, parent, false);
        SelectRouteViewHolder holder = new SelectRouteViewHolder(view);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogUtils.i(TAG, "onBindViewHolder = " + position);

        final RouteSummary route = list.get(position);
        ((SelectRouteViewHolder) holder).routeName.setText(route.getRouteName());
        Integer routeId = selectedMapOld.get(selectedMapOld.get(route.getRouteId()));
        if (routeId == null) {
            ((SelectRouteViewHolder) holder).routeSelect.setVisibility(View.VISIBLE);
            ((SelectRouteViewHolder) holder).prompt.setVisibility(View.GONE);
        }
        else {
            ((SelectRouteViewHolder) holder).routeSelect.setVisibility(View.GONE);
            ((SelectRouteViewHolder) holder).prompt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(SelectRouteViewHolder holder) {
        holder.routeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                RouteSummary route = list.get(position);
                if (holder.routeSelect.isChecked()) {
                    selectedMapNew.put(route.getRouteId(), route.getRouteId());
                }
                else {
                    selectedMapNew.remove("" + route.getRouteId());
                }
            }
        });
    }

    public HashMap<Integer, Integer> getSelectedMap() {
        return selectedMapNew;
    }
}

class SelectRouteViewHolder extends RecyclerView.ViewHolder {
    private final static String TAG = "SelectRouteViewHolder";

    View view;
    TextView routeName;
    CheckBox routeSelect;
    TextView prompt;

    SelectRouteViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        routeName = itemView.findViewById(R.id.route_name);
        routeSelect = itemView.findViewById(R.id.select);
        prompt = itemView.findViewById(R.id.selected_prompt);
    }
}
