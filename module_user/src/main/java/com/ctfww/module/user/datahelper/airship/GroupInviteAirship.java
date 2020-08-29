package com.ctfww.module.user.datahelper.airship;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.ctfww.commonlib.datahelper.IUIDataHelperCallback;
import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.MessageEvent;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.commonlib.utils.AirshipUtils;
import com.ctfww.module.user.datahelper.NetworkHelper;
import com.ctfww.module.user.datahelper.dbhelper.DBHelper;
import com.ctfww.module.user.entity.GroupInviteInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GroupInviteAirship {
    private final static String TAG = "GroupInviteAirship";

    // 同步邀请信息上云
    public static void synToCloud() {
        final List<GroupInviteInfo> inviteList = DBHelper.getInstance().getNoSynInviteList();
        if (inviteList.isEmpty()) {
            return;
        }

        CargoToCloud<GroupInviteInfo> cargoToCloud = new CargoToCloud<>(inviteList);

        NetworkHelper.getInstance().synInviteInfoToCloud(cargoToCloud, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                for (int i = 0; i < inviteList.size(); ++i) {
                    GroupInviteInfo inviteInfo = inviteList.get(i);
                    inviteInfo.setSynTag("cloud");
                    DBHelper.getInstance().updateInvite(inviteInfo);
                    if ("send".equals(inviteInfo.getStatus())) {
                        EventBus.getDefault().post(new MessageEvent("send_invite_success", GsonUtils.toJson(inviteInfo)));
                    }
                    else {
                        EventBus.getDefault().post(new MessageEvent("send_update_receive_invite_success", GsonUtils.toJson(inviteInfo)));
                    }
                }
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synToCloud fail: code = " + code);
            }
        });
    }

    // 从云上同步通知信息
    public static void synFromCloud() {
        String groupId = SPStaticUtils.getString("working_group_id");
        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        final String key = "group_invite_syn_time_stamp_cloud" + "_" + groupId;
        long startTime = SPStaticUtils.getLong(key, AirshipUtils.getDefaultStartTime());
        long endTime = System.currentTimeMillis();
        final QueryCondition condition = new QueryCondition();
        String userId = SPStaticUtils.getString("user_open_id");
        condition.setGroupId(groupId);
        condition.setUserId(userId);
        condition.setStartTime(startTime);
        condition.setEndTime(endTime);

        NetworkHelper.getInstance().synInviteInfoFromCloud(condition, new IUIDataHelperCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<GroupInviteInfo> inviteList = (List<GroupInviteInfo>)obj;

                if (!inviteList.isEmpty()) {
                    if (updateByCloud(inviteList)) {
                        EventBus.getDefault().post("finish_invite_syn");
                    }
                }

                SPStaticUtils.put(key, condition.getEndTime());
            }

            @Override
            public void onError(int code) {
                LogUtils.i(TAG, "synFromCloud fail: code = " + code);
            }
        });
    }

    private static boolean updateByCloud(List<GroupInviteInfo> inviteList) {
        boolean ret = false;
        for (int i = 0; i < inviteList.size(); ++i) {
            GroupInviteInfo invite = inviteList.get(i);
            invite.setSynTag("cloud");
            if (DBHelper.getInstance().addInvite(invite)) {
                ret = true;
                continue;
            }

            GroupInviteInfo localInvite = DBHelper.getInstance().getInvite(invite.getInviteId());
            if (localInvite.getTimeStamp() < invite.getTimeStamp()) {
                invite.setSynTag("cloud");
                DBHelper.getInstance().updateInvite(invite);
                ret = true;
            }
        }

        return ret;
    }
}
