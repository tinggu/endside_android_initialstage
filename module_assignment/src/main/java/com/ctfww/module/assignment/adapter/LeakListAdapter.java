package com.ctfww.module.assignment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.user.entity.UserInfo;

import java.util.List;


public class LeakListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TodayAssignment> list;

    public LeakListAdapter(List<TodayAssignment> list) {
        this.list = list;
    }

    public void setList(List<TodayAssignment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leak_one_item, parent, false);
        return new LeakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TodayAssignment todayAssignment = list.get(position);

        if ("desk".equals(todayAssignment.getType())) {
            DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(todayAssignment.getGroupId(), todayAssignment.getObjectId());
            if (deskInfo != null) {
                ((LeakViewHolder)holder).deskName.setText(deskInfo.getIdName());
            }
        }
        else if ("route".equals(todayAssignment.getType())) {
            RouteSummary routeSummary = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(todayAssignment.getGroupId(), todayAssignment.getObjectId());
            if (routeSummary != null) {
                ((LeakViewHolder)holder).deskName.setText(routeSummary.getIdName());
            }
        }

        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(todayAssignment.getUserId());
        if (userInfo != null) {
            ((LeakViewHolder)holder).nickName.setText(userInfo.getNickName());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class LeakViewHolder extends RecyclerView.ViewHolder {
    public TextView deskId, deskName;
    public TextView nickName;
    public View view;

    public LeakViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskId = itemView.findViewById(R.id.keepwatch_desk_id);
        deskName = itemView.findViewById(R.id.object_name);
        nickName = itemView.findViewById(R.id.nick_name);
    }
}
