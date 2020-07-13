package com.guoliang.module.keyevents.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.module.keyevents.Entity.KeyEvent;
import com.guoliang.module.keyevents.R;

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
        ((KeyEventViewHolder)holder).deskName.setText("[" + keyEvent.getDeskId() + "]" + "  " + keyEvent.getDeskName());
        ((KeyEventViewHolder)holder).keyEventName.setText(keyEvent.getEventName());
        ((KeyEventViewHolder)holder).nickName.setText(keyEvent.getNickName());
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
