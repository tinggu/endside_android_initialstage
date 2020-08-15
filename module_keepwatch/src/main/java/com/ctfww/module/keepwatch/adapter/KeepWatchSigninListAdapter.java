package com.ctfww.module.keepwatch.adapter;

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
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;

import java.util.List;


public class KeepWatchSigninListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "KeepWatchSigninListAdapter";

    private Context mContext;
    private List<KeepWatchSigninInfo> list;

    private View mV;

    public KeepWatchSigninListAdapter(List<KeepWatchSigninInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_signin_one_item, parent, false);
        mV = view;
        KeepWatchSigninViewHolder holder = new KeepWatchSigninViewHolder(view);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogUtils.i(TAG, "onBindViewHolder = " + position);
        KeepWatchSigninInfo keepWatchSigninInfo =  list.get(position);
        LogUtils.i(TAG, "onBindViewHolder: keepWatchSigninInfo = " + keepWatchSigninInfo.toString());
        ((KeepWatchSigninViewHolder) holder).deskName.setText("[" + keepWatchSigninInfo.getDeskId() + "] " + keepWatchSigninInfo.getDeskName());

        if ("excellent".equals(keepWatchSigninInfo.getMatchLevel())) {
            ((KeepWatchSigninViewHolder) holder).matchLevel.setText("优");
            ((KeepWatchSigninViewHolder) holder).matchLevel.setTextColor(0xFF7ED321);
        }
        else if ("good".equals(keepWatchSigninInfo.getMatchLevel())) {
            ((KeepWatchSigninViewHolder) holder).matchLevel.setText("良");
            ((KeepWatchSigninViewHolder) holder).matchLevel.setTextColor(0xFFFFC90E);
        }
        else if ("bad".equals(keepWatchSigninInfo.getMatchLevel())) {
            ((KeepWatchSigninViewHolder) holder).matchLevel.setText("差");
            ((KeepWatchSigninViewHolder) holder).matchLevel.setTextColor(0xFFF65066);
        }
        else {
            ((KeepWatchSigninViewHolder) holder).matchLevel.setText("无");
            ((KeepWatchSigninViewHolder) holder).matchLevel.setTextColor(Color.GRAY);
        }

        ((KeepWatchSigninViewHolder) holder).nickName.setText(keepWatchSigninInfo.getNickName());
        ((KeepWatchSigninViewHolder) holder).dateTime.setText(GlobeFun.stampToDateTime(keepWatchSigninInfo.getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<KeepWatchSigninInfo> keepWatchSigninList) {
        list = keepWatchSigninList;
    }

    private void setOnClickListener(KeepWatchSigninViewHolder holder) {
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

class KeepWatchSigninViewHolder extends RecyclerView.ViewHolder {

    View view;
    TextView deskName, matchLevel;
    TextView nickName, dateTime;

    KeepWatchSigninViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        matchLevel = itemView.findViewById(R.id.keepwatch_match_level);
        nickName = itemView.findViewById(R.id.keepwatch_nick_name);
        dateTime = itemView.findViewById(R.id.keepwatch_date_time);
    }
}
