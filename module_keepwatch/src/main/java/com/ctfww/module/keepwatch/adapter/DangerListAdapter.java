package com.ctfww.module.keepwatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.temp.DangerObject;

import java.util.List;


public class DangerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DangerObject> list;

    public DangerListAdapter(List<DangerObject> list) {
        this.list = list;
    }

    public void setList(List<DangerObject> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.danger_one_item, parent, false);
        return new DangerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DangerObject dangerObject = list.get(position);
        ((DangerViewHolder)holder).objectId.setText("" + dangerObject.getObjectId());
        DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(dangerObject.getGroupId(), dangerObject.getObjectId());
        if (deskInfo != null) {
            ((DangerViewHolder)holder).objectName.setText(deskInfo.getDeskName());
        }
        ((DangerViewHolder)holder).reason.setText("原因：" + dangerObject.combineReason());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class DangerViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView objectId, objectName;
    public TextView reason;

    public DangerViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        objectId = itemView.findViewById(R.id.object_id);
        objectName = itemView.findViewById(R.id.object_name);
        reason = itemView.findViewById(R.id.reason);
    }
}
