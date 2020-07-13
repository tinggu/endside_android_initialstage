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

public class OneValSetActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBack;
    private TextView mTittle;
    private TextView mFinish;
    private EditText mVal;
    private TextView mGrayElseDesc;

    private String key;
    private String val;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_one_val_set_activity);
        processIntent();
        initViews();
        setOnClickListener();

    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mFinish = findViewById(R.id.user_top_bar_right_btn);
        mVal = findViewById(R.id.user_line_edit);
        mGrayElseDesc = findViewById(R.id.gray_else_desc);

        if (TextUtils.isEmpty(key)) {
            return;
        }

        if ("nickname".equals(key)) {
            mTittle.setText("修改昵称");
            mGrayElseDesc.setText("限2~20个字符，一个汉字为2个字符");
            mVal.setFilters(new MaxTextTwoLengthFilter[]{new MaxTextTwoLengthFilter( this,20)});
        }
        else if ("email".equals(key)) {
            mTittle.setText("绑定邮箱");
            mGrayElseDesc.setText("请确保邮箱格式正确");
        }
        else if ("wechat".equals(key)) {
            mTittle.setText("绑定微信");
            mGrayElseDesc.setText("请确保输入正确");
        }
        else if ("blog".equals(key)) {
            mTittle.setText("绑定微博");
            mGrayElseDesc.setText("请确保输入正确");
        }
        else if ("qq".equals(key)) {
            mTittle.setText("绑定QQ");
            mGrayElseDesc.setText("请确保输入正确");
        }

        if (!TextUtils.isEmpty(val)) {
            mVal.setText(val);
            mVal.setSelection(mVal.getText().length());
        }
    }

    private void processIntent() {
        Intent intent = this.getIntent();
        key = intent.getStringExtra("type");
        val = intent.getStringExtra("value");
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
            String email = mVal.getText().toString();
            if ("email".equals(key)) {
                if (!isValidEmail(email)) {
                    ToastUtils.showShort("无效邮箱！");
                    return;
                }
            }

            if ("nickname".equals(key)) {
                if (!isValidNickname(email)) {
                    ToastUtils.showShort("无效昵称！");
                    return;
                }
            }

            if ("wechat".equals(key)) {
                if (!isValidWechat(email)) {
                    ToastUtils.showShort("无效微信！");
                    return;
                }
            }

            if ("blog".equals(key)) {
                if (!isValidWechat(email)) {
                    ToastUtils.showShort("无效博客！");
                    return;
                }
            }

            if ("qq".equals(key)) {
                if (!isValidWechat(email)) {
                    ToastUtils.showShort("无效QQ！");
                    return;
                }
            }

            Intent intent = new Intent();
            intent.putExtra("key", key);
            intent.putExtra("value", mVal.getText().toString());
            setResult(RESULT_OK, intent);
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
