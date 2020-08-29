package com.ctfww.module.keyevents.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.user.entity.UserInfo;

import java.util.Calendar;
import java.util.List;


public class KeyEventTraceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeyEventTrace> list;

    public KeyEventTraceListAdapter(List<KeyEventTrace> list) {
        this.list = list;
    }

    public void setList(List<KeyEventTrace> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keyevent_trace_one_item, parent, false);
        final KeyEventTraceViewHolder holder = new KeyEventTraceViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeyEventTrace keyEventTrace = list.get(position);
        ((KeyEventTraceViewHolder) holder).contentLL.setVisibility(View.VISIBLE);
        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keyEventTrace.getUserId());
        String headUrl = userInfo == null ? "" : userInfo.getHeadUrl();
        if (TextUtils.isEmpty(headUrl)) {
            (((KeyEventTraceViewHolder) holder).head).setImageResource(R.drawable.default_head);
        }
        else {
            Glide.with(((KeyEventTraceViewHolder) holder).view).load(headUrl).into(((KeyEventTraceViewHolder) holder).head);
        }

        String nickName = userInfo == null ? "" : userInfo.getNickName();
        ((KeyEventTraceViewHolder) holder).nickName.setText(nickName);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(keyEventTrace.getTimeStamp());
        ((KeyEventTraceViewHolder) holder).monthDay.setText(String.format("%02d-%02d", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        ((KeyEventTraceViewHolder) holder).hourMinute.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        if ("create".equals(keyEventTrace.getStatus())) {
            ((KeyEventTraceViewHolder) holder).status.setText("上报事件");
        }
        else if ("snatch".equals(keyEventTrace.getStatus())) {
            ((KeyEventTraceViewHolder) holder).status.setText("抢单成功");
        }
        else if ("free".equals(keyEventTrace.getStatus())) {
            ((KeyEventTraceViewHolder) holder).status.setText("释放工单");
        }
        else if ("received".equals(keyEventTrace.getStatus())) {
            if (position <= 3) {
                ((KeyEventTraceViewHolder) holder).status.setText("管理员已分配，责任人领取任务中");
            }
            else {
                ((KeyEventTraceViewHolder) holder).status.setText("任务被转移，新责任人领取任务中");
            }
        }
        else if ("accepted".equals(keyEventTrace.getStatus())) {
            if (position <= 5) {
                ((KeyEventTraceViewHolder) holder).status.setText("责任人已领取任务");
            }
            else {
                ((KeyEventTraceViewHolder) holder).status.setText("新的责任人已领取任务");
            }
        }
        else if ("end".equals(keyEventTrace.getStatus())) {
            ((KeyEventTraceViewHolder) holder).status.setText("事件已经处理");
            ((KeyEventTraceViewHolder) holder).line.setVisibility(View.GONE);
            showMatchLevel(keyEventTrace.getMatchLevel(), (KeyEventTraceViewHolder) holder);
        }
        else {
            ((KeyEventTraceViewHolder) holder).contentLL.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showMatchLevel(String matchLevel, KeyEventTraceViewHolder holder) {
        if (TextUtils.isEmpty(matchLevel)) {
            return;
        }

        if ("excellent".equals(matchLevel)) {
            holder.status.setTextColor(0xFF7ED321);
        }
        else if ("good".equals(matchLevel)) {
            holder.status.setTextColor(0xFFFFC90E);
        }
        else if ("bad".equals(matchLevel)) {
            holder.status.setTextColor(0xFFF65066);
        }
    }

}

class KeyEventTraceViewHolder extends RecyclerView.ViewHolder {
    public TextView monthDay, hourMinute;
    public LinearLayout contentLL;
    public View line;
    public ImageView head;
    public TextView nickName, status;
    public View view;

    public KeyEventTraceViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        monthDay = itemView.findViewById(R.id.keyevent_month_day);
        hourMinute = itemView.findViewById(R.id.keyevent_hour_minute);
        contentLL = itemView.findViewById(R.id.keyevent_content_ll);
        line = itemView.findViewById(R.id.keyevent_line);
        head = itemView.findViewById(R.id.keyevent_head);
        nickName = itemView.findViewById(R.id.keyevent_nick_name);
        status = itemView.findViewById(R.id.keyevent_status);
    }
}
