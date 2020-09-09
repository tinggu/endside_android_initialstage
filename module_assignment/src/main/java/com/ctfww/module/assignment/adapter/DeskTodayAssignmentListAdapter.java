package com.ctfww.module.assignment.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.entity.DeskTodayAssignment;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.user.entity.UserInfo;

import java.util.List;


public class DeskTodayAssignmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "DeskTodayAssignmentListAdapter";

    private List<DeskTodayAssignment> list;

    public DeskTodayAssignmentListAdapter(List<DeskTodayAssignment> list) {
        this.list = list;
    }

    public void setList(List<DeskTodayAssignment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_assignment_one_item, parent, false);
        DeskTodayAssignmentViewHolder holder = new DeskTodayAssignmentViewHolder(view);

        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeskTodayAssignment deskAssignment = list.get(position);
        ((DeskTodayAssignmentViewHolder)holder).deskType.setImageResource(R.drawable.desk);
        DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(deskAssignment.getGroupId(), deskAssignment.getDeskId());
        if (deskInfo != null) {
            ((DeskTodayAssignmentViewHolder)holder).deskName.setText(deskInfo.getIdName());
        }

        ((DeskTodayAssignmentViewHolder)holder).frequency.setText("" + deskAssignment.getFrequency() + "次");

        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(deskAssignment.getUserId());
        if (userInfo != null) {
            ((DeskTodayAssignmentViewHolder)holder).nickName.setText(userInfo.getNickName());
        }

        long startHour = deskAssignment.getStartTime() / 3600l / 1000l;
        long startMinute = (deskAssignment.getStartTime() - startHour * 3600l * 1000l) / 60 / 1000;
        long endHour = deskAssignment.getEndTime() / 3600l / 1000l;
        long endMinute = (deskAssignment.getEndTime() - endHour * 3600l * 1000l) / 60 / 1000;
        String startEndTime = String.format("%02d:%02d——%02d:%02d", startHour, startMinute, endHour, endMinute);
        ((DeskTodayAssignmentViewHolder)holder).startEndDateTime.setText(startEndTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(DeskTodayAssignmentViewHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DeskTodayAssignment deskAssignment = list.get(position);
                DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(deskAssignment.getGroupId(), deskAssignment.getDeskId());
                if (deskInfo != null) {
                    ARouter.getInstance().build("/baidumap/viewMap")
                            .withString("type", "center")
                            .withDouble("lat", deskInfo.getLat())
                            .withDouble("lng", deskInfo.getLng())
                            .withString("name", deskInfo.getDeskName())
                            .withString("address", deskInfo.getDeskAddress())
                            .navigation();
                }
            }
        });
    }

    private String toChineseCircleType(String circleType) {
        String ret = "";
        if (TextUtils.isEmpty(circleType)) {
            return ret;
        }

        if (circleType.indexOf("Monday") != -1) {
            ret += getAppend(ret, "周一");
        }

        if (circleType.indexOf("Tuesday") != -1) {
            ret += getAppend(ret, "周二");
        }

        if (circleType.indexOf("Wedndesday") != -1) {
            ret += getAppend(ret, "周三");
        }

        if (circleType.indexOf("Thursday") != -1) {
            ret += getAppend(ret, "周四");
        }

        if (circleType.indexOf("Friday") != -1) {
            ret += getAppend(ret, "周五");
        }

        if (circleType.indexOf("Saturday") != -1) {
            ret += getAppend(ret, "周六");
        }

        if (circleType.indexOf("Sunday") != -1) {
            ret += getAppend(ret, "周日");
        }

        return ret;
    }

    private String getAppend(String old, String append) {
        return TextUtils.isEmpty(old) ? append : "、" + append;
    }
}

class DeskTodayAssignmentViewHolder extends RecyclerView.ViewHolder {
    public ImageView deskType;
    public TextView deskName, frequency;
    public TextView nickName, startEndDateTime;
    public View view;

    public DeskTodayAssignmentViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskType = itemView.findViewById(R.id.desk_type);
        deskName = itemView.findViewById(R.id.desk_name);
        frequency = itemView.findViewById(R.id.frequency);
        nickName = itemView.findViewById(R.id.nick_name);
        startEndDateTime = itemView.findViewById(R.id.date_time);
    }
}
