package com.ctfww.module.keyevents.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.bumptech.glide.Glide;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.location.MyLocation;
import com.ctfww.commonlib.utils.DialogUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.commonlib.utils.PermissionUtils;
import com.ctfww.module.keyevents.datahelper.DBHelper;
import com.ctfww.module.keyevents.datahelper.SynData;
import com.ctfww.module.keyevents.Entity.KeyEvent;
import com.ctfww.module.keyevents.R;
import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.IAudioPlayListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

@Route(path = "/keyevents/reportKeyEvent")
public class ReportKeyEventActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private final static String TAG = "ReportKeyEventActivity";

    private static final int TACK_PHOTO =10001;
    private static final int TACK_RECORD =10002;
    private static final int TACK_VOICE =10003;

    private EditText mName;
    private EditText mDesc;

    private Button mTakePic;
    private Button mSoundRecord;
    private Button mVideoRecord;
    private Button mReport;

    private ImageView mPicImg;
    private ImageView mVideoImg;
    private VideoView mVideoView;

    private String mSoundFilePath = "";
    private String mPicFilePath = "";
    private String mVideoFilePath = "";

    private String mEventId;

    private int mType = 0; // 暂不区分关键事件类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyevents_report_activity);
        mEventId = GlobeFun.getSHA(SPStaticUtils.getString("open_id") + System.currentTimeMillis());
        initViews();
        setOnClickListener();
        setOnLongClickListener();
    }

    private void initViews() {
        ImageView mBack = findViewById(R.id.top_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView mTopTittle = findViewById(R.id.top_tittle);
        mTopTittle.setText("上报事件");

        mName = findViewById(R.id.keyevents_key_event_name);
        mDesc = findViewById(R.id.keyevents_key_event_desc);

        mTakePic = findViewById(R.id.keyevents_take_pic);
        mSoundRecord = findViewById(R.id.keyevents_sound_record);
        mVideoRecord = findViewById(R.id.keyevents_video_record);
        mReport = findViewById(R.id.keyevents_blue_btn);

        mPicImg = findViewById(R.id.key_events_pic_img);
        mVideoImg = findViewById(R.id.key_events_video_img);
        mVideoView = findViewById(R.id.key_events_video_view);
    }

    private void setOnClickListener() {
        mTakePic.setOnClickListener(this);
        mSoundRecord.setOnClickListener(this);
        mVideoRecord.setOnClickListener(this);
        mReport.setOnClickListener(this);

        mPicImg.setOnClickListener(this);
        mVideoView.setOnClickListener(this);
    }

    private void setOnLongClickListener() {
        mSoundRecord.setOnLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PermissionUtils.requestCameraPermission(this);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: enter");
        int id = v.getId();
        if (id == mTakePic.getId()) { // 拍照片
            processTakePic();
        }
        else if (id == mVideoRecord.getId()) { // 拍视频
            processVideoRecord();
        }
        else if (id == mSoundRecord.getId()) { // 录音
            processVoiceRecord();
        }
        else if (id == mReport.getId()) { // 上报事件
            reportKeyEvent();
        }
        else if (id == mPicImg.getId()) { // 点击图片
            if (mTakePic.getText().equals("图片已添加")) {
                Intent intent = new Intent(this, EventPreviewActivity.class);
                intent.putExtra("img_path", mPicFilePath);
                startActivity(intent);
            } else {
                ToastUtils.showShort("未添加图片");
            }
        }
        else if (id == mVideoView.getId()) { // 点击视频
            if (mVideoRecord.getText().equals("视频已添加")) {
                Log.i(TAG, "onClick: click video!");
                Intent intent = new Intent(this, EventPreviewActivity.class);
                intent.putExtra("video_path", mVideoFilePath);
                startActivity(intent);
            } else {
                ToastUtils.showShort("未添加视频");
            }
        }
    }

    // 拍照片
    private void processTakePic() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        mPicFilePath = getExternalFilesDir("") + "/" + "photo" + "/" +mEventId+".jpg";
        File tempFile = new File(mPicFilePath);

        if (tempFile.getParentFile() == null) {
            return;
        }
        if (!tempFile.getParentFile().exists()) {
            if (!tempFile.getParentFile().mkdir()) {
                LogUtils.e(TAG, "make mPicFilePath failed");
            }
        }

        Uri filePUri = FileProvider.getUriForFile(this,"com.ctfww.commonlib.utils" + ".myprovider", tempFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePUri);
        startActivityForResult(openCameraIntent, TACK_PHOTO);
    }

    // 拍视频
    private void processVideoRecord() {
        Intent openVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        openVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        mVideoFilePath = getExternalFilesDir("") + "/" + "video" + "/" +mEventId+".mp4";
        File tempFile = new File(mVideoFilePath);

        if (tempFile.getParentFile() == null) {
            return;
        }
        if (!tempFile.getParentFile().exists()) {
            if (!tempFile.getParentFile().mkdir()) {
                LogUtils.e(TAG, "make mVideoFilePath failed");
            }
        }

        Uri fileVUri = FileProvider.getUriForFile(this,"com.ctfww.commonlib.utils" + ".myprovider", tempFile);
        openVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileVUri);
        openVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(openVideoIntent, TACK_RECORD);
    }

    // 录音
    private void processVoiceRecord() {
        startActivityForResult(new Intent(this, SoundRecord2Activity.class), TACK_VOICE);
    }

    // 上报事件
    private void reportKeyEvent() {
        if (!canReport()) {
            DialogUtils.onlyPrompt("上报事件必须有标题！", this);
            return;
        }

        KeyEvent keyEvent = new KeyEvent();
        keyEvent.setEventId(mEventId);
        keyEvent.setTimeStamp(System.currentTimeMillis());
        keyEvent.setType(mType);
        keyEvent.setEventName(mName.getText().toString());
        keyEvent.setDescription(mDesc.getText().toString());
        Location location = MyLocation.getLocation(this);
        if (location != null) {
            keyEvent.setLat(location.getLatitude());
            keyEvent.setLng(location.getLongitude());
            keyEvent.setAddress(MyLocation.getAddr(this, location));
        }
        int deskId = SPStaticUtils.getInt("curr_desk_id");
        keyEvent.setDeskId(deskId);
        keyEvent.setVoicePath(mSoundFilePath);
        keyEvent.setPicPath(mPicFilePath);
        keyEvent.setVideoPath(mVideoFilePath);
        keyEvent.setGroupId(SPStaticUtils.getString("working_group_id"));
        keyEvent.setStatus("create");

        keyEvent.setUserId(SPStaticUtils.getString("user_open_id"));

        keyEvent.setSynTag("new");
        DBHelper.getInstance().addKeyEvent(keyEvent);

        SynData.uploadKeyEvent(keyEvent);

        EventBus.getDefault().post(new MessageEvent("report_event", keyEvent.getEventId()));

        finish();

//        NetworkHelper.getInstance().uploadFile(mPicFilePath, new IUIDataHelperCallback() {
//            @Override
//            public void onSuccess(Object obj) {
//                LogUtils.i(TAG, "onSuccess: upload pic file success");
//                String url = (String)obj;
//                keyEvent.setPicPath(url);
//                NetworkHelper.getInstance().uploadFile(mSoundFilePath, new IUIDataHelperCallback() {
//                    @Override
//                    public void onSuccess(Object obj) {
//                        LogUtils.i(TAG, "onSuccess: upload sound file success");
//                        String url = (String)obj;
//                        keyEvent.setVoicePath(url);
//                        NetworkHelper.getInstance().uploadFile(mVideoFilePath, new IUIDataHelperCallback() {
//                            @Override
//                            public void onSuccess(Object obj) {
//                                LogUtils.i(TAG, "onSuccess: upload video file success");
//                                String url = (String)obj;
//                                keyEvent.setVideoPath(url);
//                                NetworkHelper.getInstance().addKeyEvent(keyEvent, new IUIDataHelperCallback() {
//                                    @Override
//                                    public void onSuccess(Object obj) {
//                                        LogUtils.i(TAG, "onSuccess: add key event success");
//                                        keyEvent.setSynTag(1);
//                                        DBHelper.getInstance().updateKeyEvent(keyEvent);
//                                        ToastUtils.showShort("事件上报成功");
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onError(int code) {
//                                        LogUtils.i(TAG, "onError: add key event failed");
//                                        ToastUtils.showShort("事件上报成功失败，请检查网络");
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onError(int code) {
//                                LogUtils.i(TAG, "onError: upload video file failed");
//                                ToastUtils.showShort("上传视频文件失败，请检查网络");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(int code) {
//                        LogUtils.i(TAG, "onError: upload sound file failed");
//                        ToastUtils.showShort("上传音频文件失败，请检查网络");
//                    }
//                });
//            }
//
//            @Override
//            public void onError(int code) {
//                LogUtils.i(TAG, "onError: upload pic file failed");
//                ToastUtils.showShort("上传图片文件失败，请检查网络");
//            }
//        });
    }

    private void compressPic(String mPicFilePath) {
        Bitmap bitmap = ImageUtils.getBitmap(new File(mPicFilePath));
        bitmap = ImageUtils.compressByScale(bitmap, 0.25f, 0.25f);
        ImageUtils.save(bitmap, new File(mPicFilePath), Bitmap.CompressFormat.JPEG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TACK_VOICE) { // 保存录音文件
            if (resultCode != RESULT_OK) {
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
                if (TextUtils.isEmpty(mSoundFilePath)) {
                    mSoundFilePath = name.substring(0, index);
                    mSoundFilePath += ("/" + mEventId + ".voice");
                }

                File oldFile = new File(mSoundFilePath);
                if (oldFile.exists()) {
                    if (!oldFile.delete()) {
                        LogUtils.e(TAG, "delete file failed = " + oldFile.getName());
                    }
                }

                File file = new File(name);
                if (file.exists()) {
                    if (!file.renameTo(new File(mSoundFilePath))) {
                        LogUtils.e(TAG, "rename file failed = " + file.getName());
                    }
                }
                mSoundRecord.setText(String.valueOf(duration + "\""));
            }
        } else if (requestCode == TACK_PHOTO) { // 确认图片是否添加成功
            if (resultCode != RESULT_OK) {
                return;
            }
            if (TextUtils.isEmpty(mPicFilePath)) {
                return;
            }
            File file = new File(mPicFilePath);
            if (!file.exists()) {
                return;
            }
            mTakePic.setText("图片已添加");

            Glide.with(this).load(new File(mPicFilePath)).into(mPicImg);

            // 图片压缩
            compressPic(mPicFilePath);

        } else if (requestCode == TACK_RECORD) { // 确认视频是否添加成功
            if (resultCode != RESULT_OK) {
                return;
            }
            if (TextUtils.isEmpty(mVideoFilePath)) {
                return;
            }
            File file = new File(mVideoFilePath);
            if (!file.exists()) {
                return;
            }

            mVideoImg.setVisibility(View.GONE);

            mVideoView.setVideoPath(mVideoFilePath);
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mVideoView.start();
                }
            });
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mVideoView.start();
                }
            });
            mVideoView.setVisibility(View.VISIBLE);

            mVideoRecord.setText("视频已添加");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        if (id == mSoundRecord.getId()) { // 长按播放添加的语音内容
            LogUtils.i(TAG, "onLongClick: start play voice");
            if (TextUtils.isEmpty(mSoundFilePath)) {
                return false;
            }

            LogUtils.i(TAG, "onLongClick: path = " + mSoundFilePath);

            AudioPlayManager.getInstance().stopPlay();

            File file = new File(mSoundFilePath);
            if (!file.exists()) {
                return false;
            }
            Uri audioUri = Uri.fromFile(file);
            Log.e("LQR", audioUri.toString());
            AudioPlayManager.getInstance().startPlay(this, audioUri, new IAudioPlayListener() {
                @Override
                public void onStart(Uri var1) {
                }
                @Override
                public void onStop(Uri var1) {
                }
                @Override
                public void onComplete(Uri var1) {
                }
            });
            return true;
        }
        return false;
    }

    private boolean canReport() {
        return TextUtils.isEmpty(mName.getText().toString()) ? false : true;
    }
}
