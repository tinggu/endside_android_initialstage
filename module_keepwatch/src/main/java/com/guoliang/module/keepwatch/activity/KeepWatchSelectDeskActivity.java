package com.guoliang.module.keepwatch.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.guoliang.commonlib.datahelper.IUIDataHelperCallback;
import com.guoliang.commonlib.entity.MessageEvent;
import com.guoliang.commonlib.entity.MyDateTimeUtils;
import com.guoliang.commonlib.utils.DialogUtils;
import com.guoliang.module.keepwatch.DataHelper.DBHelper;
import com.guoliang.module.keepwatch.DataHelper.NetworkHelper;
import com.guoliang.module.keepwatch.R;
import com.guoliang.module.keepwatch.adapter.KeepWatchSelectDeskListAdapter;
import com.guoliang.module.keepwatch.entity.KeepWatchAssignment;
import com.guoliang.module.keepwatch.entity.KeepWatchDesk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = "/keepwatch/selectDesk")
public class KeepWatchSelectDeskActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UserSelectUserActivity";

    ImageView mBack;
    TextView mTittle;
    TextView mConfirm;
    RecyclerView mDeskListView;

    KeepWatchSelectDeskListAdapter mKeepWatchSelectDeskListAdapter;

    KeepWatchSelectDeskActivityData mKeepWatchSelectDeskActivityData = new KeepWatchSelectDeskActivityData();


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.keepwatch_select_desk_activity);
        initViews();
        setOnClickListener();
        getUserList();
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        mBack = findViewById(R.id.top_back);
        mTittle = findViewById(R.id.top_tittle);
        mTittle.setText("选择签到点");
        mConfirm = findViewById(R.id.top_addition);
        mConfirm.setText("确定");
        mConfirm.setVisibility(View.VISIBLE);
        mDeskListView = findViewById(R.id.keepwatch_desk_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDeskListView.setLayoutManager(layoutManager);
        mKeepWatchSelectDeskListAdapter = new KeepWatchSelectDeskListAdapter(mKeepWatchSelectDeskActivityData.getKeepWatchDeskList());
        mDeskListView.setAdapter(mKeepWatchSelectDeskListAdapter);
    }

    private void setOnClickListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    private void getUserList() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        List<KeepWatchDesk> keepWatchDeskList = DBHelper.getInstance().getDesk(groupId);
        if (keepWatchDeskList == null || keepWatchDeskList.isEmpty()) {
            return;
        }

        long startTime = MyDateTimeUtils.getTodayStartTime();
        long endTime = MyDateTimeUtils.getTodayEndTime();
        NetworkHelper.getInstance().getKeepWatchAssignmentList(startTime, endTime, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<KeepWatchAssignment> keepWatchAssignmentList = (List<KeepWatchAssignment>)obj;
                mKeepWatchSelectDeskActivityData.setKeepWatchAssignmentList(keepWatchAssignmentList);
                mKeepWatchSelectDeskActivityData.setKeepWatchDeskList(keepWatchDeskList);
                mKeepWatchSelectDeskListAdapter.setList(mKeepWatchSelectDeskActivityData.getKeepWatchDeskList());
                mKeepWatchSelectDeskListAdapter.notifyDataSetChanged();
                LogUtils.i(TAG, "getTodayAssignment success!");
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "getTodayAssignment fail: code = " + code);
            }
        });
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public  void onGetMessage(MessageEvent messageEvent) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBack.getId()) {
            finish();
        }
        else if (id == mConfirm.getId()) {
//            List<Integer> deskIdList = new ArrayList<>();
//            for (int i = 0; i < mKeepWatchSelectDeskListAdapter.getShowItemCount(); ++i) {
//                LinearLayout linearLayout = (LinearLayout)mDeskListView.getLayoutManager().findViewByPosition(i);
//                CheckBox checkBox = linearLayout.findViewById(R.id.keepwatch_select);
//                if (checkBox.isChecked()) {
//                    TextView textView = linearLayout.findViewById(R.id.keepwatch_desk_id);
//                    try {
//                        int deskId = Integer.parseInt(textView.getText().toString());
//                        deskIdList.add(deskId);
//                    }
//                    catch (NumberFormatException e) {
//                        LogUtils.i(TAG, "Integer.parseInt: 不是数字！");
//                    }
//
//                }
//            }

            HashMap<Integer, Integer> selectMap = mKeepWatchSelectDeskListAdapter.getSelectedMap();
            if (selectMap.isEmpty()) {
                DialogUtils.onlyPrompt("你没有选择任何签到点！", this);
                return;
            }

            String deskListStr = "";
            for (Integer key : selectMap.keySet()) {
                if (TextUtils.isEmpty(deskListStr)) {
                    deskListStr += ("" + key);
                }
                else {
                    deskListStr += ("," + key);
                }
            }

            LogUtils.i(TAG, "deskListStr = " + deskListStr);

            MessageEvent messageEvent = new MessageEvent("selected_desk", deskListStr);
            EventBus.getDefault().post(messageEvent);

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

class KeepWatchSelectDeskActivityData {
    List<KeepWatchDesk> keepWatchDeskList;
    HashMap<Integer, KeepWatchAssignment> keepWatchAssignmentMap;
    public KeepWatchSelectDeskActivityData() {
        keepWatchDeskList = new ArrayList<>();
        keepWatchAssignmentMap = new HashMap<>();
    }

    public List<KeepWatchDesk> getKeepWatchDeskList() {
        return keepWatchDeskList;
    }

    public void setKeepWatchDeskList(List<KeepWatchDesk> keepWatchDeskList) {
        this.keepWatchDeskList = keepWatchDeskList;
        for (int i = 0; i < this.keepWatchDeskList.size(); ++i) {
            KeepWatchAssignment keepWatchAssignment = keepWatchAssignmentMap.get(this.keepWatchDeskList.get(i).getDeskId());
            if (keepWatchAssignment == null) {
                this.keepWatchDeskList.get(i).setIsAssignmented(false);
            }
            else {
                this.keepWatchDeskList.get(i).setIsAssignmented(true);
            }
        }
    }

    public void setKeepWatchAssignmentList(List<KeepWatchAssignment> keepWatchAssignmentList) {
        keepWatchAssignmentMap.clear();
        for (int i = 0; i < keepWatchAssignmentList.size(); ++i) {
            keepWatchAssignmentMap.put(keepWatchAssignmentList.get(i).getDeskId(), keepWatchAssignmentList.get(i));
        }
    }

    public KeepWatchDesk getDeskByPostion(int position) {
        if (position == -1 || position >= keepWatchDeskList.size()) {
            return null;
        }

        return keepWatchDeskList.get(position);
    }

    public KeepWatchDesk getDeskByDeskId(int deskId) {
        for (int i = 0; i < keepWatchDeskList.size(); ++i) {
            KeepWatchDesk keepWatchDesk = keepWatchDeskList.get(i);
            if (deskId == keepWatchDesk.getDeskId()) {
                return keepWatchDesk;
            }
        }

        return null;
    }
}
