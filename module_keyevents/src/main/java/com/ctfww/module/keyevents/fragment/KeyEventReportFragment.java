package com.ctfww.module.keyevents.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.ctfww.commonlib.location.MyLocation;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.commonlib.utils.VoiceUtils;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.Entity.KeyEventTrace;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.activity.EventPreviewActivity;
import com.ctfww.module.keyevents.activity.SoundRecord2Activity;
import com.ctfww.module.keyevents.datahelper.dbhelper.DBHelper;
import com.ctfww.module.keyevents.datahelper.airship.Airship;
import com.ctfww.module.user.entity.UserInfo;

import java.io.File;

public class KeyEventReportFragment extends Fragment{

    private static final String TAG = "KeyEventReportFragment";

    private static final int TACK_PHOTO =10001;
    private static final int TACK_RECORD =10002;
    private static final int TACK_VOICE =10003;

    private EditText mName;
    private EditText mDesc;

    private ImageView mAddPic;
    private ImageView mAddVideo;
    private ImageView mAddVoice;

    private ImageView mPic;
    private VideoView mVideo;
    private ImageView mVoice;

    private String mEventId;
    private int mDeskId = 0;

    private int mType = 0; // 暂不区分关键事件类型


    public KeyEventReportFragment() {
    }

    public static KeyEventReportFragment newInstance() {
        return new KeyEventReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.keyevent_report_fragment, container, false);

        mEventId = GlobeFun.getSHA(SPStaticUtils.getString("user_open_id") + System.currentTimeMillis());
        initViews(view);
        setOnClickListener();

        return view;
    }

    private void initViews(View view) {
        mName = view.findViewById(R.id.keyevents_key_event_name);
        mDesc = view.findViewById(R.id.keyevents_key_event_desc);

        mAddPic = view.findViewById(R.id.keyevent_add_pic);
        mAddVideo = view.findViewById(R.id.keyevent_add_video);
        mAddVoice = view.findViewById(R.id.keyevent_add_voice);

        mPic = view.findViewById(R.id.keyevent_pic);
        mVideo = view.findViewById(R.id.keyevent_video);
        mVoice = view.findViewById(R.id.keyevent_voice);
    }

    private void setOnClickListener() {
        mAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processTakePic();
            }
        });

        mAddVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processVoiceRecord();
            }
        });

        mAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processVideoRecord();
            }
        });

        mPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                processTakePic();
                return true;
            }
        });

        mVideo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                processVideoRecord();
                return true;
            }
        });

        mVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                processVoiceRecord();
                return true;
            }
        });

        mPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getPicFilePath());
                if (file.exists()) {
                    Intent intent = new Intent(getContext(), EventPreviewActivity.class);
                    intent.putExtra("img_path", getPicFilePath());
                    startActivity(intent);
                } else {
                    ToastUtils.showShort("未添加图片");
                }
            }
        });

        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getVideoFilePath());
                if (file.exists()) {
                    Log.i(TAG, "onClick: click video!");
                    Intent intent = new Intent(getContext(), EventPreviewActivity.class);
                    intent.putExtra("video_path", getVideoFilePath());
                    startActivity(intent);
                } else {
                    ToastUtils.showShort("未添加视频");
                }
            }
        });

        mVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getVoiceFilePath());
                if (!file.exists()) {
                    return;
                }

                VoiceUtils.playVoice(getVoiceFilePath());
            }
        });
    }

    // 拍照片
    private void processTakePic() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        File tempFile = new File(getPicFilePath());
        if (!tempFile.getParentFile().exists()) {
            if (!tempFile.getParentFile().mkdir()) {
                LogUtils.e(TAG, "make picFilePath failed");
                return;
            }
        }

        Uri filePUri = FileProvider.getUriForFile(getContext(),"com.ctfww.commonlib.utils" + ".myprovider", tempFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePUri);
        startActivityForResult(openCameraIntent, TACK_PHOTO);
    }

    // 拍视频
    private void processVideoRecord() {
        Intent openVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        openVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        File tempFile = new File(getVideoFilePath());
        if (!tempFile.getParentFile().exists()) {
            if (!tempFile.getParentFile().mkdir()) {
                LogUtils.e(TAG, "make videoFilePath failed");
            }
        }

        Uri fileVUri = FileProvider.getUriForFile(getContext(),"com.ctfww.commonlib.utils" + ".myprovider", tempFile);
        openVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileVUri);
        openVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);
        openVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT , 10);
        startActivityForResult(openVideoIntent, TACK_RECORD);
    }

    // 录音
    private void processVoiceRecord() {
        startActivityForResult(new Intent(getContext(), SoundRecord2Activity.class), TACK_VOICE);
    }

    private void compressPic(String mPicFilePath) {
        Bitmap bitmap = ImageUtils.getBitmap(new File(mPicFilePath));
        bitmap = ImageUtils.compressByScale(bitmap, 0.25f, 0.25f);
        ImageUtils.save(bitmap, new File(mPicFilePath), Bitmap.CompressFormat.JPEG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TACK_VOICE) { // 保存录音文件
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int duration = data.getIntExtra("duration", 0);
            if (0 == duration) {
                return;
            }

            String name = data.getStringExtra("name");
            if (!TextUtils.isEmpty(name)) {
                int index = name.lastIndexOf('/');
                if (-1 == index) {
                    return;
                }

                File oldFile = new File(getVoiceFilePath());
                if (oldFile.exists()) {
                    if (!oldFile.delete()) {
                        LogUtils.e(TAG, "delete file failed = " + oldFile.getName());
                    }
                }

                File file = new File(name);
                if (file.exists()) {
                    if (!file.renameTo(new File(getVoiceFilePath()))) {
                        LogUtils.e(TAG, "rename file failed = " + file.getName());
                    }
                }

                mAddVoice.setVisibility(View.GONE);
                mVoice.setVisibility(View.VISIBLE);

//                mVoice.setText(String.valueOf(duration + "\""));
            }
        } else if (requestCode == TACK_PHOTO) { // 确认图片是否添加成功
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            File file = new File(getPicFilePath());
            if (!file.exists()) {
                return;
            }

            mAddPic.setVisibility(View.GONE);
            mPic.setVisibility(View.VISIBLE);
            Glide.with(this).load(new File(getPicFilePath())).into(mPic);

            // 图片压缩
            compressPic(getPicFilePath());

        } else if (requestCode == TACK_RECORD) { // 确认视频是否添加成功
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            File file = new File(getVideoFilePath());
            if (!file.exists()) {
                return;
            }

            mAddVideo.setVisibility(View.GONE);
            mVideo.setVisibility(View.VISIBLE);

            mVideo.setVideoPath(getVideoFilePath());
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
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();;
    }

    private boolean canReport() {
        return TextUtils.isEmpty(mName.getText().toString()) ? false : true;
    }

    // 上报事件
    public boolean reportKeyEvent() {
        if (!canReport()) {
            DialogUtils.onlyPrompt("上报事件必须有标题！", getContext());
            return false;
        }

        KeyEvent keyEvent = new KeyEvent();
        keyEvent.setEventId(mEventId);
        keyEvent.setTimeStamp(System.currentTimeMillis());
        keyEvent.setType(mType);
        keyEvent.setEventName(mName.getText().toString());
        keyEvent.setDescription(mDesc.getText().toString());
        Location location = MyLocation.getLocation(getContext());
        if (location != null) {
            keyEvent.setLat(location.getLatitude());
            keyEvent.setLng(location.getLongitude());
            keyEvent.setAddress(MyLocation.getAddr(getContext(), location));
        }
        int deskId = SPStaticUtils.getInt("curr_desk_id");
        keyEvent.setDeskId(deskId);
        keyEvent.setVoicePath(getVoiceFilePath());
        keyEvent.setPicPath(getPicFilePath());
        keyEvent.setVideoPath(getVideoFilePath());
        keyEvent.setGroupId(SPStaticUtils.getString("working_group_id"));
        keyEvent.setStatus("create");

        keyEvent.setUserId(SPStaticUtils.getString("user_open_id"));

        keyEvent.setSynTag("new");
        DBHelper.getInstance().addKeyEvent(keyEvent);
        Airship.getInstance().synKeyEventToCloud();

        KeyEventTrace keyEventTrace = new KeyEventTrace();
        keyEventTrace.setEventId(keyEvent.getEventId());
        keyEventTrace.setTimeStamp(keyEvent.getTimeStamp());
        keyEventTrace.setGroupId(keyEvent.getGroupId());
        keyEventTrace.setDeskId(keyEvent.getDeskId());
        keyEventTrace.setMatchLevel("default");
        UserInfo userInfo = com.ctfww.module.user.datahelper.DataHelper.getInstance().getUserInfo();
        if (userInfo == null) {
            return true;
        }

        keyEventTrace.setNickName(userInfo.getUserId());
        keyEventTrace.setNickName(userInfo.getNickName());
        keyEventTrace.setHeadUrl(userInfo.getHeadUrl());
        keyEventTrace.setStatus("create");
        keyEventTrace.setSynTag("new");

        DBHelper.getInstance().addKeyEventTrace(keyEventTrace);

        return true;
    }

    public void setDeskId(int deskId) {
        mDeskId = deskId;
    }

    private String getPicFilePath() {
        return getContext().getExternalFilesDir("") + "/" + "photo" + "/" +mEventId+".jpg";
    }

    private String getVideoFilePath() {
        return getContext().getExternalFilesDir("") + "/" + "video" + "/" +mEventId+".mp4";
    }

    private String getVoiceFilePath() {
        return getContext().getExternalFilesDir("") + "/" + "sound" + "/" +mEventId+".voice";
    }
}
