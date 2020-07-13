package com.guoliang.commonlib.utils;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

public class VoiceUtils {
    public static void playVoice(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
        }
        catch (IOException e) {

        }

        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public static void playVoiceByUrl(String url) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
        }
        catch (IOException e) {

        }

        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public static long getDuration(String filePaath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePaath); //在获取前，设置文件路径（应该只能是本地路径）
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release(); //释放
        if(!TextUtils.isEmpty(duration)){
            return Long.parseLong(duration);
        }

        return 0;
    }

//    private void startRecord(){
// if(mr == null){
//File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
//if(!dir.exists()){
// dir.mkdirs();
//}
//File soundFile = new File(dir,"abner"+".amr");//存储到SD卡当然也可上传到服务器
// if(!soundFile.exists()){
//try {
//soundFile.createNewFile();
//} catch (IOException e) {
//e.printStackTrace();
// }
//
//
// }
// mr = new MediaRecorder();
// mr.setAudioSource(MediaRecorder.AudioSource.MIC); //音频输入源
// mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);  //设置输出格式
// mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);  //设置编码格式
// mr.setOutputFile(soundFile.getAbsolutePath());
// try {
//mr.prepare();
// mr.start(); //开始录制
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }
//
//    停止语音功能
//
////停止录制，资源释放
// private void stopRecord(){
//if(mr != null){
// mr.stop();
// mr.release();
// mr = null;
// }
// }

}
