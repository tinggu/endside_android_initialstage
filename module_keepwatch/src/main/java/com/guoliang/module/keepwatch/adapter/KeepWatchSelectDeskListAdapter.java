package com.guoliang.module.keepwatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.entity.KeepWatchDesk;
import com.guoliang.module.user.entity.GroupUserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;


public class KeepWatchSelectDeskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "UserSelectUserListAdapter";

    private List<KeepWatchDesk> list;
    private HashMap<Integer, Integer> selectedMap = new HashMap<>();

    public KeepWatchSelectDeskListAdapter(List<KeepWatchDesk> list) {
        this.list = list;
        selectedMap.clear();
    }

    public void setList(List<KeepWatchDesk> list) {
        this.list = list;
        selectedMap.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_select_desk_one_item, parent, false);
        KeepWatchSelectDeskViewHolder holder = new KeepWatchSelectDeskViewHolder(view);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogUtils.i(TAG, "onBindViewHolder = " + position);

        final KeepWatchDesk keepWatchDesk = list.get(position);
        ((KeepWatchSelectDeskViewHolder) holder).deskId.setText("" + keepWatchDesk.getDeskId());
        ((KeepWatchSelectDeskViewHolder) holder).deskName.setText(keepWatchDesk.getDeskName());
        if (keepWatchDesk.getIsAssignmented()) {
            ((KeepWatchSelectDeskViewHolder) holder).deskSelect.setVisibility(View.GONE);
            ((KeepWatchSelectDeskViewHolder) holder).prompt.setVisibility(View.VISIBLE);
        }
        else {
            ((KeepWatchSelectDeskViewHolder) holder).deskSelect.setVisibility(View.VISIBLE);
            ((KeepWatchSelectDeskViewHolder) holder).prompt.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(KeepWatchSelectDeskViewHolder holder) {
        holder.deskSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                KeepWatchDesk keepWatchDesk = list.get(position);
                if (holder.deskSelect.isChecked()) {
                    selectedMap.put(keepWatchDesk.getDeskId(), keepWatchDesk.getDeskId());
                }
                else {
                    selectedMap.remove(keepWatchDesk.getDeskId());
                }
            }
        });
    }

    public HashMap<Integer, Integer> getSelectedMap() {
        return selectedMap;
    }
}

class KeepWatchSelectDeskViewHolder extends RecyclerView.ViewHolder {
    private final static String TAG = "UserGroupViewHolder";

    View view;
    TextView deskId, deskName;
    CheckBox deskSelect;
    TextView prompt;

    KeepWatchSelectDeskViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskId = itemView.findViewById(R.id.keepwatch_desk_id);
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        deskSelect = itemView.findViewById(R.id.keepwatch_select);
        prompt = itemView.findViewById(R.id.keepwatch_selected_prompt);
    }
}
