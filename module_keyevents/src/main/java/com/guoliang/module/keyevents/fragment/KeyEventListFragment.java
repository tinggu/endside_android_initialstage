package com.guoliang.module.keyevents.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MyDateTimeUtils;
import com.guoliang.commonlib.location.MyLocation;
import com.guoliang.commonlib.utils.DialogUtils;
import com.guoliang.commonlib.utils.GlobeFun;
import com.guoliang.module.keyevents.Entity.KeyEvent;
import com.guoliang.module.keyevents.R;
import com.guoliang.module.keyevents.activity.EventPreviewActivity;
import com.guoliang.module.keyevents.activity.SoundRecord2Activity;
import com.guoliang.module.keyevents.adapter.KeyEventListAdapter;
import com.guoliang.module.keyevents.datahelper.DBHelper;
import com.guoliang.module.keyevents.datahelper.NetworkHelper;
import com.guoliang.module.keyevents.datahelper.SynData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KeyEventListFragment extends Fragment{

    private static final String TAG = "KeyEventListFragment";

    private TextView mNoData;
    private RecyclerView mKeyEventListView;
    private List<KeyEvent> mKeyEventList = new ArrayList<>();
    KeyEventListAdapter mKeyEventListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.keyevent_list_fragment, container, false);

        initViews(view);
        setOnClickListener();

        return view;
    }


    private void setOnClickListener() {

    }

    private void initViews(View view) {
        mNoData = view.findViewById(R.id.keyevent_no_data);

        mKeyEventListView = view.findViewById(R.id.keyevent_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mKeyEventListView.setLayoutManager(linearLayoutManager);
        mKeyEventListAdapter = new KeyEventListAdapter(mKeyEventList);
        mKeyEventListView.setAdapter(mKeyEventListAdapter);
    }

    public void getEndList(long timeStamp) {
        long startTime = MyDateTimeUtils.getDayStartTime(timeStamp);
        long endTime = MyDateTimeUtils.getDayEndTime(timeStamp);
        NetworkHelper.getInstance().getEndKeyEventList(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeyEventList = (List<KeyEvent>)obj;
                showList("");
                LogUtils.i(TAG, "getEndList success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getEndList fail: code = " + code);
            }
        });
    }

    public void getNoEndList() {
        NetworkHelper.getInstance().getNoEndKeyEventList(new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                mKeyEventList = (List<KeyEvent>)obj;
                showList("");
                LogUtils.i(TAG, "getNoEndList success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getNoEndList fail: code = " + code);
            }
        });
    }

    public void showList(String userId) {
        List<KeyEvent> keyEventListThis;
        if (TextUtils.isEmpty(userId)) {
            keyEventListThis = mKeyEventList;
        }
        else {
            keyEventListThis = new ArrayList<>();
            for (int i = 0; i < mKeyEventList.size(); ++i) {
                KeyEvent keyEvent = mKeyEventList.get(i);
                if (userId.equals(keyEvent.getUserId())) {
                    keyEventListThis.add(keyEvent);
                }
            }
        }

        if (keyEventListThis.isEmpty()) {
            mNoData.setVisibility(View.VISIBLE);
            mKeyEventListView.setVisibility(View.GONE);
        }
        else {
            mKeyEventListAdapter.setList(keyEventListThis);
            mKeyEventListAdapter.notifyDataSetChanged();
            mNoData.setVisibility(View.GONE);
            mKeyEventListView.setVisibility(View.VISIBLE);
        }
    }
}
