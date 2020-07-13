package com.ctfww.module.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.module.user.R;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.entity.GroupUserInfo;

import static com.blankj.utilcode.util.ActivityUtils.finishAllActivities;

public class GroupUserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "GroupUserInfoActivity";

    private ImageView mBack;
    private TextView mTittle;
    private TextView mNickname;
    private ImageView mNickNameImg;
    private TextView mMobile;
    private ImageView mMobileImg;
    private LinearLayout mRoleLinearLayout;
    private TextView mRole;
    private ImageView mRoleImg;

    private GroupUserInfo mGroupUserInfo;

    private boolean mIsGroupUserInfoChanged = false;
    private boolean mIsCanUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_user_info_activity);

        processIntent();
        initViews();

        setOnClickListener();
    }

    private void processIntent() {
        mGroupUserInfo = GsonUtils.fromJson(getIntent().getStringExtra("groupUserInfo"), GroupUserInfo.class);
        mIsCanUpdate = getIntent().getBooleanExtra("isCanUpdate", false);
    }

    private void initViews() {
        mBack = findViewById(R.id.user_back);
        mTittle = findViewById(R.id.user_top_bar_tittle);
        mTittle.setText(mGroupUserInfo.getNickName() + "的详细信息");
        mNickname = findViewById(R.id.group_user_info_nickname);
        mNickname.setText(mGroupUserInfo.getNickName());
        mNickNameImg = findViewById(R.id.group_user_info_nickname_img);
        mMobile = findViewById(R.id.group_user_info_mobile);
        mMobile.setText(mGroupUserInfo.getMobile());
        mMobileImg = findViewById(R.id.group_user_info_mobile_img);
        mRoleLinearLayout = findViewById(R.id.group_user_info_role_linear_layout);
        mRole = findViewById(R.id.group_user_info_role);
        mRole.setText("admin".equals(mGroupUserInfo.getRole()) ? "管理员" : "成员");
        mRoleImg = findViewById(R.id.group_user_info_role_img);

        mNickNameImg.setVisibility(View.GONE);
        mMobileImg.setVisibility(View.GONE);
        String selfUserId = SPStaticUtils.getString("user_open_id");
        if (!mIsCanUpdate) {
            mRoleImg.setVisibility(View.GONE);
        }
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        if (mIsCanUpdate) {
            mRoleLinearLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBack.getId()) {
            if (mIsGroupUserInfoChanged) {
                Intent intent = new Intent();
                intent.putExtra("groupUserInfo", GsonUtils.toJson(mGroupUserInfo));
                setResult(RESULT_OK, intent);
            }
            finish();
        }
        else if (id == mRoleLinearLayout.getId()) {
            promptUpdateRole();
        }
    }

    private int mSelectWhich = 0;
    private void updateRole() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("更新角色：");
        final String[] roleStrArr = {"管理员", "成员"};
        int checkedItem = "admin".equals(mGroupUserInfo.getRole()) ? 0 : 1;

        builder.setSingleChoiceItems(roleStrArr, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                mSelectWhich = which;
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String role = mSelectWhich == 0 ? "admin" : "member";
                if (role.equals(mGroupUserInfo.getRole())) {
                    return;
                }

                NetworkHelper.getInstance().updateGroupUserRole(mGroupUserInfo.getUserId(), role, new IUIDataHelperCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        mGroupUserInfo.setRole(role);
                        mRole.setText(roleStrArr[mSelectWhich]);
                        mIsGroupUserInfoChanged = true;
                        if ("admin".equals(role)) {
                            mIsCanUpdate = false;
                            mRoleImg.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(int code) {
                        ToastUtils.showShort("更新角色失败，请检查网络是否正常！");
                    }
                });
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();  //创建AlertDialog对象
        dialog.show();                           //显示对话框
    }

    private void promptUpdateSelfRole() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你现在是管理员，如果更改角色，你将失去管理员的所有权限，确认修改？");
        builder.setCancelable(true);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateRole();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        dialog.show();                              //显示对话框
    }

    private void promptUpdateRole() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("如果将他修改为管理员，他将拥有管理员的所有权限，请确认是否需要修改？");
        builder.setCancelable(true);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateRole();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        dialog.show();                              //显示对话框
    }
}
