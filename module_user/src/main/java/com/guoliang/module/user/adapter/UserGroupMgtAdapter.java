package com.guoliang.module.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.module.user.R;
import com.guoliang.module.user.activity.GroupActivity;
import com.guoliang.module.user.activity.GroupCreateOrUpdateActivity;
import com.guoliang.module.user.activity.GroupMgtActivity;
import com.guoliang.module.user.datahelper.NetworkHelper;
import com.guoliang.module.user.entity.UserGroupInfo;

import java.util.List;


public class UserGroupMgtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "UserGroupMgtAdapter";

    private List<UserGroupInfo> list;
    private Context mContext;

    public UserGroupMgtAdapter(List<UserGroupInfo> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<UserGroupInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_group_one_item, parent, false);
        final UserGroupViewHolder holder = new UserGroupViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(parent.getContext(), GroupActivity.class);
                intent.putExtra("userGroupInfo", GsonUtils.toJson(list.get(position)));
                ((GroupMgtActivity)mContext).startActivityForResult(intent, 2);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserGroupInfo userGroupInfo = list.get(position);
        ((UserGroupViewHolder) holder).groupHeadImg.setImageResource(R.mipmap.user_group_member_head);
        ((UserGroupViewHolder) holder).groupName.setText(userGroupInfo.getGroupName());
        if ("admin".equals(list.get(position).getRole())) {
            ((UserGroupViewHolder) holder).adminImg.setVisibility(View.VISIBLE);
        }
        else {
            ((UserGroupViewHolder) holder).adminImg.setVisibility(View.GONE);
        }

        String workingGroupId = SPStaticUtils.getString("working_group_id");
        if (userGroupInfo.getGroupId().equals(workingGroupId)) {
            ((UserGroupViewHolder) holder).view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_btn_bg_01_30));
        }
        else {
            ((UserGroupViewHolder) holder).view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white_100));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class UserGroupViewHolder extends RecyclerView.ViewHolder {
    private final static String TAG = "UserGroupViewHolder";

    public ImageView groupHeadImg;
    public TextView groupName;
    public ImageView adminImg;
    public View view;

    public UserGroupViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        groupHeadImg = itemView.findViewById(R.id.user_group_head_img);
        groupName = itemView.findViewById(R.id.user_group_name);
        adminImg = itemView.findViewById(R.id.group_admin_img);
    }
}
