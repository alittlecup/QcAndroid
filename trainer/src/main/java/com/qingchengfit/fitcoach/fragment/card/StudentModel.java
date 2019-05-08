package com.qingchengfit.fitcoach.fragment.card;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
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
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
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
import cn.qingchengfit.student.respository.IStudentModel;
import com.qingchengfit.fitcoach.http.TrainerAllApi;
import com.qingchengfit.fitcoach.http.bean.QcAllStudentResponse;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.functions.Func1;

public class StudentModel implements IStudentModel {
  TrainerAllApi api;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public StudentModel(QcRestRepository restRepository) {
    api = restRepository.createRxJava1Api(TrainerAllApi.class);
  }

    @Override public Flowable<QcDataResponse<StudentBeanListWrapper>> getAllStudentNoPermission() {
    return RxJavaInterop.toV2Flowable(
        api.qcGetAllStudent(Integer.parseInt(loginStatus.staff_id()), gymWrapper.getParams())
            .map(qcAllStudentResponse -> {
              QcDataResponse<StudentBeanListWrapper> qcDataResponse = new QcDataResponse<>();
              qcDataResponse.setStatus(qcAllStudentResponse.status);
              qcDataResponse.setError_code(qcAllStudentResponse.error_code);
              qcDataResponse.setMsg(qcAllStudentResponse.msg);
              qcDataResponse.setLevel(qcAllStudentResponse.level);
              QcAllStudentResponse.Ship data = qcAllStudentResponse.data;
              if (data != null && !data.users.isEmpty()) {
                List<QcStudentBean> users = data.users;
                StudentBeanListWrapper studentBeanListWrapper = new StudentBeanListWrapper();
                for (QcStudentBean qcStudentBean : users) {
                  qcStudentBean.setId(qcStudentBean.getCloud_user().getId());
                }
                studentBeanListWrapper.users = users;
                qcDataResponse.setData(studentBeanListWrapper);
              } else {
                qcDataResponse.setData(new StudentBeanListWrapper());
              }
              return qcDataResponse;
            }));
  }

  @Override
  public Flowable<QcDataResponse<StudentBeanListWrapper>> loadStudentsByPhone(String phone) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("q", phone);
    params.put("show_all", 1);
    return RxJavaInterop.toV2Flowable(api.qcLoadStudentByPhone(loginStatus.staff_id(), params));
  }

  @Override public Flowable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id,
      Map<String, Object> params) {
    params.remove("show_all");
    return RxJavaInterop.toV2Flowable(api.qcGetAllStudent(Integer.parseInt(loginStatus.staff_id()), params)
        .map(
            (Func1<QcAllStudentResponse, QcDataResponse<StudentListWrapper>>) qcAllStudentResponse -> {
              QcDataResponse response = new QcDataResponse.Builder().msg(qcAllStudentResponse.msg)
                  .error_code(qcAllStudentResponse.error_code)
                  .info(qcAllStudentResponse.info)
                  .level(qcAllStudentResponse.level)
                  .status(qcAllStudentResponse.status)
                  .build();
              if (qcAllStudentResponse.data.users != null && !qcAllStudentResponse.data.users.isEmpty()) {
                StudentListWrapper wrapper = new StudentListWrapper();
                List<QcStudentBeanWithFollow> fllows = new ArrayList<>();
                for (QcStudentBean bean : qcAllStudentResponse.data.users){
                  QcStudentBeanWithFollow fllow = new QcStudentBeanWithFollow();
                  fllow.status = bean.status;
                  fllow.username = bean.username;
                  fllow.phone = bean.phone;
                  fllow.avatar = bean.avatar;
                  fllow.gender = bean.gender;
                  fllow.id = bean.id;
                  fllow.head = bean.head;
                  fllow.setCloud_user(bean.getCloud_user());
                  fllows.add(fllow);
                }
                wrapper.users = fllows;
                response.data = wrapper;
              }
              return response;
            }));
  }

  @Override public Flowable<QcDataResponse<AllotDataResponseWrap>> qcGetStaffList(String staff_id,
      String type, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id,
      String type, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid,
      String shopid, String gymid, String model) {
    return RxJavaInterop.toV2Flowable(api.qcGetSalers(loginStatus.staff_id(), null, null, gymWrapper.id(),
        gymWrapper.model()));
  }

  @Override
  public Flowable<QcResponse> qcModifySellers(String staff_id, HashMap<String, Object> params,
      HashMap<String, Object> body) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcResponse> qcAllocateCoach(String staff_id, HashMap<String, Object> body) {
    return null;
  }

  @Override public Flowable<QcDataResponse> qcRemoveStaff(String staff_id, String type,
      HashMap<String, Object> body) {
    return null;
  }

  @Override public Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
      String staff_id, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(String staff_id,
      String type, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterCoaches(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(String id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(String id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<ShortMsgList>> qcQueryShortMsgList(String id, Integer status,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<ShortMsgDetail>> qcQueryShortMsgDetail(String id, String messageid,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcResponse> qcPostShortMsg(String staffid, ShortMsgBody body,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcResponse> qcDelShortMsg(String staffid, String messageid,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcResponse> qcPutShortMsg(String staffid, ShortMsgBody body,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse> qcDataImport(String staff_id, HashMap<String, Object> body) {
    return null;
  }

  @Override public Flowable<QcDataResponse<StudentInfoGlance>> qcGetHomePageInfo(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<List<StatDate>>> qcGetIncreaseStat(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<StatDate>> qcGetFollowStat(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<InactiveStat>> qcGetInactiveStat(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<StudentListWrapper>> qcGetSellerInactiveUsers(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<QcStudentBirthdayWrapper>> qcGetStudentBirthday(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<Object>> qcAddTrackStatus(HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return RxJavaInterop.toV2Flowable(api.qcAddTrackStatus(loginStatus.staff_id(), params));
  }

  @Override public Flowable<QcDataResponse<FollowRecordStatusListWrap>> qcGetTrackStatus() {
    return RxJavaInterop.toV2Flowable(api.qcGetTrackStatus(loginStatus.staff_id(), gymWrapper.getParams()));
  }

  @Override public Flowable<QcDataResponse<Object>> qcEditTrackStatus(String track_status_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Flowable<QcDataResponse<Object>> qcDelTrackStatus(String track_status_id) {
    return null;
  }

  @Override
  public Flowable<QcDataResponse<Object>> qcAddTrackRecord(String user_id, FollowRecordAdd body) {
    body.setId(gymWrapper.id());
    body.setModel(gymWrapper.model());
    return RxJavaInterop.toV2Flowable(api.qcAddTrackRecord(loginStatus.staff_id(), user_id, body));
  }

  @Override public Flowable<QcDataResponse<FollowRecordListWrap>> qcGetTrackRecords(String user_id,
      HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return RxJavaInterop.toV2Flowable(api.qcGetStudentFollowRecord(loginStatus.staff_id(), user_id, params));
  }

  @Override
  public Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetPtagQuestionnaire(String coach_id,
      HashMap<String, Object> params) {
    return RxJavaInterop.toV2Flowable(api.qcGetPtagQuestionnaire(coach_id, params));
  }

  @Override public Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetPtagAnswers(String coachId,
      HashMap<String, Object> params) {
    return RxJavaInterop.toV2Flowable(api.qcGetPtagAnswers(coachId, params));
  }

  @Override
  public Flowable<QcDataResponse<CoachStudentOverview>> qcGetCoachStudentOverview(String coach_id,
      HashMap<String, Object> params) {
    return RxJavaInterop.toV2Flowable(api.qcGetStudentOverview(coach_id, params));
  }

  @Override public Flowable<QcDataResponse<Object>> qcSubmitPtagAnswer(CoachPtagAnswerBody body) {
    return RxJavaInterop.toV2Flowable(api.qcSubmitPtagAnswer(loginStatus.staff_id(), gymWrapper.getParams(), body));
  }

  @Override
  public Flowable<QcDataResponse<CoachPtagQuestionnaire>> qcGetTrainerFeedbackNaire(String naireId) {
    return RxJavaInterop.toV2Flowable(api.qcGetTrainerFeedbackNaire(loginStatus.staff_id(), naireId, gymWrapper.getParams()));
  }

  @Override public Flowable<QcDataResponse<Object>> qcModifyTrainerFeedbackNaire(String naireId, HashMap<String, Object> params) {
    return RxJavaInterop.toV2Flowable(api.qcModifyTrainerFeedbackNaire(loginStatus.staff_id(), naireId, gymWrapper.getParams(), params));
  }

  @Override
  public Flowable<QcDataResponse<CoachStudentFilterWrapper>> qcGetCoachStudentPtagFilter() {
    return RxJavaInterop.toV2Flowable(api.qcGetStudentFilter(loginStatus.staff_id(), gymWrapper.getParams()));
  }
}
