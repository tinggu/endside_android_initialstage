package com.ctfww.module.keyevents.fragment;

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
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;
import com.ctfww.module.keyevents.datahelper.airship.Airship;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.UserInfo;

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

    private KeyEvent mKeyEvent;

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
                if (mKeyEvent == null) {
                    return;
                }

                ARouter.getInstance().build("/keyevents/keyevent").withString("key_event", GsonUtils.toJson(mKeyEvent)).navigation();
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

    public void refresh() {
        List<KeyEvent> keyEventList = DBQuickEntry.getDoingList();
        if (!keyEventList.isEmpty()) {
            updateKeyEventToUI(keyEventList, false);
            return;
        }

        keyEventList = DBQuickEntry.getCanSnatchList();
        updateKeyEventToUI(keyEventList, true);

        if (!keyEventList.isEmpty()) {
            mKeyEvent = keyEventList.get(0);
        }
    }

    public void snatch() {
        if (mKeyEvent == null) {
            return;
        }

        KeyEventTrace keyEventTrace = DBHelper.getInstance().getKeyEventTrace(mKeyEvent.getEventId(), System.currentTimeMillis());
        if (keyEventTrace == null || "end".equals(keyEventTrace.getStatus()) || "snatch".equals(keyEventTrace.getStatus()) || "accepted".equals(keyEventTrace.getStatus())) {
            refresh();
            return;
        }

        keyEventTrace.setStatus("snatch");
        keyEventTrace.setTimeStamp(System.currentTimeMillis());
        keyEventTrace.setSynTag("modify");

        DBHelper.getInstance().updateKeyEventTrace(keyEventTrace);
        Airship.getInstance().synToCloud();
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

        String deskName = "[" + keyEvent.getDeskId() + "]";
        DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(keyEvent.getGroupId(), keyEvent.getDeskId());
        if (deskInfo != null) {
            deskName += " " + deskInfo.getDeskName();
        }
        mDeskName.setText(deskName);

        mEventName.setText(keyEvent.getEventName());

        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keyEvent.getUserId());
        String nickName = userInfo == null ? "" : userInfo.getNickName();
        mNickName.setText(nickName);

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

    public boolean isDoing() {
        return mSnatch.getVisibility() == View.GONE;
    }
}
