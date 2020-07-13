package com.ctfww.module.user.activity;



import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelDatePicker;
import com.ctfww.module.user.R;

public class UpdateBirthdayDialog extends DialogFragment {
    private final static String TAG = "UpdateBirthdayDialog";

    private TextView mCancel;
    private TextView mTittle;
    private TextView mConfirm;
    private WheelPicker mSex;
    private LinearLayout mArea;
    private WheelDatePicker mDate;

    public interface ISelectBirthday {
        void onSelected(int year, int month, int day);
        Object getBirthday();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.user_update_birthday_dialog, null);
        builder.setView(view);
        initViews(view);
        setOnClickListener();

        return builder.create();
    }

    private void initViews(View view) {
        mCancel = view.findViewById(R.id.txt_cancel);
        mTittle = view.findViewById(R.id.txt_tittle);
        mTittle.setText("生日");
        mConfirm = view.findViewById(R.id.txt_confirm);
        mSex = view.findViewById(R.id.wheel_sex);
        mArea = view.findViewById(R.id.ll_area);
        mDate = view.findViewById(R.id.wheel_date);

        mDate.setVisibility(View.VISIBLE);
        mArea.setVisibility(View.GONE);
        mSex.setVisibility(View.GONE);
        mDate.setMinimumWidth(500);
        mDate.setCyclic(true);
        mDate.setAtmospheric(true);
        mDate.setIndicator(true);

        ISelectBirthday selectBirthday = (ISelectBirthday)getActivity();
        int[] arr = (int[])selectBirthday.getBirthday();
        mDate.setSelectedYear(arr[0]);
        mDate.setSelectedMonth(arr[1]);
        mDate.setSelectedDay(arr[2]);
    }

    private void setOnClickListener() {
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISelectBirthday selectBirthday = (ISelectBirthday)getActivity();
                selectBirthday.onSelected(mDate.getCurrentYear(), mDate.getCurrentMonth(), mDate.getCurrentDay());
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
