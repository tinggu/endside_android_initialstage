package com.ctfww.module.keepwatch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.module.datapicker.data.DataPicker;
import com.ctfww.module.datapicker.time.HourAndMinutePicker;
import com.ctfww.module.keepwatch.DataHelper.airship.Airship;
import com.ctfww.module.keepwatch.DataHelper.dbhelper.DBHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.entity.KeepWatchAssignment;
import com.ctfww.module.keepwatch.entity.KeepWatchDesk;
import com.ctfww.module.keepwatch.entity.KeepWatchRouteSummary;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = "/keepwatch/createAssignment")
public class KeepWatchCreateAssignmentActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "KeepWatchCreateAssignmentActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mRelease;

    private LinearLayout mSelectObjectLL;
    private TextView mObjectIdList;

    private CheckBox mMonday;
    private CheckBox mTuesday;
    private CheckBox mWednesday;
    private CheckBox mThursday;
    private CheckBox mFriday;
    private CheckBox mSaturday;
    private CheckBox mSunday;

    private HourAndMinutePicker mStartHourMinute;
    private HourAndMinutePicker mEndHourMinute;

    private LinearLayout mSelectMemberLL;
    private TextView mNickName;

    private DataPicker mFrequency;

    private GroupUserInfo mUserInfo;

    private List<String> mDeskIdList = new ArrayList<>();
    private List<String> mRouteIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.keepwatch_create_assignment_activity);

        initViews();

        setOnClickListener();
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("创建任务");
        mRelease = findViewById(R.id.top_addition);
        mRelease.setVisibility(View.VISIBLE);
        mRelease.setText("发布");

        mSelectObjectLL = findViewById(R.id.keepwatch_select_object_ll);
        mObjectIdList = findViewById(R.id.keepwatch_object_id_list);

        mMonday = findViewById(R.id.keepwatch_monday);
        mTuesday = findViewById(R.id.keepwatch_tuesday);
        mWednesday = findViewById(R.id.keepwatch_wednesday);
        mThursday = findViewById(R.id.keepwatch_thursday);
        mFriday = findViewById(R.id.keepwatch_friday);
        mSaturday = findViewById(R.id.keepwatch_saturday);
        mSunday = findViewById(R.id.keepwatch_sunday);

        mStartHourMinute = findViewById(R.id.keepwatch_start_hour_minute);
        mStartHourMinute.setTime(8, 0);
        mEndHourMinute = findViewById(R.id.keepwatch_end_hour_minute);
        mEndHourMinute.setTime(21, 0);

        mSelectMemberLL = findViewById(R.id.keepwatch_select_member_ll);
        mNickName = findViewById(R.id.keepwatch_nick_name);

//        mFrequency = findViewById(R.id.keepwatch_frequcency);

        mFrequency = findViewById(R.id.keepwatch_frequcency);
        List<Integer> frequencyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        mFrequency.updateData(frequencyList);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mSelectObjectLL.setOnClickListener(this);
        mRelease.setOnClickListener(this);

        mStartHourMinute.setOnTimeSelectedListener(new HourAndMinutePicker.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hour, int minute) {

            }
        });

        mEndHourMinute.setOnTimeSelectedListener(new HourAndMinutePicker.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hour, int minute) {

            }
        });

        mSelectMemberLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mRelease.getId()) {
            createAssignment();
        }
        else if (id == mSelectObjectLL.getId()) {
            startActivityForResult(new Intent(this, KeepWatchSelectSigninObjectActivity.class), 1);
        }

        else if (id == mSelectMemberLL.getId()) {
            ARouter.getInstance().build("/user/selectUser").navigation();
        }
    }

    // 处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("selected_user".equals(messageEvent.getMessage())) {
            mUserInfo = GsonUtils.fromJson(messageEvent.getValue(), GroupUserInfo.class);
            mNickName.setText(mUserInfo.getNickName());
        }
    }

    private void createAssignment() {
        if (mDeskIdList.isEmpty() && mRouteIdList.isEmpty()) {
            DialogUtils.onlyPrompt("请选择签到点或签到线路！", this);
            return;
        }

        if (mUserInfo == null) {
            DialogUtils.onlyPrompt("请选择任务的负责人！", this);
            return;
        }

        long startTime = getStartTime();
        long endTime = getEndTime();
        if (endTime - startTime < 30 * 60 * 1000) {
            DialogUtils.onlyPrompt("时间段不能低于30分钟！", this);
        }

        String circleType = getCircleType();
        if (TextUtils.isEmpty(circleType)) {
            DialogUtils.onlyPrompt("必须选择签到周期！", this);
            return;
        }

        int frequency = mFrequency.getCurrentData();
        if (frequency == 0) {
            DialogUtils.onlyPrompt("必须选择签到次数！", this);
            return;
        }

        KeepWatchAssignment assignment = new KeepWatchAssignment();

        String groupId = SPStaticUtils.getString("working_group_id");
        assignment.setGroupId(groupId);
        assignment.setUserId(mUserInfo.getUserId());
        assignment.setCircleType(circleType);
        assignment.setStartTime(startTime);
        assignment.setEndTime(endTime);
        assignment.setFrequency(frequency);
        assignment.setTimeStamp(System.currentTimeMillis());
        assignment.setStatus("reserve");
        assignment.setNickName(mUserInfo.getNickName());
        assignment.setSynTag("new");
        for (int i = 0; i < mDeskIdList.size(); ++i) {
            int deskId = GlobeFun.parseInt(mDeskIdList.get(i));
            assignment.setDeskId(deskId);
            assignment.setRouteId("");
            KeepWatchDesk desk = DBHelper.getInstance().getKeepWatchDesk(groupId, deskId);
            if (desk == null) {
                continue;
            }

            assignment.setDeskName(desk.getDeskName());
            assignment.setDeskType(desk.getDeskType());

            assignment.combineAssignmentId();
            DBHelper.getInstance().addKeepWatchAssignment(assignment);
        }

        for (int i = 0; i < mRouteIdList.size(); ++i) {
            assignment.setDeskId(0);
            String routeId = mRouteIdList.get(i);
            assignment.setRouteId(routeId);
            KeepWatchRouteSummary routeSummary = DBHelper.getInstance().getKeepWatchRouteSummary(routeId);
            if (routeSummary == null) {
                continue;
            }

            assignment.setDeskName(routeSummary.getRouteName());
            assignment.setDeskType("");

            assignment.combineAssignmentId();
            DBHelper.getInstance().addKeepWatchAssignment(assignment);
        }

        Airship.getInstance().synKeepWatchAssignmentToCloud();
    }

    private String getCircleType() {
        String ret = "";
        if (mMonday.isChecked()) {
            ret += ",Monday";
        }

        if (mTuesday.isChecked()) {
            ret += ", Tuesday";
        }

        if (mWednesday.isChecked()) {
            ret += ", Wednesday";
        }

        if (mThursday.isChecked()) {
            ret += ", Thursday";
        }

        if (mFriday.isChecked()) {
            ret += ", Friday";
        }

        if (mSaturday.isChecked()) {
            ret += ", Saturday";
        }

        if (mSunday.isChecked()) {
            ret += ", Sunday";
        }

        if (!TextUtils.isEmpty(ret)) {
            return ret.substring(1);
        }

        return ret;
    }

    private long getStartTime() {
        long startHour = (long)mStartHourMinute.getHour();
        long startMinute = (long)mStartHourMinute.getMinute();

        return startHour * 3600l * 1000l + startMinute * 60 * 1000;
    }

    private long getEndTime() {
        long endHour = (long)mEndHourMinute.getHour();
        long endMinute = (long)mEndHourMinute.getMinute();

        return endHour * 3600l * 1000l + endMinute * 60l * 1000l;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                return;
            }

            String deskList = SPStaticUtils.getString("select_desk");
            if (!TextUtils.isEmpty(deskList)) {
                Type type = new TypeToken<List<String>>() {}.getType();
                mDeskIdList = GsonUtils.fromJson(deskList, type);
            }

            String routeList = SPStaticUtils.getString("route_desk");
            if (!TextUtils.isEmpty(deskList)) {
                Type type = new TypeToken<List<String>>() {}.getType();
                mRouteIdList = GsonUtils.fromJson(routeList, type);
            }

            mObjectIdList.setText("签到点：" + mDeskIdList.size() + "个" + ", 签到线路：" + mRouteIdList.size() + "条");
        }
    }
}
