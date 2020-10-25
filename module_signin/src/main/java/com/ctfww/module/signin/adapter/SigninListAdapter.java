package com.ctfww.module.signin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.service.autofill.FieldClassification;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.signin.R;
import com.ctfww.module.signin.datahelper.dbhelper.DBHelper;
import com.ctfww.module.signin.datahelper.sp.Const;
import com.ctfww.module.signin.entity.SigninInfo;
import com.ctfww.module.user.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;


public class SigninListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SigninListAdapter";

    private Context mContext;
    private List<SigninInfo> list;

    private View mV;

    private int mMaxCount;

    public SigninListAdapter(List<SigninInfo> signinList, int maxCount) {
        mMaxCount = maxCount;
        setList(signinList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.signin_one_item, parent, false);
        mV = view;
        SigninViewHolder holder = new SigninViewHolder(view);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return;
        }

        SigninInfo info =  list.get(position);
        LogUtils.i(TAG, "onBindViewHolder: info = " + info.toString());

        if ("desk".equals(info.getType())) {
            DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(groupId, info.getObjectId());
            String deskName = deskInfo == null ? "" : deskInfo.getDeskName();
            ((SigninViewHolder) holder).objectName.setText(deskInfo.getIdName());
        }
        else if ("route".equals(info.getType())) {
            RouteSummary routeSummary = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(groupId, info.getObjectId());
            String routeName = routeSummary == null ? "" : routeSummary.getRouteName();
            ((SigninViewHolder) holder).objectName.setText(routeSummary.getIdName());
        }

        if ("excellent".equals(info.getMatchLevel())) {
            ((SigninViewHolder) holder).matchLevel.setText("优");
            ((SigninViewHolder) holder).matchLevel.setTextColor(0xFF7ED321);
        }
        else if ("good".equals(info.getMatchLevel())) {
            ((SigninViewHolder) holder).matchLevel.setText("良");
            ((SigninViewHolder) holder).matchLevel.setTextColor(0xFFFFC90E);
        }
        else if ("bad".equals(info.getMatchLevel())) {
            ((SigninViewHolder) holder).matchLevel.setText("差");
            ((SigninViewHolder) holder).matchLevel.setTextColor(0xFFF65066);
        }
        else {
            ((SigninViewHolder) holder).matchLevel.setText("无");
            ((SigninViewHolder) holder).matchLevel.setTextColor(Color.GRAY);
        }

        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(info.getUserId());
        String nickName = userInfo == null ? "" : userInfo.getNickName();
        ((SigninViewHolder) holder).nickName.setText(nickName);
        ((SigninViewHolder) holder).dateTime.setText(GlobeFun.stampToDateTime(info.getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<SigninInfo> list) {
        this.list = list.subList(0, Math.min(mMaxCount, list.size()));
    }

    private void setOnClickListener(SigninViewHolder holder) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Intent intent = new Intent(mContext, KeepWatchModifyAssignmentActivity.class);
//                String memberStr = GsonUtils.toJson(list.get(position));
//                intent.putExtra("keep_watch_assignment_member", memberStr);
//                mContext.startActivity(intent);
            }
        });
    }
}

class SigninViewHolder extends RecyclerView.ViewHolder {

    View view;
    TextView objectName, matchLevel;
    TextView nickName, dateTime;

    SigninViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        objectName = itemView.findViewById(R.id.object_name);
        matchLevel = itemView.findViewById(R.id.match_level);
        nickName = itemView.findViewById(R.id.nick_name);
        dateTime = itemView.findViewById(R.id.date_time);
    }
}
