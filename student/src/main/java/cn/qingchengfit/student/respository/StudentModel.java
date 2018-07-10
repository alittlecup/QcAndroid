package cn.qingchengfit.student.respository;

import android.support.annotation.IntRange;
import android.util.Log;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.SalerTeachersListWrap;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.ShortMsgBody;
import cn.qingchengfit.student.bean.ShortMsgDetail;
import cn.qingchengfit.student.bean.ShortMsgList;
import cn.qingchengfit.student.bean.StudentListWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Flowable;
import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by huangbaole on 2017/10/26.
 */
public class StudentModel implements IStudentModel {
  StudentApi studentApi;

  @Inject public StudentModel(QcRestRepository qcRestRepository) {
    OkHttpClient client = qcRestRepository.getClient();
    OkHttpClient http = client.newBuilder().addInterceptor(new HttpLoggingInterceptor(message -> {
      Log.d("HTTP", message);
    })).build();
    Gson customGsonInstance = (new GsonBuilder()).enableComplexMapKeySerialization().create();

    Retrofit retrofit = new Retrofit.Builder().client(http)
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

  //@Override
  //public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id,
  //    HashMap<String, Object> params) {
  //  return studentApi.qcGetAllotSaleOwenUsers(staff_id, params);
  //}
  //
  @Override
  public Flowable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id,
      String type, HashMap<String, Object> params) {
    return studentApi.qcGetAllotStaffMembers(staff_id, type, params);
  }
  //
  //@Override public Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(
  //    String staff_id, HashMap<String, Object> params) {
  //  return studentApi.qcGetCoachStudentDetail(staff_id, params);
  //}
  //
  @Override
  public Flowable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid,
      String shopid, String gymid, String model) {
    return studentApi.qcGetSalers(staff_id, brandid, shopid, gymid, model);
  }
  //
  @Override
  public Flowable<QcResponse> qcModifySellers(String staff_id, HashMap<String, Object> params,
      HashMap<String, Object> body) {
    return studentApi.qcModifySellers(staff_id, params, body);
  }
  //
  @Override
  public Flowable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id,
      HashMap<String, Object> params) {
    return studentApi.qcGetAllAllocateCoaches(staff_id, params);
  }
  //
  @Override
  public Flowable<QcResponse> qcAllocateCoach(String staff_id, HashMap<String, Object> body) {
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
