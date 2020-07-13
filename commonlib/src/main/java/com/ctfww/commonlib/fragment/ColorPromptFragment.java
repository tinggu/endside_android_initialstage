package com.ctfww.commonlib.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.ctfww.commonlib.R;

public class ColorPromptFragment extends Fragment {
    private final static String TAG = "ColorPromptFragment";

    private View mV;

    private LinearLayout mLL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.color_prompt_fragment, container, false);
        initViews(mV);

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mLL = v.findViewById(R.id.ll);
        mLL.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setVisibility(int type) {
        mLL.setVisibility(type);
    }
}
