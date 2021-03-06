package cn.qingchengfit.api;

import android.support.annotation.Nullable;
import cn.qingchengfit.model.responese.GymExtra;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.CopyScheduleWrapper;
import cn.qingchengfit.saasbase.course.batch.bean.ScheduleTemplete;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutPlansResponse;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutResponse;
import cn.qingchengfit.saasbase.course.batch.network.body.ArrangeBatchBody;
import cn.qingchengfit.saasbase.course.batch.network.body.BatchCopyBody;
import cn.qingchengfit.saasbase.course.batch.network.body.DelBatchScheduleBody;
import cn.qingchengfit.saasbase.course.batch.network.body.SingleBatchBody;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchDetailWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchSchedulesWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.QcResponsePrivateDetail;
import cn.qingchengfit.saasbase.course.batch.network.response.SingleBatchWrap;
import cn.qingchengfit.saasbase.course.course.bean.JacketPhotoWrap;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhotoListWrap;
import cn.qingchengfit.saasbase.course.course.network.body.CourseBody;
import cn.qingchengfit.saasbase.course.course.network.body.EditJacketBody;
import cn.qingchengfit.saasbase.course.course.network.response.CourseLisWrap;
import cn.qingchengfit.saasbase.course.course.network.response.CoursePlans;
import cn.qingchengfit.saasbase.course.course.network.response.CourseTeacherWrapper;
import cn.qingchengfit.saasbase.course.course.network.response.CourseTypeWrap;
import cn.qingchengfit.saasbase.course.course.network.response.ShopCommentWrap;
import cn.qingchengfit.saasbase.course.detail.ScheduleCandidates;
import cn.qingchengfit.saasbase.course.detail.ScheduleDetailWrapper;
import cn.qingchengfit.saasbase.course.detail.ScheduleOrders;
import cn.qingchengfit.saasbase.course.detail.SchedulePhotos;
import cn.qingchengfit.saasbase.course.detail.ScheduleShareDetail;
import cn.qingchengfit.saasbase.staff.beans.SimpleSuccessResponse;
import java.util.HashMap;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2018/1/26.
 */

public interface CourseApi {

  //所有的团课排期
  @GET("/api/v1/coaches/{coach_id}/batches/{batch_id}/{schedules}/?order_by=start&show_all=1")
  rx.Observable<QcDataResponse<BatchSchedulesWrap>> qcGetbatchSchedules(
      @Path("coach_id") String coach_id, @Path("batch_id") String batch_id,
      @Path("schedules") String schedules, @QueryMap HashMap<String, Object> params);

  //获取某个排期的详情
  @GET("/api/v1/coaches/{id}/batches/{batch_id}/")
  rx.Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(@Path("id") String coach_id,
      @Path("batch_id") String batch_id, @QueryMap Map<String, Object> params);

  /**
   * 课程
   */

  //获取健身房课程列表
  @GET("/api/v2/coaches/{id}/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseLisWrap>> qcGetCourses(@Path("id") String coach_id,
      @QueryMap HashMap<String, Object> params, @Query("is_private") Integer is_private);

  /**
   * 获取课程计划
   */
  @GET("/api/v1/coaches/{coach_id}/plantpls/?show_all=1")
  rx.Observable<QcDataResponse<CoursePlans>> qcGetCoursePlan(@Path("coach_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程下教练
   */
  @GET("/api/v1/coaches/{coach_id}/courses/teachers/")
  rx.Observable<QcDataResponse<CourseTeacherWrapper>> qcGetCourseTeacher(
      @Path("coach_id") String coach_id, @Query("course_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 课程下照片
   */
  @GET("/api/v2/coaches/{coach_id}/courses/schedules/photos/")
  rx.Observable<QcDataResponse<SchedulePhotoListWrap>> qcGetSchedulePhotos(
      @Path("coach_id") String coach_id, @Query("course_id") String id, @Query("page") int page,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程详情
   */
  @GET("/api/v1/coaches/{coach_id}/courses/{course_id}/")
  rx.Observable<QcDataResponse<CourseTypeWrap>> qcGetCourseDetail(@Path("coach_id") String coach_id,
      @Path("course_id") String id, @QueryMap HashMap<String, Object> params);

  /**
   * 分场馆评分
   */
  @GET("/api/v1/coaches/{coach_id}/courses/shops/score/")
  rx.Observable<QcDataResponse<ShopCommentWrap>> qcGetShopComment(@Path("coach_id") String coach_id,
      @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/coaches/{coach_id}/courses/photos/")
  rx.Observable<QcDataResponse<JacketPhotoWrap>> qcGetJacket(@Path("coach_id") String id,
      @Query("course_id") String course_id, @QueryMap HashMap<String, Object> params);

  @POST("/api/v1/coaches/{id}/{schedules}/bulk/delete/")
  rx.Observable<QcDataResponse> qcDelBatchSchedule(@Path("id") String id,
      @Path("schedules") String schedules, @Body DelBatchScheduleBody delCourseManage,
      @QueryMap HashMap<String, Object> params);

  //获取私教课排期
  @GET("/api/v1/coaches/{id}/batches/?is_private=1")
  rx.Observable<QcDataResponse<QcResponsePrivateDetail>> qcGetPrivateCourses(
      @Path("id") String coach_id, @QueryMap HashMap<String, Object> params);

  //获取团课排期
  @GET("/api/v1/coaches/{id}/batches/?is_private=0")
  rx.Observable<QcDataResponse<QcResponsePrivateDetail>> qcGetGroupCourses(
      @Path("id") String coach_id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v1/coaches/{id}/{type}/{single_id}/")
  rx.Observable<QcDataResponse<SingleBatchWrap>> qcGetSingleBatch(@Path("id") String coach_id,
      @Path("type") String type, @Path("single_id") String single_id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 私教 timetables
   */

  @PUT("/api/v1/coaches/{id}/{type}/{single_id}/")
  rx.Observable<QcDataResponse> qcUpdateBatchSchedule(@Path("id") String staff_id,
      @Path("type") String type, @Path("single_id") String scheduleid, @Body SingleBatchBody body,
      @QueryMap HashMap<String, Object> params);

  //排课填充
  @GET("/api/v1/coaches/{id}/{type}/arrange/template/")
  rx.Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(@Path("id") String id,
      @Path("type") String type, @Nullable @Query("teacher_id") String teacher_id,
      @Query("course_id") String course_id, @QueryMap HashMap<String, Object> params);

  /**
   * 创建课程
   */
  @POST("/api/v2/coaches/{id}/courses/") rx.Observable<QcDataResponse> qcCreateCourse(
      @Path("id") String coachid, @Body CourseBody courseBody,
      @QueryMap HashMap<String, Object> params);

  //修改课程
  @PUT("/api/v2/coaches/{id}/courses/{course_id}/") rx.Observable<QcDataResponse> qcUpdateCourse(
      @Path("id") String coachid, @Path("course_id") String course_id,
      @QueryMap HashMap<String, Object> params, @Body CourseBody courseBody);

  //删除课程
  @DELETE("/api/v2/coaches/{id}/courses/{course_id}/") rx.Observable<QcDataResponse> qcDelCourse(
      @Path("id") String coachid, @Path("course_id") String course_id,
      @QueryMap HashMap<String, Object> params);

  //修改封面
  @POST("/api/v2/coaches/{id}/courses/photos/") rx.Observable<QcDataResponse> qcEditJacket(
      @Path("id") String coachid, @Query("course_id") String course_id,
      @QueryMap HashMap<String, Object> params, @Body EditJacketBody body);

  /**
   * 排期
   */
  @POST("/api/v1/coaches/{id}/arrange/batches/") rx.Observable<QcDataResponse> qcArrangeBatch(
      @Path("id") String coach_id, @Body ArrangeBatchBody body,
      @QueryMap HashMap<String, Object> params);

  //修改排期
  @PUT("/api/v1/coaches/{id}/batches/{batchid}/") rx.Observable<QcDataResponse> qcUpdateBatch(
      @Path("id") String coach_id, @Path("batchid") String batchid, @Body ArrangeBatchBody body,
      @QueryMap HashMap<String, Object> params);

  //排期检查
  @POST("/api/v1/coaches/{id}/{type}/arrange/check/") rx.Observable<QcDataResponse> qcCheckBatch(
      @Path("id") String coach_id, @Path("type") String type, @Body ArrangeBatchBody body,
      @QueryMap HashMap<String, Object> params);

  //删除排期
  @DELETE("/api/v1/coaches/{coach_id}/batches/{batch_id}/") rx.Observable<QcDataResponse> delBatch(
      @Path("coach_id") String coach_id, @Path("batch_id") String batch_id,
      @QueryMap HashMap<String, Object> params);

  //复制排期检查课程冲突
  @POST("api/v2/coaches/{coach_id}/copy-batch/check/")
  rx.Observable<QcDataResponse> qcCheckBatchConflict(@Path("coach_id") String staff_id,
      @Body BatchCopyBody body, @QueryMap HashMap<String, Object> params);

  //复制排期
  @POST("api/coaches/{coach_id}/copy-batch/") rx.Observable<QcDataResponse> qcSureCopyBatch(
      @Path("coach_id") String staff_id, @Body BatchCopyBody body,
      @QueryMap HashMap<String, Object> params);

  //复制排期页拉取所有数据
  @GET("api/coaches/{coach_id}/copy-batch/schedules/")
  rx.Observable<QcDataResponse<CopyScheduleWrapper>> qcBatchCopySchedule(
      @Path("coach_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //获取排期的课程体系
  @GET("api/v2/coaches/{staff_id}/workouts/")
  rx.Observable<QcDataResponse<WorkoutResponse>> qcGetCourseWorkout(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //获取排期的课程体系
  @GET("api/v2/coaches/{staff_id}/workouts/{workout_id}/plans/")
  rx.Observable<QcDataResponse<WorkoutPlansResponse>> qcGetCourseWorkoutPlans(
      @Path("staff_id") String staff_id, @Path("workout_id") String workout_id,
      @QueryMap HashMap<String, Object> params);

  //获取已经排好课程的课程详情，从课程预约进入
  @GET("/api/v2/coaches/{staff_id}/schedules/{schedule_id}/detail/")
  rx.Observable<QcDataResponse<ScheduleDetailWrapper>> qcGetScheduleDetail(
      @Path("staff_id") String staff_id, @Path("schedule_id") String schedule_id,
      @QueryMap Map<String, Object> params);

  //获取已经排好课程的预约人数列表
  @GET("/api/v2/coaches/{staff_id}/schedules/{schedule_id}/orders/")
  rx.Observable<QcDataResponse<ScheduleOrders>> qcGetScheduleDetailOrder(
      @Path("staff_id") String staff_id, @Path("schedule_id") String schedule_id,
      @QueryMap Map<String, Object> params);

  //获取已经排好课程的课程照片列表
  @GET("/api/v2/coaches/{staff_id}/schedules/{schedule_id}/photos/")
  rx.Observable<QcDataResponse<SchedulePhotos>> qcGetScheduleDetailPhotos(
      @Path("staff_id") String staff_id, @Path("schedule_id") String schedule_id,
      @QueryMap Map<String, Object> params);

  //取消预约
  @POST("/api/v2/coaches/{staff_id}/orders/{order_id}/cancel/")
  rx.Observable<QcDataResponse<SimpleSuccessResponse>> qcPostScheduleOrderCancel(
      @Path("staff_id") String staff_id, @Path("order_id") String order_id,
      @QueryMap Map<String, Object> query, @Body Map<String, Object> body);

  // 课程照片 批量删除
  @DELETE("/api/v2/coaches/{staff_id}/schedules/{schedule_id}/photos/")
  rx.Observable<QcDataResponse<SimpleSuccessResponse>> qcDeleteSchedulePhotos(
      @Path("staff_id") String staff_id, @Path("schedule_id") String schedule_id,
      @QueryMap Map<String, Object> params);

  @GET("/api/v2/coaches/{staff_id}/gym-extra/")
  rx.Observable<QcDataResponse<GymExtra>> qcGetGymExtra(@Path("staff_id") String staff_id,
      @QueryMap Map<String, Object> params);

  /**
   * 获取签课设置
   */
  @GET("/api/coaches/{staff_id}/shops/configs/")
  rx.Observable<QcDataResponse<SignInConfig.Data>> qcGetShopConfig(
      @Path("staff_id") String staff_id, @Query("keys") String key,
      @QueryMap Map<String, Object> params);

  @GET("/api/v2/coaches/{staff_id}/schedules/{schedule_id}/share/")
  rx.Observable<QcDataResponse<ScheduleShareDetail>> qcGetScheduleShareInfo(
      @Path("staff_id") String staff_id, @Path("schedule_id") String schedule_id,
      @QueryMap Map<String, Object> params);

  @PUT("/api/v2/coaches/{staff_id}/schedules/{schedule_id}/share/")
  rx.Observable<QcDataResponse<ScheduleShareDetail>> qcPutScheduleShareInfo(
      @Path("staff_id") String staff_id, @Path("schedule_id") String schedule_id,
      @QueryMap Map<String, Object> params, @Body Map<String, Object> body);

  @GET("/api/v2/coaches/{staff_id}/schedules/{schedule_id}/candidate/")
  rx.Observable<QcDataResponse<ScheduleCandidates>> qcGetScheduleCandidate(
      @Path("staff_id") String staff_id, @Path("schedule_id") String schedule_id,
      @QueryMap Map<String, Object> params);
}
