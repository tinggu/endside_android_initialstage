package com.ctfww.module.keyevents.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class KeyEventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "KeyEventListAdapter";

    private List<KeyEvent> list;

    public KeyEventListAdapter(List<KeyEvent> list) {
        this.list = list;
    }

    public void setList(List<KeyEvent> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keyevent_key_event_one_item, parent, false);
        KeyEventViewHolder keyEventViewHolder = new KeyEventViewHolder(view);
        setOnClickListener(keyEventViewHolder);
        return keyEventViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KeyEvent keyEvent = list.get(position);
        String deskName = "[" + keyEvent.getDeskId() + "]";
        DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(keyEvent.getGroupId(), keyEvent.getDeskId());
        if (deskInfo != null) {
            deskName += " " + deskInfo.getDeskName();
        }
        ((KeyEventViewHolder)holder).deskName.setText(deskName);
        ((KeyEventViewHolder)holder).keyEventName.setText(keyEvent.getEventName());

        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keyEvent.getUserId());
        String nickName = userInfo == null ? "" : userInfo.getNickName();
        ((KeyEventViewHolder)holder).nickName.setText(nickName);
        ((KeyEventViewHolder)holder).dateTime.setText(GlobeFun.stampToDateTime(keyEvent.getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(KeyEventViewHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ARouter.getInstance().build("/keyevents/keyevent").navigation();
                EventBus.getDefault().postSticky(new MessageEvent("view_key_event", GsonUtils.toJson(list.get(position))));
                LogUtils.i(TAG, "holder.view.setOnClickListener");
            }
        });
    }
}

class KeyEventViewHolder extends RecyclerView.ViewHolder {
    public TextView deskName;
    public TextView keyEventName;
    public TextView nickName, dateTime;
    public View view;

    public KeyEventViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskName = itemView.findViewById(R.id.keyevent_desk_name);
        keyEventName = itemView.findViewById(R.id.keyevent_key_event_name);
        nickName = itemView.findViewById(R.id.keyevent_nick_name);
        dateTime = itemView.findViewById(R.id.keyevent_date_time);
    }
}
