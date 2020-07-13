package com.guoliang.module.keepwatch.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.LogUtils;
import com.guoliang.module.keepwatch.R;

public class PromptSuccessrDialog extends DialogFragment {
    private final static String TAG = "PromptSuccessrDialog";

    TextView mSuccessText;

    public interface IExchangeDataWithActivity {
        long getSigninDeskId();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.keepwatch_prompt_success_dialog, null);
        builder.setView(view);

        initViews(view);

        return builder.create();
    }

    private void initViews(View v) {
        mSuccessText = v.findViewById(R.id.keepwatch_success_text);
        IExchangeDataWithActivity exchangeDataWithActivity = (IExchangeDataWithActivity)getActivity();
        long signinDeskId = exchangeDataWithActivity.getSigninDeskId();
        mSuccessText.setText("" + signinDeskId + "号签到成功");
    }

    //    @Override
//    public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.add(this, tag);
//        ft.commitAllowingStateLoss();
//    }

}
