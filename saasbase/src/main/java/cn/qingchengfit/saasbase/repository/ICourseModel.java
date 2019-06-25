package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.model.responese.GymExtra;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.CopyScheduleWrapper;
import cn.qingchengfit.saasbase.course.batch.bean.ScheduleTemplete;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutPlansResponse;
import cn.qingchengfit.saasbase.course.batch.bean.WorkoutResponse;
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
import cn.qingchengfit.saasbase.course.detail.ScheduleDetailWrapper;
import cn.qingchengfit.saasbase.course.detail.ScheduleOrders;
import cn.qingchengfit.saasbase.course.detail.SchedulePhotos;
import cn.qingchengfit.saasbase.staff.beans.SimpleSuccessResponse;
import java.util.HashMap;

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
  rx.Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(String course_id);

  //获取某个排期的详情
  rx.Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(String batch_id);

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
  rx.Observable<QcDataResponse> qcDelBatchSchedule(boolean isPrivate, String ids);

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
  rx.Observable<QcDataResponse<CourseTeacherWrapper>> qcGetCourseTeacher(String id, String shopid);

  /**
   * 课程下照片
   */
  rx.Observable<QcDataResponse<SchedulePhotoListWrap>> qcGetSchedulePhotos(String id, int page);

  /**
   * 获取课程详情
   */
  rx.Observable<QcDataResponse<CourseTypeWrap>> qcGetCourseDetail(String id);

  /**
   * 分场馆评分
   */
  rx.Observable<QcDataResponse<ShopCommentWrap>> qcGetShopComment(String id);

  rx.Observable<QcDataResponse> qcCreateCourse(CourseBody courseBody);

  //修改课程
  rx.Observable<QcDataResponse> qcUpdateCourse(String course_id, CourseBody courseBody);

  //删除课程
  rx.Observable<QcDataResponse> qcDelCourse(String course_id);

  //修改封面
  rx.Observable<QcDataResponse> qcEditJacket(String course_id, EditJacketBody body);

  //修改课程适用场馆
  rx.Observable<QcDataResponse> qcEditCourseShops(String course_id);

  rx.Observable<QcDataResponse<JacketPhotoWrap>> qcGetJacket(String course_id);

  rx.Observable<QcDataResponse> qcCheckBatchConflict(BatchCopyBody body);

  rx.Observable<QcDataResponse> qcSureCopyBatch(BatchCopyBody body);

  rx.Observable<QcDataResponse<CopyScheduleWrapper>> qcBatchCopySchedule(
      HashMap<String, Object> params);

  //获取排期的课程体系
  rx.Observable<QcDataResponse<WorkoutResponse>> qcGetCourseWorkout(HashMap<String, Object> params);

  rx.Observable<QcDataResponse<WorkoutPlansResponse>> qcGetCourseWorkoutPlans(String workoutID,
      HashMap<String, Object> params);

  //获取已经排好课程的课程详情，从课程预约进入
  rx.Observable<QcDataResponse<ScheduleDetailWrapper>> qcGetScheduleDetail(String schedule_id);

  //获取已经排好课程的预约人数列表
  rx.Observable<QcDataResponse<ScheduleOrders>> qcGetScheduleDetailOrder(String schedule_id);

  //获取已经排好课程的课程照片列表
  rx.Observable<QcDataResponse<SchedulePhotos>> qcGetScheduleDetailPhotos(String schedule_id);

  rx.Observable<QcDataResponse<SimpleSuccessResponse>> qcPostScheduleOrderCancel(String order_id);

  // 课程照片 批量删除
  rx.Observable<QcDataResponse<SimpleSuccessResponse>> qcDeleteSchedulePhotos(String schedule_id,
      String photoIDs);

  rx.Observable<QcDataResponse<GymExtra>> qcGetGymExtra();
}
