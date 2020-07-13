package com.guoliang.module.keepwatch.activity;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.guoliang.commonlib.utils.FileUtils;
import com.guoliang.module.keepwatch.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.co.senab.photoview.PhotoView;

public class KeepWatchImageAndVideoPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "KeepWatchImageAndVideoPreviewActivity";

    ImageView mBack;
    TextView mTittle;

    private PhotoView mImage;
    private VideoView mVideo;
    private RelativeLayout mVideoLayout;

    private String mImgPath;
    private String mVideoPath;

    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keepwatch_image_and_video_preview);

        processIntent();
        initViews();
        setOnClickListener();
    }

    private void processIntent() {
        mImgPath = getIntent().getStringExtra("img_path");
        mVideoPath = getIntent().getStringExtra("video_path");
    }

    private void initViews() {
        // 设置顶部
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("预览");

        mImage = findViewById(R.id.keepwatch_img_preview);
        mVideo = findViewById(R.id.keepwatch_video_preview);
        mVideoLayout = findViewById(R.id.keepwatch_video_preview_layout);

        setImg();
        setVideo();
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
    }

    private void setVideo() {
        if (TextUtils.isEmpty(mVideoPath)) {
            return;
        }

        if (FileUtils.isNetworkUrl(mVideoPath)) {
            downloadFile(mVideoPath, "video");
        }
        else {
            mFile = new File(mVideoPath);
            _setVideo();
        }
    }

    private void _setVideo() {
        MediaController localMediaController = new MediaController(this);

        mVideo.setMediaController(localMediaController);
        try {
            mVideo.setVideoPath(mFile.getCanonicalPath());
        }
        catch (IOException e) {
            return;
        }

        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideo.start();
            }
        });
        mVideoLayout.setVisibility(View.VISIBLE);
    }

    private void setImg() {
        if (TextUtils.isEmpty(mImgPath)) {
            return;
        }

        if (FileUtils.isNetworkUrl(mImgPath)) {
            LogUtils.i(TAG, "FileUtils.isNetworkUrl = true, mImagePath = " + mImgPath);
            downloadFile(mImgPath, "img");
        }
        else {
            mFile = new File(mImgPath);
            _setImg();
        }
    }

    private void _setImg() {
        mImage.setImageBitmap(ImageUtils.getBitmap(mFile));
        mImage.setVisibility(View.VISIBLE);
    }

    private void downloadFile(String filePath, String type) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mFile = FileUtils.downloadFile(filePath, KeepWatchImageAndVideoPreviewActivity.this, type, "");
                if (mFile == null) {
                    ToastUtils.showShort("文件下载失败");
                } else { // 切回主线程更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("img".equals(type)) {
                                LogUtils.i(TAG, "_setImg");
                                _setImg();
                            }
                            else if ("video".equals(type)) {
                                LogUtils.i(TAG, "_setVideo");
                                _setVideo();
                            }
                        }
                    });
                }
            }
        });
    }
}
