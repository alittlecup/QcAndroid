package cn.qingchengfit.repository;

import cn.qingchengfit.model.responese.CoursePlans;
import cn.qingchengfit.model.responese.CourseSchedules;
import cn.qingchengfit.model.responese.CourseTeacherResponse;
import cn.qingchengfit.model.responese.CourseTypeResponse;
import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.GroupCourseScheduleDetail;
import cn.qingchengfit.model.responese.QcResponseBtaches;
import cn.qingchengfit.model.responese.QcResponseJacket;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.model.responese.QcResponsePrivateBatchDetail;
import cn.qingchengfit.model.responese.QcResponsePrivateDetail;
import cn.qingchengfit.model.responese.QcResponseSchedulePhotos;
import cn.qingchengfit.model.responese.QcResponseShopComment;
import cn.qingchengfit.model.responese.ScheduleTemplete;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchCoachListWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchCourseListWrap;
import cn.qingchengfit.saasbase.course.course.network.response.CourseLisWrap;
import java.util.HashMap;
import retrofit2.http.GET;
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
  @GET("/api/staffs/{id}/batches/?course__is_private=1") rx.Observable<QcResponseBtaches> qcGetTeacherBatches(@Path("id") String id,
    @Query("brand_id") String brand_id, @Query("teacher_id") String teacher_id);

  //获取团课排课
  @GET("/api/staffs/{id}/group/courses/") rx.Observable<QcDataResponse<BatchCourseListWrap>> getGroupBatch(@Path("id") String staff_id,
    @QueryMap HashMap<String,Object> params);

  //获取私教排课
  @GET("/api/staffs/{id}/private/coaches/") rx.Observable<QcDataResponse<BatchCoachListWrap>> getPrivateBatch(@Path("id") String staff_id,
    @QueryMap HashMap<String,Object> params);



  //获取某个排期的详情
  @GET("/api/staffs/{id}/batches/{batch_id}/") rx.Observable<QcResponsePrivateBatchDetail> qcGetBatchDetail(@Path("id") String staff_id,
    @Path("batch_id") String batch_id, @Query("id") String gym_id, @Query("model") String gym_model,
    @Query("brand_id") String brand_id);


  //获取教练排期
  @GET("/api/staffs/{id}/coaches/{coach_id}/batches/?course__is_private=1") rx.Observable<QcResponsePrivateDetail> qcGetPrivateCoaches(
    @Path("id") String staff_id, @Path("coach_id") String coach_id, @Query("id") String gym_id, @Query("model") String gym_model,
    @Query("brand_id") String brand_id);

  //获取团课排期
  @GET("/api/staffs/{id}/courses/{course_id}/batches/") rx.Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(
    @Path("id") String staff_id, @Path("course_id") String course_id, @Query("id") String gym_id, @Query("model") String gym_model,
    @Query("brand_id") String brand_id);


  //获取健身房课程
  @GET("/api/staffs/{id}/courses/?&show_all=1") rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetCourses(@Path("id") String staff_id,
    @Query("id") String gym_id, @Query("model") String gym_model, @Query("is_private") int is_private);

  //获取健身房课程权限
  @GET("/api/v2/staffs/{id}/method/courses/?&show_all=1") rx.Observable<QcDataResponse<CourseLisWrap>> qcGetCoursesPermission(
    @Path("id") String staff_id, @QueryMap HashMap<String, Object> params, @Query("is_private") int is_private);



  //获取健身房课程权限
  @GET("/api/v2/staffs/{id}/method/courses/?&show_all=1") rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetAllCoursesPermission(
    @Path("id") String staff_id, @QueryMap HashMap<String, Object> params);

  //获取健身房课程列表
  @GET("/api/v2/staffs/{id}/courses/?&show_all=1") rx.Observable<QcDataResponse<CourseLisWrap>> qcGetCourses(@Path("id") String staff_id,
    @QueryMap HashMap<String, Object> params, @Query("is_private") int is_private);

  @GET("/api/staffs/{id}/courses/?&show_all=1") rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetAllCourses(
    @Path("id") String staff_id, @Query("brand_id") String brandid, @Query("id") String gym_id, @Query("model") String gym_model);


  @GET("/api/staffs/{staff_id}/batches/{batch_id}/{type}/?order_by=start&show_all=1")
  rx.Observable<QcDataResponse<CourseSchedules>> qcGetbatchSchedules(@Path("staff_id") String staff_id, @Path("batch_id") String batch_id,
    @Path("type") String type, @Query("brand_id") String brandid, @Query("id") String gymid, @Query("model") String gymmodel);

  //排课填充
  @GET("/api/staffs/{id}/{type}/arrange/template/") rx.Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(
    @Path("id") String id, @Path("type") String type, @Query("id") String gymid, @Query("model") String gymmodel,
    @Query("teacher_id") String teacher_id, @Query("course_id") String course_id);

  @GET("/api/v2/staffs/{staff_id}/courses/photos/") rx.Observable<QcDataResponse<QcResponseJacket>> qcGetJacket(
    @Path("staff_id") String id, @Query("course_id") String course_id, @QueryMap HashMap<String, Object> params);


  /**
   * 获取课程计划
   */
  @GET("/api/v2/staffs/{staff_id}/plantpls/?show_all=1") rx.Observable<QcDataResponse<CoursePlans>> qcGetCoursePlan(
    @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程下教练
   */
  @GET("/api/v2/staffs/{staff_id}/courses/teachers/") rx.Observable<QcDataResponse<CourseTeacherResponse>> qcGetCourseTeacher(
    @Path("staff_id") String staff_id, @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

  /**
   * 课程下照片
   */
  @GET("/api/v2/staffs/{staff_id}/courses/schedules/photos/") rx.Observable<QcResponseSchedulePhotos> qcGetSchedulePhotos(
    @Path("staff_id") String staff_id, @Query("course_id") String id, @Query("page") int page,
    @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程详情
   */
  @GET("/api/v2/staffs/{staff_id}/courses/") rx.Observable<QcDataResponse<CourseTypeResponse>> qcGetCourseDetail(
    @Path("staff_id") String staff_id, @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

  /**
   * 分场馆评分
   */
  @GET("/api/v2/staffs/{staff_id}/courses/shops/score/") rx.Observable<QcResponseShopComment> qcGetShopComment(
    @Path("staff_id") String staff_id, @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

  @GET("/api/staffs/{id}/permissions/") rx.Observable<QcResponsePermission> qcPermission(@Path("id") String staff_id,
    @QueryMap HashMap<String, Object> params);


}
