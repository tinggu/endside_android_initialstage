package com.guoliang.module.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.guoliang.module.user.R;

public class VerifyPasswordDialog extends DialogFragment {
    private final static String TAG = "VerifyPasswordDialog";

    private TextView mTxtCancel;
    private TextView mTxtConfirm;
    private EditText mEdtPassword;

    public interface IInputPassword {
        void onConfirm(String password);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.user_verify_password_dialog, null);
        builder.setView(view);

        initViews(view);
        setOnClickListener();

        return builder.create();
    }

    private void initViews(View view) {
        mTxtCancel = view.findViewById(R.id.dialog_cancel);
        mTxtConfirm = view.findViewById(R.id.dialog_confirm);
        mEdtPassword = view.findViewById(R.id.user_password_edit);
    }

    private void setOnClickListener() {
        mTxtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTxtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IInputPassword inputPassword = (IInputPassword)getActivity();
                inputPassword.onConfirm(mEdtPassword.getText().toString());
                dismiss();
            }
        });
    }
}
