package com.ctfww.module.assignment.datahelper;

import com.ctfww.commonlib.entity.CargoToCloud;
import com.ctfww.commonlib.entity.QueryCondition;
import com.ctfww.module.assignment.entity.DeskAssignment;
import com.ctfww.module.assignment.entity.RouteAssignment;

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
    @POST("/microcloudkeyevents/assignment/synDeskAssignmentToCloud")
    Call<ResponseBody> synDeskAssignmentToCloud(@Body CargoToCloud<DeskAssignment> info);

    /**
     * 从云上同步任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/assignment/synDeskAssignmentFromCloud")
    Call<ResponseBody> synDeskAssignmentFromCloud(@Body QueryCondition condition);

    /**
     * 同步任务上云
     * @param info
     * @return 返回值
     */
    @POST("/microcloudkeyevents/assignment/synRouteAssignmentToCloud")
    Call<ResponseBody> synRouteAssignmentToCloud(@Body CargoToCloud<RouteAssignment> info);

    /**
     * 从云上同步任务
     * @param condition
     * @return 返回值
     */
    @POST("/microcloudkeyevents/assignment/synRouteAssignmentFromCloud")
    Call<ResponseBody> synRouteAssignmentFromCloud(@Body QueryCondition condition);
}
