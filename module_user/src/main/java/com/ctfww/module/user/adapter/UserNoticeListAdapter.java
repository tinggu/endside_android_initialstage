package com.ctfww.module.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.module.user.R;
import com.ctfww.module.user.entity.NoticeInfo;


import java.util.List;


public class UserNoticeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NoticeInfo> list;

    public UserNoticeListAdapter(List<NoticeInfo> list) {
        this.list = list;
    }

    public void setList(List<NoticeInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_notice_one_item, parent, false);
        final UserNoticeViewHolder holder = new UserNoticeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoticeInfo noticeInfo = list.get(position);
        ((UserNoticeViewHolder)holder).tittle.setText(noticeInfo.getTittle());
        ((UserNoticeViewHolder)holder).readStatus.setText(noticeInfo.getReadStatusText());
        ((UserNoticeViewHolder)holder).content.setText(noticeInfo.getContent());
        ((UserNoticeViewHolder)holder).nickName.setText(noticeInfo.getNickName());
        ((UserNoticeViewHolder)holder).dateTime.setText(noticeInfo.getDateTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(final UserSendInviteViewHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.delete.getVisibility() == View.VISIBLE) {
                    holder.delete.setVisibility(View.GONE);
                }
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    private void read(final int position) {
        NoticeInfo noticeInfo = list.get(position);
        noticeInfo.setFlag(2);
    }
}

class UserNoticeViewHolder extends RecyclerView.ViewHolder {
    public TextView tittle, readStatus;
    public TextView content;
    public TextView nickName, dateTime;
    public View view;

    public UserNoticeViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        tittle = itemView.findViewById(R.id.user_notice_tittle);
        readStatus = itemView.findViewById(R.id.user_read_status);
        content = itemView.findViewById(R.id.user_notice_content);
        nickName = itemView.findViewById(R.id.user_nick_name);
        dateTime = itemView.findViewById(R.id.user_date_time);
    }
}
