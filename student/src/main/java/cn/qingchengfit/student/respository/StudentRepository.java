package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.IntRange;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.MemberStat;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.QcStudentBirthdayWrapper;
import cn.qingchengfit.student.bean.SalerListWrap;
import cn.qingchengfit.student.bean.SalerTeachersListWrap;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.ShortMsgDetail;
import cn.qingchengfit.student.bean.ShortMsgList;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.bean.StudentInfoGlance;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by huangbaole on 2017/11/29.
 */

public interface StudentRepository {

  //
  //LiveData<AttendanceCharDataBean> qcGetAttendanceChart(String id, HashMap<String, Object> params);
  //
  //LiveData<AbsentceListWrap> qcGetUsersAbsences(String id, HashMap<String, Object> params);
  //
  //LiveData<AttendanceListWrap> qcGetUsersAttendances(String id, HashMap<String, Object> params);
  //
  //
  //LiveData<List<StudentWIthCount>> qcGetNotSignStudent(String staffId, HashMap<String, Object> params);
  //
  //
  //LiveData<StudentTransferBean> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params);
  //
  //LiveData<FollowUpDataStatistic> qcGetTrackStudentsStatistics(String staff_id, HashMap<String, Object> params);
  //
  LiveData<Resource<StudentListWrappeForFollow>> qcGetTrackStudents(String staff_id, String type, HashMap<String, Object> params);
  //
  LiveData<Resource<AllotDataResponseWrap>> qcGetStaffList(String staff_id, String type,
      HashMap<String, Object> params);

  //
  LiveData<Resource<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id, String type,
      HashMap<String, Object> params);

  //
  //LiveData<Boolean> qcRemoveStaff(String staff_id, String type, HashMap<String, Object> params);
  //
  //
  LiveData<Resource<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id,
      HashMap<String, Object> params);

  //
  //
  LiveData<Resource<Boolean>> qcAllocateCoach(String staff_id, HashMap<String, Object> body);

  //
  //
  LiveData<Resource<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid, String shopid,
      String gymid, String model);

  //
  LiveData<Resource<Boolean>> qcModifySellers(String staff_id, HashMap<String, Object> params,
      HashMap<String, Object> body);

  LiveData<Resource<StudentInfoGlance>> qcGetHomePageInfo(String staff_id,
      HashMap<String, Object> params);
  LiveData<Resource<List<StatDate>>> qcGetIncreaseStat(String staff_id,
       HashMap<String, Object> params);

  LiveData<Resource<StatDate>> qcGetFollowStat(
       HashMap<String, Object> params);

  LiveData<Resource<StudentListWrappeForFollow>> qcGetTrackStudentFollow(Map<String, Object> params);


  //全部会员-会员用户统计数据
  LiveData<Resource<MemberStat>> qcGetMemberStat(String type);

  //全部会员-新注册用户所属销售按时段统计数据
  LiveData<Resource<List<QcStudentBeanWithFollow>>> qcGetMemberSeller(String type,
      Map<String, Object> params);
  LiveData<Resource<QcStudentBirthdayWrapper>> qcGetStudentBirthday(
     Map<String, Object> params);



  //
  //
  LiveData<Resource<StudentListWrapper>> qcGetAllStudents(Map<String, Object> params);
  //
  //LiveData<List<FilterModel>> qcGetFilterModelFromLocal();
  //
  //LiveData<SalerUserListWrap> qcGetTrackStudentsRecommends(String id, HashMap<String, Object> params);
  //
  //LiveData<SourceBeans> qcGetTrackStudentsOrigins(String id, HashMap<String, Object> params);
  //LiveData<SalerListWrap> qcGetTrackStudentsFilterSalers(String id, HashMap<String, Object> params);
}
