package com.ctfww.module.user.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.bumptech.glide.Glide;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.UserInfo;

import java.util.List;


public class GroupUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GroupUserInfo> list;
    private GroupUserInfo mSelfGroupUserInfo = new GroupUserInfo();
    private Context mContext;

    public GroupUserListAdapter(List<GroupUserInfo> list, Context context) {
        this.list = list;
        updateSelfGroupUserInfo(this.list);
        mContext = context;
    }

    public void setList(List<GroupUserInfo> list) {
        this.list = list;
        updateSelfGroupUserInfo(this.list);
    }

    private void updateSelfGroupUserInfo(List<GroupUserInfo> list) {
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
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
                .inflate(R.layout.group_user_one_item, parent, false);
        final GroupUserViewHolder holder = new GroupUserViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (holder.delete.getVisibility() == View.VISIBLE) {
                    holder.delete.setVisibility(View.GONE);
                    return;
                }
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
                holder.delete.setVisibility(View.GONE);

                int position = holder.getAdapterPosition();
                GroupUserInfo groupUserInfo = list.get(position);
                groupUserInfo.setStatus("delete");
                groupUserInfo.setTimeStamp(System.currentTimeMillis());
                String synTag = "new".equals(groupUserInfo.getSynTag()) ? "new" : "modify";
                groupUserInfo.setSynTag(synTag);

                DBHelper.getInstance().updateGroupUser(groupUserInfo);
                Airship.getInstance().synGroupUserInfoToCloud();
            }
        });

        holder.roleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup();
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

                        groupUserInfo.setRole(role);
                        groupUserInfo.setTimeStamp(System.currentTimeMillis());
                        String synTag = "new".equals(groupUserInfo.getSynTag()) ? "new" : "modify";
                        groupUserInfo.setSynTag(synTag);

                        DBHelper.getInstance().updateGroupUser(groupUserInfo);
                        Airship.getInstance().synGroupUserInfoToCloud();
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
        UserInfo userInfo = DBHelper.getInstance().getUser(groupUserInfo.getUserId());
        if (userInfo != null) {
            if (TextUtils.isEmpty(userInfo.getHeadUrl())) {
                ((GroupUserViewHolder)holder).headImg.setImageResource(R.drawable.default_head);
            }
            else {
                Glide.with(((GroupUserViewHolder)holder).view).load(userInfo.getHeadUrl()).into(((GroupUserViewHolder)holder).headImg);
            }

            ((GroupUserViewHolder)holder).userName.setText(userInfo.getNickName());
            ((GroupUserViewHolder)holder).mobile.setText(userInfo.getMobile());
        }

        if ("admin".equals(groupUserInfo.getRole())) {
            ((GroupUserViewHolder)holder).role.setImageResource(R.drawable.user_admin);
        }
        else if ("member".equals(groupUserInfo.getRole())) {
            ((GroupUserViewHolder)holder).role.setImageResource(R.drawable.user_member);
        }
        else if ("repair".equals(groupUserInfo.getRole())) {
            ((GroupUserViewHolder)holder).role.setImageResource(R.drawable.user_repair);
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

class GroupUserViewHolder extends RecyclerView.ViewHolder {
    public ImageView headImg;
    public TextView userName;
    public TextView mobile;
    public LinearLayout roleLL;
    public ImageView role;
    public Button delete;
    public View view;

    public GroupUserViewHolder(View itemView) {
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
