package com.ctfww.module.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.GroupInviteInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class UserSendInviteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GroupInviteInfo> list;

    public UserSendInviteListAdapter(List<GroupInviteInfo> list) {
        this.list = list;
    }

    public void setList(List<GroupInviteInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_send_invite_one_item, parent, false);
        final UserSendInviteViewHolder holder = new UserSendInviteViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupInviteInfo groupInviteInfo = list.get(position);
        ((UserSendInviteViewHolder)holder).group.setText(groupInviteInfo.getGroupName());
        if ("send".equals(groupInviteInfo.getStatus())) {
            ((UserSendInviteViewHolder)holder).status.setText("等待...");
        }
        else if ("accept".equals(groupInviteInfo.getStatus())) {
            ((UserSendInviteViewHolder)holder).status.setText("已接受");
        }
        else if ("refuse".equals(groupInviteInfo.getStatus())) {
            ((UserSendInviteViewHolder)holder).status.setText("已拒绝");
        }

        ((UserSendInviteViewHolder)holder).nickName.setText(groupInviteInfo.getToNickName());
        ((UserSendInviteViewHolder)holder).mobile.setText(groupInviteInfo.getToMobile());

        ((UserSendInviteViewHolder)holder).dateTime.setText(GlobeFun.stampToDateTime(groupInviteInfo.getTimeStamp()));
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                holder.delete.setVisibility(View.GONE);
                GroupInviteInfo inviteInfo = list.get(position);
                inviteInfo.setStatus("delete");
                inviteInfo.setSynTag("modify");
                inviteInfo.setTimeStamp(System.currentTimeMillis());

                DBHelper.getInstance().updateInvite(inviteInfo);
            }
        });
    }
}

class UserSendInviteViewHolder extends RecyclerView.ViewHolder {
    public TextView group, status, delete;
    public TextView nickName, mobile;
    public TextView dateTime;
    public View view;

    public UserSendInviteViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        group = itemView.findViewById(R.id.user_invite_group);
        status = itemView.findViewById(R.id.user_invite_status);
        delete = itemView.findViewById(R.id.user_invite_delete);
        nickName = itemView.findViewById(R.id.user_invite_nick_name);
        mobile = itemView.findViewById(R.id.user_invite_mobile);
        dateTime = itemView.findViewById(R.id.user_invite_date_time);
    }
}
