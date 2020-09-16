package com.ctfww.module.assignment.datahelper;

import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.assignment.entity.AssignmentInfo;
import com.ctfww.module.assignment.entity.TodayAssignment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ICloudMethod {
    /**
     * 同步任务上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/assignment/synAssignmentToCloud")
    Call<ResponseBody> synAssignmentToCloud(@Body CargoToCloud<AssignmentInfo> info);

    /**
     * 从云上同步任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/assignment/synAssignmentFromCloud")
    Call<ResponseBody> synAssignmentFromCloud(@Body QueryCondition condition);

    /**
     * 从云上同步任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/assignment/synTodayAssignmentFromCloud")
    Call<ResponseBody> synTodayAssignmentFromCloud(@Body QueryCondition condition);
}
