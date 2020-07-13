package com.ctfww.module.keepwatch.activity;

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
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.module.datapicker.data.DataPicker;
import com.ctfww.module.datapicker.time.HourAndMinutePicker;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.user.entity.GroupUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

@Route(path = "/keepwatch/createAssignment")
public class KeepWatchCreateAssignmentActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "KeepWatchCreateAssignmentActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mRelease;

    private LinearLayout mSelectDeskLL;
    private TextView mDeskIdList;

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

        mSelectDeskLL = findViewById(R.id.keepwatch_select_desk_ll);
        mDeskIdList = findViewById(R.id.keepwatch_desk_id_list);

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
        mSelectDeskLL.setOnClickListener(this);
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
        else if (id == mSelectDeskLL.getId()) {
            ARouter.getInstance().build("/keepwatch/selectDesk").navigation();
        }

        else if (id == mSelectMemberLL.getId()) {
            ARouter.getInstance().build("/user/selectUser").navigation();
        }
    }

    // 处理事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public  void onGetMessage(MessageEvent messageEvent) {
        if ("selected_desk".equals(messageEvent.getMessage())) {
            LogUtils.i(TAG, "onGetMessage: messageEvent.getValue() = " + messageEvent.getValue());
            mDeskIdList.setText(messageEvent.getValue());
        }
        else if ("selected_user".equals(messageEvent.getMessage())) {
            mUserInfo = GsonUtils.fromJson(messageEvent.getValue(), GroupUserInfo.class);
            mNickName.setText(mUserInfo.getNickName());
        }
    }

    private void createAssignment() {
        if (TextUtils.isEmpty(mDeskIdList.getText().toString())) {
            DialogUtils.onlyPrompt("请选择签到点！", this);
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

        NetworkHelper.getInstance().addKeepWatchAssignment(mDeskIdList.getText().toString(), circleType, startTime, endTime, mUserInfo.getUserId(), frequency, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                ToastUtils.showShort("创建成功！");
                finish();
            }

            @Override
            public void onError(int code) {
                ToastUtils.showShort("创建失败！");
            }
        });
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
