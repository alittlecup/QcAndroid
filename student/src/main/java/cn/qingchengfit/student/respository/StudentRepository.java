package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.student.bean.AbsentceListWrap;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.AttendanceCharDataBean;
import cn.qingchengfit.student.bean.AttendanceListWrap;
import cn.qingchengfit.student.bean.FollowRecord;
import cn.qingchengfit.student.bean.InactiveStat;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.QcStudentBirthdayWrapper;
import cn.qingchengfit.student.bean.FollowRecordAdd;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import cn.qingchengfit.student.bean.SalerTeachersListWrap;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.bean.StudentInfoGlance;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.bean.StudentTransferBean;
import cn.qingchengfit.student.bean.StudentWIthCount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangbaole on 2017/11/29.
 */

public interface StudentRepository {

  //
  LiveData<Resource<AttendanceCharDataBean>> qcGetAttendanceChart(String id, HashMap<String, Object> params);
  //
  LiveData<Resource<AbsentceListWrap>> qcGetUsersAbsences(String id, HashMap<String, Object> params);
  //
  LiveData<Resource<AttendanceListWrap>> qcGetUsersAttendances(String id, HashMap<String, Object> params);
  //
  //
  LiveData<Resource<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId, HashMap<String, Object> params);
  //
  //
  LiveData<Resource<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params);
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


  //全部会员-用户不活跃情况统计数据
  void qcGetInactiveStat(MutableLiveData<InactiveStat> liveData,
      MutableLiveData<Resource<Object>> rst, int status);

  //全部会员-销售名下会员列表
  void qcGetSellerInactiveUsers(MutableLiveData<List<QcStudentBeanWithFollow>> liveData,
      MutableLiveData<Resource<Object>> rst, int status,int time_period_id,String seller_id);
  LiveData<Resource<QcStudentBirthdayWrapper>> qcGetStudentBirthday(
     Map<String, Object> params);




  //
  LiveData<Resource<StudentListWrapper>> qcGetAllStudents(Map<String, Object> params);
  //
  //LiveData<List<FilterModel>> qcGetFilterModelFromLocal();
  //
  //LiveData<SalerUserListWrap> qcGetTrackStudentsRecommends(String id, HashMap<String, Object> params);
  //
  //LiveData<SourceBeans> qcGetTrackStudentsOrigins(String id, HashMap<String, Object> params);
  //LiveData<SalerListWrap> qcGetTrackStudentsFilterSalers(String id, HashMap<String, Object> params);

  void qcGetTrackStatus(MutableLiveData<List<FollowRecordStatus>> liveData,
    MutableLiveData<Resource<Object>> rst);

  void qcGetTrackRecords(MutableLiveData<List<FollowRecord>> liveData,
    MutableLiveData<Resource<Object>> rst, String studentId);

  /**
   * @param params track_status
   */
  void qcAddTrackStatus(HashMap<String, Object> params,MutableLiveData<Resource<Object>> rst);
  void qcEditTrackStatus(String status_id,HashMap<String, Object> params,MutableLiveData<Resource<Object>> rst);

  void qcDelTrackStatus(String id,MutableLiveData<Resource<Object>> rst);

  void qcAddTrackRecord(String user_id, FollowRecordAdd body,
    MutableLiveData<Resource<Object>> rst);
}
