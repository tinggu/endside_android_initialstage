package com.ctfww.module.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.user.R;
import com.ctfww.module.user.entity.UserGroupInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class UserSelectGroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "UserSelectGroupListAdapter";

    private List<UserGroupInfo> list;
    private String mGroupId;

    public UserSelectGroupListAdapter(List<UserGroupInfo> list, String groupId) {
        this.list = list;
        mGroupId = groupId;
    }

    public void setList(List<UserGroupInfo> list, String groupId) {
        this.list = list;
        mGroupId = groupId;
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

        final UserGroupInfo userGroupInfo = list.get(position);
        ((UserSelectGroupViewHolder) holder).groupName.setText(userGroupInfo.getGroupName());
        if (userGroupInfo.getGroupId().equals(mGroupId)) {
            ((UserSelectGroupViewHolder) holder).groupSelect.setImageResource(R.mipmap.selected);
        }
        else {
            ((UserSelectGroupViewHolder) holder).groupSelect.setImageResource(R.mipmap.unselected);
        }

        ((UserSelectGroupViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroupId = userGroupInfo.getGroupId();
                EventBus.getDefault().post(new MessageEvent("select_group", userGroupInfo.getGroupId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
