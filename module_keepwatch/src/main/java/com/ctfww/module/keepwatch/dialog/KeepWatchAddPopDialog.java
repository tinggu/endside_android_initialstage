package com.ctfww.module.keepwatch.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.keepwatch.R;

public class KeepWatchAddPopDialog extends PopupWindow {
    private static final String TAG = "KeepWatchAddPopDialog";

    private LinearLayout mCreateGroupLL;
    private LinearLayout mInviteMemberLL;
    private LinearLayout mAddDeskLL;
    private LinearLayout mCreateAssignmentLL;
    private LinearLayout mCreateNoticeLL;

    public KeepWatchAddPopDialog(Context context) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
 //       setFocusable(true);
//        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.keepwatch_add_pop_dialog, null, false);
        setContentView(contentView);
        initViews(contentView);
        setOnClickListener();
    }

    private void initViews(View v) {
        mCreateGroupLL = v.findViewById(R.id.keepwatch_create_group_ll);
        mInviteMemberLL = v.findViewById(R.id.keepwatch_invite_member_ll);
        mAddDeskLL = v.findViewById(R.id.keepwatch_add_desk_ll);
        mCreateAssignmentLL = v.findViewById(R.id.keepwatch_create_assignment_ll);
        mCreateNoticeLL = v.findViewById(R.id.keepwatch_create_notice_ll);
    }

    public void setOnClickListener() {
        mCreateGroupLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/createGroup").navigation();
                dismiss();
            }
        });

        mInviteMemberLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/inviteMember").navigation();
                dismiss();
            }
        });

        mAddDeskLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/desk/addDesk").navigation();
                dismiss();
            }
        });

        mCreateAssignmentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i(TAG, "mCreateAssignmentLL.onClick...");
                ARouter.getInstance().build("/assignment/createAssignment").navigation();
                dismiss();
            }
        });

        mCreateNoticeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i(TAG, "mCreateNoticeLL.onClick...");
                ARouter.getInstance().build("/user/createNotice").navigation();
                dismiss();
            }
        });
    }
}