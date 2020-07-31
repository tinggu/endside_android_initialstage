package com.ctfww.module.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.R;
import com.ctfww.module.user.activity.NoticeDescActivity;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;


import java.util.List;


public class UserNoticeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "UserNoticeListAdapter";

    private List<NoticeInfo> list;
    private Context context;

    public UserNoticeListAdapter(List<NoticeInfo> list, Context context) {
        this.list = list;
        this.context = context;
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
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoticeInfo noticeInfo = list.get(position);
        if (noticeInfo.getType() == 1) {
            ((UserNoticeViewHolder)holder).tittle.setText("你有邀请");
        }
        else if (noticeInfo.getType() == 2) {
            ((UserNoticeViewHolder)holder).tittle.setText("你的邀请被接受");
        }
        else if (noticeInfo.getType() == 2) {
            ((UserNoticeViewHolder)holder).tittle.setText("你的邀请被拒绝");
        }
        else {
            ((UserNoticeViewHolder)holder).tittle.setText(noticeInfo.getTittle());
        }

        ((UserNoticeViewHolder)holder).readStatus.setText(noticeInfo.getReadStatusText());
        ((UserNoticeViewHolder)holder).readStatus.setTextColor(noticeInfo.getReadStatusTextColor());
        if (noticeInfo.getType() == 1 || noticeInfo.getType() == 2 || noticeInfo.getType() == 3) {
            ((UserNoticeViewHolder)holder).content.setVisibility(View.GONE);
        }
        else {
            ((UserNoticeViewHolder)holder).content.setVisibility(View.VISIBLE);
            ((UserNoticeViewHolder)holder).content.setText(noticeInfo.getContent());
        }
        ((UserNoticeViewHolder)holder).nickName.setText(noticeInfo.getNickName());
        ((UserNoticeViewHolder)holder).dateTime.setText(noticeInfo.getDateTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(final UserNoticeViewHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                NoticeInfo noticeInfo = list.get(position);
                addNoticeReadStatus(noticeInfo);
            }
        });
    }

    private void addNoticeReadStatus(final NoticeInfo noticeInfo) {
        String userId = SPStaticUtils.getString("user_open_id");
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        NetworkHelper.getInstance().addNoticeReadStatus(noticeInfo.getNoticeId(), 2, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                noticeInfo.setFlag(2);
                notifyDataSetChanged();
                if (noticeInfo.getType() == 1 || noticeInfo.getType() == 2 || noticeInfo.getType() == 3) {
                    ARouter.getInstance().build("/user/invite").navigation();
                }
                else {
                    Intent intent = new Intent(context, NoticeDescActivity.class);
                    intent.putExtra("notice_info", GsonUtils.toJson(noticeInfo));
                    context.startActivity(intent);
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "addNoticeReadStatus fail: code = " + code);
            }
        });
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
