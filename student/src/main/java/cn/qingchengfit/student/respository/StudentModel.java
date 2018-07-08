package cn.qingchengfit.student.respository;

import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Flowable;
import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huangbaole on 2017/10/26.
 */
public class StudentModel implements IStudentModel {
  StudentApi studentApi;

  @Inject public StudentModel(QcRestRepository qcRestRepository) {
    OkHttpClient client = qcRestRepository.getClient();
    Gson customGsonInstance = (new GsonBuilder()).enableComplexMapKeySerialization().create();

    Retrofit retrofit = new Retrofit.Builder().client(client)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(qcRestRepository.getHost())
        .build();
    studentApi = retrofit.create(StudentApi.class);
  }

  //
  //@Override public Observable<QcDataResponse<StudentListWrapper>> getAllStudentNoPermission() {
  //  return null;
  //}
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
  //@Override public Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetAllStudents(id, params);
  //}
  //
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

  //@Override
  //public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetAllotSaleOwenUsers(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id,
  //    String type, HashMap<String, Object> params) {
  //  return studentApi.qcGetAllotStaffMembers(staff_id, type, params);
  //}
  //
  //@Override public Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(
  //    String staff_id, HashMap<String, Object> params) {
  //  return studentApi.qcGetCoachStudentDetail(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid,
  //    String shopid, String gymid, String model) {
  //  return studentApi.qcGetSalers(staff_id, brandid, shopid, gymid, model);
  //}
  //
  //@Override
  //public Observable<QcResponse> qcModifySellers(String staff_id, HashMap<String, Object> params,
  //    HashMap<String, Object> body) {
  //  return studentApi.qcModifySellers(staff_id, params, body);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetAllAllocateCoaches(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcResponse> qcAllocateCoach(String staff_id, HashMap<String, Object> body) {
  //  return studentApi.qcAllocateCoach(staff_id, body);
  //}
  //
  //@Override
  //public Observable<QcResponse> qcRemoveStudent(String staff_id, HashMap<String, Object> body) {
  //  return studentApi.qcRemoveStudent(staff_id, body);
  //}
  //
  //@Override
  //public Observable<QcResponse> qcDeleteStudents(String staff_id, HashMap<String, Object> body) {
  //  return studentApi.qcDeleteStudents(staff_id, body);
  //}
  //
  //@Override public Observable<QcResponse> qcRemoveStaff(String staff_id, String type,
  //    HashMap<String, Object> body) {
  //  return studentApi.qcRemoveStaff(staff_id, type, body);
  //}
  //
  //@Override public Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(
  //    String staff_id, HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentsStatistics(staff_id, params);
  //}
  //
  //@Override public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(
  //    String staff_id, HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentCreate(staff_id, params);
  //}
  //
  //@Override public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
  //    String staff_id, HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentFollow(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(String staff_id,
  //    String type, HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudents(staff_id, type, params);
  //}
  //
  //@Override public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(
  //    String staff_id, HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentMember(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentsFilterSalers(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentsConver(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetAttendanceChart(id, params);
  //}
  //
  //@Override public Observable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(String id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetUsersAbsences(id, params);
  //}
  //
  //@Override public Observable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(String id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetUsersAttendances(id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetNotSignStudent(staffId, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentsRecommends(staff_id, params);
  //}
  //
  //@Override
  //public Observable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentsOrigins(staff_id, params);
  //}
}
