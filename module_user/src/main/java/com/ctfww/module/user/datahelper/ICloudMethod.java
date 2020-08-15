package com.ctfww.module.user.datahelper;

import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.user.bean.GroupInfoBean;
import com.ctfww.module.user.bean.GroupInviteBean;
import com.ctfww.module.user.bean.NoticeBean;
import com.ctfww.module.user.bean.NoticeReadStatusBean;
import com.ctfww.module.user.bean.PasswordLoginBean;
import com.ctfww.module.user.bean.SMSLoginBean;
import com.ctfww.module.user.bean.User2GroupBean;
import com.ctfww.module.user.bean.UserInfoBean;

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
     * 获取用户token
     * @param url 固定的URL：http://39.98.147.77:9090/token/getAccessTokenByUserId
     * @param userId 用户ID
     * @return 返回值
     */
//    @POST("token/getAccessTokenByUserId")
//    Call<ResponseBody> getAccessTokenByUserId(@Url String url, @Body String userId);

    /**
     * 账户注销
     * @param userId 用户ID
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/deleteAccount")
    Call<ResponseBody> deleteAccount(@Body String userId);

    /**
     * 通过用户ID来获取用户信息
     * @param userId 用户ID
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/getUserInfoByUserId")
    Call<ResponseBody> getUserInfoByUserId(@Body String userId);

    /**
     * 通过用户ID来更新用户信息
     * @param userInfoBean 用户信息
     * @return 返回值
     */
    @POST("/microusermanagement/userMgt/updateUserInfoByUserId")
    Call<ResponseBody> updateUserInfoByUserId(@Body UserInfoBean userInfoBean);

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
     * 创建群组
     * @param groupInfoBean 群组信息
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/createGroup")
    Call<ResponseBody> createGroup(@Body GroupInfoBean groupInfoBean);

    /**
     * 更新群组信息（主要是更新群组名称）
     * @param groupInfoBean 群组信息
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/updateGroupInfo")
    Call<ResponseBody> updateGroupInfo(@Body GroupInfoBean groupInfoBean);

    /**
     * 删除群组
     * @param groupId 群组编号
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/deleteGroup")
    Call<ResponseBody> deleteGroup(@Body String groupId);

    /**
     * 增加邀请
     * @param groupInviteBean 邀请信息
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/addInvite")
    Call<ResponseBody> addInvite(@Body GroupInviteBean groupInviteBean);

    /**
     * 修改邀请状态，如接受或拒绝
     * @param groupInviteBean 邀请信息
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/updateInviteStatus")
    Call<ResponseBody> updateInviteStatus(@Body GroupInviteBean groupInviteBean);

    /**
     * 删除邀请
     * @param inviteId 邀请编号
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/deleteInvite")
    Call<ResponseBody> deleteInvite(@Body String inviteId);

    /**
     * 获取自己发出的邀请
     * @param userId 用户编号
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/getSendInvite")
    Call<ResponseBody> getSendInvite(@Body String userId);

    /**
     * 获取邀请自己的邀请
     * @param userId 用户编号
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/getInvite")
    Call<ResponseBody> getInvite(@Body String userId);

    @POST("/microusermanagement/groupMgt/getNewMessageCount")
    Call<ResponseBody> getNewMessageCount(@Body String userId);

    /**
     * 退群
     * @param user2GroupBean 用户和群组对应信息
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/withdrawGroup")
    Call<ResponseBody> withdrawGroup(@Body User2GroupBean user2GroupBean);

    /**
     * 更改用户和群组的对应信息（目前主要是更新角色）
     * @param user2GroupBean 用户和群组对应信息
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/updateGroupUserRole")
    Call<ResponseBody> updateGroupUserRole(@Body User2GroupBean user2GroupBean);

    /**
     * 获取用户所在的群
     * @param userId 用户Id
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/getUserGroupInfo")
    Call<ResponseBody> getUserGroupInfo(@Body String userId);

    /**
     * 获取群包含的用户信息
     * @param groupId 群Id
     * @return 返回值
     */
    @POST("/microusermanagement/groupMgt/getGroupUserInfo")
    Call<ResponseBody> getGroupUserInfo(@Body String groupId);

    /**
     * 上传文件 http://39.98.147.77:8003/fileMgt/uploadFile
     * @param file 文件
     * @return 返回值
     */
    @Multipart
    @POST("/microapkmgtserver/fileMgt/uploadFile")
    Call<ResponseBody> uploadFile(@Header("userId") String userId, @Part MultipartBody.Part file);

    /**
     * 增加通知
     * @param info
     * @return 返回值
     */
    @POST("/microusermanagement/noticeMgt/addNotice")
    Call<ResponseBody> addNotice(@Body NoticeBean info);

    /**
     * 获得通知信息
     * @param condition 主要是groupId和userId
     * @return 返回值
     */
    @POST("/microusermanagement/noticeMgt/getNotice")
    Call<ResponseBody> getNotice(@Body QueryCondition condition);

    /**
     * 更改通知信息阅读状态
     * @param info 通知信息
     * @return 返回值
     */
    @POST("/microusermanagement/noticeMgt/addNoticeReadStatus")
    Call<ResponseBody> addNoticeReadStatus(@Body NoticeReadStatusBean info);

    /**
     * 获得某条通知的阅读状态（管理员与发送人才有此权限）
     * @param condition 通知编号以及所在的群组
     * @return 返回值
     */
    @POST("/microusermanagement/noticeMgt/getNoticeReadStatus")
    Call<ResponseBody> getNoticeReadStatus(@Body QueryCondition condition);

    /**
     * 获得没有查看的通知个数
     * @param condition groupId与userId
     * @return 返回值
     */
    @POST("/microusermanagement/noticeMgt/getNoLookOverCount")
    Call<ResponseBody> getNoLookOverCount(@Body QueryCondition condition);
}
