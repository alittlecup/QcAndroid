package cn.qingchengfit.saasbase.repository;

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
import retrofit2.http.Body;
import retrofit2.http.Path;

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
  rx.Observable<QcResponsePrivateDetail> qcGetPrivateCoaches(String coach_id);

  //获取某节课种类的排课列表
  rx.Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(
      @Path("course_id") String course_id);

  //获取某个排期的详情
  rx.Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(@Path("batch_id") String batch_id);

  //获取某个排期的schedule
  rx.Observable<QcDataResponse<BatchSchedulesWrap>> qcGetbatchSchedules(String batch_id,
      boolean isPrivate);

  //排课填充
  rx.Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(boolean isPrivate,
      String teacher_id, String course_id);

  //检查排期是否冲突
  rx.Observable<QcResponse> qcCheckBatch(boolean isPrivate, ArrangeBatchBody body);

  //安排排期
  rx.Observable<QcResponse> qcArrangeBatch(ArrangeBatchBody body);

  //更新排课
  rx.Observable<QcResponse> qcUpdateBatch(String batchid, ArrangeBatchBody body);

  //删除排期 多条schedule
  rx.Observable<QcResponse> qcDelBatchSchedule(boolean isPrivate, @Body DelBatchScheduleBody body);

  /**
   * 获取某一条排期的详情
   * @param isPrivate 是否为私教课
   * @param single_id 排期id
   */
  rx.Observable<QcDataResponse<SingleBatchWrap>> qcGetSingleBatch(boolean isPrivate,String single_id);
  //删除某个排期批次
  rx.Observable<QcResponse> delBatch(String batch_id);

  rx.Observable<QcResponse> qcUpdateBatchSchedule(boolean isPirvate, String scheduleid,
      SingleBatchBody body);
}
