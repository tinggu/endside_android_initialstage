package com.ctfww.module.keepwatch.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ctfww.module.keepwatch.R;

public class FunctionDialog extends DialogFragment implements View.OnClickListener {
    private  View masker;
    private LinearLayout layoutBottom;

    Button mSignin;
    Button mReport;

    public interface IFunction {
        void startFunction(String function);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //去掉dialog的标题，需要在setContentView()之前
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getDialog().getWindow();
        //去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的位置在底部
        lp.gravity = Gravity.BOTTOM;
        //设置dialog的动画
//        lp.windowAnimations = R.style.BottomDialog;
        lp.windowAnimations = R.style.BottomDialogAnimation;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable());

        final View view = inflater.inflate(R.layout.keepwatch_function_dialog, null);
        initViews(view);
        setOnClickListener();

        return view;
    }

    private void initViews(View v) {
        mSignin = v.findViewById(R.id.keepwatch_sign_in);
        mReport = v.findViewById(R.id.keepwatch_report_event);
    }

    private void setOnClickListener() {
        mSignin.setOnClickListener(this);
        mReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mSignin.getId()) {
            dismiss();
            IFunction function = (IFunction)getActivity();
            function.startFunction("signin");
        }
        else if (id == mReport.getId()) {
            dismiss();
            IFunction function = (IFunction)getActivity();
            function.startFunction("report");
        }
    }
}
