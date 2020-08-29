package com.ctfww.module.assignment.adapter;

import android.content.Intent;
import android.net.RouteInfo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.datahelper.airship.Airship;
import com.ctfww.module.assignment.datahelper.dbhelper.DBHelper;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class AssignmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "KeepWatchAssignmentListAdapter";

    private List<AssignmentInfo> list;
    private String  mType;

    public AssignmentListAdapter(List<AssignmentInfo> list, String type) {
        this.list = list;
        mType = type;
    }

    public void setList(List<AssignmentInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_one_item, parent, false);
        AssignmentViewHolder holder = new AssignmentViewHolder(view);

        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AssignmentInfo assignmentInfo = list.get(position);

        if (assignmentInfo.getDeskId() == 0) {
            ((AssignmentViewHolder)holder).deskType.setImageResource(R.drawable.route);
            RouteSummary route = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(assignmentInfo.getRouteId());
            if (route != null) {
                ((AssignmentViewHolder)holder).deskName.setText(route.getRouteName());
            }
        }
        else {
            ((AssignmentViewHolder)holder).deskType.setImageResource(R.drawable.desk);
            DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(assignmentInfo.getGroupId(), assignmentInfo.getDeskId());
            if (deskInfo != null) {
                ((AssignmentViewHolder)holder).deskName.setText(deskInfo.getIdName());
            }
        }

        ((AssignmentViewHolder)holder).circleType.setText(toChineseCircleType(assignmentInfo.getCircleType()));
        if (!"period".equals(mType)) {
            ((AssignmentViewHolder)holder).circleTypeLL.setVisibility(View.GONE);
        }
        ((AssignmentViewHolder)holder).frequency.setText("" + assignmentInfo.getFrequency() + "次");

        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(assignmentInfo.getUserId());
        if (userInfo != null) {
            ((AssignmentViewHolder)holder).nickName.setText(userInfo.getNickName());
        }

        long startHour = assignmentInfo.getStartTime() / 3600l / 1000l;
        long startMinute = (assignmentInfo.getStartTime() - startHour * 3600l * 1000l) / 60 / 1000;
        long endHour = assignmentInfo.getEndTime() / 3600l / 1000l;
        long endMinute = (assignmentInfo.getEndTime() - endHour * 3600l * 1000l) / 60 / 1000;
        String startEndTime = String.format("%02d:%02d——%02d:%02d", startHour, startMinute, endHour, endMinute);
        ((AssignmentViewHolder)holder).startEndDateTime.setText(startEndTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(AssignmentViewHolder holder) {
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
                AssignmentInfo assignmentInfo = list.get(position);
                if (assignmentInfo.getDeskId() == 0) {
                    EventBus.getDefault().post(new MessageEvent("view_route_on_map", assignmentInfo.getRouteId()));
                }
                else {
                    EventBus.getDefault().post(new MessageEvent("view_route_on_map", "" + assignmentInfo.getDeskId()));
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.delete.setVisibility(View.GONE);
                int position = holder.getAdapterPosition();
                AssignmentInfo assignmentInfo = list.get(position);
                assignmentInfo.setStatus("delete");
                assignmentInfo.setTimeStamp(System.currentTimeMillis());
                assignmentInfo.setSynTag("modify");
                DBHelper.getInstance().updateAssignment(assignmentInfo);
                Airship.getInstance().synAssignmentToCloud();

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

class AssignmentViewHolder extends RecyclerView.ViewHolder {
    public ImageView deskType;
    public TextView deskName, frequency, delete;
    public LinearLayout circleTypeLL;
    public TextView circleType;
    public TextView nickName, startEndDateTime;
    public View view;

    public AssignmentViewHolder(View itemView) {
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
