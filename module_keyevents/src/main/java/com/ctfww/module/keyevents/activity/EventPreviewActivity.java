package com.ctfww.module.keyevents.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.ctfww.module.keyevents.R;

public class EventPreviewActivity extends AppCompatActivity {

    private static final String TAG = "EventPreviewActivity";

//    private PhotoView mPreviewImg;
    private ImageView mPic;
    private VideoView mVideoView;
    private RelativeLayout mVideoViewLayout;

    private String mImgPath;
    private String mVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_preview);

        initViews();

        setData();
    }

    private void setData() {
        if (getIntent() == null) {
            return;
        }

        mImgPath = getIntent().getStringExtra("img_path");

        mVideoPath = getIntent().getStringExtra("video_path");

        setImg();

        setVideo();
    }

    private void setVideo() {
        if (!TextUtils.isEmpty(mVideoPath)) {
            MediaController localMediaController = new MediaController(this);

            mVideoView.setMediaController(localMediaController);
            mVideoView.setVideoPath(mVideoPath);
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoView.start();
                }
            });
            mVideoViewLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setImg() {
        if (!TextUtils.isEmpty(mImgPath)) {
//            mPreviewImg.setImageBitmap(ImageUtils.getBitmap(new File(mImgPath)));
            Glide.with(this).load(mImgPath).into(mPic);
            mPic.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        // 设置顶部
        ImageView mBack = findViewById(R.id.top_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView mTopTittle = findViewById(R.id.top_tittle);
        mTopTittle.setText("预览");

 //       mPreviewImg = findViewById(R.id.event_preview_img_view);
        mPic = findViewById(R.id.keyevent_pic);
        mVideoView = findViewById(R.id.event_video_view);
        mVideoViewLayout = findViewById(R.id.layout_preview_video);
    }
}
