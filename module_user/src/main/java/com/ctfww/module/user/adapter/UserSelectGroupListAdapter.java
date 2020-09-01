package com.ctfww.module.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupUserInfo;

import java.util.List;


public class UserSelectGroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "UserSelectGroupListAdapter";

    private List<GroupUserInfo> list;
    private String mGroupId;

    public UserSelectGroupListAdapter(List<GroupUserInfo> list) {
        this.list = list;
        mGroupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
    }

    public void setList(List<GroupUserInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_select_group_one_item, parent, false);
        return new UserSelectGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogUtils.i(TAG, "onBindViewHolder = " + position);

        final GroupUserInfo groupUserInfo = list.get(position);
        GroupInfo groupInfo = DBHelper.getInstance().getGroup(groupUserInfo.getGroupId());
        String groupName = groupInfo == null ? "" : groupInfo.getGroupName();
        ((UserSelectGroupViewHolder) holder).groupName.setText(groupName);
        if (groupUserInfo.getGroupId().equals(mGroupId)) {
            ((UserSelectGroupViewHolder) holder).groupSelect.setImageResource(R.mipmap.selected);
        }
        else {
            ((UserSelectGroupViewHolder) holder).groupSelect.setImageResource(R.mipmap.unselected);
        }

        ((UserSelectGroupViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroupId = groupUserInfo.getGroupId();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getGroupId() {
        return mGroupId;
    }
}

class UserSelectGroupViewHolder extends RecyclerView.ViewHolder {
    private final static String TAG = "UserGroupViewHolder";

    View view;
    TextView groupName;
    ImageView groupSelect;

    UserSelectGroupViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        groupName = itemView.findViewById(R.id.user_group_name);
        groupSelect = itemView.findViewById(R.id.user_group_select);
    }
}
