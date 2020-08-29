package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.PersonTrends;
import com.ctfww.module.user.entity.UserInfo;

import java.util.Calendar;
import java.util.List;


public class KeepWatchPersonTrendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PersonTrends> list;
    private Context mContext;

    public KeepWatchPersonTrendsListAdapter(List<PersonTrends> list, Context context) {
        this.list = list;
        mContext = context;
    }

    public void setList(List<PersonTrends> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keepwatch_person_trends_one_item, parent, false);
        return new KeepWatchPersonTrendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PersonTrends keepWatchPersonTrends = list.get(position);
        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keepWatchPersonTrends.getUserId());
        if (userInfo != null) {
            Glide.with(((KeepWatchPersonTrendsViewHolder)holder).view).load(userInfo.getHeadUrl()).into(((KeepWatchPersonTrendsViewHolder)holder).head);
            ((KeepWatchPersonTrendsViewHolder)holder).nickName.setText(userInfo.getNickName());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(keepWatchPersonTrends.getTimeStamp());
        ((KeepWatchPersonTrendsViewHolder)holder).dateTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        if ("desk".equals(keepWatchPersonTrends.getObjectType())) {
            DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(keepWatchPersonTrends.getGroupId(), GlobeFun.parseInt(keepWatchPersonTrends.getObjectId()));
            if (deskInfo != null) {
                ((KeepWatchPersonTrendsViewHolder)holder).deskName.setText(deskInfo.getIdName());
            }
        }
        else if ("route".equals(keepWatchPersonTrends.getObjectType())) {
            RouteSummary routeSummary = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(keepWatchPersonTrends.getObjectId());
            if (routeSummary != null) {
                ((KeepWatchPersonTrendsViewHolder)holder).deskName.setText(routeSummary.getRouteName());
            }
        }
        else {
            ((KeepWatchPersonTrendsViewHolder)holder).deskName.setText("自由上报");
        }

        ((KeepWatchPersonTrendsViewHolder)holder).status.setText(keepWatchPersonTrends.getStatusChinese());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class KeepWatchPersonTrendsViewHolder extends RecyclerView.ViewHolder {
    public ImageView head;
    public TextView nickName, dateTime;
    public TextView deskName, status;
    public View view;

    public KeepWatchPersonTrendsViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        head = itemView.findViewById(R.id.keepwatch_head);
        nickName = itemView.findViewById(R.id.keepwatch_nick_name);
        dateTime = itemView.findViewById(R.id.keepwatch_date_time);
        deskName = itemView.findViewById(R.id.keepwatch_desk_name);
        status = itemView.findViewById(R.id.keepwatch_status);
    }
}
