package com.ctfww.module.assignment.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.datahelper.dbhelper.DBHelper;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.assignment.entity.TodayAssignment;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteDesk;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.user.entity.UserInfo;

import java.util.List;


public class TodayAssignmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "TodayAssignmentListAdapter";

    private List<TodayAssignment> list;

    public TodayAssignmentListAdapter(List<TodayAssignment> list) {
        this.list = list;
    }

    public void setList(List<TodayAssignment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_assignment_one_item, parent, false);
        TodayAssignmentViewHolder holder = new TodayAssignmentViewHolder(view);

        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TodayAssignment todayAssignment = list.get(position);
        if ("desk".equals(todayAssignment.getType())) {
            ((TodayAssignmentViewHolder)holder).deskType.setImageResource(R.drawable.desk);
            DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(todayAssignment.getGroupId(), todayAssignment.getObjectId());
//            List<DeskInfo> deskInfoList = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDeskList(todayAssignment.getGroupId())
            if (deskInfo != null) {
                ((TodayAssignmentViewHolder)holder).deskName.setText(deskInfo.getIdName());
            }
        }
        else if ("route".equals(todayAssignment.getType())) {
            ((TodayAssignmentViewHolder)holder).deskType.setImageResource(R.drawable.route);
            RouteSummary routeSummary = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(todayAssignment.getGroupId(), todayAssignment.getObjectId());
            if (routeSummary != null) {
                ((TodayAssignmentViewHolder)holder).deskName.setText(routeSummary.getIdName());
            }
        }


        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(todayAssignment.getUserId());
        if (userInfo != null) {
            ((TodayAssignmentViewHolder)holder).nickName.setText(userInfo.getNickName());
        }

        ((TodayAssignmentViewHolder)holder).frequency.setText("" + todayAssignment.getFrequency() + "次");

        long startHour = todayAssignment.getStartTime() / 3600l / 1000l;
        long startMinute = (todayAssignment.getStartTime() - startHour * 3600l * 1000l) / 60 / 1000;
        long endHour = todayAssignment.getEndTime() / 3600l / 1000l;
        long endMinute = (todayAssignment.getEndTime() - endHour * 3600l * 1000l) / 60 / 1000;
        String startEndTime = String.format("%02d:%02d——%02d:%02d", startHour, startMinute, endHour, endMinute);
        ((TodayAssignmentViewHolder)holder).startEndDateTime.setText(startEndTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(TodayAssignmentViewHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                TodayAssignment todayAssignment = list.get(position);
                if ("desk".equals(todayAssignment.getType())) {
                    DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(todayAssignment.getGroupId(), todayAssignment.getObjectId());
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
                else if ("route".equals(todayAssignment.getType())) {
                    List<RouteDesk> routeDeskList = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteDeskInOneRoute(todayAssignment.getGroupId(), todayAssignment.getObjectId());
                    if (!routeDeskList.isEmpty()) {
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
                }

            }
        });
    }

    private String getAppend(String old, String append) {
        return TextUtils.isEmpty(old) ? append : "、" + append;
    }
}

class TodayAssignmentViewHolder extends RecyclerView.ViewHolder {
    public ImageView deskType;
    public TextView deskName, frequency;
    public TextView nickName, startEndDateTime;
    public View view;

    public TodayAssignmentViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskType = itemView.findViewById(R.id.desk_type);
        deskName = itemView.findViewById(R.id.desk_name);
        frequency = itemView.findViewById(R.id.frequency);
        nickName = itemView.findViewById(R.id.nick_name);
        startEndDateTime = itemView.findViewById(R.id.date_time);
    }
}
