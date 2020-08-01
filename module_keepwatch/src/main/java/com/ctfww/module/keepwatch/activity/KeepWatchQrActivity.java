package com.ctfww.module.keepwatch.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.ctfww.commonlib.utils.ImageUtils;
import com.ctfww.commonlib.utils.QRCodeUtils;
import com.ctfww.module.keepwatch.R;
import com.ctfww.module.keepwatch.Utils;
import com.ctfww.module.keepwatch.entity.KeepWatchSigninInfo;
import com.ctfww.module.keepwatch.utils.QRHelper;

import java.util.Calendar;

public class KeepWatchQrActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "KeepWatchQrActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mSave;
    private ImageView mQr;

    private int mDeskId;
    private String mDeskName;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.keepwatch_qr_activity);

        processIntent();

        initViews();

        setOnClickListener();
    }

    private void processIntent() {
        mDeskId = getIntent().getIntExtra("desk_id", 0);
        mDeskName = getIntent().getStringExtra("desk_name");
        if (mDeskName == null) {
            mDeskName = "";
        }
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("签到点二维码");
        mSave = findViewById(R.id.top_addition);
        mSave.setVisibility(View.VISIBLE);
        mSave.setText("保存");
        mQr = findViewById(R.id.keepwatch_qr);
        mBitmap = QRCodeUtils.createDataQRCodeBitmap(Utils.getDeskQrUrl(mDeskId), mDeskId, mDeskName);
        if (mBitmap != null) {
            mQr.setImageBitmap(mBitmap);
        }

        mQr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mBitmap != null) {
                    String result = QRHelper.getResult(mBitmap);
                    int deskId = QRCodeUtils.getQrDeskId(result, KeepWatchQrActivity.this);
                    if (deskId != 0) {
                        com.ctfww.module.fingerprint.Utils.startScan("calc");

                        KeepWatchSigninInfo keepWatchSigninInfo = new KeepWatchSigninInfo();
                        keepWatchSigninInfo.setDeskId(deskId);
                        keepWatchSigninInfo.setFinishType("qr");
                        Intent intent = new Intent(KeepWatchQrActivity.this, KeepWatchReportSigninActivity.class);
                        intent.putExtra("signin", GsonUtils.toJson(keepWatchSigninInfo));
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mSave.getId()) {
            Calendar calendar = Calendar.getInstance();
            String dateTimeStr = String.format("%d%02d%02d%02d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            String fileName = "" + mDeskId + "_" + dateTimeStr + ".jpg";
            ImageUtils.saveImageToGallery(this, mBitmap, fileName);
        }
    }


}
