package com.ctfww.module.keepwatch.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPStaticUtils;
import com.bumptech.glide.Glide;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.datahelper.sp.Const;
import com.ctfww.module.keepwatch.entity.PersonTrends;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.signin.entity.SigninInfo;
import com.ctfww.module.user.entity.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PersonTrendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PersonTrends> list;
    private Context mContext;

    public PersonTrendsListAdapter(List<SigninInfo> signinList, List<KeyEventTrace> keyEventTraceList, Context context) {
        setList(signinList, keyEventTraceList);
        mContext = context;
    }

    public void setList(List<SigninInfo> signinList, List<KeyEventTrace> keyEventTraceList) {
        List<PersonTrends> personTrendsList = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < signinList.size(); ++i) {
            PersonTrends personTrends = new PersonTrends();
            SigninInfo signin = signinList.get(i);
            personTrends.setUserId(signin.getUserId());
            personTrends.setObjectId(signin.getObjectId());
            personTrends.setTimeStamp(signin.getTimeStamp());
            personTrends.setType(signin.getType());
            personTrends.setStatus("signin");

            personTrendsList.add(personTrends);
        }

        start += signinList.size();
        for (int i = 0; i < keyEventTraceList.size(); ++i) {
            PersonTrends personTrends = personTrendsList.get(start + i);
            KeyEventTrace keyEventTrace = keyEventTraceList.get(i);
            personTrends.setUserId(keyEventTrace.getUserId());
            personTrends.setObjectId(keyEventTrace.getObjectId());
            personTrends.setTimeStamp(keyEventTrace.getTimeStamp());
            personTrends.setType("key_event");
            personTrends.setStatus(keyEventTrace.getStatus());
            personTrends.setAddition(keyEventTrace.getEventId());

            personTrendsList.add(personTrends);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_trends_one_item, parent, false);
        return new PersonTrendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        PersonTrends personTrends = list.get(position);
        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(personTrends.getUserId());
        if (userInfo != null) {
            Glide.with(((PersonTrendsViewHolder)holder).view).load(userInfo.getHeadUrl()).into(((PersonTrendsViewHolder)holder).head);
            ((PersonTrendsViewHolder)holder).nickName.setText(userInfo.getNickName());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(personTrends.getTimeStamp());
        ((PersonTrendsViewHolder)holder).dateTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        if ("desk".equals(personTrends.getType())) {
            DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(groupId, personTrends.getObjectId());
            if (deskInfo != null) {
                ((PersonTrendsViewHolder)holder).deskName.setText(deskInfo.getIdName());
            }
        }
        else if ("route".equals(personTrends.getType())) {
            RouteSummary routeSummary = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(groupId, personTrends.getObjectId());
            if (routeSummary != null) {
                ((PersonTrendsViewHolder)holder).deskName.setText(routeSummary.getIdName());
            }
        }
        else if ("key_event".equals(personTrends.getType())) {
            if (personTrends.getObjectId() == 0) {
                ((PersonTrendsViewHolder)holder).deskName.setText("无参考位置");
            }
            else {
                DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(groupId, personTrends.getObjectId());
                if (deskInfo != null) {
                    ((PersonTrendsViewHolder)holder).deskName.setText(deskInfo.getIdName());
                }
            }
        }

        ((PersonTrendsViewHolder)holder).status.setText(personTrends.getStatusChinese());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class PersonTrendsViewHolder extends RecyclerView.ViewHolder {
    public ImageView head;
    public TextView nickName, dateTime;
    public TextView deskName, status;
    public View view;

    public PersonTrendsViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        head = itemView.findViewById(R.id.head);
        nickName = itemView.findViewById(R.id.nick_name);
        dateTime = itemView.findViewById(R.id.date_time);
        deskName = itemView.findViewById(R.id.object_name);
        status = itemView.findViewById(R.id.type_status);
    }
}
