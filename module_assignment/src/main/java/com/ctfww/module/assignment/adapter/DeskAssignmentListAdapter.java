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
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.datahelper.airship.Airship;
import com.ctfww.module.assignment.datahelper.dbhelper.DBHelper;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.user.entity.UserInfo;

import java.util.List;


public class DeskAssignmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "DeskAssignmentListAdapter";

    private List<DeskAssignment> list;

    public DeskAssignmentListAdapter(List<DeskAssignment> list) {
        this.list = list;
    }

    public void setList(List<DeskAssignment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_one_item, parent, false);
        DeskAssignmentViewHolder holder = new DeskAssignmentViewHolder(view);

        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeskAssignment deskAssignment = list.get(position);
        LogUtils.i("bbbbbbbbb", "deskAssignment = " + deskAssignment.toString());
        ((DeskAssignmentViewHolder)holder).deskType.setImageResource(R.drawable.desk);
        DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(deskAssignment.getGroupId(), deskAssignment.getDeskId());
        if (deskInfo != null) {
            ((DeskAssignmentViewHolder)holder).deskName.setText(deskInfo.getIdName());
        }

        ((DeskAssignmentViewHolder)holder).circleType.setText(toChineseCircleType(deskAssignment.getCircleType()));
        ((DeskAssignmentViewHolder)holder).frequency.setText("" + deskAssignment.getFrequency() + "次");

        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(deskAssignment.getUserId());
        if (userInfo != null) {
            ((DeskAssignmentViewHolder)holder).nickName.setText(userInfo.getNickName());
        }

        long startHour = deskAssignment.getStartTime() / 3600l / 1000l;
        long startMinute = (deskAssignment.getStartTime() - startHour * 3600l * 1000l) / 60 / 1000;
        long endHour = deskAssignment.getEndTime() / 3600l / 1000l;
        long endMinute = (deskAssignment.getEndTime() - endHour * 3600l * 1000l) / 60 / 1000;
        String startEndTime = String.format("%02d:%02d——%02d:%02d", startHour, startMinute, endHour, endMinute);
        ((DeskAssignmentViewHolder)holder).startEndDateTime.setText(startEndTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(DeskAssignmentViewHolder holder) {
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.delete.getVisibility() == View.VISIBLE) {
                    holder.delete.setVisibility(View.GONE);
                    return;
                }

                int position = holder.getAdapterPosition();
                DeskAssignment deskAssignment = list.get(position);
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.delete.setVisibility(View.GONE);
                int position = holder.getAdapterPosition();
                DeskAssignment deskAssignment = list.get(position);
                deskAssignment.setStatus("delete");
                deskAssignment.setTimeStamp(System.currentTimeMillis());
                deskAssignment.setSynTag("modify");
                DBHelper.getInstance().updateDeskAssignment(deskAssignment);
                DBHelper.getInstance().updateDeskTodayAssignment(deskAssignment);
                Airship.getInstance().synDeskAssignmentToCloud();

                list.remove(position);
                notifyDataSetChanged();
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

class DeskAssignmentViewHolder extends RecyclerView.ViewHolder {
    public ImageView deskType;
    public TextView deskName, frequency, delete;
    public LinearLayout circleTypeLL;
    public TextView circleType;
    public TextView nickName, startEndDateTime;
    public View view;

    public DeskAssignmentViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskType = itemView.findViewById(R.id.desk_type);
        deskName = itemView.findViewById(R.id.desk_name);
        frequency = itemView.findViewById(R.id.frequency);
        delete = itemView.findViewById(R.id.assignment_delete);
        circleTypeLL = itemView.findViewById(R.id.circle_type_ll);
        circleType = itemView.findViewById(R.id.circle_type);
        nickName = itemView.findViewById(R.id.nick_name);
        startEndDateTime = itemView.findViewById(R.id.date_time);
    }
}
