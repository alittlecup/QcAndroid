package cn.qingchengfit.student.respository;

import android.support.annotation.IntRange;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.student.bean.AbsentceListWrap;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.AttendanceCharDataBean;
import cn.qingchengfit.student.bean.AttendanceListWrap;
import cn.qingchengfit.student.bean.ClassRecords;
import cn.qingchengfit.student.bean.CoachPtagAnswerBody;
import cn.qingchengfit.student.bean.CoachPtagQuestionnaire;
import cn.qingchengfit.student.bean.CoachStudentFilterWrapper;
import cn.qingchengfit.student.bean.CoachStudentOverview;
import cn.qingchengfit.student.bean.FollowRecordAdd;
import cn.qingchengfit.student.bean.FollowRecordListWrap;
import cn.qingchengfit.student.bean.FollowRecordStatusListWrap;
import cn.qingchengfit.student.bean.InactiveStat;
import cn.qingchengfit.student.bean.QcStudentBirthdayWrapper;
import cn.qingchengfit.student.bean.SalerListWrap;
import cn.qingchengfit.student.bean.SalerTeachersListWrap;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.ShortMsgBody;
import cn.qingchengfit.student.bean.ShortMsgDetail;
import cn.qingchengfit.student.bean.ShortMsgList;
import cn.qingchengfit.student.bean.SourceBeans;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.bean.StudentBeanListWrapper;
import cn.qingchengfit.student.bean.StudentInfoGlance;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.bean.StudentTransferBean;
import cn.qingchengfit.student.bean.StudentWIthCount;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by Paper on 2017/9/30.
 */

public interface IStudentModel {
  /**
   * 购卡 绑定会员时选择会员列表
   */
  Flowable<QcDataResponse<StudentBeanListWrapper>> getAllStudentNoPermission(String method);

  /**
   * 收银台模块获取会员列表，通过手机号前几位筛选
   * @return
   */
  Flowable<QcDataResponse<StudentBeanListWrapper>> loadStudentsByPhone(String phone);


  /**
   * 新增会员
   */
  //Observable<QcDataResponse> addStudent(AddStudentBody body);

  /**
   * 会员卡可绑定的会员列表
   */
  Flowable<QcDataResponse<StudentBeanListWrapper>> qcGetCardBundldStudents(String id);

  /**
   * 工作人员下所有会员
   *
   * @param id
   * @param params
   * @return
   */
  Flowable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id,
      Map<String, Object> params);

  /**
   * 教练分配
   *
   * @param staff_id
   * @param params
   * @return
   */
  //Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 销售列表预览接口
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?brand_id=2&shop_id=1
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?id=5370&model=staff_gym
   */
  //Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(String staff_id,
  //    HashMap<String, Object> params);
  //
  Flowable<QcDataResponse<AllotDataResponseWrap>> qcGetStaffList(String staff_id, String type,
    HashMap<String, Object> params);

  /**
   * 销售名下会员列表接口(无销售的会员列表不需要传seller_id)
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=2&seller_id=1
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym&seller_id=1
   */
  //Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id,
  //    HashMap<String, Object> params);
  //
  Flowable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id, String type,
    HashMap<String, Object> params);

  /**
   * 教练名下会员列表接口(无销售的会员列表不需要传seller_id)
   */
  //Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 获取销售 卖卡  包含销售 不包含教练
   */
  Flowable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid,
    String shopid, String gymid, String model);

  /**
   * /**
   * 批量变更销售
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym
   * PUT {"user_ids":"1,2,3,4,5", "seller_ids":"10,11"}
   *
   * @param body user_ids  seller_ids
   */
  Flowable<QcResponse> qcModifySellers(String staff_id, HashMap<String, Object> params,
    HashMap<String, Object> body);

  /**
   * 获取教练列表
   * 获取教练列表
   */
  Flowable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id,
    HashMap<String, Object> params);

  /**
   * 分配教练
   */
  Flowable<QcResponse> qcAllocateCoach(String staff_id, HashMap<String, Object> body);

  /**
   * 批量移除某个教练名下会员
   */
  //Observable<QcResponse> qcRemoveStudent(String staff_id, HashMap<String, Object> body);
  //
  /**
   * 批量移除某个销售名下会员:
   */
  //Observable<QcResponse> qcDeleteStudents(String staff_id, HashMap<String, Object> body);

  /**
   * 批量移除某个销售或者教练名下会员:
   */
  Flowable<QcDataResponse> qcRemoveStaff(String staff_id, String type, HashMap<String, Object> body);

  /**
   * 数据统计
   * /api/staffs/:staff_id/users/stat/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:status,[start][end][seller_id(无销售seller_id=0)]
   */
  //Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(String staff_id,
  //    HashMap<String, Object> params);
  //

  /**
   * 新增注册
   */
  //Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 新增跟进
   */
  Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(String staff_id,
      HashMap<String, Object> params);

  /**
   * 新增查询
   */
  Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(String staff_id,
      String type, HashMap<String, Object> params);

  /**
   * 新增学员
   */
  //Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 具有名下会员的销售列表
   * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
   */
  Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(String staff_id,
      HashMap<String, Object> params);

  /**
   * 具有名下会员的销售列表
   * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
   */
  Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterCoaches(String staff_id,
      HashMap<String, Object> params);

  /**
   * 转换率
   * /api/staffs/:staff_id/users/conver/stat/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:[start] [end] [seller_id(无销售seller_id=0)]
   */
  Flowable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id,
      HashMap<String, Object> params);

  /**
   * 获取缺勤图表数据
   */
  Flowable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
      HashMap<String, Object> params);

  /**
   * 缺勤列表
   *
   * @param params 缺勤<7： absence__lt=7，
   *               7-30天：absence__gte=7&absence__lte=30,
   *               缺勤>60天：absence__ge=60
   * @return "attendances": [{"user": {"username": "俞小西","gender": 1,"id": 2131,"avatar":
   * "https://img.qingchengfit.cn/9360bb9fb982a95c915edf44f611678f.jpeg!120x120","phone":
   * "18611985427"},"absence": 390,"date_and_time": "2016-01-30 13:30-14:30","id": 5933,"title": "娜娜私教 杨娜娜"},]
   */
  Flowable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(String id,
      HashMap<String, Object> params);

  /**
   * @param params 必传start, end，
   *               可选排序字段 加 “-”说明是倒序
   *               order_by=
   *               days      -days
   *               group     -group
   *               private     -private
   *               checkin   -checkin
   * @return "attendances": [{"checkin": 0,"group": 139,"user": {"username": "孙正其","gender": 0,"id": 2219,"avatar":
   * "https://img.qingchengfit.cn/a15bec431224aa638a4b8eccb2e96955.jpg!120x120","phone": "18536668518"},"private_count": 8,"days":
   * 142},
   */
  Flowable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(String id,
      HashMap<String, Object> params);

  /**
   * 获取未签课的学员
   */
  Flowable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId,
  HashMap<String, Object> params);

  /**
   * 推荐人列表
   *
   */
  Flowable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(String staff_id,
      HashMap<String, Object> params);

  /**
   * 来源列表
   */
  Flowable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(String staff_id,
      HashMap<String, Object> params);

  /**
   * 短信部分
   */
  Flowable<QcDataResponse<ShortMsgList>> qcQueryShortMsgList(String id,
    @IntRange(from = 1, to = 2) Integer status, HashMap<String, Object> params);

  Flowable<QcDataResponse<ShortMsgDetail>> qcQueryShortMsgDetail(String id, String messageid,
    HashMap<String, Object> params);

  Flowable<QcResponse> qcPostShortMsg(String staffid, ShortMsgBody body,
    HashMap<String, Object> params);

  Flowable<QcResponse> qcDelShortMsg(String staffid, String messageid,
    HashMap<String, Object> params);

  Flowable<QcResponse> qcPutShortMsg(String staffid, ShortMsgBody body,
    HashMap<String, Object> params);

  Flowable<QcDataResponse> qcDataImport(String staff_id, HashMap<String, Object> body);

  Flowable<QcDataResponse<StudentInfoGlance>> qcGetHomePageInfo(String staff_id,
    HashMap<String, Object> params);

  Flowable<QcDataResponse<List<StatDate>>> qcGetIncreaseStat(String staff_id,
      HashMap<String, Object> params);

  Flowable<QcDataResponse<StatDate>> qcGetFollowStat(String staff_id,
      HashMap<String, Object> params);


  //全部会员-用户不活跃情况统计数据
  Flowable<QcDataResponse<InactiveStat>> qcGetInactiveStat( String staff_id,
       HashMap<String, Object> params);

  //全部会员-销售名下会员列表
  Flowable<QcDataResponse<StudentListWrapper>> qcGetSellerInactiveUsers( String staff_id,
       HashMap<String, Object> params);

  Flowable<QcDataResponse<QcStudentBirthdayWrapper>> qcGetStudentBirthday(String staff_id,
     HashMap<String, Object> params);


  Flowable<QcDataResponse<Object>> qcAddTrackStatus(HashMap<String, Object> params);


  Flowable<QcDataResponse<FollowRecordStatusListWrap>> qcGetTrackStatus();

  Flowable<QcDataResponse<Object>> qcEditTrackStatus(String track_status_id,
    HashMap<String, Object> params);

  Flowable<QcDataResponse<Object>> qcDelTrackStatus(String track_status_id);

  Flowable<QcDataResponse<Object>> qcAddTrackRecord(String user_id, FollowRecordAdd body);

  Flowable<QcDataResponse<FollowRecordListWrap>> qcGetTrackRecords(String user_id,
    HashMap<String, Object> params);

  Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetPtagQuestionnaire(String coach_id, HashMap<String, Object> params);

  Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetPtagAnswers(String coachId, HashMap<String, Object> params);

  Flowable<QcDataResponse<CoachStudentOverview>> qcGetCoachStudentOverview(String coach_id, HashMap<String, Object> params);

  Flowable<QcDataResponse<Object>> qcSubmitPtagAnswer(CoachPtagAnswerBody body);

  Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetTrainerFeedbackNaire(String naireId);

  Flowable<QcDataResponse<Object>> qcModifyTrainerFeedbackNaire(String naireId, HashMap<String, Object> params);

  Flowable<QcDataResponse<CoachStudentFilterWrapper>> qcGetCoachStudentPtagFilter();

  Flowable<QcDataResponse<ClassRecords>> qcGetStudentClassRecords(String studentid,
       HashMap<String, Object> params);
  Flowable<QcDataResponse<ClassRecords>> qcGetClassRecordTemp(String studentid,  HashMap<String, Object> params);

}
