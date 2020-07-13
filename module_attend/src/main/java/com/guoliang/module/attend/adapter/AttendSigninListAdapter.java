package com.ctfww.module.attend.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.attend.R;
import com.ctfww.module.attend.entity.AttendSigninInfo;

import java.util.List;


public class AttendSigninListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AttendSigninListAdapter";

    private Context mContext;
    private List<AttendSigninInfo> list;

    private View mV;

    public AttendSigninListAdapter(List<AttendSigninInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attend_signin_one_item, parent, false);
        mV = view;
        AttendSigninViewHolder holder = new AttendSigninViewHolder(view);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogUtils.i(TAG, "onBindViewHolder = " + position);
        AttendSigninInfo attendSigninInfo =  list.get(position);
        LogUtils.i(TAG, "onBindViewHolder: attendSigninInfo = " + attendSigninInfo.toString());
        ((AttendSigninViewHolder) holder).deskName.setText(attendSigninInfo.getDeskName());
        if ("normal".equals(attendSigninInfo.getStatus())) {
            ((AttendSigninViewHolder) holder).status.setText("正常");
            ((AttendSigninViewHolder) holder).status.setTextColor(0xFF7ED321);
        }
        else if ("abnomal".equals(attendSigninInfo.getStatus())) {
            ((AttendSigninViewHolder) holder).status.setText("异常");
            ((AttendSigninViewHolder) holder).status.setTextColor(Color.RED);
        }

        if ("excellent".equals(attendSigninInfo.getMatchLevel())) {
            ((AttendSigninViewHolder) holder).matchLevel.setText("优");
            ((AttendSigninViewHolder) holder).matchLevel.setTextColor(0xFF7ED321);
        }
        else if ("good".equals(attendSigninInfo.getMatchLevel())) {
            ((AttendSigninViewHolder) holder).matchLevel.setText("良");
            ((AttendSigninViewHolder) holder).matchLevel.setTextColor(0xFFFFC90E);
        }
        else if ("bad".equals(attendSigninInfo.getMatchLevel())) {
            ((AttendSigninViewHolder) holder).matchLevel.setText("差");
            ((AttendSigninViewHolder) holder).matchLevel.setTextColor(Color.RED);
        }
        else {
            ((AttendSigninViewHolder) holder).matchLevel.setText("无");
            ((AttendSigninViewHolder) holder).matchLevel.setTextColor(Color.GRAY);
        }

        ((AttendSigninViewHolder) holder).nickName.setText(attendSigninInfo.getNickName());
        ((AttendSigninViewHolder) holder).dateTime.setText(GlobeFun.stampToDateTime(attendSigninInfo.getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<AttendSigninInfo> keepWatchSigninList) {
        list = keepWatchSigninList;
    }

    private void setOnClickListener(AttendSigninViewHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Intent intent = new Intent(mContext, KeepWatchModifyAssignmentActivity.class);
//                String memberStr = GsonUtils.toJson(list.get(position));
//                intent.putExtra("keep_watch_assignment_member", memberStr);
//                mContext.startActivity(intent);
            }
        });
    }
}

class AttendSigninViewHolder extends RecyclerView.ViewHolder {

    View view;
    TextView deskName, status, matchLevel;
    TextView nickName, dateTime;

    AttendSigninViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskName = itemView.findViewById(R.id.attend_desk_name);
        status = itemView.findViewById(R.id.attend_status);
        matchLevel = itemView.findViewById(R.id.attend_match_level);
        nickName = itemView.findViewById(R.id.attend_nick_name);
        dateTime = itemView.findViewById(R.id.attend_date_time);
    }
}
