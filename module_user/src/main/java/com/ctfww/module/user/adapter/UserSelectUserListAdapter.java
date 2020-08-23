package com.ctfww.module.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.user.R;
import com.ctfww.module.user.entity.GroupUserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class UserSelectUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "UserSelectUserListAdapter";

    private List<GroupUserInfo> list;

    public UserSelectUserListAdapter(List<GroupUserInfo> list) {
        this.list = list;
    }

    public void setList(List<GroupUserInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_select_user_one_item, parent, false);
        return new UserSelectUserViewHolder(view);
    }

    private String mUserId;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogUtils.i(TAG, "onBindViewHolder = " + position);

        final GroupUserInfo groupUserInfo = list.get(position);
        ((UserSelectUserViewHolder) holder).nickName.setText(groupUserInfo.getNickName());
        if (groupUserInfo.getUserId().equals(mUserId)) {
            ((UserSelectUserViewHolder) holder).groupSelect.setImageResource(R.drawable.radio_button_selected);
        }
        else {
            ((UserSelectUserViewHolder) holder).groupSelect.setImageResource(R.drawable.radio_button_unselected);
        }

        ((UserSelectUserViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserId = groupUserInfo.getUserId();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getUserId() {
        return mUserId;
    }
}

class UserSelectUserViewHolder extends RecyclerView.ViewHolder {
    private final static String TAG = "UserGroupViewHolder";

    View view;
    TextView nickName;
    ImageView groupSelect;

    UserSelectUserViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        nickName = itemView.findViewById(R.id.user_group_user_name);
        groupSelect = itemView.findViewById(R.id.user_group_select);
    }
}
