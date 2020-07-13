package com.ctfww.module.user.activity;

import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.module.user.R;

public class SelectGenderDialog extends DialogFragment {
    private final static String TAG = "SelectGenderDialog";

    private ImageView mImgSelectedMale;
    private ImageView mImgUnselectedMale;
    private ImageView mImgSelectedFemale;
    private ImageView mImgUnselectedFemale;

    public interface ISelectGender {
        void onGenderSelected(boolean isMale);
        int genderType();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.user_select_gender_dialog, null);
        builder.setView(view);

        initViews(view);
        setOnClickListener();

        return builder.create();
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.user_select_gender_dialog, container);
//
//        initViews(view);
//        setOnClickListener();
//
//        return view;
//    }

/*    @Override
    public void onClick(DialogInterface dialog, int id) {
        LogUtils.i("TAG", "onClick: " + "探测到对话款的点击");
        if (id == R.id.user_gender_img_selected_male || id == R.id.user_gender_img_unselected_male) {
            LogUtils.i("TAG", "onClick: " + "选择男性");
            ISelectGender selectedGender = (ISelectGender)getActivity();
            selectedGender.onGenderSelected(true);
            dismiss();
        }

        if (id == R.id.user_gender_img_selected_female || id == R.id.user_gender_img_unselected_female) {
            LogUtils.i("TAG", "onClick: " + "选择女性");
            ISelectGender selectedGender = (ISelectGender)getActivity();
            selectedGender.onGenderSelected(false);
            dismiss();
        }
    }*/

    private void initViews(View view){
        mImgSelectedMale = view.findViewById(R.id.user_gender_img_selected_male);
        mImgUnselectedMale = view.findViewById(R.id.user_gender_img_unselected_male);
        mImgSelectedFemale = view.findViewById(R.id.user_gender_img_selected_female);
        mImgUnselectedFemale = view.findViewById(R.id.user_gender_img_unselected_female);

        ISelectGender selectGender = (ISelectGender)getActivity();
        int genderType = selectGender.genderType();
        switch (genderType) {
            case 0:
                mImgSelectedMale.setVisibility(View.GONE);
                mImgSelectedFemale.setVisibility(View.GONE);
                mImgUnselectedMale.setVisibility(View.VISIBLE);
                mImgUnselectedFemale.setVisibility(View.VISIBLE);
                break;
            case 1:
                mImgSelectedMale.setVisibility(View.GONE);
                mImgSelectedFemale.setVisibility(View.VISIBLE);
                mImgUnselectedMale.setVisibility(View.VISIBLE);
                mImgUnselectedFemale.setVisibility(View.GONE);
                break;
            case 2:
                mImgSelectedMale.setVisibility(View.VISIBLE);
                mImgSelectedFemale.setVisibility(View.GONE);
                mImgUnselectedMale.setVisibility(View.GONE);
                mImgUnselectedFemale.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setOnClickListener() {
        mImgSelectedMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("TAG", "onClick: " + "选择男性");
                ISelectGender selectedGender = (ISelectGender)getActivity();
                selectedGender.onGenderSelected(true);
                dismiss();
            }
        });

        mImgUnselectedMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("TAG", "onClick: " + "选择男性");
                ISelectGender selectedGender = (ISelectGender)getActivity();
                selectedGender.onGenderSelected(true);
                dismiss();
            }
        });

        mImgSelectedFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("TAG", "onClick: " + "选择女性");
                ISelectGender selectedGender = (ISelectGender)getActivity();
                selectedGender.onGenderSelected(false);
                dismiss();
            }
        });

        mImgUnselectedFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("TAG", "onClick: " + "选择女性");
                ISelectGender selectedGender = (ISelectGender)getActivity();
                selectedGender.onGenderSelected(false);
                dismiss();
            }
        });
    }

}
