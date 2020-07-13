package com.guoliang.module.keyevents.fragment;

import android.os.Bundle;
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
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.module.keyevents.Entity.KeyEvent;
import com.guoliang.module.keyevents.R;
import com.guoliang.module.keyevents.datahelper.NetworkHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KeyEventSnatchFragment extends Fragment {
    private final static String TAG = "KeyEventSnatchFragment";

    private View mV;

    private TextView mMore;

    private TextView mNoData;

    private LinearLayout mLL;
    private TextView mDeskName;
    private TextView mEventName;
    private TextView mNickName;
    private TextView mPrompt;
    private ImageView mSnatch;

    private TextView mDateTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mV = inflater.inflate(R.layout.keyevent_snatch_fragment, container, false);
        initViews(mV);
        setOnClickListener();

        LogUtils.i(TAG, "onCreateView");
        return mV;
    }

    private void initViews(View v) {
        mMore = v.findViewById(R.id.keyevent_more);

        mNoData = v.findViewById(R.id.keyevent_no_data);

        mLL = v.findViewById(R.id.keyevent_snatch_ll);
        mDeskName = v.findViewById(R.id.keyevent_desk_name);
        mNickName = v.findViewById(R.id.keyevent_nick_name);
        mEventName = v.findViewById(R.id.keyevent_event_name);
        mPrompt = v.findViewById(R.id.keyevent_process_prompt);
        mSnatch = v.findViewById(R.id.keyevent_snatch);
        mDateTime = v.findViewById(R.id.keyevent_date_time);

        mNoData.setVisibility(View.VISIBLE);
        mLL.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        mLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mKeyEventList.isEmpty()) {
                    return;
                }

                ARouter.getInstance().build("/keyevents/keyevent").navigation();
                EventBus.getDefault().postSticky(new MessageEvent("view_key_event", GsonUtils.toJson(mKeyEventList.get(0))));
            }
        });

        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mSnatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snatch();
            }
        });
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

    public void getDoingKeyEvent() {
        final int count = 1;
        NetworkHelper.getInstance().getSomeOneDoingKeyEvent(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeyEventList = (List<KeyEvent>)obj;
                updateKeyEventToUI(mKeyEventList, false);
                if (mKeyEventList.isEmpty()) {
                    getCanBeSnatchedKeyEvent();
                }
                LogUtils.i(TAG, "getDoingKeyEvent success: mKeyEventList.size() = " + mKeyEventList.size());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getDoingKeyEvent fail: code = " + code);
            }
        });
    }

    private List<KeyEvent> mKeyEventList = new ArrayList<>();
    public void getCanBeSnatchedKeyEvent() {
        final int count = 1;
        NetworkHelper.getInstance().getCanBeSnatchedKeyEvent(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeyEventList = (List<KeyEvent>)obj;
                updateKeyEventToUI(mKeyEventList, true);
                LogUtils.i(TAG, "getCanBeSnatchedKeyEvent success: mKeyEventList.size() = " + mKeyEventList.size());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getCanBeSnatchedKeyEvent fail: code = " + code);
            }
        });
    }

    public void snatch() {
        if (mKeyEventList.isEmpty()) {
            return;
        }

        KeyEvent keyEvent = mKeyEventList.get(0);
        NetworkHelper.getInstance().snatchKeyEvent(keyEvent.getEventId(), keyEvent.getDeskId(), new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                LogUtils.i(TAG, "snatch success!");
                getDoingKeyEvent();
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "snatch fail: code = " + code);
            }
        });
    }

    private void updateKeyEventToUI(List<KeyEvent> keyEventList, boolean isSnatch) {
        if (keyEventList.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mLL.setVisibility(View.GONE);
            mSnatch.setVisibility(View.GONE);
            mPrompt.setVisibility(View.GONE);
            mMore.setVisibility(View.GONE);
            return;
        }

        if (keyEventList.size() <= 1) {
            mMore.setVisibility(View.GONE);
        }
        else {
            mMore.setVisibility(View.VISIBLE);
        }

        if (isSnatch) {
            mSnatch.setVisibility(View.VISIBLE);
            mPrompt.setVisibility(View.GONE);
        }
        else {
            mSnatch.setVisibility(View.GONE);
            mPrompt.setVisibility(View.VISIBLE);
        }

        mNoData.setVisibility(View.GONE);
        mLL.setVisibility(View.VISIBLE);

        KeyEvent keyEvent = keyEventList.get(0);

//        LogUtils.i(TAG, "updateKeyEventToUI: keyEvent = " + keyEvent.toString());

        mDeskName.setText("[" + keyEvent.getDeskId() + "]" + "  " + keyEvent.getDeskName());
        mEventName.setText(keyEvent.getEventName());
        mNickName.setText(keyEvent.getNickName());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(keyEvent.getTimeStamp());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = String.format("%04d-%02d-%02d %02d:%02d", year, month, day, hour, minute);
        mDateTime.setText(time);
    }
}
