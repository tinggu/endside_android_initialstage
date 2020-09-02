package com.ctfww.module.user.datahelper.airship;

import com.ctfww.module.user.entity.UserInfo;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Airship {
    private static final String TAG = "Airship";

    private Airship() {

    }

    private static class Inner {
        private static final Airship INSTANCE = new Airship();
    }

    public static Airship getInstance() {
        return Airship.Inner.INSTANCE;
    }

    public void startTimedSyn() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d")
                        .daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synUserInfoToCloud();
                synGroupInfoToCloud();
                synGroupUserInfoToCloud();
                synInviteInfoToCloud();
                synNoticeInfoToCloud();
                synNoticeReadStatusToCloud();
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    public void synToCloud() {
        synUserInfoToCloud();
        synGroupInfoToCloud();
        synGroupUserInfoToCloud();
        synInviteInfoToCloud();
        synNoticeInfoToCloud();
        synNoticeReadStatusToCloud();
    }

    public void synFromCloud() {
        synUserInfoFromCloud();
        synGroupInfoFromCloud();
        synGroupUserInfoFromCloud();
        synInviteInfoFromCloud();
        synNoticeInfoFromCloud();
        synNoticeReadStatusFromCloud();
    }

    // 1.与用户信息相关

    public void synUserInfoToCloud() {
        UserAirship.synToCloud();
        UserAirship.synAdditionToCloud();
    }

    public void synUserInfoFromCloud() {
        UserAirship.synFromCloud();
    }

    // 2.与群组信息相关

    public void synGroupInfoToCloud() {
        GroupAirship.synToCloud();
    }

    public void synGroupInfoFromCloud() {
        GroupAirship.synFromCloud();
    }

    // 3.与邀请信息相关

    public void synInviteInfoToCloud() {
        GroupInviteAirship.synToCloud();
    }

    public void synInviteInfoFromCloud() {
        GroupInviteAirship.synFromCloud();
    }

    // 4.与通知信息相关

    public void synNoticeInfoToCloud() {
        NoticeAirship.synToCloud();
    }

    public void synNoticeInfoFromCloud() {
        NoticeAirship.synFromCloud();
    }

    // 5.与群组成员信息相关

    public void synGroupUserInfoToCloud() {
        GroupUserAirship.synToCloud();
    }

    public void synGroupUserInfoFromCloud() {
        GroupUserAirship.synFromCloud();
    }

    // 6.与通知读取状态相关

    public void synNoticeReadStatusToCloud() {
        NoticeReadStatusAirship.synToCloud();
    }

    public void synNoticeReadStatusFromCloud() {
        NoticeReadStatusAirship.synFromCloud();
    }
}
