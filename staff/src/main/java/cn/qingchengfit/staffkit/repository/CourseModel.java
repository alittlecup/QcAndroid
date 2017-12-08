package cn.qingchengfit.staffkit.repository;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.ScheduleTemplete;
import cn.qingchengfit.saasbase.course.batch.network.body.ArrangeBatchBody;
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
import cn.qingchengfit.saasbase.repository.ICourseModel;
import rx.Observable;

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

public class CourseModel implements ICourseModel {

  private GymWrapper gymWrapper;
  private LoginStatus loginStatus;
  private CourseApi api;

  public CourseModel(QcRestRepository restRepository, GymWrapper gymWrapper,
    LoginStatus loginStatus) {
    api = restRepository.createGetApi(CourseApi.class);
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
  }

  /**
   * 已排期的团课列表
   */
  @Override public Observable<QcDataResponse<BatchCourseListWrap>> qcGetGroupBatch() {
    return api.getGroupBatch(loginStatus.staff_id(), gymWrapper.getParams());
  }

  /**
   * 已排课的教练
   */
  @Override public Observable<QcDataResponse<BatchCoachListWrap>> qcGetPrivateBatch() {
    return api.getPrivateBatch(loginStatus.staff_id(), gymWrapper.getParams());
  }

  /**
   * 获取所有课程列表
   *
   * @param is_private 是否为私教
   */
  @Override public Observable<QcDataResponse<CourseLisWrap>> qcGetCourses(boolean is_private) {
    return api.qcGetCourses(loginStatus.staff_id(), gymWrapper.getParams(), is_private ? 1 : 0);
  }

  /**
   * 获取所有课程，后台不检查权限
   *
   * @param is_private 是否为私教
   */
  @Override public Observable<QcDataResponse<CourseLisWrap>> qcGetCoursesPermission(
    boolean is_private) {
    return api.qcGetCoursesPermission(loginStatus.staff_id(), gymWrapper.getParams(),
      is_private ? 1 : 0);
  }

  /**
   * 获取 某个教练的排课
   *
   * @param coach_id 教练的id
   */
  @Override public Observable<QcDataResponse<QcResponsePrivateDetail>> qcGetPrivateCoaches(
    String coach_id) {
    return api.qcGetPrivateCoaches(loginStatus.staff_id(), coach_id, gymWrapper.getParams());
  }

  /**
   * 获取某个团课 下的排期
   *
   * @param course_id 课程ID
   */
  @Override public Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(
    String course_id) {
    return api.qcGetGroupCourses(loginStatus.staff_id(), course_id, gymWrapper.getParams());
  }

  /**
   * 获取某条排课周期的详情，包括付款方式 场地 和周期算法
   */
  @Override public Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(String batch_id) {
    return api.qcGetBatchDetail(loginStatus.staff_id(), batch_id, gymWrapper.getParams());
  }

  /**
   * 获取某条 排课周期生成的所有 排课实例
   *
   * @param batch_id 周期id
   * @param isPrivate 是否为私教
   */
  @Override public Observable<QcDataResponse<BatchSchedulesWrap>> qcGetbatchSchedules(
    String batch_id, boolean isPrivate) {
    return api.qcGetbatchSchedules(loginStatus.staff_id(), batch_id, isPrivate ? "1" : "0",
      gymWrapper.getParams());
  }

  /**
   * 获取某次排课周期 的上次排课（模板
   *
   * @param isPrivate 私教
   * @param teacher_id 教练
   * @param course_id 课程
   */
  @Override public Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(
    boolean isPrivate, String teacher_id, String course_id) {
    return api.qcGetBatchTemplate(loginStatus.staff_id(), isPrivate ? "1" : "0", teacher_id,
      course_id, gymWrapper.getParams());
  }

  /**
   * 检查排期是否冲突
   *
   * @param isPrivate 是否私教
   */
  @Override public Observable<QcDataResponse> qcCheckBatch(boolean isPrivate,
    ArrangeBatchBody body) {
    return api.qcCheckBatch(loginStatus.staff_id(), isPrivate ? "private" : "group", body,
      gymWrapper.getParams());
  }

  /**
   * 排课
   */
  @Override public Observable<QcDataResponse> qcArrangeBatch(ArrangeBatchBody body) {
    return api.qcArrangeBatch(loginStatus.staff_id(), body, gymWrapper.getParams());
  }

  /**
   * 修改排课周期
   */
  @Override public Observable<QcDataResponse> qcUpdateBatch(String batchid, ArrangeBatchBody body) {
    return api.qcUpdateBatch(loginStatus.staff_id(), batchid, body, gymWrapper.getParams());
  }

  /**
   * 删除某条 排课实例
   */
  @Override public Observable<QcDataResponse> qcDelBatchSchedule(boolean isPrivate,
    DelBatchScheduleBody body) {
    return api.qcDelBatchSchedule(loginStatus.staff_id(), isPrivate ? "1" : "0", body,
      gymWrapper.getParams());
  }

  /**
   * 获取某条 排课实例
   *
   * @param isPrivate 是否为私教课
   * @param single_id 排期id
   */
  @Override public Observable<QcDataResponse<SingleBatchWrap>> qcGetSingleBatch(boolean isPrivate,
    String single_id) {
    return api.qcGetSingleBatch(loginStatus.staff_id(), isPrivate ? "1" : "0", single_id,
      gymWrapper.getParams());
  }

  /**
   * 删除排课周期
   */
  @Override public Observable<QcDataResponse> delBatch(String batch_id) {
    return api.delBatch(loginStatus.staff_id(), batch_id, gymWrapper.getParams());
  }

  /**
   * 更新某条排课实例
   */
  @Override public Observable<QcDataResponse> qcUpdateBatchSchedule(boolean isPirvate,
    String scheduleid, SingleBatchBody body) {
    return api.qcUpdateBatchSchedule(loginStatus.staff_id(), isPirvate ? "1" : "0", scheduleid,
      body, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<CoursePlans>> qcGetCoursePlan() {
    return api.qcGetCoursePlan(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<CourseTeacherWrapper>> qcGetCourseTeacher(String id,
    String shopid) {
    // TODO: 2017/11/30 处理shop id 的问题
    return api.qcGetCourseTeacher(loginStatus.staff_id(), id, gymWrapper.getParams());
  }



  @Override public Observable<QcDataResponse<SchedulePhotoListWrap>> qcGetSchedulePhotos(String id,
    int page) {
    return api.qcGetSchedulePhotos(loginStatus.staff_id(), id, page, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<CourseTypeWrap>> qcGetCourseDetail(String id) {
    return api.qcGetCourseDetail(loginStatus.staff_id(), id, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<ShopCommentWrap>> qcGetShopComment(String id) {
    return api.qcGetShopComment(loginStatus.staff_id(), id, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> qcCreateCourse(CourseBody courseBody) {
    return api.qcCreateCourse(loginStatus.staff_id(), courseBody, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCourse(String course_id, CourseBody courseBody) {
    return api.qcUpdateCourse(loginStatus.staff_id(), course_id, gymWrapper.getParams(),
      courseBody);
  }

  @Override public Observable<QcDataResponse> qcDelCourse(String course_id) {
    return api.qcDelCourse(loginStatus.staff_id(), course_id, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> qcEditJacket(String course_id, EditJacketBody body) {
    return api.qcEditJacket(loginStatus.staff_id(), course_id, gymWrapper.getParams(), body);
  }

  @Override public Observable<QcDataResponse> qcEditCourseShops(String course_id) {
    return api.qcEditCourseShops(loginStatus.staff_id(), course_id, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<JacketPhotoWrap>> qcGetJacket(String course_id) {
    return api.qcGetJacket(loginStatus.staff_id(),course_id,gymWrapper.getParams());
  }
}
