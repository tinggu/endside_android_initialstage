package com.ctfww.module.desk.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.desk.R;
import com.ctfww.module.desk.entity.DeskInfo;

import java.util.HashMap;
import java.util.List;


public class SelectDeskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "UserSelectUserListAdapter";

    private List<DeskInfo> list;
    private HashMap<String, String> selectedMapOld = new HashMap<>();
    private HashMap<String, String> selectedMapNew = new HashMap<>();

    public SelectDeskListAdapter(List<DeskInfo> list) {
        this.list = list;
    }

    public void setList(List<DeskInfo> list) {
        this.list = list;
    }

    public void setSelectedDeskIdList(List<String> deskIdList) {
        for (int i = 0; i < deskIdList.size(); ++i) {
            String deskId = deskIdList.get(i);
            selectedMapOld.put(deskId, deskId);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_desk_one_item, parent, false);
        SelectDeskViewHolder holder = new SelectDeskViewHolder(view);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LogUtils.i(TAG, "onBindViewHolder = " + position);

        final DeskInfo desk = list.get(position);
        ((SelectDeskViewHolder) holder).deskId.setText("" + desk.getDeskId());
        ((SelectDeskViewHolder) holder).deskName.setText(desk.getDeskName());
        String deskIdStr = selectedMapOld.get("" + desk.getDeskId());
        if (TextUtils.isEmpty(deskIdStr)) {
            ((SelectDeskViewHolder) holder).deskSelect.setVisibility(View.VISIBLE);
            ((SelectDeskViewHolder) holder).prompt.setVisibility(View.GONE);
        }
        else {
            ((SelectDeskViewHolder) holder).deskSelect.setVisibility(View.GONE);
            ((SelectDeskViewHolder) holder).prompt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOnClickListener(SelectDeskViewHolder holder) {
        holder.deskSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DeskInfo desk = list.get(position);
                if (holder.deskSelect.isChecked()) {
                    selectedMapNew.put("" + desk.getDeskId(), "" + desk.getDeskId());
                }
                else {
                    selectedMapNew.remove("" + desk.getDeskId());
                }
            }
        });
    }

    public HashMap<String, String> getSelectedMap() {
        return selectedMapNew;
    }
}

class SelectDeskViewHolder extends RecyclerView.ViewHolder {
    private final static String TAG = "SelectDeskViewHolder";

    View view;
    TextView deskId, deskName;
    CheckBox deskSelect;
    TextView prompt;

   SelectDeskViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        deskId = itemView.findViewById(R.id.desk_id);
        deskName = itemView.findViewById(R.id.desk_name);
        deskSelect = itemView.findViewById(R.id.select);
        prompt = itemView.findViewById(R.id.selected_prompt);
    }
}
