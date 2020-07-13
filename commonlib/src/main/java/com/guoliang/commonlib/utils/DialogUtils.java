package com.guoliang.commonlib.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;

public class DialogUtils {
    public interface Callback {
        void onConfirm(int radioSelectItem);
        void onCancel();
    }

    public static void selectDialog(final String prompt, Context context, final Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(prompt);
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    callback.onConfirm(0);
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    callback.onCancel();
                }
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        dialog.getWindow().setAttributes(params);
//        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
//        dialog.getWindow().getDecorView().bringToFront();

        dialog.show();
    }

    public static void onlyPrompt(final String prompt, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(prompt);
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static int mRadioSelectItem = 0;
    public static void radioDialog(final String [] strArr, int checkedItem, String tittle, Context context, final Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(strArr, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                mRadioSelectItem = which;
            }
        });

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    callback.onConfirm(mRadioSelectItem);
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();  //创建AlertDialog对象
        dialog.show();
    }
}
