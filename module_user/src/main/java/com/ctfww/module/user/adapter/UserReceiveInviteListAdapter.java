package com.ctfww.module.user.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.GroupInviteInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class UserReceiveInviteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GroupInviteInfo> list;

    public UserReceiveInviteListAdapter(List<GroupInviteInfo> list) {
        this.list = list;
    }

    public void setList(List<GroupInviteInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_receive_invite_one_item, parent, false);
        UserReceiveInviteViewHolder holder = new UserReceiveInviteViewHolder(view);
        setOnClickListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupInviteInfo groupInviteInfo = list.get(position);

        ((UserReceiveInviteViewHolder)holder).group.setText(groupInviteInfo.getGroupName());
        if ("send".equals(groupInviteInfo.getStatus())) {
            ((UserReceiveInviteViewHolder)holder).status.setVisibility(View.GONE);
            ((UserReceiveInviteViewHolder)holder).actionLL.setVisibility(View.VISIBLE);
        }
        else if ("accept".equals(groupInviteInfo.getStatus())) {
            ((UserReceiveInviteViewHolder)holder).status.setText("已接受");
            ((UserReceiveInviteViewHolder)holder).status.setVisibility(View.VISIBLE);
            ((UserReceiveInviteViewHolder)holder).actionLL.setVisibility(View.GONE);
        }
        else if ("refuse".equals(groupInviteInfo.getStatus())) {
            ((UserReceiveInviteViewHolder)holder).status.setText("已拒绝");
            ((UserReceiveInviteViewHolder)holder).status.setVisibility(View.VISIBLE);
            ((UserReceiveInviteViewHolder)holder).actionLL.setVisibility(View.GONE);
        }

        ((UserReceiveInviteViewHolder)holder).nickName.setText(groupInviteInfo.getFromNickName());
        ((UserReceiveInviteViewHolder)holder).mobile.setText(groupInviteInfo.getFromMobile());

        ((UserReceiveInviteViewHolder)holder).dateTime.setText(GlobeFun.stampToDateTime(groupInviteInfo.getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(final UserReceiveInviteViewHolder holder) {
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
                GroupInviteInfo groupInviteInfo = list.get(position);
                groupInviteInfo.setStatus("delete");
                groupInviteInfo.setTimeStamp(System.currentTimeMillis());
                groupInviteInfo.setSynTag("modify");
                DBHelper.getInstance().updateInvite(groupInviteInfo);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                GroupInviteInfo groupInviteInfo = list.get(position);
                groupInviteInfo.setStatus("accept");
                groupInviteInfo.setTimeStamp(System.currentTimeMillis());
                groupInviteInfo.setSynTag("modify");
                DBHelper.getInstance().updateInvite(groupInviteInfo);

                String groupId = SPStaticUtils.getString("working_group_id");
                if (TextUtils.isEmpty(groupId)) {
                    SPStaticUtils.put("working_group_id", groupInviteInfo.getGroupId());
                    EventBus.getDefault().post(new MessageEvent("bind_group"));
                }
            }
        });

        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                GroupInviteInfo groupInviteInfo = list.get(position);
                groupInviteInfo.setStatus("refuse");
                groupInviteInfo.setTimeStamp(System.currentTimeMillis());
                groupInviteInfo.setSynTag("modify");
                DBHelper.getInstance().updateInvite(groupInviteInfo);
            }
        });
    }
}

class UserReceiveInviteViewHolder extends RecyclerView.ViewHolder {
    public TextView group, status, delete;
    public TextView nickName, mobile;
    public TextView dateTime;
    public LinearLayout actionLL;
    public TextView accept, refuse;
    public View view;

    public UserReceiveInviteViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        group = itemView.findViewById(R.id.user_invite_group);
        status = itemView.findViewById(R.id.user_invite_status);
        delete = itemView.findViewById(R.id.user_invite_delete);
        nickName = itemView.findViewById(R.id.user_invite_nick_name);
        mobile = itemView.findViewById(R.id.user_invite_mobile);
        dateTime = itemView.findViewById(R.id.user_invite_date_time);
        actionLL = itemView.findViewById(R.id.user_invite_action_ll);
        accept = itemView.findViewById(R.id.user_invite_accept);
        refuse = itemView.findViewById(R.id.user_invite_refuse);
    }
}
