package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.CopyScheduleWrapper;
import cn.qingchengfit.saasbase.course.batch.bean.ScheduleTemplete;
import cn.qingchengfit.saasbase.course.batch.network.body.ArrangeBatchBody;
import cn.qingchengfit.saasbase.course.batch.network.body.BatchCopyBody;
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
 * Created by Paper on 2017/9/11.
 */

public interface ICourseModel {

  //获取团课排课
  rx.Observable<QcDataResponse<BatchCourseListWrap>> qcGetGroupBatch();

  //获取私教排课
  rx.Observable<QcDataResponse<BatchCoachListWrap>> qcGetPrivateBatch();

  //获取健身房课程
  rx.Observable<QcDataResponse<CourseLisWrap>> qcGetCourses(boolean is_private);

  //获取健身房课程权限(无权限限制的所有健身房)
  rx.Observable<QcDataResponse<CourseLisWrap>> qcGetCoursesPermission(boolean is_private);

  //获取某个教练的排课列表
  rx.Observable<QcDataResponse<QcResponsePrivateDetail>> qcGetPrivateCoaches(String coach_id);

  //获取某节课种类的排课列表
  rx.Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(
    @Path("course_id") String course_id);

  //获取某个排期的详情
  rx.Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(
    @Path("batch_id") String batch_id);

  //获取某个排期的schedule
  rx.Observable<QcDataResponse<BatchSchedulesWrap>> qcGetbatchSchedules(String batch_id,
    boolean isPrivate);

  //排课填充
  rx.Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(boolean isPrivate,
    String teacher_id, String course_id);

  //检查排期是否冲突
  rx.Observable<QcDataResponse> qcCheckBatch(boolean isPrivate, ArrangeBatchBody body);

  //安排排期
  rx.Observable<QcDataResponse> qcArrangeBatch(ArrangeBatchBody body);

  //更新排课
  rx.Observable<QcDataResponse> qcUpdateBatch(String batchid, ArrangeBatchBody body);

  //删除排期 多条schedule
  rx.Observable<QcDataResponse> qcDelBatchSchedule(boolean isPrivate,
     String ids);

  /**
   * 获取某一条排期的详情
   *
   * @param isPrivate 是否为私教课
   * @param single_id 排期id
   */
  rx.Observable<QcDataResponse<SingleBatchWrap>> qcGetSingleBatch(boolean isPrivate,
    String single_id);

  //删除某个排期批次
  rx.Observable<QcDataResponse> delBatch(String batch_id);

  rx.Observable<QcDataResponse> qcUpdateBatchSchedule(boolean isPirvate, String scheduleid,
    SingleBatchBody body);

  /**
   * 获取课程计划
   */
  rx.Observable<QcDataResponse<CoursePlans>> qcGetCoursePlan();

  /**
   * 获取课程下教练
   */
  rx.Observable<QcDataResponse<CourseTeacherWrapper>> qcGetCourseTeacher(
    @Query("course_id") String id,String shopid);

  /**
   * 课程下照片
   */
  @GET("/api/v2/staffs/{staff_id}/courses/schedules/photos/")
  rx.Observable<QcDataResponse<SchedulePhotoListWrap>> qcGetSchedulePhotos(
    @Query("course_id") String id, @Query("page") int page);

  /**
   * 获取课程详情
   */
  @GET("/api/v2/staffs/{staff_id}/courses/")
  rx.Observable<QcDataResponse<CourseTypeWrap>> qcGetCourseDetail(@Query("course_id") String id);

  /**
   * 分场馆评分
   */
  @GET("/api/v2/staffs/{staff_id}/courses/shops/score/")
  rx.Observable<QcDataResponse<ShopCommentWrap>> qcGetShopComment(@Query("course_id") String id);

  @POST("/api/v2/staffs/{id}/courses/") rx.Observable<QcDataResponse> qcCreateCourse(
    @Body CourseBody courseBody);

  //修改课程
  @PUT("/api/v2/staffs/{id}/courses/{course_id}/") rx.Observable<QcDataResponse> qcUpdateCourse(
    @Path("course_id") String course_id, @Body CourseBody courseBody);

  //删除课程
  @DELETE("/api/v2/staffs/{id}/courses/{course_id}/") rx.Observable<QcDataResponse> qcDelCourse(
    @Path("course_id") String course_id);

  //修改封面
  @POST("/api/v2/staffs/{id}/courses/photos/") rx.Observable<QcDataResponse> qcEditJacket(
    @Query("course_id") String course_id, @Body EditJacketBody body);

  //修改课程适用场馆
  @PUT("/api/v2/staffs/{staff_id}/courses/{course_id}/shops/")
  rx.Observable<QcDataResponse> qcEditCourseShops(@Path("course_id") String course_id);

  @GET("/api/v2/staffs/{staff_id}/courses/photos/")
  rx.Observable<QcDataResponse<JacketPhotoWrap>> qcGetJacket(@Query("course_id") String course_id);

  rx.Observable<QcDataResponse> qcCheckBatchConflict(@Body BatchCopyBody body);

  rx.Observable<QcDataResponse> qcSureCopyBatch(@Body BatchCopyBody body);

  rx.Observable<QcDataResponse<CopyScheduleWrapper>> qcBatchCopySchedule(HashMap<String, Object> params);
}
