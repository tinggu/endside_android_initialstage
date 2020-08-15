package com.ctfww.module.keepwatch.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.keepwatch.DataHelper.airship.Airship;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.activity.ViewMapActivity;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class KeepWatchAssignmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "KeepWatchAssignmentListAdapter";

    private List<KeepWatchAssignment> list;
    private String  mType;

    public KeepWatchAssignmentListAdapter(List<KeepWatchAssignment> list, String type) {
        this.list = list;
        mType = type;
    }

    public void setList(List<KeepWatchAssignment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_assignment_one_item, parent, false);
        KeepWatchAssignmentViewHolder holder = new KeepWatchAssignmentViewHolder(view);

        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeepWatchAssignment keepWatchAssignment = list.get(position);
        ((KeepWatchAssignmentViewHolder)holder).deskName.setText("[" + keepWatchAssignment.getDeskId() + "]" + "  " + keepWatchAssignment.getDeskName());
        ((KeepWatchAssignmentViewHolder)holder).circleType.setText(toChineseCircleType(keepWatchAssignment.getCircleType()));
        if (!"period".equals(mType)) {
            ((KeepWatchAssignmentViewHolder)holder).circleTypeLL.setVisibility(View.GONE);
        }
        ((KeepWatchAssignmentViewHolder)holder).frequency.setText("" + keepWatchAssignment.getFrequency() + "次");
        ((KeepWatchAssignmentViewHolder)holder).nickName.setText(keepWatchAssignment.getNickName());
        long startHour = keepWatchAssignment.getStartTime() / 3600l / 1000l;
        long startMinute = (keepWatchAssignment.getStartTime() - startHour * 3600l * 1000l) / 60 / 1000;
        long endHour = keepWatchAssignment.getEndTime() / 3600l / 1000l;
        long endMinute = (keepWatchAssignment.getEndTime() - endHour * 3600l * 1000l) / 60 / 1000;
        String startEndTime = String.format("%02d:%02d——%02d:%02d", startHour, startMinute, endHour, endMinute);
        ((KeepWatchAssignmentViewHolder)holder).startEndDateTime.setText(startEndTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(KeepWatchAssignmentViewHolder holder) {
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
                KeepWatchAssignment keepWatchAssignment = list.get(position);
                String groupId = SPStaticUtils.getString("working_group_id");
                KeepWatchDesk keepWatchDesk = DBHelper.getInstance().getKeepWatchDesk(groupId, keepWatchAssignment.getDeskId());
                if (keepWatchDesk != null) {
                    Intent intent = new Intent(v.getContext(), ViewMapActivity.class);
                    intent.putExtra("type", "center");
                    intent.putExtra("lat", keepWatchDesk.getLat());
                    intent.putExtra("lng", keepWatchDesk.getLng());
                    intent.putExtra("name", keepWatchDesk.getDeskName());
                    intent.putExtra("address", keepWatchDesk.getDeskAddress());
                    v.getContext().startActivity(intent);
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.delete.setVisibility(View.GONE);
                int position = holder.getAdapterPosition();
                deleteData(position);
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

    public void deleteData(int position) {
        KeepWatchAssignment keepWatchAssignment = list.get(position);
        DBHelper.getInstance().deleteKeepWatchAssignment(keepWatchAssignment);

        list.remove(position);
        notifyDataSetChanged();
    }
}

class KeepWatchAssignmentViewHolder extends RecyclerView.ViewHolder {
    public TextView deskName, frequency, delete;
    public LinearLayout circleTypeLL;
    public TextView circleType;
    public TextView nickName, startEndDateTime;
    public View view;

    public KeepWatchAssignmentViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        frequency = itemView.findViewById(R.id.keepwatch_frequency);
        delete = itemView.findViewById(R.id.keepwatch_assignment_delete);
        circleTypeLL = itemView.findViewById(R.id.keepwatch_circle_type_ll);
        circleType = itemView.findViewById(R.id.keepwatch_circle_type);
        nickName = itemView.findViewById(R.id.keepwatch_nick_name);
        startEndDateTime = itemView.findViewById(R.id.keepwatch_date_time);
    }
}
