package com.ctfww.module.attend.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.attend.R;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AttendMyFragment extends Fragment {
    private final static String TAG = "AttendMyFragment";

    private View mV;

    private RelativeLayout mInformationRL;
    private TextView mUnreadCount;
    private LinearLayout mGroupLL;
    private ImageView mGroupSelect;
    private TextView mGroupName;
    private ImageView mSetup;

    private LinearLayout mUserInfoLL;
    private ImageView mHead;
    private TextView mNickName;
    private TextView mRole;
    private TextView mMemberCount;
    private TextView mDeskCount;

    private LinearLayout mAssignmentLL;
    private LinearLayout mGroupMemberLL;
    private LinearLayout mDeskLL;
    private LinearLayout mKeyEventLL;
    private LinearLayout mDayReportLL;
    private LinearLayout mWeekReportLL;
    private LinearLayout mMonthReportLL;

    private AttendSigninListFragment mAttendSigninListFragment;
    private TextView mSigninListTittle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.attend_my_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return mV;
    }

    private void initViews(View v) {
        mInformationRL = v.findViewById(R.id.top_information_rl);
        mUnreadCount = v.findViewById(R.id.information_unread_count);
        mGroupLL = v.findViewById(R.id.top_tittle_ll);
        mGroupSelect = v.findViewById(R.id.top_select);
        mGroupSelect.setVisibility(View.VISIBLE);
        mGroupName = v.findViewById(R.id.top_tittle);
        String groupName = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getWorkingGroupName();
        if (TextUtils.isEmpty(groupName)) {
            mGroupName.setText("请选择群组");
        }
        else {
            mGroupName.setText(groupName);
        }
        mSetup = v.findViewById(R.id.top_addition);
        mSetup.setImageResource(R.drawable.attend_setup);

        UserInfo userInfo = com.ctfww.module.user.datahelper.DataHelper.getInstance().getUserInfo();
        mUserInfoLL = v.findViewById(R.id.attend_user_info_ll);
        mHead = v.findViewById(R.id.attend_head);
        Glide.with(this).load(userInfo.getHeadUrl()).into(mHead);
        mNickName = v.findViewById(R.id.attend_nick_name);
        mNickName.setText(userInfo.getNickName());

        mRole = v.findViewById(R.id.attend_role);
        String role = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup();
        if ("admin".equals(role)) {
            mRole.setText("管理员");
        }
        else {
            mRole.setText("巡检员");
        }

        mMemberCount = v.findViewById(R.id.attend_member_count);
        mDeskCount = v.findViewById(R.id.attend_desk_count);

        mAssignmentLL = v.findViewById(R.id.attend_assignment_ll);
        mGroupMemberLL = v.findViewById(R.id.attend_group_member_ll);
        mDeskLL = v.findViewById(R.id.attend_desk_ll);
        mKeyEventLL = v.findViewById(R.id.attend_key_event_ll);
        mDayReportLL = v.findViewById(R.id.attend_day_report_ll);
        mWeekReportLL = v.findViewById(R.id.attend_week_report_ll);
        mMonthReportLL = v.findViewById(R.id.attend_month_report_ll);

        mAttendSigninListFragment = (AttendSigninListFragment)getChildFragmentManager().findFragmentById(R.id.attend_signin_list_fragment);
        mSigninListTittle = mAttendSigninListFragment.getView().findViewById(R.id.attend_signin_list_tittle);
        mSigninListTittle.setText("今日巡检记录");
        mAttendSigninListFragment.setMaxCount(3);

        String groupId = SPStaticUtils.getString(UserSPConstant.WORKING_GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            mRole.setText("");
            mMemberCount.setText("");
            mDeskCount.setText("");
        }
    }

    private void setOnClickListener() {
        mInformationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/invite").navigation();
            }
        });

        mGroupLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/selectGroup").navigation();
            }
        });

        mSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/setting").navigation();
            }
        });

        mUserInfoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/info").navigation();
            }
        });

        mAssignmentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString(UserSPConstant.WORKING_GROUP_ID))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/assignmentList").navigation();
            }
        });

        mGroupMemberLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString(UserSPConstant.WORKING_GROUP_ID))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/user/groupUserList").navigation();
            }
        });

        mDeskLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString(UserSPConstant.WORKING_GROUP_ID))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/deskList").navigation();
            }
        });

        mKeyEventLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString(UserSPConstant.WORKING_GROUP_ID))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keyevents/noEndList").navigation();
            }
        });

        mDayReportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString(UserSPConstant.WORKING_GROUP_ID))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/dayReport").navigation();
            }
        });

        mWeekReportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString(UserSPConstant.WORKING_GROUP_ID))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/weekReport").navigation();
            }
        });

        mMonthReportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString(UserSPConstant.WORKING_GROUP_ID))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/monthReport").navigation();
            }
        });
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
//        if ("tms_first_token".equals(msg)) {
//            getGroupSummary();
//            mKeepWatchSigninListFragment.getTodaySigninList();
//        }
//        else if ("bind_group".equals(msg)) {
//            getGroupSummary();
//            mKeepWatchSigninListFragment.getTodaySigninList();
//            String groupName = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getWorkingGroupName();
//            if (TextUtils.isEmpty(groupName)) {
//                mGroupName.setText("请选择群组");
//            }
//            else {
//                mGroupName.setText(groupName);
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (Utils.isFirstToken()) {
//            LogUtils.i(TAG, "onResume: Utils.isFirstToken()");
//            getGroupSummary();
//            mKeepWatchSigninListFragment.getTodaySigninList();
//        }
//
//        String groupName = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getWorkingGroupName();
//        if (TextUtils.isEmpty(groupName)) {
//            mGroupName.setText("请选择群组");
//        }
//        else {
//            mGroupName.setText(groupName);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void getGroupSummary() {
//        NetworkHelper.getInstance().getKeepWatchGroupSummary(new IUIDataHelperCallback() {
//            @Override
//            public void onSuccess(Object obj) {
//                KeepWatchGroupSummary keepWatchGroupSummary = (KeepWatchGroupSummary) obj;
//                updateGroupSummaryToUI(keepWatchGroupSummary);
//            }
//
//            @Override
//            public void onError(int code) {
//                LogUtils.i(TAG, "getKeepWatchInfo fail: code = " + code);
//            }
//        });
    }

//    private void updateGroupSummaryToUI(KeepWatchGroupSummary keepWatchGroupSummary) {
//        mMemberCount.setText("" + keepWatchGroupSummary.getMemberCount());
//        mDeskCount.setText("" + keepWatchGroupSummary.getDeskCount());
//    }
}
