package com.ctfww.module.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.Utils;
import com.ctfww.module.main.R;
import com.ctfww.module.main.storage.table.DeviceElectricInfo;

import java.util.List;

public class DeviceElectricAdapter extends RecyclerView.Adapter<DeviceElectricAdapter.ViewHolder> {

    private static final String TAG = "DeviceElectricAdapter";

    private List<DeviceElectricInfo> deviceElectricInfos;

    private DeviceElectricCallback electricCallback;

    public interface DeviceElectricCallback {
        void onItemClick();
    }

    public DeviceElectricAdapter(List<DeviceElectricInfo> deviceElectricInfos, DeviceElectricCallback electricCallback) {
        this.deviceElectricInfos = deviceElectricInfos;
        this.electricCallback = electricCallback;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;

        TextView devIdTxt;
        TextView devNameTxt;

        TextView elecATxt;
        TextView elecBTxt;
        TextView elecCTxt;
        TextView elecETxt;
        TextView elecNTxt;

        ViewHolder(View view) {
            super(view);
            rootView = view;

            devIdTxt = view.findViewById(R.id.status_device_info_item_devId);
            devNameTxt = view.findViewById(R.id.status_device_info_item_devName);

            elecATxt = view.findViewById(R.id.status_device_info_item_elecA);
            elecBTxt = view.findViewById(R.id.status_device_info_item_elecB);
            elecCTxt = view.findViewById(R.id.status_device_info_item_elecC);
            elecETxt = view.findViewById(R.id.status_device_info_item_elecE);
            elecNTxt = view.findViewById(R.id.status_device_info_item_elecN);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.status_device_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceElectricInfo deviceElectricInfo = deviceElectricInfos.get(position);
        holder.devIdTxt.setText(deviceElectricInfo.getDevId());
        holder.devNameTxt.setText(deviceElectricInfo.getDevName());
        holder.elecATxt.setText(Utils.getApp().getString(R.string.status_device_electric_value, deviceElectricInfo.getElecA()));
        holder.elecBTxt.setText(Utils.getApp().getString(R.string.status_device_electric_value, deviceElectricInfo.getElecB()));
        holder.elecCTxt.setText(Utils.getApp().getString(R.string.status_device_electric_value, deviceElectricInfo.getElecC()));
        holder.elecETxt.setText(Utils.getApp().getString(R.string.status_device_electric_value, deviceElectricInfo.getElecE()));
        holder.elecNTxt.setText(Utils.getApp().getString(R.string.status_device_electric_value, deviceElectricInfo.getElecN()));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                electricCallback.onItemClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceElectricInfos.size();
    }
}
