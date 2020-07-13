package com.ctfww.module.keepwatch.util;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.module.keepwatch.R;

public class PopupWindowUtil {

//    /**
//     * 弹出PopupWindow，可根据不同的layout，弹出不同的风格的PopupWindow
//     * @param context 上下文
//     * @param view 在哪个view下面弹出PopupWindow
//     * @param title 标题
//     * @param content 内容
//     * @param xOffset PopupWindow显示位置偏移量
//     */
//    public static void showPopupWindow(Context context, View view, String title, String content, int xOffset) {
//        View contentView = View.inflate(context, R.layout.popup_window_layout, null);
//
//        TextView titleText = contentView.findViewById(R.id.popup_window_title);
//        titleText.setText(title);
//
//        TextView contentText = contentView.findViewById(R.id.popup_window_content);
//        contentText.setText(content);
//
//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//
//        popupWindow.showAsDropDown(view, xOffset, 0);
//    }

    public static void showKeepWatchAddPopupWindow(Context context, View view, int xOffset) {
        View contentView = View.inflate(context, R.layout.keepwatch_add_pop_dialog, null);

        LinearLayout mCreateGroupLL = contentView.findViewById(R.id.keepwatch_create_group_ll);
        LinearLayout mInviteMemberLL = contentView.findViewById(R.id.keepwatch_invite_member_ll);
        LinearLayout mAddDeskLL = contentView.findViewById(R.id.keepwatch_add_desk_ll);
        LinearLayout mCreateAssignmentLL = contentView.findViewById(R.id.keepwatch_create_assignment_ll);
        if (!"admin".equals(SPStaticUtils.getString("role"))) {
            mInviteMemberLL.setVisibility(View.GONE);
            mAddDeskLL.setVisibility(View.GONE);
            mCreateAssignmentLL.setVisibility(View.GONE);
        }

        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mCreateGroupLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/createGroup").navigation();
                popupWindow.dismiss();
            }
        });

        mInviteMemberLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/user/inviteMember").navigation();
                popupWindow.dismiss();
            }
        });

        mAddDeskLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/addDesk").navigation();
                popupWindow.dismiss();
            }
        });

        mCreateAssignmentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/keepwatch/createAssignment").navigation();
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(view, xOffset, 0);
    }

}
