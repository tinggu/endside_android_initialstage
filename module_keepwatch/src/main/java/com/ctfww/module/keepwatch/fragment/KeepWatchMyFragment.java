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
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.keepwatch.DataHelper.NetworkHelper;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.Utils;
import com.ctfww.module.keepwatch.entity.KeepWatchGroupSummary;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class KeepWatchMyFragment extends Fragment {
    private final static String TAG = "KeepWatchFragment";

    private View mV;

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
    private LinearLayout mInviteLL;

    private KeepWatchSigninListFragment mKeepWatchSigninListFragment;
    private TextView mSigninListTittle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keepwatch_my_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return mV;
    }

    private void initViews(View v) {
        UserInfo userInfo = com.ctfww.module.user.datahelper.DataHelper.getInstance().getUserInfo();
        mUserInfoLL = v.findViewById(R.id.keepwatch_user_info_ll);
        mHead = v.findViewById(R.id.keepwatch_head);
        Glide.with(this).load(userInfo.getHeadUrl()).into(mHead);
        mNickName = v.findViewById(R.id.keepwatch_nick_name);
        mNickName.setText(userInfo.getNickName());

        mRole = v.findViewById(R.id.keepwatch_role);
        String role = SPStaticUtils.getString("role");
        if ("admin".equals(role)) {
            mRole.setText("管理员");
        }
        else {
            mRole.setText("巡检员");
        }

        mMemberCount = v.findViewById(R.id.keepwatch_member_count);
        mDeskCount = v.findViewById(R.id.keepwatch_desk_count);

        mAssignmentLL = v.findViewById(R.id.keepwatch_assignment_ll);
        mGroupMemberLL = v.findViewById(R.id.keepwatch_group_member_ll);
        mDeskLL = v.findViewById(R.id.keepwatch_desk_ll);
        mKeyEventLL = v.findViewById(R.id.keepwatch_key_event_ll);
        mDayReportLL = v.findViewById(R.id.keepwatch_day_report_ll);
        mWeekReportLL = v.findViewById(R.id.keepwatch_week_report_ll);
        mMonthReportLL = v.findViewById(R.id.keepwatch_month_report_ll);
        mInviteLL = v.findViewById(R.id.keepwatch_invite_ll);

        mKeepWatchSigninListFragment = (KeepWatchSigninListFragment)getChildFragmentManager().findFragmentById(R.id.keepwatch_signin_list_fragment);
        mSigninListTittle = mKeepWatchSigninListFragment.getView().findViewById(R.id.keepwatch_signin_list_tittle);
        mSigninListTittle.setText("今日巡检记录");
        mKeepWatchSigninListFragment.setMaxCount(3);

        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            mRole.setText("");
            mMemberCount.setText("");
            mDeskCount.setText("");
        }
    }

    private void setOnClickListener() {
        mUserInfoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/info").navigation();
            }
        });

        mAssignmentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString("working_group_id"))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/assignmentList").navigation();
            }
        });

        mGroupMemberLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString("working_group_id"))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/user/groupUser").navigation();
            }
        });

        mDeskLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString("working_group_id"))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/deskList").navigation();
            }
        });

        mKeyEventLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString("working_group_id"))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keyevents/noEndList").navigation();
            }
        });

        mDayReportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString("working_group_id"))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/dayReport").navigation();
            }
        });

        mWeekReportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString("working_group_id"))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/weekReport").navigation();
            }
        });

        mMonthReportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString("working_group_id"))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/keepwatch/monthReport").navigation();
            }
        });

        mInviteLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SPStaticUtils.getString("working_group_id"))) {
                    ToastUtils.showShort("请先选择群组！");
                    return;
                }

                ARouter.getInstance().build("/user/invite").navigation();
            }
        });
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if ("tms_first_token".equals(msg)) {
            getGroupSummary();
            mKeepWatchSigninListFragment.getTodaySigninList();
        }
        else if ("bind_group".equals(msg)) {
            getGroupSummary();
            mKeepWatchSigninListFragment.getTodaySigninList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isFirstToken()) {
            LogUtils.i(TAG, "onResume: Utils.isFirstToken()");
            getGroupSummary();
            mKeepWatchSigninListFragment.getTodaySigninList();
        }
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
        NetworkHelper.getInstance().getKeepWatchGroupSummary(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                KeepWatchGroupSummary keepWatchGroupSummary = (KeepWatchGroupSummary) obj;
                updateGroupSummaryToUI(keepWatchGroupSummary);
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getKeepWatchInfo fail: code = " + code);
            }
        });
    }

    private void updateGroupSummaryToUI(KeepWatchGroupSummary keepWatchGroupSummary) {
        mMemberCount.setText("" + keepWatchGroupSummary.getMemberCount());
        mDeskCount.setText("" + keepWatchGroupSummary.getDeskCount());
    }
}
