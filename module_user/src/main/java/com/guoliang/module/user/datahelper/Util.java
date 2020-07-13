package com.guoliang.module.user.datahelper;

import android.content.Context;

import com.guoliang.module.user.bean.GroupUserBean;
import com.guoliang.module.user.bean.UserGroupBean;
import com.guoliang.module.user.bean.UserInfoBean;
import com.guoliang.module.user.entity.GroupInviteInfo;
import com.guoliang.module.user.entity.GroupUserInfo;
import com.guoliang.module.user.entity.UserGroupInfo;
import com.guoliang.module.user.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void start(Context ctx){
        // 初始化用户中心网络化模块
        ICloudMethod userMethod = com.guoliang.commonlib.network.CloudClient.getInstance().create(ICloudMethod.class);
        CloudClient.getInstance().setCloudMethod(userMethod);

        // 初始化用户中心数据库模块
        DBHelper.getInstance().init(ctx);

        // 初始化用户中心的数据同步
 //       com.guoliang.module.user.storage.db.SelfSynDB.getInstance().startSyn();
    }

    public static UserInfo userInfoBean2UserInfo(UserInfoBean userInfoBean) {
        UserInfo userInfo = new UserInfo();
        userInfo.setRegisterTimestamp(userInfoBean.getRegisterTimestamp());
        userInfo.setUserId(userInfoBean.getUserId());
        userInfo.setNickName(userInfoBean.getNickName());
        userInfo.setPassword(userInfoBean.getPassword());
        userInfo.setMobile(userInfoBean.getMobile());
        userInfo.setEmail(userInfoBean.getEmail());
        userInfo.setHeadUrl(userInfoBean.getHeadUrl());
        userInfo.setBirthday(userInfoBean.getBirthday());
        userInfo.setGender(userInfoBean.getGender());
        userInfo.setWechatNum(userInfoBean.getWechatNum());
        userInfo.setBlogNum(userInfoBean.getBlogNum());
        userInfo.setQqNum(userInfoBean.getQqNum());

        return userInfo;
    }

    public static UserGroupInfo userGroupBean2UserGroupInfo(UserGroupBean userGroupBean) {
        UserGroupInfo userGroupInfo = new UserGroupInfo();
        userGroupInfo.setGroupId(userGroupBean.getGroupId());
        userGroupInfo.setGroupName(userGroupBean.getGroupName());
        userGroupInfo.setRole(userGroupBean.getRole());

        return userGroupInfo;
    }

    public static List<UserGroupInfo> userGroupBeanList2UserGroupInfoList(List<UserGroupBean> userGroupBeanList) {
        List<UserGroupInfo> userGroupInfoList = new ArrayList<>();
        for (int i = 0; i < userGroupBeanList.size(); ++i) {
            UserGroupInfo userGroupInfo = userGroupBean2UserGroupInfo(userGroupBeanList.get(i));
            userGroupInfoList.add(userGroupInfo);
        }

        return userGroupInfoList;
    }

    public static GroupUserInfo groupUserBean2GroupUserInfo(GroupUserBean groupUserBean) {
        GroupUserInfo groupUserInfo = new GroupUserInfo();
        groupUserInfo.setHeadUrl(groupUserBean.getHeadUrl());
        groupUserInfo.setMobile(groupUserBean.getMobile());
        groupUserInfo.setNickName(groupUserBean.getNickName());
        groupUserInfo.setRole(groupUserBean.getRole());
        groupUserInfo.setUserId(groupUserBean.getUserId());

        return groupUserInfo;
    }

    public static List<GroupUserInfo> groupUserBeanList2GroupUserInfoList(List<GroupUserBean> groupUserBeanList) {
        List<GroupUserInfo> groupUserInfoList = new ArrayList<>();
        for (int i = 0; i < groupUserBeanList.size(); ++i) {
            GroupUserInfo groupUserInfo = groupUserBean2GroupUserInfo(groupUserBeanList.get(i));
            groupUserInfoList.add(groupUserInfo);
        }

        return groupUserInfoList;
    }
}
