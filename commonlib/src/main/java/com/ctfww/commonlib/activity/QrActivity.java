package com.ctfww.commonlib.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.ctfww.commonlib.R;
import com.ctfww.commonlib.entity.Qr;
import com.ctfww.commonlib.utils.ImageUtils;
import com.ctfww.commonlib.utils.QRCodeUtils;
import com.ctfww.commonlib.utils.QRUtils;

import java.util.Calendar;

@Route(path = "/commonlib/qr")
public class QrActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "QrActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mSave;
    private ImageView mQrImg;

    private Bitmap mBitmap;

    private Qr mQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qr_activity);

        processIntent();

        initViews();

        setOnClickListener();
    }

    private void processIntent() {
        String qr = getIntent().getStringExtra("qr");
        mQr = GsonUtils.fromJson(qr, Qr.class);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("签到点二维码");
        mSave = findViewById(R.id.top_addition);
        mSave.setVisibility(View.VISIBLE);
        mSave.setText("保存");
        mQrImg = findViewById(R.id.keepwatch_qr);
        mBitmap = QRCodeUtils.createDataQRCodeBitmap(mQr);
        if (mBitmap != null) {
            mQrImg.setImageBitmap(mBitmap);
        }

        mQrImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mBitmap != null) {
                    String result = QRUtils.getResult(mBitmap);
                    int deskId = QRCodeUtils.getQrDeskId(result, getApplicationContext());
                    if (deskId != 0) {
                        ARouter.getInstance().build("/keepwatch/reportSignin")
                                .withInt("desk_id", deskId)
                                .withString("finish_type", "qr")
                                .navigation();
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
            String fileName = "" + mQr.getLogo() + "_" + dateTimeStr + ".jpg";
            ImageUtils.saveImageToGallery(this, mBitmap, fileName);
        }
    }


}
