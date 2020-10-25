package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.user.entity.UserInfo;

import java.util.List;


public class FinishStatusListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TodayAssignment> todayAssignmentList;
    private Context mContext;

    public FinishStatusListAdapter(List<TodayAssignment> todayAssignmentList, Context context) {
        this.todayAssignmentList = todayAssignmentList;
        mContext = context;
    }

    public void setList(List<TodayAssignment> todayAssignmentList) {
        this.todayAssignmentList = todayAssignmentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.finish_status_one_item, parent, false);
        return new FinishStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TodayAssignment todayAssignment = todayAssignmentList.get(position);
        if ("desk".equals(todayAssignment.getType())) {
            DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(todayAssignment.getGroupId(), todayAssignment.getObjectId());
            if (deskInfo != null) {
                ((FinishStatusViewHolder) holder).objectName.setText(deskInfo.getIdName());
            }
        } else if ("route".equals(todayAssignment.getType())) {
            RouteSummary routeSummary = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(todayAssignment.getGroupId(), todayAssignment.getObjectId());
            if (routeSummary != null) {
                ((FinishStatusViewHolder) holder).objectName.setText(routeSummary.getIdName());
            }
        }

        ((FinishStatusViewHolder) holder).signinStatus.setText("" + todayAssignment.getSigninCount() + "/" + todayAssignment.getFrequency());

        if (todayAssignment.getSigninCount() == 0) {
            ((FinishStatusViewHolder) holder).signinStatus.setTextColor(0xFFF65066);
        } else if (todayAssignment.getSigninCount() < todayAssignment.getFrequency()) {
            ((FinishStatusViewHolder) holder).signinStatus.setTextColor(0xFFEDC00E);
        } else {
            ((FinishStatusViewHolder) holder).signinStatus.setTextColor(0xFF7ED321);
        }

        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(todayAssignment.getUserId());
        if (userInfo != null) {
            ((FinishStatusViewHolder) holder).nickName.setText(userInfo.getNickName());
        }
    }

    @Override
    public int getItemCount() {
        return todayAssignmentList.size();
    }
}

class FinishStatusViewHolder extends RecyclerView.ViewHolder {
    public TextView objectName, signinStatus;
    public TextView nickName;
    public View view;

    public FinishStatusViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        objectName = itemView.findViewById(R.id.object_name);
        signinStatus = itemView.findViewById(R.id.signin_status);
        nickName = itemView.findViewById(R.id.nick_name);
    }
}
