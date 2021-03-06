package com.ctfww.module.keyevents.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.bumptech.glide.Glide;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.commonlib.utils.VoiceUtils;
import com.ctfww.module.desk.entity.DeskInfo;
import com.ctfww.module.desk.entity.RouteSummary;
import com.ctfww.module.fingerprint.entity.DistResult;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.adapter.KeyEventTraceListAdapter;
import com.ctfww.module.keyevents.datahelper.NetworkHelper;
import com.ctfww.module.keyevents.datahelper.airship.Airship;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.sp.Const;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Route(path = "/keyevents/keyevent")
public class KeyEventActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "KeyEventActivity";

    private ImageView mBack;
    private TextView mTittle;

    private TextView mResponsible;
    private TextView mCurrentStatus;

    private TextView mDateTime;
    private TextView mCreate;
    private TextView mDeskName;
    private TextView mEventName;

    private ImageView mPic;
    private ImageView mSounnd;
    private ImageView mVideoImg;
    private VideoView mVideo;

    private RecyclerView mKeyEventTraceListView;
    private KeyEventTraceListAdapter mKeyEventTraceListAdapter;
    private List<KeyEventTrace> mKeyEventTraceList = new ArrayList<>();

    private LinearLayout mOperateLL;
    private TextView mSnatch;
    private TextView mFree;
    private TextView mTransfer;
    private TextView mAssignment;
    private TextView mAccept;
    private TextView mEnd;

    private KeyEvent mKeyEvent;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.key_event_activity);

        initViews();

        setOnClickListener();

        EventBus.getDefault().register(this);

        com.ctfww.module.fingerprint.Utils.startScan("calc");

        processIntent();
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("事件详情");

        mResponsible = findViewById(R.id.keyevent_responsible_nick_name);
        mCurrentStatus = findViewById(R.id.keyevent_current_status);

        mDateTime = findViewById(R.id.keyevent_date_time);
        mCreate = findViewById(R.id.keyevent_create_nick_name);
        mDeskName = findViewById(R.id.keyevent_desk_name);
        mEventName = findViewById(R.id.keyevent_event_name);

        mPic = findViewById(R.id.key_event_pic);
        mSounnd = findViewById(R.id.key_event_voice);
        mVideoImg = findViewById(R.id.key_event_video_img);
        mVideo = findViewById(R.id.key_event_video);

        mKeyEventTraceListView = findViewById(R.id.keyevent_trace_list);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mKeyEventTraceListView.setLayoutManager(layoutManager);
        mKeyEventTraceListAdapter = new KeyEventTraceListAdapter(mKeyEventTraceList);
        mKeyEventTraceListView.setAdapter(mKeyEventTraceListAdapter);

        mOperateLL = findViewById(R.id.keyevent_action_operate);
        mSnatch = findViewById(R.id.keyevent_snatch);
        mFree = findViewById(R.id.keyevent_free);
        mTransfer = findViewById(R.id.keyevent_transfer);
        mAssignment = findViewById(R.id.keyevent_assignment);
        mAccept = findViewById(R.id.keyevent_accept);
        mEnd = findViewById(R.id.keyevent_end);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mPic.setOnClickListener(this);
        mSounnd.setOnClickListener(this);
        mVideo.setOnClickListener(this);
        mTransfer.setOnClickListener(this);
        mSnatch.setOnClickListener(this);
        mFree.setOnClickListener(this);
        mAssignment.setOnClickListener(this);
        mAccept.setOnClickListener(this);
        mEnd.setOnClickListener(this);
    }

    private void processIntent() {
        KeyEvent keyEvent = GsonUtils.fromJson(getIntent().getStringExtra("key_event"), KeyEvent.class);
        if (keyEvent == null) {
            return;
        }

        showEventInfoInUI(mKeyEvent);
        getActionList(mKeyEvent.getEventId());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            LogUtils.i(TAG, "onClick: id == mBack.getId()");
            finish();
        }
        else if (id == mPic.getId()) {
            if (TextUtils.isEmpty(mKeyEvent.getPicPath())) {
                return;
            }

            LogUtils.i(TAG, "onClick: id == mPic.getId()");
            Intent intent = new Intent(this, EventPreviewActivity.class);
            intent.putExtra("img_path", mKeyEvent.getPicPath());
            startActivity(intent);
        }
        else if (id == mSounnd.getId()) {
            if (TextUtils.isEmpty(mKeyEvent.getVoicePath())) {
                return;
            }

            VoiceUtils.playVoiceByUrl(mKeyEvent.getVoicePath());
        }
        else if (id == mVideo.getId()) {
            if (TextUtils.isEmpty(mKeyEvent.getVideoPath())) {
                return;
            }

            Intent intent = new Intent(this, EventPreviewActivity.class);
            intent.putExtra("video_path", mKeyEvent.getVideoPath());
            startActivity(intent);
        }
        else if (id == mTransfer.getId()) {
            ARouter.getInstance().build("/user/selectUser").navigation();
        }
        else if (id == mAssignment.getId()) {
            ARouter.getInstance().build("/user/selectUser").navigation();
        }
        else if (id == mSnatch.getId()) {
            newKeyEventTrace("snatch");
        }
        else if (id == mFree.getId()) {
            newKeyEventTrace("free");
        }
        else if (id == mAccept.getId()) {
            newKeyEventTrace("accepted");
        }
        else if (id == mEnd.getId()) {
            newKeyEventTrace("end");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if ("selected_user".equals(messageEvent.getMessage())) {
            String userId = messageEvent.getValue();
            if (mAssignment.getVisibility() == View.VISIBLE || mTransfer.getVisibility() == View.VISIBLE) { // 要分派给某人
                newKeyEventTrace("received");
            }
        }
    }

    private void showEventInfoInUI(KeyEvent keyEvent) {
        long timeStamp = keyEvent.getTimeStamp();
        mDateTime.setText(GlobeFun.stampToDateTime(timeStamp));
        UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keyEvent.getUserId());
        if (userInfo != null) {
            mCreate.setText(userInfo.getNickName());
        }

        if (keyEvent.getType() < 100) {
            DeskInfo deskInfo = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getDesk(keyEvent.getGroupId(), keyEvent.getObjectId());
            if (deskInfo != null) {
                mDeskName.setText(deskInfo.getIdName());
            }
        }
        else {
            RouteSummary routeSummary = com.ctfww.module.desk.datahelper.dbhelper.DBHelper.getInstance().getRouteSummary(keyEvent.getGroupId(), keyEvent.getObjectId());
            if (routeSummary != null) {
                mDeskName.setText(routeSummary.getIdName());
            }
        }

        mEventName.setText(keyEvent.getEventName());
        if (!TextUtils.isEmpty(mKeyEvent.getPicPath())) {
            Glide.with(this).load(mKeyEvent.getPicPath()).into(mPic);
        }

        if (!TextUtils.isEmpty(mKeyEvent.getVoicePath())) {
            mSounnd.setImageResource(R.drawable.keyevent_has_voice);
        }

        showVideo();
    }

    private void getActionList(String eventId) {
        LogUtils.i(TAG, "getActionList: eventId = " + eventId);
        NetworkHelper.getInstance().getKeyEventActionList(eventId, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeyEventTraceList = (List<KeyEventTrace>)obj;
                setCurrentProcessStatus(mKeyEventTraceList);
                showActionButton(mKeyEventTraceList);
                mKeyEventTraceListAdapter.setList(mKeyEventTraceList);
                mKeyEventTraceListAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getActionList success: mKeyEventTraceList.size()" + mKeyEventTraceList.size());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getActionList fail: code = " + code);
            }
        });
    }

    private void newKeyEventTrace(String status) {
        String groupId = SPStaticUtils.getString(Const.WORKING_GROUP_ID);
        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) {
            return;
        }

        KeyEventTrace keyEventTrace = new KeyEventTrace();
        keyEventTrace.setGroupId(groupId);
        keyEventTrace.setUserId(userId);
        keyEventTrace.setStatus(status);
        keyEventTrace.setObjectId(mKeyEvent.getObjectId());
        keyEventTrace.setEventId(mKeyEvent.getEventId());
        keyEventTrace.setTimeStamp(System.currentTimeMillis());
        keyEventTrace.setType(mKeyEvent.getType() < 100 ? "desk" : "route");
        keyEventTrace.setSynTag("new");

        keyEventTrace.setMatchLevel("default");
        if ("end".equals(keyEventTrace.getStatus()) && "desk".equals(keyEventTrace.getType())) {
            String print = com.ctfww.module.fingerprint.Utils.getWifiCalculateFingerPrint();
            DistResult distResult = com.ctfww.module.fingerprint.Utils.getWifiDist(print, mKeyEvent.getObjectId());
            String matchLevel = distResult.getStringMatchLevel();
            keyEventTrace.setMatchLevel(matchLevel);
        }

        DBHelper.getInstance().addKeyEventTrace(keyEventTrace);
        Airship.getInstance().synKeyEventTraceToCloud();

        mKeyEvent.setStatus(keyEventTrace.getStatus());
        mKeyEvent.setTimeStamp(keyEventTrace.getTimeStamp());
        mKeyEvent.setUserId(keyEventTrace.getUserId());
        if ("new".equals(mKeyEvent.getSynTag())) {
            mKeyEvent.setSynTag("modify");
        }

        DBHelper.getInstance().updateKeyEvent(mKeyEvent);
        Airship.getInstance().synKeyEventToCloud();
    }

    private void setCurrentProcessStatus(List<KeyEventTrace> keyEventTraceList) {
        if (keyEventTraceList.isEmpty()) {
            return;
        }

        if (keyEventTraceList.size() == 1) {
            mResponsible.setText("群组成员");
            mCurrentStatus.setText("抢单或分配中");
        }
        else {
            KeyEventTrace keyEventTrace = keyEventTraceList.get(keyEventTraceList.size() - 1);
            UserInfo userInfo = com.ctfww.module.user.datahelper.dbhelper.DBHelper.getInstance().getUser(keyEventTrace.getUserId());
            String nickName = userInfo == null ? "" : userInfo.getNickName();
            mResponsible.setText(nickName);
            if ("end".equals(keyEventTrace.getStatus())) {
                mResponsible.setText(nickName);
                mCurrentStatus.setText("已完成");
            }
            else if ("accepted".equals(keyEventTrace.getStatus())) {
                mCurrentStatus.setText("处理中");
            }
            else if ("received".equals(keyEventTrace.getStatus())) {
                mCurrentStatus.setText("领取任务中");
            }
            else if ("free".equals(keyEventTrace.getStatus())) {
                mResponsible.setText("群组成员");
                mCurrentStatus.setText("抢单或分配中");
            }
        }
    }

    private void showActionButton(List<KeyEventTrace> keyEventTraceList) {
        if (keyEventTraceList.isEmpty()) {
            mOperateLL.setVisibility(View.GONE);
            return;
        }

        String userId = SPStaticUtils.getString(Const.USER_OPEN_ID);
        String role = com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry.getRoleInWorkingGroup();
        KeyEventTrace keyEventTrace = keyEventTraceList.get(keyEventTraceList.size() - 1);
        if ("end".equals(keyEventTrace.getStatus())) {
            mOperateLL.setVisibility(View.GONE);
        }
        else if ("create".equals(keyEventTrace.getStatus()) || "free".equals(keyEventTrace.getStatus())) {
            mOperateLL.setVisibility(View.VISIBLE);
            mSnatch.setVisibility(View.VISIBLE);
            mFree.setVisibility(View.GONE);
            if ("admin".equals(role)) {
                mAssignment.setVisibility(View.VISIBLE);
            }
            else {
                mAssignment.setVisibility(View.GONE);
            }
            mTransfer.setVisibility(View.GONE);
            mAccept.setVisibility(View.GONE);
            mEnd.setVisibility(View.GONE);
        }
        else {
            if (!userId.equals(keyEventTrace.getUserId())) {
                mOperateLL.setVisibility(View.GONE);
            }
            else {
                if ("accepted".equals(keyEventTrace.getStatus()) || "snatch".equals(keyEventTrace.getStatus())) {
                    mOperateLL.setVisibility(View.VISIBLE);
                    mSnatch.setVisibility(View.GONE);
                    mFree.setVisibility(View.VISIBLE);
                    mAssignment.setVisibility(View.GONE);
                    mTransfer.setVisibility(View.VISIBLE);
                    mAccept.setVisibility(View.GONE);
                    mEnd.setVisibility(View.VISIBLE);
                }
                else if ("received".equals(keyEventTrace.getStatus())) {
                    mOperateLL.setVisibility(View.VISIBLE);
                    mSnatch.setVisibility(View.GONE);
                    mFree.setVisibility(View.GONE);
                    mAssignment.setVisibility(View.GONE);
                    mTransfer.setVisibility(View.GONE);
                    mAccept.setVisibility(View.VISIBLE);
                    mEnd.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private String getPicPathName() {
        return getExternalFilesDir("") + "/" + "photo" + "/" + mKeyEvent.getEventId() +".jpg";
    }

    private String getVoicePathName() {
        return getExternalFilesDir("") + "/" + "sound" + "/" + mKeyEvent.getEventId() +".voice";
    }

    private String getVideoPathName() {
        return getExternalFilesDir("") + "/" + "video" + "/" + mKeyEvent.getEventId() +".mp4";
    }

    private String getPicPath() {
        return getExternalFilesDir("") + "/" + "photo" ;
    }

    private String getVoicePath() {
        return getExternalFilesDir("") + "/" + "sound";
    }

    private String getVideoPath() {
        return getExternalFilesDir("") + "/" + "video";
    }

    private String getPicName() {
        return "" + mKeyEvent.getEventId() +".jpg";
    }

    private String getVoiceName() {
        return "" + mKeyEvent.getEventId() +".voice";
    }

    private String getVideoName() {
        return "" + mKeyEvent.getEventId() +".mp4";
    }

    private void showPic(File file) {
        Glide.with(this).load(file).into(mPic);
    }

    private void showVideo() {
        if (TextUtils.isEmpty(mKeyEvent.getVideoPath())) {
            return;
        }

        mVideoImg.setVisibility(View.GONE);

 //       mVideo.setVideoPath(getVideoPathName());
        mVideo.setVideoPath(mKeyEvent.getVideoPath());
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mVideo.start();
            }
        });
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVideo.start();
            }
        });
        mVideo.setVisibility(View.VISIBLE);
    }
}
