package com.ctfww.module.keyevents.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.module.keyevents.R;
import com.ctfww.module.keyevents.button.RecordAudioButton;
import com.ctfww.module.keyevents.window.RecordVoicePopWindow;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import kr.co.namee.permissiongen.PermissionGen;

public class SoundRecord2Activity extends AppCompatActivity {

    private static final String TAG = "SoundRecord2Activity";

    private static final int MAX_VOICE_TIME = 20; // 最大录音时间
    private static final String AUDIO_DIR_NAME = "sound";

    private LinearLayout mRoot;
    private RecordAudioButton mBtnVoice;
    private RecordVoicePopWindow mRecordVoicePopWindow;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.keyevents_sound_record2_activity);
        initViews();
        if (isPermissionRepaired()) {
            initVoice();
        }
        else {
            request();
        }
    }

    private void initViews() {
        mRoot = findViewById(R.id.root);
        mBtnVoice = findViewById(R.id.btnVoice);
    }

    private void request() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.RECORD_AUDIO
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.WAKE_LOCK
                        , Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();
    }

    private void initVoice() {
        File mAudioDir = new File(getExternalFilesDir(""), AUDIO_DIR_NAME);
        if (!mAudioDir.exists()) {
            if (!mAudioDir.mkdirs()) {
                LogUtils.e(TAG, "make dir failed = " + mAudioDir.getName());
            }
        }
        AudioRecordManager.getInstance(mContext).setAudioSavePath(mAudioDir.getAbsolutePath());
        AudioRecordManager.getInstance(mContext).setMaxVoiceDuration(MAX_VOICE_TIME);
        mBtnVoice.setOnVoiceButtonCallBack(new RecordAudioButton.OnVoiceButtonCallBack() {
            @Override
            public void onStartRecord() {
                AudioRecordManager.getInstance(mContext).startRecord();
            }

            @Override
            public void onStopRecord() {
                AudioRecordManager.getInstance(mContext).stopRecord();
            }

            @Override
            public void onWillCancelRecord() {
                AudioRecordManager.getInstance(mContext).willCancelRecord();
            }

            @Override
            public void onContinueRecord() {
                AudioRecordManager.getInstance(mContext).continueRecord();
            }
        });
        AudioRecordManager.getInstance(this).setAudioRecordListener(new IAudioRecordListener() {
            @Override
            public void initTipView() {
                if (mRecordVoicePopWindow == null) {
                    mRecordVoicePopWindow = new RecordVoicePopWindow(mContext);
                }
                mRecordVoicePopWindow.showAsDropDown(mRoot);
            }

            @Override
            public void setTimeoutTipView(int counter) {
                if (mRecordVoicePopWindow != null) {
                    mRecordVoicePopWindow.showTimeOutTipView(counter);
                }
            }

            @Override
            public void setRecordingTipView() {
                if (mRecordVoicePopWindow != null) {
                    mRecordVoicePopWindow.showRecordingTipView();
                }
            }

            @Override
            public void setAudioShortTipView() {
                if (mRecordVoicePopWindow != null) {
                    mRecordVoicePopWindow.showRecordTooShortTipView();
                }
            }

            @Override
            public void setCancelTipView() {
                if (mRecordVoicePopWindow != null) {
                    mRecordVoicePopWindow.showCancelTipView();
                }
            }

            @Override
            public void destroyTipView() {
                if (mRecordVoicePopWindow != null) {
                    mRecordVoicePopWindow.dismiss();
                }
            }

            @Override
            public void onStartRecord() {

            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                if (audioPath.getPath() == null) {
                    return;
                }
                File file = new File(audioPath.getPath());
                if (file.exists()) {
                    ToastUtils.showShort("录制成功");
                    Intent intent = new Intent();
                    intent.putExtra("duration", duration);
                    intent.putExtra("name", file.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                } else {
                    ToastUtils.showShort("录制失败");
                }
                finish();
            }

            @Override
            public void onAudioDBChanged(int db) {
                if (mRecordVoicePopWindow != null) {
                    mRecordVoicePopWindow.updateCurrentVolume(db);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.i(TAG, "onRequestPermissionsResult: grequestCode = " + requestCode);
        if (requestCode == 100) {
            if (isPermissionRepaired()) {
                initVoice();
            }
            else {
                ToastUtils.showShort("语音必须要录音等权限！");
                finish();
            }
        }
    }

    private boolean isPermissionRepaired() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
