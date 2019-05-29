package cn.qingchengfit.student.respository;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.bean.AbsentceListWrap;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.AttendanceCharDataBean;
import cn.qingchengfit.student.bean.AttendanceListWrap;
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
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/10/26.
 */
public class StudentModel implements IStudentModel {
  StudentApi studentApi;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;

  @Inject public StudentModel(QcRestRepository qcRestRepository) {

    studentApi = qcRestRepository.createRxJava2Api(StudentApi.class);
  }

  //
  @Override public Flowable<QcDataResponse<StudentBeanListWrapper>> getAllStudentNoPermission(String method) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("key", PermissionServerUtils.MANAGE_COSTS);
    if(StringUtils.isEmpty(method)){
      params.put("method", "post");
    }else{
      params.put("method", method);

    }
    return studentApi.qcGetCardBundldStudents(loginStatus.staff_id(), params);
  }

  @Override
  public Flowable<QcDataResponse<StudentBeanListWrapper>> loadStudentsByPhone(String phone) {
    HashMap<String, Object> params = gymWrapper.getParams();
    if (phone.length() >= 4) {
      params.put("q", phone);
    } else {
      params.put("username__icontains", phone);
    }
    params.put("show_all", 1);
    return studentApi.qcLoadStudentByPhone(loginStatus.staff_id(), params);
  }

  @Override
  public Flowable<QcDataResponse<StudentBeanListWrapper>> qcGetCardBundldStudents(String id) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("card_id", id);
    return studentApi.qcGetBindStudent(loginStatus.staff_id(), params);
  }

  //
  //@Override public Observable<QcDataResponse> addStudent(AddStudentBody body) {
  //  return null;
  //}
  //
  //@Override public Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(String id,
  //    HashMap<String, Object> params) {
  //
  //  return studentApi.qcGetCardBundldStudents(id, params);
  //}
  //
  @Override public Flowable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id,
      Map<String, Object> params) {
    return studentApi.qcGetAllStudents(id, params);
  }

  //@Override public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetCoachList(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetAllotSalesPreView(staff_id, params);
  //}
  //
  @Override public Flowable<QcDataResponse<AllotDataResponseWrap>> qcGetStaffList(String staff_id,
      String type, HashMap<String, Object> params) {
    return studentApi.qcGetStaffList(staff_id, type, params);
  }

  @Override
  public Flowable<QcDataResponse<ShortMsgList>> qcQueryShortMsgList(String id, Integer status,
      HashMap<String, Object> params) {
    return studentApi.qcQueryShortMsgList(id, status, params);
  }

  @Override
  public Flowable<QcDataResponse<ShortMsgDetail>> qcQueryShortMsgDetail(String id, String messageid,
      HashMap<String, Object> params) {
    return studentApi.qcQueryShortMsgDetail(id, messageid, params);
  }

  @Override public Flowable<QcResponse> qcPostShortMsg(String staffid, ShortMsgBody body,
      HashMap<String, Object> params) {
    return studentApi.qcPostShortMsg(staffid, body, params);
  }

  @Override public Flowable<QcResponse> qcDelShortMsg(String staffid, String messageid,
      HashMap<String, Object> params) {
    return studentApi.qcDelShortMsg(staffid, messageid, params);
  }

  @Override public Flowable<QcResponse> qcPutShortMsg(String staffid, ShortMsgBody body,
      HashMap<String, Object> params) {
    return studentApi.qcPutShortMsg(staffid, body, params);
  }

  @Override
  public Flowable<QcDataResponse> qcDataImport(String staff_id, HashMap<String, Object> body) {
    return studentApi.qcDataImport(staff_id, body);
  }

  @Override public Flowable<QcDataResponse<StudentInfoGlance>> qcGetHomePageInfo(String staff_id,
      HashMap<String, Object> params) {

    return studentApi.qcGetHomePageInfo(staff_id, params);
  }

  @Override public Flowable<QcDataResponse<List<StatDate>>> qcGetIncreaseStat(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetIncreaseStat(staff_id, params);
  }

  @Override public Flowable<QcDataResponse<StatDate>> qcGetFollowStat(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetFollowStat(staff_id, params);
  }

  @Override public Flowable<QcDataResponse<InactiveStat>> qcGetInactiveStat(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetInactiveStat(staff_id, params);
  }

  @Override
  public Flowable<QcDataResponse<StudentListWrapper>> qcGetSellerInactiveUsers(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetSellerInactiveUsers(staff_id, params);
  }

  @Override
  public Flowable<QcDataResponse<QcStudentBirthdayWrapper>> qcGetStudentBirthday(String staff_id,
      HashMap<String, Object> params) {

    return studentApi.qcGetStudentBirthday(staff_id, params);
  }

  @Override
  public Flowable<QcDataResponse<Object>> qcAddTrackStatus(HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return studentApi.qcAddTrackStatus(loginStatus.staff_id(), params);
  }

  @Override public Flowable<QcDataResponse<FollowRecordStatusListWrap>> qcGetTrackStatus() {
    return studentApi.qcGetTrackStatus(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<Object>> qcEditTrackStatus(String track_status_id,
      HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return studentApi.qcEditTrackStatus(loginStatus.staff_id(), track_status_id, params);
  }

  @Override public Flowable<QcDataResponse<Object>> qcDelTrackStatus(String track_status_id) {
    return studentApi.qcDelTrackStatus(loginStatus.staff_id(), track_status_id,
        gymWrapper.getParams());
  }

  @Override
  public Flowable<QcDataResponse<Object>> qcAddTrackRecord(String user_id, FollowRecordAdd body) {
    body.setId(gymWrapper.id());
    body.setModel(gymWrapper.model());
    return studentApi.qcAddTrackRecord(loginStatus.staff_id(), user_id, body);
  }

  @Override public Flowable<QcDataResponse<FollowRecordListWrap>> qcGetTrackRecords(String user_id,
      HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return studentApi.qcGetTrackRecords(loginStatus.staff_id(), user_id, params);
  }

  @Override
  public Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetPtagQuestionnaire(String coach_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetPtagAnswers(String coachId,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<CoachStudentOverview>> qcGetCoachStudentOverview(String coach_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<Object>> qcSubmitPtagAnswer(CoachPtagAnswerBody body) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetTrainerFeedbackNaire(String coachId) {
    return null;
  }

  @Override public Flowable<QcDataResponse<Object>> qcModifyTrainerFeedbackNaire(String naireId, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<CoachStudentFilterWrapper>> qcGetCoachStudentPtagFilter() {
    return null;
  }

  //@Override
  //public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetAllotSaleOwenUsers(staff_id, params);
  //}
  //
  @Override public Flowable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(
      String staff_id, String type, HashMap<String, Object> params) {
    return studentApi.qcGetAllotStaffMembers(staff_id, type, params);
  }

  //
  //@Override public Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(
  //    String staff_id, HashMap<String, Object> params) {
  //  return studentApi.qcGetCoachStudentDetail(staff_id, params);
  //}
  //
  @Override public Flowable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id,
      String brandid, String shopid, String gymid, String model) {
    return studentApi.qcGetSalers(loginStatus.staff_id(), null, null, gymWrapper.id(),
        gymWrapper.model());
  }

  //
  @Override public Flowable<QcResponse> qcModifySellers(String staff_id,
      HashMap<String, Object> params, HashMap<String, Object> body) {
    return studentApi.qcModifySellers(staff_id, params, body);
  }

  //
  @Override public Flowable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(
      String staff_id, HashMap<String, Object> params) {
    return studentApi.qcGetAllAllocateCoaches(staff_id, params);
  }

  //
  @Override public Flowable<QcResponse> qcAllocateCoach(String staff_id,
      HashMap<String, Object> body) {
    return studentApi.qcAllocateCoach(staff_id, body);
  }

  @Override public Flowable<QcDataResponse> qcRemoveStaff(String staff_id, String type,
      HashMap<String, Object> body) {
    return studentApi.qcRemoveStaff(staff_id, type, body);
  }


  @Override public Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
      String staff_id, HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentFollow(staff_id, params);
  }


  @Override public Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(
      String staff_id, String type, HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudents(staff_id, type, params);
  }


  @Override public Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(
      String staff_id, HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentsFilterSalers(staff_id, params);
  }

  @Override
  public Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterCoaches(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentsFilterCoaches(staff_id, params);
  }

  //
  @Override public Flowable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(
      String staff_id, HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentsConver(staff_id, params);
  }

  //
  @Override public Flowable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
      HashMap<String, Object> params) {
    return studentApi.qcGetAttendanceChart(id, params);
  }

  @Override public Flowable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(String id,
      HashMap<String, Object> params) {
    return studentApi.qcGetUsersAbsences(id, params);
  }

  @Override public Flowable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(String id,
      HashMap<String, Object> params) {
    return studentApi.qcGetUsersAttendances(id, params);
  }

  @Override
  public Flowable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId,
      HashMap<String, Object> params) {
    return studentApi.qcGetNotSignStudent(staffId, params);
  }

  @Override
  public Flowable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentsRecommends(staff_id, params);
  }

  @Override public Flowable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentsOrigins(staff_id, params);
  }
}
