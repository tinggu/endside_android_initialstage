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
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.activity.NoticeDescActivity;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;
import com.ctfww.module.user.entity.UserInfo;


import java.util.List;


public class UserNoticeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "UserNoticeListAdapter";

    private List<NoticeInfo> list;
    private Context context;
    private UserInfo selfInfo;
    private String groupId;

    public UserNoticeListAdapter(List<NoticeInfo> list, Context context) {
        this.list = list;
        this.context = context;
        groupId = SPStaticUtils.getString("working_group_id");
        selfInfo = DBQuickEntry.getSelfInfo();
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
        else if (noticeInfo.getType() == 3) {
            ((UserNoticeViewHolder)holder).tittle.setText("你的邀请被拒绝");
        }
        else {
            ((UserNoticeViewHolder)holder).tittle.setText(noticeInfo.getTittle());
        }

        if (TextUtils.isEmpty(groupId) || selfInfo == null) {
            return;
        }

        NoticeReadStatus readStatus = DBHelper.getInstance().getNoticeReadStatus(noticeInfo.getNoticeId(), selfInfo.getUserId());
        if (readStatus == null) {
            readStatus = new NoticeReadStatus();
            readStatus.setGroupId(groupId);
            readStatus.setNoticeId(noticeInfo.getNoticeId());
            readStatus.setUserId(selfInfo.getUserId());
            readStatus.setTimeStamp(System.currentTimeMillis());
            readStatus.setFlag(1);
            readStatus.combieId();
            readStatus.setSynTag("new");
            readStatus.setNickName(selfInfo.getNickName());
            DBHelper.getInstance().addNoticeReadStatus(readStatus);
        }
        else if (readStatus.getFlag() == 0) {
            readStatus.setFlag(1);
            noticeInfo.setTimeStamp(System.currentTimeMillis());
            noticeInfo.setSynTag("modify");
            DBHelper.getInstance().updateNotice(noticeInfo);
        }

        ((UserNoticeViewHolder)holder).readStatus.setText(getReadStatusText(readStatus.getFlag()));
        ((UserNoticeViewHolder)holder).readStatus.setTextColor(getReadStatusTextColor(readStatus.getFlag()));
        if (noticeInfo.getType() == 1 || noticeInfo.getType() == 2 || noticeInfo.getType() == 3) {
            ((UserNoticeViewHolder)holder).content.setVisibility(View.GONE);
        }
        else {
            ((UserNoticeViewHolder)holder).content.setVisibility(View.VISIBLE);
            ((UserNoticeViewHolder)holder).content.setText(noticeInfo.getContent());
        }

        UserInfo userInfo = DBHelper.getInstance().getUser(noticeInfo.getUserId());
        String nickName = userInfo == null ? "" : userInfo.getNickName();
        ((UserNoticeViewHolder)holder).nickName.setText(nickName);
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
                if (TextUtils.isEmpty(groupId) || selfInfo == null) {
                    return;
                }

                int position = holder.getAdapterPosition();
                NoticeInfo noticeInfo = list.get(position);
                NoticeReadStatus readStatus = DBHelper.getInstance().getNoticeReadStatus(noticeInfo.getNoticeId(), selfInfo.getUserId());
                if (readStatus == null) {
                    readStatus = new NoticeReadStatus();
                    readStatus.setGroupId(groupId);
                    readStatus.setNoticeId(noticeInfo.getNoticeId());
                    readStatus.setUserId(selfInfo.getUserId());
                    readStatus.setTimeStamp(System.currentTimeMillis());
                    readStatus.setFlag(2);
                    readStatus.combieId();
                    readStatus.setSynTag("new");
                    readStatus.setNickName(selfInfo.getNickName());
                    DBHelper.getInstance().addNoticeReadStatus(readStatus);
                }
                else if (readStatus.getFlag() != 2) {
                    readStatus.setFlag(2);
                    noticeInfo.setTimeStamp(System.currentTimeMillis());
                    noticeInfo.setSynTag("modify");
                    DBHelper.getInstance().updateNotice(noticeInfo);
                }

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
        });
    }

        public String getReadStatusText(int flag) {
        String ret = "未读";
        if (flag == 0) {
            ret = "未读";
        }
        else if (flag == 1) {
            ret = "未读";
        }
        else if (flag == 2) {
            ret = "已读";
        }

        return ret;
    }

    public int getReadStatusTextColor(int flag) {
        int ret = 0xFF4A4A4A;
        if (flag == 0) {
            ret = 0xFFFF0000;
        }
        else if (flag == 1) {
            ret = 0xFFFF0000;
        }
        else if (flag == 2) {
            ret = 0xFF4A4A4A;
        }

        return ret;
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
