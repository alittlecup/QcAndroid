package cn.qingchengfit.student.respository;

import android.util.Log;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.student.bean.AbsentceListWrap;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.AttendanceCharDataBean;
import cn.qingchengfit.student.bean.AttendanceListWrap;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huangbaole on 2017/10/26.
 */
public class StudentModel implements IStudentModel {
  StudentApi studentApi;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;

  @Inject public StudentModel(QcRestRepository qcRestRepository) {
    OkHttpClient client = qcRestRepository.getClient();
    OkHttpClient http = client.newBuilder().addInterceptor(new HttpLoggingInterceptor(message -> {
      Log.d("HTTP", message);
    }).setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    Gson customGsonInstance = (new GsonBuilder()).enableComplexMapKeySerialization().create();

    Retrofit retrofit = new Retrofit.Builder().client(http)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(qcRestRepository.getHost())
        .build();
    studentApi = retrofit.create(StudentApi.class);
  }

  //
  @Override public Flowable<QcDataResponse<StudentBeanListWrapper>> getAllStudentNoPermission() {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("key", PermissionServerUtils.MANAGE_COSTS);
    params.put("method", "post");
    return studentApi.qcGetCardBundldStudents(loginStatus.staff_id(), params);
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
    return studentApi.qcAddTrackStatus(loginStatus.staff_id(),params);
  }

  @Override public Flowable<QcDataResponse<FollowRecordStatusListWrap>> qcGetTrackStatus() {
    return studentApi.qcGetTrackStatus(loginStatus.staff_id(),gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<Object>> qcEditTrackStatus(String track_status_id,
    HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return studentApi.qcEditTrackStatus(loginStatus.staff_id(),track_status_id,params);
  }

  @Override public Flowable<QcDataResponse<Object>> qcDelTrackStatus(String track_status_id) {
    return studentApi.qcDelTrackStatus(loginStatus.staff_id(),track_status_id,gymWrapper.getParams());
  }

  @Override
  public Flowable<QcDataResponse<Object>> qcAddTrackRecord(String user_id, FollowRecordAdd body) {
    body.setId(gymWrapper.id());
    body.setModel(gymWrapper.model());
    return studentApi.qcAddTrackRecord(loginStatus.staff_id(),user_id,body);
  }

  @Override public Flowable<QcDataResponse<FollowRecordListWrap>> qcGetTrackRecords(String user_id,
    HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return studentApi.qcGetTrackRecords(loginStatus.staff_id(),user_id,params);
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
    return studentApi.qcGetSalers(staff_id, brandid, shopid, gymid, model);
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
  @Override public Flowable<QcDataResponse> qcRemoveStaff(String staff_id, String type,
      HashMap<String, Object> body) {
    return studentApi.qcRemoveStaff(staff_id, type, body);
  }
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
  @Override public Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
      String staff_id, HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentFollow(staff_id, params);
  }

  //
  @Override public Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(
      String staff_id, String type, HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudents(staff_id, type, params);
  }

  //
  //@Override public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(
  //    String staff_id, HashMap<String, Object> params) {
  //  return studentApi.qcGetTrackStudentMember(staff_id, params);
  //}
  //
  @Override public Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(
      String staff_id, HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentsFilterSalers(staff_id, params);
  }
  //
  @Override
  public Flowable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentsConver(staff_id, params);
  }
  //
  @Override
  public Flowable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
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

  @Override
  public Flowable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetTrackStudentsOrigins(staff_id, params);
  }
}
