package com.ctfww.module.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.activity.GroupActivity;
import com.ctfww.module.user.entity.GroupUserInfo;

import java.util.List;


public class User2GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GroupUserInfo> list;
    private GroupUserInfo mSelfGroupUserInfo = new GroupUserInfo();
    private Context mContext;

    public User2GroupAdapter(List<GroupUserInfo> list, Context context) {
        this.list = list;
        updateSelfGroupUserInfo(this.list);
        mContext = context;
    }

    public void setList(List<GroupUserInfo> list) {
        this.list = list;
        updateSelfGroupUserInfo(this.list);
    }

    private void updateSelfGroupUserInfo(List<GroupUserInfo> list) {
        String userId = SPStaticUtils.getString("user_open_id");
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).getUserId().equals(userId)) {
                mSelfGroupUserInfo = list.get(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_group_member_one_item, parent, false);
        final User2GroupViewHolder holder = new User2GroupViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (holder.delete.getVisibility() == View.VISIBLE) {
                    holder.delete.setVisibility(View.GONE);
                    return;
                }

//                ((GroupActivity)mContext).showGroupUserInfoActivity(list.get(position));
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!"admin".equals(mSelfGroupUserInfo.getRole())) {
                    return true;
                }

                int position = holder.getAdapterPosition();

                // 如果点击选择到自己，没有任何反应
                if (mSelfGroupUserInfo.getUserId() == list.get(position).getUserId()) {
                    return true;
                }

                holder.delete.setVisibility(View.VISIBLE);

                return true;
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                GroupUserInfo groupUserInfo = list.get(position);
                ((GroupActivity)mContext).deleteMember(groupUserInfo.getUserId());
            }
        });

        holder.roleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = SPStaticUtils.getString("role");
                if (!"admin".equals(role)) {
                    return;
                }

                int position = holder.getAdapterPosition();
                final GroupUserInfo groupUserInfo = list.get(position);
                if ("admin".equals(groupUserInfo.getRole())) {
                    return;
                }

                String[] strArr = {"管理员", "普通成员", "维修员"};
                DialogUtils.radioDialog(strArr, getCheckIndex(groupUserInfo.getRole()), "更换角色", mContext, new DialogUtils.Callback() {
                    @Override
                    public void onConfirm(int radioSelectItem) {
                        final String role = getRole(radioSelectItem);
                        if (role.equals(groupUserInfo.getRole())) {
                            return;
                        }

                        ((GroupActivity)mContext).updateRole(groupUserInfo.getUserId(), role);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupUserInfo groupUserInfo = list.get(position);
        ((User2GroupViewHolder)holder).headImg.setImageResource(R.mipmap.user_group_member_head);
        ((User2GroupViewHolder)holder).userName.setText(groupUserInfo.getNickName());
        ((User2GroupViewHolder)holder).mobile.setText(groupUserInfo.getMobile());
        if ("admin".equals(groupUserInfo.getRole())) {
            ((User2GroupViewHolder)holder).role.setImageResource(R.drawable.user_admin);
        }
        else if ("member".equals(groupUserInfo.getRole())) {
            ((User2GroupViewHolder)holder).role.setImageResource(R.drawable.user_member);
        }
        else if ("repair".equals(groupUserInfo.getRole())) {
            ((User2GroupViewHolder)holder).role.setImageResource(R.drawable.user_repair);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private int getCheckIndex(String role) {
        int index = 0;
        if ("admin".equals(role)) {
            index = 0;
        }
        else if ("member".equals(role)) {
            index = 1;
        }
        else if ("repair".equals(role)) {
            index = 2;
        }

        return index;
    }

    private String getRole(int checkIndex) {
        String role = "admin";
        if (checkIndex == 0) {
            role = "admin";
        }
        else if (checkIndex == 1) {
            role = "member";
        }
        else if (checkIndex == 2) {
            role = "repair";
        }

        return role;
    }
}

class User2GroupViewHolder extends RecyclerView.ViewHolder {
    public ImageView headImg;
    public TextView userName;
    public TextView mobile;
    public LinearLayout roleLL;
    public ImageView role;
    public Button delete;
    public View view;

    public User2GroupViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        headImg = itemView.findViewById(R.id.group_member_head_img);
        userName = itemView.findViewById(R.id.group_member_name);
        mobile = itemView.findViewById(R.id.group_member_mobile);
        roleLL = itemView.findViewById(R.id.group_member_role_ll);
        role = itemView.findViewById(R.id.group_member_role);
        delete = itemView.findViewById(R.id.group_member_delete);
    }
}
