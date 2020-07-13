package com.guoliang.module.keepwatch.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.module.keepwatch.DataHelper.NetworkHelper;
import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.Utils;
import com.guoliang.module.keepwatch.adapter.KeepWatchSigninListAdapter;
import com.guoliang.module.keepwatch.entity.KeepWatchGroupSummary;
import com.guoliang.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class KeepWatchMyFragment extends Fragment {
    private final static String TAG = "KeepWatchFragment";

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
        mInformationRL = v.findViewById(R.id.top_information_rl);
        mUnreadCount = v.findViewById(R.id.information_unread_count);
        mGroupLL = v.findViewById(R.id.top_tittle_ll);
        mGroupSelect = v.findViewById(R.id.top_select);
        mGroupSelect.setVisibility(View.VISIBLE);
        mGroupName = v.findViewById(R.id.top_tittle);
        String groupName = SPStaticUtils.getString("working_group_name");
        if (TextUtils.isEmpty(groupName)) {
            mGroupName.setText("请选择群组");
        }
        else {
            mGroupName.setText(groupName);
        }
        mSetup = v.findViewById(R.id.top_addition);
        mSetup.setImageResource(R.drawable.keepwatch_setup);

        UserInfo userInfo = com.guoliang.module.user.datahelper.DataHelper.getInstance().getUserInfo();
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
    }

    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        if ("tms_first_token".equals(msg)) {
            getGroupSummary();
            mKeepWatchSigninListFragment.getTodaySigninList();
            getUnreadcount();
        }
        else if ("bind_group".equals(msg)) {
            getGroupSummary();
            mKeepWatchSigninListFragment.getTodaySigninList();
            String groupName = SPStaticUtils.getString("working_group_name");
            if (TextUtils.isEmpty(groupName)) {
                mGroupName.setText("请选择群组");
            }
            else {
                mGroupName.setText(groupName);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isFirstToken()) {
            LogUtils.i(TAG, "onResume: Utils.isFirstToken()");
            getGroupSummary();
            mKeepWatchSigninListFragment.getTodaySigninList();
            getUnreadcount();
        }

        String groupName = SPStaticUtils.getString("working_group_name");
        if (TextUtils.isEmpty(groupName)) {
            mGroupName.setText("请选择群组");
        }
        else {
            mGroupName.setText(groupName);
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

    private void getUnreadcount() {
        com.guoliang.module.user.datahelper.NetworkHelper.getInstance().getNewMessageCount(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                int count = (int)obj;
                if (count == 0) {
                    return;
                }

                mUnreadCount.setVisibility(View.VISIBLE);
                if (count <= 9) {
                    mUnreadCount.setText("" + count);
                }
                else {
                    mUnreadCount.setText("9+");
                }
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}
