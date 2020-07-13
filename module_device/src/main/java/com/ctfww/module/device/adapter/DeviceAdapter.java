package com.ctfww.module.device.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctfww.commonlib.network.ICloudCallback;
import com.ctfww.module.device.R;
import com.ctfww.module.device.network.CloudClient;
import com.ctfww.module.device.storage.DatabaseUtil;
import com.ctfww.module.device.storage.table.DeviceInfo;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DeviceInfo> list;
    IDeviceInfoActivityCallback callback;

    public interface IDeviceInfoActivityCallback {
        void start(int pos);
    }

    public DeviceAdapter(List<DeviceInfo> list, IDeviceInfoActivityCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_info_one_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (holder.del.getVisibility() == View.VISIBLE) {
                    holder.del.setVisibility(View.GONE);
                    return;
                }

                if (callback != null) {
                    callback.start(position);
                }
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.del.setVisibility(View.VISIBLE);
                return true;
            }
        });

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                final DeviceInfo deviceInfo = list.get(position);
                CloudClient.getInstance().deleteDevice(deviceInfo.getDevId(), new ICloudCallback() {
                    @Override
                    public void onSuccess(String data) {
                        DatabaseUtil.getInstance().deleteDevice(deviceInfo);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {

                    }

                    @Override
                    public void onFailure(String errorMsg) {

                    }
                });

                list.remove(position);
                notifyDataSetChanged();

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).id.setText(list.get(position).getDevId());
        ((MyViewHolder) holder).name.setText(list.get(position).getDevName());
        ((MyViewHolder) holder).addr.setText(list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView id, name, addr;
    public View view;
    public Button del;

    public MyViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        id = itemView.findViewById(R.id.device_id);
        name = itemView.findViewById(R.id.device_name);
        addr = itemView.findViewById(R.id.device_addr);
        del = itemView.findViewById(R.id.device_delete);
    }
}
