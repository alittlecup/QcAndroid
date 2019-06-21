package cn.qingchengfit.saasbase.apis;

import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.QcResponseBtaches;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.model.responese.QcSchedulesResponse;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.CopyScheduleWrapper;
import cn.qingchengfit.saasbase.course.batch.bean.ScheduleTemplete;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutPlansResponse;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutResponse;
import cn.qingchengfit.saasbase.course.batch.network.body.ArrangeBatchBody;
import cn.qingchengfit.saasbase.course.batch.network.body.BatchCopyBody;
import cn.qingchengfit.saasbase.course.batch.network.body.DelBatchScheduleBody;
import cn.qingchengfit.saasbase.course.batch.network.body.SingleBatchBody;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchCoachListWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchCourseListWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchDetailWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchSchedulesWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.GroupCourseScheduleDetail;
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
import java.util.HashMap;
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
 * Created by Paper on 2017/11/7.
 */

public interface CourseApi {

  //获取教练排期
  @GET("/api/staffs/{id}/batches/?course__is_private=1")
  rx.Observable<QcResponseBtaches> qcGetTeacherBatches(@Path("id") String id,
      @Query("brand_id") String brand_id, @Query("teacher_id") String teacher_id);

  //获取团课排课
  @GET("/api/staffs/{id}/group/courses/")
  rx.Observable<QcDataResponse<BatchCourseListWrap>> getGroupBatch(@Path("id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  //获取私教排课
  @GET("/api/staffs/{id}/private/coaches/")
  rx.Observable<QcDataResponse<BatchCoachListWrap>> getPrivateBatch(@Path("id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  //获取某个排期的详情
  @GET("/api/staffs/{id}/batches/{batch_id}/")
  rx.Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(@Path("id") String staff_id,
      @Path("batch_id") String batch_id, @QueryMap HashMap<String, Object> params);

  //获取教练排期
  @GET("/api/staffs/{id}/coaches/{coach_id}/batches/?course__is_private=1")
  rx.Observable<QcDataResponse<QcResponsePrivateDetail>> qcGetPrivateCoaches(
      @Path("id") String staff_id, @Path("coach_id") String coach_id,
      @QueryMap HashMap<String, Object> params);

  //获取团课排期
  @GET("/api/staffs/{id}/courses/{course_id}/batches/")
  rx.Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(
      @Path("id") String staff_id, @Path("course_id") String course_id,
      @QueryMap HashMap<String, Object> params);

  //获取健身房课程
  @GET("/api/staffs/{id}/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetCourses(@Path("id") String staff_id,
      @Query("id") String gym_id, @Query("model") String gym_model,
      @Query("is_private") int is_private);

  //获取健身房课程权限
  @GET("/api/v2/staffs/{id}/method/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseLisWrap>> qcGetCoursesPermission(@Path("id") String staff_id,
      @QueryMap HashMap<String, Object> params, @Query("is_private") int is_private);

  //获取健身房课程权限
  @GET("/api/v2/staffs/{id}/method/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetAllCoursesPermission(
      @Path("id") String staff_id, @QueryMap HashMap<String, Object> params);

  //获取健身房课程列表
  @GET("/api/v2/staffs/{id}/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseLisWrap>> qcGetCourses(@Path("id") String staff_id,
      @QueryMap HashMap<String, Object> params, @Query("is_private") int is_private);

  @GET("/api/staffs/{id}/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetAllCourses(@Path("id") String staff_id,
      @Query("brand_id") String brandid, @Query("id") String gym_id,
      @Query("model") String gym_model);

  @GET("/api/staffs/{staff_id}/batches/{batch_id}/{type}/?order_by=start&show_all=1")
  rx.Observable<QcDataResponse<BatchSchedulesWrap>> qcGetbatchSchedules(
      @Path("staff_id") String staff_id, @Path("batch_id") String batch_id,
      @Path("type") String type, @QueryMap HashMap<String, Object> params);

  //排课填充
  @GET("/api/staffs/{id}/{type}/arrange/template/")
  rx.Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(@Path("id") String id,
      @Path("type") String type, @Query("teacher_id") String teacher_id,
      @Query("course_id") String course_id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{staff_id}/courses/photos/")
  rx.Observable<QcDataResponse<JacketPhotoWrap>> qcGetJacket(@Path("staff_id") String id,
      @Query("course_id") String course_id, @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程计划
   */
  @GET("/api/v2/staffs/{staff_id}/plantpls/?show_all=1")
  rx.Observable<QcDataResponse<CoursePlans>> qcGetCoursePlan(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程下教练
   */
  @GET("/api/v2/staffs/{staff_id}/courses/teachers/")
  rx.Observable<QcDataResponse<CourseTeacherWrapper>> qcGetCourseTeacher(
      @Path("staff_id") String staff_id, @Query("course_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 课程下照片
   */
  @GET("/api/v2/staffs/{staff_id}/courses/schedules/photos/")
  rx.Observable<QcDataResponse<SchedulePhotoListWrap>> qcGetSchedulePhotos(
      @Path("staff_id") String staff_id, @Query("course_id") String id, @Query("page") int page,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程详情
   */
  @GET("/api/v2/staffs/{staff_id}/courses/")
  rx.Observable<QcDataResponse<CourseTypeWrap>> qcGetCourseDetail(@Path("staff_id") String staff_id,
      @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

  /**
   * 分场馆评分
   */
  @GET("/api/v2/staffs/{staff_id}/courses/shops/score/")
  rx.Observable<QcDataResponse<ShopCommentWrap>> qcGetShopComment(@Path("staff_id") String staff_id,
      @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

  @GET("/api/staffs/{id}/permissions/") rx.Observable<QcResponsePermission> qcPermission(
      @Path("id") String staff_id, @QueryMap HashMap<String, Object> params);

  @POST("/api/v2/staffs/{id}/courses/") rx.Observable<QcDataResponse> qcCreateCourse(
      @Path("id") String staffid, @Body CourseBody courseBody,
      @QueryMap HashMap<String, Object> params);

  //修改课程
  @PUT("/api/v2/staffs/{id}/courses/{course_id}/") rx.Observable<QcDataResponse> qcUpdateCourse(
      @Path("id") String staffid, @Path("course_id") String course_id,
      @QueryMap HashMap<String, Object> params, @Body CourseBody courseBody);

  //删除课程
  @DELETE("/api/v2/staffs/{id}/courses/{course_id}/") rx.Observable<QcDataResponse> qcDelCourse(
      @Path("id") String staffid, @Path("course_id") String course_id,
      @QueryMap HashMap<String, Object> params);

  //修改封面
  @POST("/api/v2/staffs/{id}/courses/photos/") rx.Observable<QcDataResponse> qcEditJacket(
      @Path("id") String staffid, @Query("course_id") String course_id,
      @QueryMap HashMap<String, Object> params, @Body EditJacketBody body);

  //修改课程适用场馆
  @PUT("/api/v2/staffs/{staff_id}/courses/{course_id}/shops/")
  rx.Observable<QcDataResponse> qcEditCourseShops(@Path("staff_id") String staffid,
      @Path("course_id") String course_id, @Body HashMap<String, Object> params);

  /**
   * 排期
   */
  //获取所有排期
  @GET("/api/staffs/{id}/schedules/") rx.Observable<QcSchedulesResponse> qcGetSchedules(
      @Path("id") String id, @Query("date") String date, @QueryMap HashMap<String, Object> params);

  //单挑排期
  @GET("/api/staffs/{id}/{type}/{single_id}/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<SingleBatchWrap>> qcGetSingleBatch(
      @Path("id") String id, @Path("type") String type, @Path("single_id") String single_id,
      @QueryMap HashMap<String, Object> params);

  @POST("/api/staffs/{id}/arrange/batches/") rx.Observable<QcDataResponse> qcArrangeBatch(
      @Path("id") String staff_id, @Body ArrangeBatchBody body,
      @QueryMap HashMap<String, Object> params);

  @PUT("/api/staffs/{id}/batches/{batchid}/") rx.Observable<QcDataResponse> qcUpdateBatch(
      @Path("id") String staff_id, @Path("batchid") String batchid, @Body ArrangeBatchBody body,
      @QueryMap HashMap<String, Object> params);

  @POST("/api/staffs/{id}/{type}/arrange/check/") rx.Observable<QcDataResponse> qcCheckBatch(
      @Path("id") String staff_id, @Path("type") String type, @Body ArrangeBatchBody body,
      @QueryMap HashMap<String, Object> params);

  //删除排期
  @POST("api/staffs/{id}/{type}/bulk/delete/") rx.Observable<QcDataResponse> qcDelBatchSchedule(
      @Path("id") String staff_id, @Path("type") String type, @Body DelBatchScheduleBody body,
      @QueryMap HashMap<String, Object> params);

  @DELETE("/api/staffs/{id}/batches/{batch_id}/") rx.Observable<QcDataResponse> delBatch(
      @Path("id") String staff_id, @Path("batch_id") String batch_id,
      @QueryMap HashMap<String, Object> paras);

  @PUT("api/staffs/{id}/{type}/{scheduleid}/") rx.Observable<QcDataResponse> qcUpdateBatchSchedule(
      @Path("id") String staff_id, @Path("type") String type, @Path("scheduleid") String scheduleid,
      @Body SingleBatchBody body, @QueryMap HashMap<String, Object> params);

  //复制排期检查课程冲突
  @POST("/api/staffs/{staff_id}/copy-batch/check/")
  rx.Observable<QcDataResponse> qcCheckBatchConflict(@Path("staff_id") String staff_id,
      @Body BatchCopyBody body, @QueryMap HashMap<String, Object> params);

  //复制排期
  @POST("/api/staffs/{staff_id}/copy-batch/") rx.Observable<QcDataResponse> qcSureCopyBatch(
      @Path("staff_id") String staff_id, @Body BatchCopyBody body,
      @QueryMap HashMap<String, Object> params);

  //复制排期页拉取所有数据
  @GET("api/staffs/{staff_id}/copy-batch/schedules/")
  rx.Observable<QcDataResponse<CopyScheduleWrapper>> qcBatchCopySchedule(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //获取排期的课程体系
  @GET("api/v2/staffs/{staff_id}/workouts/")
  rx.Observable<QcDataResponse<WorkoutResponse>> qcGetCourseWorkout(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //获取排期的课程体系
  @GET("api/v2/staffs/{staff_id}/workouts/{workout_id}/plans/")
  rx.Observable<QcDataResponse<WorkoutPlansResponse>> qcGetCourseWorkoutPlans(
      @Path("staff_id") String staff_id, @Path("workout_id") String workout_id,
      @QueryMap HashMap<String, Object> params);
}
