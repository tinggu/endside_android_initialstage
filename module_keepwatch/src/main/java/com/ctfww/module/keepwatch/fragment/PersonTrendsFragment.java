package com.ctfww.module.keepwatch.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.bumptech.glide.Glide;
import com.ctfww.commonlib.entity.MyDateTimeUtils;
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

public class PersonTrendsFragment extends Fragment {
    private final static String TAG = "PersonTrendsFragment";

    private View mV;

    private TextView mNoData;
    private LinearLayout mLL;
    private ImageView mHead;
    private TextView mNickName;
    private TextView mTime;
    private TextView mDeskName;
    private TextView mStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_person_trends_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mNoData = v.findViewById(R.id.keepwatch_no_data);
        mLL = v.findViewById(R.id.keepwatch_person_trends);
        mHead = v.findViewById(R.id.keepwatch_person_trends_head);
        mNickName = v.findViewById(R.id.keepwatch_person_trends_nick_name);
        mTime = v.findViewById(R.id.keepwatch_person_trends_time);
        mDeskName = v.findViewById(R.id.keepwatch_person_trends_desk_name);
        mStatus = v.findViewById(R.id.keepwatch_person_trends_status);

        mNoData.setVisibility(View.VISIBLE);
        mLL.setVisibility(View.GONE);

        showPersonTrends();
    }

    private void setOnClickListener() {
        mLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/personTrends").navigation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showPersonTrends() {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        List<SigninInfo> signinList = com.ctfww.module.signin.datahelper.dbhelper.DBQuickEntry.getSigninList(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        List<KeyEventTrace> keyEventTraceList = com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry.getKeyEventTraceListForGroup(MyDateTimeUtils.getTodayStartTime(), MyDateTimeUtils.getTodayEndTime());
        List<PersonTrends> personTrendsList = new ArrayList<>();
        if (!signinList.isEmpty()) {
            PersonTrends personTrends = new PersonTrends();
            personTrends.set(signinList.get(0));
            personTrendsList.add(personTrends);
        }

        if (!keyEventTraceList.isEmpty()) {
            PersonTrends personTrends = new PersonTrends();
            personTrends.set(keyEventTraceList.get(0));
            personTrendsList.add(personTrends);
        }

        if (personTrendsList.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mLL.setVisibility(View.GONE);
        }
        else {
            mNoData.setVisibility(View.GONE);
            mLL.setVisibility(View.VISIBLE);

            PersonTrends personTrends = personTrendsList.get(0);
            for (int i = 1; i < personTrendsList.size(); ++i) {
                if (personTrends.getTimeStamp() < personTrendsList.get(i).getTimeStamp()) {
                    personTrends = personTrendsList.get(i);
                }
            }

            UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(personTrends.getUserId());
            if (userInfo != null) {
                Glide.with(this).load(userInfo.getHeadUrl()).into(mHead);
                mNickName.setText(userInfo.getNickName());
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(personTrends.getTimeStamp());
            mTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

            if ("desk".equals(personTrends.getType())) {
                DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(groupId, personTrends.getObjectId());
                if (deskInfo != null) {
                    mDeskName.setText(deskInfo.getIdName());
                }
            }
            else if ("route".equals(personTrends.getType())) {
                RouteSummary routeSummary = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(groupId, personTrends.getObjectId());
                if (routeSummary != null) {
                    mDeskName.setText(routeSummary.getIdName());
                }
            }
            else if ("key_event".equals(personTrends.getType())) {
                if (personTrends.getObjectId() == 0) {
                    mDeskName.setText("无参考位置");
                }
                else {
                    DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(groupId, personTrends.getObjectId());
                    if (deskInfo != null) {
                        mDeskName.setText(deskInfo.getIdName());
                    }
                }
            }

            mStatus.setText(personTrends.getStatusChinese());
        }


    }
}
