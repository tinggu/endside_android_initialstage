package com.ctfww.module.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.bumptech.glide.Glide;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.utils.FileUtils;
import com.ctfww.commonlib.utils.GlobeFun;
import com.ctfww.commonlib.utils.PermissionUtils;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.airship.Airship;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.UserInfo;
import java.io.File;
import java.util.Calendar;

@Route(path = "/user/info")
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener, SelectGenderDialog.ISelectGender, UpdateBirthdayDialog.ISelectBirthday{
    private final static String TAG = "UserInfoActivity";

    private final static int REQUEST_CODE_SET_ONE_VAL = 1;
    private final static int REQUEST_CODE_UPDATE_MOBILE = 2;
    private final static int REQUEST_CODE_CHOOSE_PHOTO = 3;

    private final static int RESULT_CODE = 1;

    private ImageView mBack;
    private TextView mTittle;
    private ImageView mHead;
    private LinearLayout mUpdateHead;
    private TextView mNickname;
    private LinearLayout mUpdateNickname;
    private TextView mGender;
    private LinearLayout mUpdateGender;
    private TextView mBirthday;
    private LinearLayout mUpdateBirthday;
    private TextView mMobile;
    private LinearLayout mUpdateMobile;
    private TextView mEmail;
    private LinearLayout mUpdateEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_info_activity);

        initViews();
        setOnClickListener();
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText("我的信息");

        UserInfo userInfo = DBQuickEntry.getSelfInfo();
        LogUtils.i(TAG, "initViews = " + userInfo.toString());
        mHead = findViewById(R.id.user_info_head);
        Glide.with(this).load(userInfo.getHeadUrl()).into(mHead);
        mUpdateHead = findViewById(R.id.user_info_update_head);
        mNickname = findViewById(R.id.user_info_nickname);
        mUpdateNickname = findViewById(R.id.user_info_update_nickname);
        mGender = findViewById(R.id.user_info_gender);
        mUpdateGender = findViewById(R.id.user_info_update_gender);
        mBirthday = findViewById(R.id.user_info_birthday);
        mUpdateBirthday = findViewById(R.id.user_info_update_birthday);
        mMobile = findViewById(R.id.user_info_mobile);
        mUpdateMobile = findViewById(R.id.user_info_update_mobile);
        mEmail = findViewById(R.id.user_info_email);
        mUpdateEmail = findViewById(R.id.user_info_update_email);

        String nickname = userInfo.getNickName();
        if (!TextUtils.isEmpty(nickname) && !"null".equals(nickname)) {
            mNickname.setText(nickname);
        }

        int gender = userInfo.getGender();
        if (gender == 1) {
            mGender.setText("女");
        }
        else if (gender == 2) {
            mGender.setText("男");
        }

        String birthday = userInfo.getBirthday();
        if (!TextUtils.isEmpty(birthday) && !"null".equals(birthday)) {
            mBirthday.setText(birthday);
        }

        String mobile = userInfo.getMobile();
        if (!TextUtils.isEmpty(mobile) && !"null".equals(mobile)) {
            mMobile.setText(mobile);
        }

        String email = userInfo.getEmail();
        if (!TextUtils.isEmpty(email) && !"null".equals(email)) {
            mEmail.setText(email);
        }
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mUpdateHead.setOnClickListener(this);
        mUpdateNickname.setOnClickListener(this);
        mUpdateGender.setOnClickListener(this);
        mUpdateBirthday.setOnClickListener(this);
        mUpdateMobile.setOnClickListener(this);
        mUpdateEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int index = view.getId();
        if (index == R.id.user_back) {
            Airship.getInstance().synUserInfoToCloud();
            finish();
        }
        else if (index == mUpdateHead.getId()) {
            if (PermissionUtils.isLocationPermissionGranted(this)) {
                choosePhoto();
            }
        }
        else if (index == mUpdateNickname.getId()) {
            Intent intent = new Intent(this, OneValSetActivity.class);
            intent.putExtra("type", "nickname");
            startActivity(intent);
        }
        else if (index == mUpdateGender.getId()) {
            SelectGenderDialog dlg = new SelectGenderDialog();
            dlg.show(getSupportFragmentManager(), "SelectGenderDialog");
        }
        else if (index == mUpdateMobile.getId()) {
            Intent intent = new Intent(this, UpdateMobileActivity.class);
            startActivity(intent);
        }
        else if (index == mUpdateEmail.getId()) {
            Intent intent = new Intent(this, OneValSetActivity.class);
            intent.putExtra("type", "email");
            startActivity(intent);
        }
        else if (index == mUpdateBirthday.getId()) {
            UpdateBirthdayDialog dlg = new UpdateBirthdayDialog();
            dlg.show(getSupportFragmentManager(), "UpdateBirthdayDialog");
        }
    }

    private void updateHeadImg() {
        // 从相册中获取图片文件
 /*       File file;
        NetworkHelper.getInstance().updateHeadImg(file, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                // 更新图片
            }

            @Override
            public void onError(int code) {

            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            Uri uri = data.getData();
            String filePath = FileUtils.getRealPathFromUri(this, uri);
            Bitmap bitmapSrc = ImageUtils.getBitmap(new File(filePath));
//            bitmap = ImageUtils.compressByScale(bitmap, 0.1f, 0.1f);
            Bitmap bitmap = ImageUtils.compressByScale(bitmapSrc, 200, 200, false);
            String userId = SPStaticUtils.getString("user_open_id");
            String headPath = getExternalFilesDir("") + "/" + "head" + "/" +userId+".jpg";
            ImageUtils.save(bitmap, new File(headPath), Bitmap.CompressFormat.JPEG);
            uploadHead(headPath);
        }
    }

    @Override
    public void onGenderSelected(boolean isMale) {
        int gender = isMale ? 2 : 1;
        UserInfo userInfo = DBQuickEntry.getSelfInfo();
        userInfo.setGender(gender);
        userInfo.setTimeStamp(System.currentTimeMillis());
        userInfo.setSynTag("modify");
        DBHelper.getInstance().updateUser(userInfo);
    }

    @Override
    public int genderType() {
        UserInfo userInfo = DBQuickEntry.getSelfInfo();
        return userInfo.getGender();
    }

    @Override
    public void onSelected(int year, int month, int day) {
        String birthday = "" + year + "-" + month + "-" + day;
        UserInfo userInfo = DBQuickEntry.getSelfInfo();
        userInfo.setBirthday(birthday);
        userInfo.setTimeStamp(System.currentTimeMillis());
        userInfo.setSynTag("modify");
        DBHelper.getInstance().updateUser(userInfo);
    }

    @Override
    public Object getBirthday() {
        UserInfo userInfo = DBQuickEntry.getSelfInfo();
        String birthday = userInfo.getBirthday();
        if (TextUtils.isEmpty(birthday) || "null".equals(birthday) || GlobeFun.getKeyCount(birthday, '-') != 2) {
            int[] arr = new int[3];
            Calendar calendar = Calendar.getInstance();
            arr[0] = calendar.get(Calendar.YEAR);
            arr[1] = calendar.get(Calendar.MONTH) + 1;
            arr[2] = calendar.get(Calendar.DAY_OF_MONTH);
            return arr;
        }
        else {
            return GlobeFun.getYearMonthDay(birthday, '-');
        }
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, REQUEST_CODE_CHOOSE_PHOTO);
    }

    private void uploadHead(final String filePath) {
        NetworkHelper.getInstance().uploadFile(filePath, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                String url = (String)obj;
                Glide.with(UserInfoActivity.this).load(url).into(mHead);
                UserInfo userInfo = DBQuickEntry.getSelfInfo();
                userInfo.setHeadUrl(url);
                userInfo.setTimeStamp(System.currentTimeMillis());
                userInfo.setSynTag("modify");
                DBHelper.getInstance().updateUser(userInfo);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        PermissionUtils.requestStoragePermission(this);
    }
}
