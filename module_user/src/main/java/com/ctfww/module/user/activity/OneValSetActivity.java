package com.ctfww.module.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.utils.MaxTextTwoLengthFilter;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBQuickEntry;
import com.ctfww.module.user.entity.UserInfo;

public class OneValSetActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBack;
    private TextView mFinish;
    private EditText mVal;

    private String key;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_one_val_set_activity);
        processIntent();
        initViews();
        setOnClickListener();

    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        TextView tittle = findViewById(R.id.top_tittle);
        mFinish = findViewById(R.id.top_addition);
        mFinish.setVisibility(View.VISIBLE);
        mFinish.setText("完成");
        mVal = findViewById(R.id.user_line_edit);
        TextView grayElseDesc = findViewById(R.id.gray_else_desc);

        if (TextUtils.isEmpty(key)) {
            return;
        }

        UserInfo userInfo = DBQuickEntry.getSelfInfo();
        if (userInfo == null) {
            return;
        }

        if ("nickname".equals(key)) {
            tittle.setText("修改昵称");
            grayElseDesc.setText("限2~20个字符，一个汉字为2个字符");
            mVal.setFilters(new MaxTextTwoLengthFilter[]{new MaxTextTwoLengthFilter( this,20)});
            mVal.setText(userInfo.getNickName());
        }
        else if ("email".equals(key)) {
            tittle.setText("绑定邮箱");
            grayElseDesc.setText("请确保邮箱格式正确");
            mVal.setText(userInfo.getEmail());
        }
        else if ("wechat".equals(key)) {
            tittle.setText("绑定微信");
            grayElseDesc.setText("请确保输入正确");
            mVal.setText(userInfo.getWechatNum());
        }
        else if ("blog".equals(key)) {
            tittle.setText("绑定微博");
            grayElseDesc.setText("请确保输入正确");
            mVal.setText(userInfo.getBlogNum());
        }
        else if ("qq".equals(key)) {
            tittle.setText("绑定QQ");
            grayElseDesc.setText("请确保输入正确");
            mVal.setText(userInfo.getQqNum());
        }

        mVal.setSelection(mVal.getText().length());
    }

    private void processIntent() {
        Intent intent = this.getIntent();
        key = intent.getStringExtra("type");
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mFinish.getId()) {
            UserInfo userInfo = DBQuickEntry.getSelfInfo();
            if ("email".equals(key)) {
                if (!isValidEmail(mVal.getText().toString())) {
                    ToastUtils.showShort("无效邮箱！");
                    return;
                }

                userInfo.setEmail(mVal.getText().toString());
            }

            if ("nickname".equals(key)) {
                if (!isValidNickname(mVal.getText().toString())) {
                    ToastUtils.showShort("无效昵称！");
                    return;
                }

                userInfo.setNickName(mVal.getText().toString());
            }

            if ("wechat".equals(key)) {
                if (!isValidWechat(mVal.getText().toString())) {
                    ToastUtils.showShort("无效微信！");
                    return;
                }

                userInfo.setWechatNum(mVal.getText().toString());
            }

            if ("blog".equals(key)) {
                if (!isValidBlog(mVal.getText().toString())) {
                    ToastUtils.showShort("无效博客！");
                    return;
                }

                userInfo.setBlogNum(mVal.getText().toString());
            }

            if ("qq".equals(key)) {
                if (!isValidQq(mVal.getText().toString())) {
                    ToastUtils.showShort("无效QQ！");
                    return;
                }

                userInfo.setQqNum(mVal.getText().toString());
            }

            userInfo.setTimeStamp(System.currentTimeMillis());
            userInfo.setSynTag("modify");

            DBHelper.getInstance().updateUser(userInfo);

            finish();
        }
    }

    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        if (email.length() < 5 || -1 == email.indexOf('@')) {
            return false;
        }

        return true;
    }

    private boolean isValidNickname(String nickname) {
        if (TextUtils.isEmpty(nickname) || nickname.length() < 2) {
            return false;
        }

        return true;
    }

    private boolean isValidWechat(String wechat) {
        if (TextUtils.isEmpty(wechat) || wechat.length() < 2) {
            return false;
        }

        return true;
    }

    private boolean isValidBlog(String blog) {
        if (TextUtils.isEmpty(blog) || blog.length() < 2) {
            return false;
        }

        return true;
    }

    private boolean isValidQq(String qq) {
        if (TextUtils.isEmpty(qq) || qq.length() < 2) {
            return false;
        }

        return true;
    }
}
