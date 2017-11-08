package cn.qingchengfit.repository;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
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
import cn.qingchengfit.saasbase.course.course.network.response.CourseLisWrap;
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

  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  CourseApi api;
  public CourseModel(QcRestRepository restRepository,GymWrapper gymWrapper,LoginStatus loginStatus) {
    api = restRepository.createGetApi(CourseApi.class);
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
  }

  @Override public Observable<QcDataResponse<BatchCourseListWrap>> qcGetGroupBatch() {
    return api.getGroupBatch(loginStatus.staff_id(),gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<BatchCoachListWrap>> qcGetPrivateBatch() {
    return api.getPrivateBatch(loginStatus.staff_id(),gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<CourseLisWrap>> qcGetCourses(boolean is_private) {
    return api.qcGetCourses(loginStatus.staff_id(),gymWrapper.getParams(),is_private?1:0);
  }

  @Override
  public Observable<QcDataResponse<CourseLisWrap>> qcGetCoursesPermission(boolean is_private) {
    return api.qcGetCoursesPermission(loginStatus.staff_id(),gymWrapper.getParams(),is_private?1:0);
  }

  @Override public Observable<QcResponsePrivateDetail> qcGetPrivateCoaches(String coach_id) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(String course_id) {
    return null;
  }

  @Override public Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(String batch_id) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<BatchSchedulesWrap>> qcGetbatchSchedules(String batch_id,
    boolean isPrivate) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(boolean isPrivate,
    String teacher_id, String course_id) {
    return null;
  }

  @Override public Observable<QcResponse> qcCheckBatch(boolean isPrivate, ArrangeBatchBody body) {
    return null;
  }

  @Override public Observable<QcResponse> qcArrangeBatch(ArrangeBatchBody body) {
    return null;
  }

  @Override public Observable<QcResponse> qcUpdateBatch(String batchid, ArrangeBatchBody body) {
    return null;
  }

  @Override
  public Observable<QcResponse> qcDelBatchSchedule(boolean isPrivate, DelBatchScheduleBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<SingleBatchWrap>> qcGetSingleBatch(boolean isPrivate,
    String single_id) {
    return null;
  }

  @Override public Observable<QcResponse> delBatch(String batch_id) {
    return null;
  }

  @Override
  public Observable<QcResponse> qcUpdateBatchSchedule(boolean isPirvate, String scheduleid,
    SingleBatchBody body) {
    return null;
  }
}
