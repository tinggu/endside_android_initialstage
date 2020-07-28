package com.ctfww.module.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.module.user.R;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;

import java.util.Calendar;
import java.util.List;


public class UserNoticeReadStatusListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NoticeReadStatus> list;

    public UserNoticeReadStatusListAdapter(List<NoticeReadStatus> list) {
        this.list = list;
    }

    public void setList(List<NoticeReadStatus> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_notice_read_status_one_item, parent, false);
        final UserNoticeReadStatusViewHolder holder = new UserNoticeReadStatusViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoticeReadStatus noticeReadStatus = list.get(position);
        ((UserNoticeReadStatusViewHolder)holder).nickName.setText(noticeReadStatus.getNickName());
        if (noticeReadStatus.getFlag() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(noticeReadStatus.getTimeStamp());
            String dateTime = String.format("%04d-%02d-%02d %02d:%02d:%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            ((UserNoticeReadStatusViewHolder)holder).dateTime.setText(dateTime);
        }
        ((UserNoticeReadStatusViewHolder)holder).status.setText(getStatusStr(noticeReadStatus.getFlag()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String getStatusStr(int flag) {
        String ret = "未读";
        if (flag == 0) {
            ret = "未读";
        }
        else if (flag == 1) {
            ret = "已查看";
        }
        else if (flag == 2) {
            ret = "已阅读";
        }

        return ret;
    }
}

class UserNoticeReadStatusViewHolder extends RecyclerView.ViewHolder {
    public TextView nickName, dateTime, status;
    public View view;

    public UserNoticeReadStatusViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        nickName = itemView.findViewById(R.id.user_nick_name);
        dateTime = itemView.findViewById(R.id.user_date_time);
        status = itemView.findViewById(R.id.user_read_status);
    }
}
