package com.ctfww.module.assignment.activity;

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
import com.ctfww.module.assignment.R;
import com.ctfww.module.assignment.datahelper.airship.Airship;
import com.ctfww.module.assignment.datahelper.dbhelper.DBHelper;
import com.ctfww.module.assignment.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.RouteAssignment;
import com.ctfww.module.datapicker.data.DataPicker;
import com.ctfww.module.datapicker.time.HourAndMinutePicker;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.entity.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = "/assignment/createAssignment")
public class CreateAssignmentActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "CreateAssignmentActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mRelease;

    private LinearLayout mSelectObjectLL;
    private TextView mSelectObjectSummary;

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

    private UserInfo mUserInfo;

    private List<Integer> mDeskIdList = new ArrayList<>();
    private List<String> mRouteIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_assignment_activity);

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

        mSelectObjectLL = findViewById(R.id.select_object_ll);
        mSelectObjectSummary = findViewById(R.id.select_object_summary);

        mMonday = findViewById(R.id.monday);
        mTuesday = findViewById(R.id.tuesday);
        mWednesday = findViewById(R.id.wednesday);
        mThursday = findViewById(R.id.thursday);
        mFriday = findViewById(R.id.friday);
        mSaturday = findViewById(R.id.saturday);
        mSunday = findViewById(R.id.sunday);

        mStartHourMinute = findViewById(R.id.start_hour_minute);
        mStartHourMinute.setTime(8, 0);
        mEndHourMinute = findViewById(R.id.end_hour_minute);
        mEndHourMinute.setTime(21, 0);

        mSelectMemberLL = findViewById(R.id.select_member_ll);
        mNickName = findViewById(R.id.nick_name);

//        mFrequency = findViewById(R.id.keepwatch_frequcency);

        mFrequency = findViewById(R.id.frequcency);
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
            List<DeskAssignment> deskAssignmentList = DBQuickEntry.getWorkingDeskAssignmentList();
            ArrayList<Integer> deskIdList = new ArrayList<>();
            for (int i = 0; i < deskAssignmentList.size(); ++i) {
                deskIdList.add(deskAssignmentList.get(i).getDeskId());
            }

            List<RouteAssignment> routeAssignmentList = DBQuickEntry.getWorkingRouteAssignmentList();
            ArrayList<String> routeIdList = new ArrayList<>();
            for (int i = 0; i < routeAssignmentList.size(); ++i) {
                routeIdList.add(routeAssignmentList.get(i).getRouteId());
            }

            ARouter.getInstance().build("/desk/selectSignObject")
                    .withIntegerArrayList("selected_desk_id_list", deskIdList)
                    .withStringArrayList("selected_route_id_list", routeIdList)
                    .navigation();
        }

        else if (id == mSelectMemberLL.getId()) {
            ARouter.getInstance().build("/user/selectUser").navigation();
        }
    }

    // 处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("selected_user".equals(messageEvent.getMessage())) {
            String userId = messageEvent.getValue();
            if (TextUtils.isEmpty(userId)) {
                return;
            }

            mUserInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(userId);
            if (mUserInfo == null) {
                return;
            }

            mNickName.setText(mUserInfo.getNickName());
        }
        else if ("selected_desk".equals(messageEvent.getMessage())) {
            Type type = new TypeToken<List<Integer>>(){}.getType();
            mDeskIdList = GsonUtils.fromJson(messageEvent.getValue(), type);
            mSelectObjectSummary.setText("签到点：" + mDeskIdList.size() + "个" + ", 签到线路：" + mRouteIdList.size() + "条");
        }
        else if ("selected_route".equals(messageEvent.getMessage())) {
            Type type = new TypeToken<List<String>>(){}.getType();
            mRouteIdList = GsonUtils.fromJson(messageEvent.getValue(), type);
            mSelectObjectSummary.setText("签到点：" + mDeskIdList.size() + "个" + ", 签到线路：" + mRouteIdList.size() + "条");
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


        for (int i = 0; i < mDeskIdList.size(); ++i) {
            DeskAssignment assignment = new DeskAssignment();
            String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
            assignment.setGroupId(groupId);
            assignment.setUserId(mUserInfo.getUserId());
            assignment.setCircleType(circleType);
            assignment.setStartTime(startTime);
            assignment.setEndTime(endTime);
            assignment.setFrequency(frequency);
            assignment.setTimeStamp(System.currentTimeMillis());
            assignment.setStatus("reserve");
            assignment.setSynTag("new");
            int deskId = mDeskIdList.get(i);
            assignment.setDeskId(deskId);
            assignment.combineId();
            DBHelper.getInstance().addDeskAssignment(assignment);
            DBHelper.getInstance().updateDeskTodayAssignment(assignment);
        }

        if (!mDeskIdList.isEmpty()) {
            Airship.getInstance().synDeskAssignmentToCloud();
        }

        for (int i = 0; i < mRouteIdList.size(); ++i) {
            RouteAssignment assignment = new RouteAssignment();
            String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
            assignment.setGroupId(groupId);
            assignment.setUserId(mUserInfo.getUserId());
            assignment.setCircleType(circleType);
            assignment.setStartTime(startTime);
            assignment.setEndTime(endTime);
            assignment.setFrequency(frequency);
            assignment.setTimeStamp(System.currentTimeMillis());
            assignment.setStatus("reserve");
            assignment.setSynTag("new");
            String routeId = mRouteIdList.get(i);
            assignment.setRouteId(routeId);
            assignment.combineId();
            DBHelper.getInstance().addRouteAssignment(assignment);
            DBHelper.getInstance().updateRouteTodayAssignment(assignment);
        }

        if (!mRouteIdList.isEmpty()) {
            Airship.getInstance().synRouteAssignmentToCloud();
        }

        finish();
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
}
