package com.guoliang.module.user.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.utils.FileUtils;
import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.commonlib.utils.PermissionUtils;
import com.guoliang.module.user.R;
import com.guoliang.module.user.datahelper.DBHelper;
import com.guoliang.module.user.datahelper.DataHelper;
import com.guoliang.module.user.datahelper.NetworkHelper;
import com.guoliang.module.user.entity.UserInfo;

import org.greenrobot.eventbus.EventBus;

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

        UserInfo userInfo = DataHelper.getInstance().getUserInfo();
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
            UserInfo userInfo = DataHelper.getInstance().getUserInfo();
            String val = userInfo.getNickName();
            intent.putExtra("value", !TextUtils.isEmpty(val) && !"null".equals(val) ? val : "");
            startActivityForResult(intent, REQUEST_CODE_SET_ONE_VAL);
        }
        else if (index == mUpdateGender.getId()) {
            SelectGenderDialog dlg = new SelectGenderDialog();
            dlg.show(getSupportFragmentManager(), "SelectGenderDialog");
        }
        else if (index == mUpdateMobile.getId()) {
            Intent intent = new Intent(this, UpdateMobileActivity.class);
            UserInfo userInfo = DataHelper.getInstance().getUserInfo();
            intent.putExtra("mobile", userInfo.getMobile());
            intent.putExtra("password", userInfo.getPassword());
            startActivityForResult(intent, REQUEST_CODE_UPDATE_MOBILE);
        }
        else if (index == mUpdateEmail.getId()) {
            Intent intent = new Intent(this, OneValSetActivity.class);
            intent.putExtra("type", "email");
            UserInfo userInfo = DataHelper.getInstance().getUserInfo();
            String val = userInfo.getEmail();
            intent.putExtra("value", !TextUtils.isEmpty(val) && !"null".equals(val) ? val : "");
            startActivityForResult(intent, REQUEST_CODE_SET_ONE_VAL);
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
        if (requestCode == REQUEST_CODE_SET_ONE_VAL) {
            if (resultCode==RESULT_OK) {
                final String key = data.getStringExtra("key");
                final String val = data.getStringExtra("value");
                if ("nickname".equals(key)) {
                    UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                    userInfo.setNickName(val);
                    userInfo.setRegisterTimestamp(System.currentTimeMillis());
                    updateUserInfo(userInfo, "nickName");
                }
                else if ("email".equals(key)) {
                    UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                    userInfo.setEmail(val);
                    userInfo.setRegisterTimestamp(System.currentTimeMillis());
                    updateUserInfo(userInfo, "email");
                }
            }
        }
        else if (requestCode == REQUEST_CODE_UPDATE_MOBILE) {
            if (resultCode==RESULT_OK) {
                String mobile = data.getStringExtra("mobile");
                UserInfo userInfo = DataHelper.getInstance().getUserInfo();
                userInfo.setMobile(mobile);
                userInfo.setRegisterTimestamp(System.currentTimeMillis());
                updateUserInfo(userInfo, "mobile");
            }
        }
        else if (requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
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
        UserInfo userInfo = DataHelper.getInstance().getUserInfo();
        userInfo.setGender(gender);
        userInfo.setRegisterTimestamp(System.currentTimeMillis());
        updateUserInfo(userInfo, "gender");
    }

    @Override
    public int genderType() {
        UserInfo userInfo = DataHelper.getInstance().getUserInfo();
        return userInfo.getGender();
    }

    @Override
    public void onSelected(int year, int month, int day) {
        String birthday = "" + year + "-" + month + "-" + day;
        UserInfo userInfo = DataHelper.getInstance().getUserInfo();
        userInfo.setBirthday(birthday);
        userInfo.setRegisterTimestamp(System.currentTimeMillis());
        updateUserInfo(userInfo, "birthday");
    }

    @Override
    public Object getBirthday() {
        UserInfo userInfo = DataHelper.getInstance().getUserInfo();
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
                updateHead(url, filePath);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    private void updateHead(final String url, final String filePath) {
        final UserInfo userInfo = DataHelper.getInstance().getUserInfo();
        userInfo.setHeadUrl(url);
        userInfo.setModifyTimestamp(System.currentTimeMillis());
        NetworkHelper.getInstance().updateUserInfo(userInfo, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                userInfo.setSynTag("cloud");
                DBHelper.getInstance().updateUser(userInfo);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                mHead.setImageBitmap(bitmap);
                EventBus.getDefault().post(new MessageEvent("head_url"));
            }

            @Override
            public void onError(int code) {
                userInfo.setSynTag("modify");
                DBHelper.getInstance().updateUser(userInfo);
                LogUtils.i(TAG, "updateHead faile: code = " + code);
            }
        });
    }

    private void updateUserInfo(final UserInfo userInfo, final String type) {
        NetworkHelper.getInstance().updateUserInfo(userInfo,
                new IUIDataHelperCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        userInfo.setSynTag("cloud");
                        DBHelper.getInstance().updateUser(userInfo);
                        if ("nickName".equals(type)) {
                            mNickname.setText(userInfo.getNickName());
                            LogUtils.i(TAG, "updateUserInfo: 昵称更新成功");
                        }
                        else if ("gender".equals(type)){
                            mGender.setText(userInfo.getGender() == 1 ? "女" : "男");
                            LogUtils.i(TAG, "updateUserInfo: 性别更新成功");
                        }
                        else if ("email".equals(type)){
                            mEmail.setText(userInfo.getEmail());
                            LogUtils.i(TAG, "updateUserInfo: 邮箱更新成功");
                        }
                        else if ("birthday".equals(type)){
                            mBirthday.setText(userInfo.getBirthday());
                            LogUtils.i(TAG, "updateUserInfo: 生日更新成功");
                        }
                        else if ("mobile".equals(type)){
                            mMobile.setText(userInfo.getMobile());
                            LogUtils.i(TAG, "updateUserInfo: 电话更新成功");
                        }
                    }

                    @Override
                    public void onError(int code) {
                        ToastUtils.showShort("updateUserInfo: 更新失败，请确认网络是否正常！");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        PermissionUtils.requestStoragePermission(this);
    }
}
