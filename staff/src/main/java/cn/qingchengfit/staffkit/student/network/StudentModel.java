package cn.qingchengfit.staffkit.student.network;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.bean.SourceBeans;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.saasbase.student.network.body.AbsentceListWrap;
import cn.qingchengfit.saasbase.student.network.body.AddStdudentBody;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponseWrap;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.network.body.AttendanceListWrap;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.saasbase.student.network.body.StudentWithCoashListWrap;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import java.util.HashMap;
import java.util.List;
import rx.Observable;

/**
 * Created by fb on 2017/12/18.
 */

public class StudentModel implements IStudentModel {

  QcRestRepository repository;
  GymWrapper gymWrapper;
  LoginStatus loginStatus;

  public StudentModel(QcRestRepository repository, GymWrapper gymWrapper, LoginStatus loginStatus) {
    this.repository = repository;
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> getAllStudentNoPermission() {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("key", PermissionServerUtils.MANAGE_COSTS);
    params.put("method", "post");
    return repository.createGetApi(Get_Api.class)
        .qcGetCardBundldStudents(loginStatus.staff_id(), params);
  }

  @Override public Observable<QcDataResponse> addStudent(AddStdudentBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(String id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetStaffList(String staff_id,
      String type, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id,
      String type, HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(
      String staff_id, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid,
      String shopid, String gymid, String model) {
    return null;
  }

  @Override
  public Observable<QcResponse> qcModifySellers(String staff_id, HashMap<String, Object> params,
      HashMap<String, Object> body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcResponse> qcAllocateCoach(String staff_id, HashMap<String, Object> body) {
    return null;
  }

  @Override
  public Observable<QcResponse> qcRemoveStudent(String staff_id, HashMap<String, Object> body) {
    return null;
  }

  @Override
  public Observable<QcResponse> qcDeleteStudents(String staff_id, HashMap<String, Object> body) {
    return null;
  }

  @Override public Observable<QcResponse> qcRemoveStaff(String staff_id, String type,
      HashMap<String, Object> body) {
    return null;
  }

  @Override public Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(
      String staff_id, HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(
      String staff_id, HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
      String staff_id, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(String staff_id,
      String type, HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(
      String staff_id, HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(String id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(String id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(String staff_id,
      HashMap<String, Object> params) {
    return null;
  }
}
