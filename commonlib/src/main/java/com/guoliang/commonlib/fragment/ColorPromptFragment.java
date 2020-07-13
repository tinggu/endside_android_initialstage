package com.guoliang.commonlib.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.guoliang.commonlib.R;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.FileInfo;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.entity.MyDateTimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
