package com.ctfww.module.user.datahelper;

import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.user.bean.NoticeReadStatusBean;
import com.ctfww.module.user.bean.PasswordLoginBean;
import com.ctfww.module.user.bean.SMSLoginBean;
import com.ctfww.module.user.entity.GroupInfo;
import com.ctfww.module.user.entity.GroupInviteInfo;
import com.ctfww.module.user.entity.GroupUserInfo;
import com.ctfww.module.user.entity.NoticeInfo;
import com.ctfww.module.user.entity.NoticeReadStatus;
import com.ctfww.module.user.entity.UserGroupInfo;
import com.ctfww.module.user.entity.UserInfo;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 暂时写成函数的形式，后续改成通用的接口处理函数
 *
 */
public interface ICloudMethod {
    /**
     * 账户注销
     * @param userId 用户ID
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/deleteAccount")
    Call<ResponseBody> deleteAccount(@Body String userId);

    /**
     * 通过密码进行登录
     * @param userMap 手机号码+密码
     * @return 返回值
     */
    @POST("/microusermanagement/login/loginByPassword")
    Call<ResponseBody> loginByPassword(@Body PasswordLoginBean userMap);

    /**
     * 通过验证码进行登录
     * @param userMap 手机号码+验证码
     * @return 返回值
     */
    @POST("/microusermanagement/login/loginBySmsCode")
    Call<ResponseBody> loginBySmsCode(@Body SMSLoginBean userMap);

    /**
     * 向用户手机发送验证码
     * @param phoneNumbers 手机号码
     * @return 返回值
     */
    @POST("/microusermanagement/login/sendSms")
    Call<ResponseBody> sendSms(@Body String phoneNumbers);

    /**
     * 同步用户信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synUserInfoToCloud")
    Call<ResponseBody> synUserInfoToCloud(@Body UserInfo info);

    /**
     * 从云上同步用户信息
     * @param userId
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synUserInfoFromCloud")
    Call<ResponseBody> synUserInfoFromCloud(@Body String userId);

    /**
     * 同步群组信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synGroupInfoToCloud")
    Call<ResponseBody> synGroupInfoToCloud(@Body CargoToCloud<GroupInfo> info);

    /**
     * 从云上同步群组信息
     * @param condition
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synGroupInfoFromCloud")
    Call<ResponseBody> synGroupInfoFromCloud(@Body QueryCondition condition);

    /**
     * 同步用户对应的群组信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synUserGroupInfoToCloud")
    Call<ResponseBody> synUserGroupInfoToCloud(@Body CargoToCloud<UserGroupInfo> info);

    /**
     * 从云上同步用户对应的群组信息
     * @param condition
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synUserGroupInfoFromCloud")
    Call<ResponseBody> synUserGroupInfoFromCloud(@Body QueryCondition condition);

    /**
     * 同步邀请信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synInviteInfoToCloud")
    Call<ResponseBody> synInviteInfoToCloud(@Body CargoToCloud<GroupInviteInfo> info);

    /**
     * 从云上同步邀请信息
     * @param condition
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synInviteInfoFromCloud")
    Call<ResponseBody> synInviteInfoFromCloud(@Body QueryCondition condition);

    /**
     * 同步群组成员信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synGroupUserInfoToCloud")
    Call<ResponseBody> synGroupUserInfoToCloud(@Body CargoToCloud<GroupUserInfo> info);

    /**
     * 从云上同步群组成员信息
     * @param condition
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synGroupUserInfoFromCloud")
    Call<ResponseBody> synGroupUserInfoFromCloud(@Body QueryCondition condition);

    /**
     * 上传文件 http://39.98.147.77:8003/fileMgt/uploadFile
     * @param file 文件
     * @return 返回值
     */
    @Multipart
    @POST("/microapkmgtserver/fileMgt/uploadFile")
    Call<ResponseBody> uploadFile(@Header("userId") String userId, @Part MultipartBody.Part file);

    /**
     * 同步通知信息上云
     * @param info
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synNoticeInfoToCloud")
    Call<ResponseBody> synNoticeInfoToCloud(@Body CargoToCloud<NoticeInfo> info);

    /**
     * 从云上同步通知信息
     * @param condition
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synNoticeInfoFromCloud")
    Call<ResponseBody> synNoticeInfoFromCloud(@Body QueryCondition condition);

    /**
     * 同步通知读取状态上云
     * @param info
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synNoticeReadStatusToCloud")
    Call<ResponseBody> synNoticeReadStatusToCloud(@Body CargoToCloud<NoticeReadStatus> info);

    /**
     * 从云上同步通知读取状态
     * @param condition
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/synNoticeReadStatusFromCloud")
    Call<ResponseBody> synNoticeReadStatusFromCloud(@Body QueryCondition condition);
}
