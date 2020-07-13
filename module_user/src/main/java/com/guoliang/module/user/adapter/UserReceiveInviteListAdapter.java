package com.guoliang.module.user.adapter;

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
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.module.user.R;
import com.guoliang.module.user.datahelper.NetworkHelper;
import com.guoliang.module.user.entity.GroupInviteInfo;

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
                delete(position);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                accept(list.get(position));
            }
        });

        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                refuse(list.get(position));
            }
        });
    }

    private void accept(final GroupInviteInfo groupInviteInfo) {
        NetworkHelper.getInstance().updateInviteStatus(groupInviteInfo.getInviteId(), "accept", new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                groupInviteInfo.setStatus("accept");
                EventBus.getDefault().post(new MessageEvent("user_update_receive_invite", GsonUtils.toJson(groupInviteInfo)));

                String groupId = SPStaticUtils.getString("working_group_id");
                if (TextUtils.isEmpty(groupId)) {
                    SPStaticUtils.put("working_group_id", groupInviteInfo.getGroupId());
                    SPStaticUtils.put("working_group_name", groupInviteInfo.getGroupName());
                    SPStaticUtils.put("role", "member");
                    EventBus.getDefault().post(new MessageEvent("bind_group"));
                }
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("接受失败，请检查网络是否正常！");
            }
        });
    }

    private void refuse(final GroupInviteInfo groupInviteInfo) {
        NetworkHelper.getInstance().updateInviteStatus(groupInviteInfo.getInviteId(), "refuse", new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                groupInviteInfo.setStatus("refuse");
                EventBus.getDefault().post(new MessageEvent("user_update_receive_invite", GsonUtils.toJson(groupInviteInfo)));
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("拒绝失败，请检查网络是否正常！");
            }
        });
    }

    private void delete(final int position) {
        NetworkHelper.getInstance().deleteInvite(list.get(position).getInviteId(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                EventBus.getDefault().post(new MessageEvent("user_delete_receive_invite", "" + position));
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("删除失败，请检查网络是否正常！");
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
