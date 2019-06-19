package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.BindLiveData;
import cn.qingchengfit.saascommon.network.HttpException;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.student.bean.AbsentceListWrap;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.AttendanceCharDataBean;
import cn.qingchengfit.student.bean.AttendanceListWrap;
import cn.qingchengfit.student.bean.ClassRecords;
import cn.qingchengfit.student.bean.CoachPtagAnswerBody;
import cn.qingchengfit.student.bean.CoachPtagQuestionnaire;
import cn.qingchengfit.student.bean.CoachStudentOverview;
import cn.qingchengfit.student.bean.FollowRecord;
import cn.qingchengfit.student.bean.FollowRecordAdd;
import cn.qingchengfit.student.bean.FollowRecordListWrap;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import cn.qingchengfit.student.bean.FollowRecordStatusListWrap;
import cn.qingchengfit.student.bean.InactiveStat;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.QcStudentBirthdayWrapper;
import cn.qingchengfit.student.bean.SalerTeachersListWrap;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.bean.StudentInfoGlance;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.bean.StudentTransferBean;
import cn.qingchengfit.student.bean.StudentWIthCount;
import cn.qingchengfit.student.respository.local.LocalRespository;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * 继承自StudentRespository,为Student模块的唯一数据来源
 * 作为M-V-VM中的Model层，应该持有两个两个数据源，一个是服务端数据源，另一个是本地持久化的数据源（目前没有）
 * 在Model层中应该是用RxJava来进行数据的响应式处理，然后在传递给VM层时，统一转换为LiveData.
 * 由于目前是用的RxJava1,在{@link StudentRepository}中写了一个toLiveData()函数，
 * 用于将Observable转化为Publisher,再变成LiveData，同时在转化的时候，对Observable对象进行了统一的线程处理。
 * <p>
 * 关于从Observablez转化为LiveData的时候，会遇到的一个问题就是由于数据的来源是有一定状态的，比如请求成功，请求失败或者请求中等
 * 在转化的时候，这些状态是由Observable订阅的时候进行处理，但是当使用{@link }
 * 进行转化的时候回默认将异常的处理屏蔽掉，这里目前有两种试下的思路
 * <p>
 * 1. 使用RxBus事件总线，直接将异常的处理传递处理异常的地方。
 * 2. 使用数据包装类，将数据源的数据与当前的数据状态包装到一个类中{@link Resource}
 * <p>
 * 目前的前提是当使用{@link } 进行转型的时候，如果事件流中有出现异常就是走到onError
 * 那么会直接抛异常。有两个方式去进行规避
 * 1. 自己写一个转换的方式不去抛 异常
 * 2. 使用操作符onErrorReturn等
 * <p>
 * Created by huangbaole on 2017/11/17.
 */
public class StudentRepositoryImpl implements StudentRepository {

  @Inject IStudentModel remoteService;

  @Inject LocalRespository localRespository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public StudentRepositoryImpl() {
  }

  static <T> LiveData<Resource<T>> toLiveData(Flowable<QcDataResponse<T>> observable) {
    return LiveDataTransfer.fromPublisher(observable.compose(RxHelper.schedulersTransformerFlow()));
  }

  static <T> void bindToLiveData(MutableLiveData<T> liveData,
      Flowable<QcDataResponse<T>> observable, MutableLiveData<Resource<Object>> result,
      String tag) {
    BindLiveData.bindLiveData(observable.compose(RxHelper.schedulersTransformerFlow()), liveData,
        result, tag);
  }

  public LiveData<Resource<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetAttendanceChart(id, params));
  }

  public LiveData<Resource<AbsentceListWrap>> qcGetUsersAbsences(String id,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetUsersAbsences(id, params));
  }

  public LiveData<Resource<AttendanceListWrap>> qcGetUsersAttendances(String id,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetUsersAttendances(id, params));
  }

  //
  //
  public LiveData<Resource<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetNotSignStudent(staffId, params));
  }

  public LiveData<Resource<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id,
      HashMap<String, Object> params) {

    return toLiveData(remoteService.qcGetTrackStudentsConver(staff_id, params));
  }

  //
  //
  //public LiveData<FollowUpDataStatistic> qcGetTrackStudentsStatistics(String staff_id, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetTrackStudentsStatistics(staff_id, params));
  //
  //}
  //
  public LiveData<Resource<StudentListWrappeForFollow>> qcGetTrackStudents(String staff_id,
      String type, HashMap<String, Object> params) {

    return toLiveData(remoteService.qcGetTrackStudents(staff_id, type, params));
  }

  public LiveData<Resource<AllotDataResponseWrap>> qcGetStaffList(String staff_id, String type,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetStaffList(staff_id, type, params));
  }

  public LiveData<Resource<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id, String type,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetAllotStaffMembers(staff_id, type, params));
  }

  @Override public void qcRemoveStaff(MutableLiveData<Boolean> result,
      MutableLiveData<Resource<Object>> defaultResult, String type, Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    bindToLiveData(result,
        remoteService.qcRemoveStaff(loginStatus.staff_id(), type, params1).map(qcDataResponse -> {
          QcDataResponse<Boolean> qcDataResponse1;
          if (qcDataResponse.status == 200) {
            qcDataResponse1 = qcDataResponse.copyResponse(true);
          } else {
            qcDataResponse1 = qcDataResponse.copyResponse(false);
          }
          return qcDataResponse1;
        }), defaultResult, "");
  }

  @Override
  public LiveData<Resource<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id,
      HashMap<String, Object> params) {

    return toLiveData(remoteService.qcGetAllAllocateCoaches(staff_id, params));
  }

  @Override public LiveData<Resource<Boolean>> qcAllocateCoach(String staff_id,
      HashMap<String, Object> body) {
    return toLiveData(remoteService.qcAllocateCoach(staff_id, body).map(qcResponse -> {
      QcDataResponse<Boolean> objectQcDataResponse = new QcDataResponse<>();
      objectQcDataResponse.setStatus(qcResponse.getStatus());
      if (qcResponse.status == 200) {
        objectQcDataResponse.setData(true);
      } else {
        throw new HttpException(qcResponse.getMsg(), qcResponse.getStatus());
      }
      return objectQcDataResponse;
    }));
  }

  @Override
  public LiveData<Resource<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid,
      String shopid, String gymid, String model) {
    return toLiveData(remoteService.qcGetSalers(staff_id, brandid, shopid, gymid, model));
  }

  @Override public void qcGetSalers(MutableLiveData<List<Staff>> result,
      MutableLiveData<Resource<Object>> defaultRes) {
    bindToLiveData(result, remoteService.qcGetSalers(null, null, null, null, null)
        .map(response -> response.copyResponse(response.data.users)), defaultRes, "");
  }

  @Override public LiveData<Resource<Boolean>> qcModifySellers(String staff_id,
      HashMap<String, Object> params, HashMap<String, Object> body) {
    return toLiveData(remoteService.qcModifySellers(staff_id, params, body).map(qcResponse -> {
      QcDataResponse<Boolean> objectQcDataResponse = new QcDataResponse<>();
      objectQcDataResponse.setStatus(qcResponse.getStatus());

      if (qcResponse.status == 200) {
        objectQcDataResponse.setData(true);
      } else {
        throw new HttpException(qcResponse.getMsg(), qcResponse.getStatus());
      }
      return objectQcDataResponse;
    }));
  }

  @Override public LiveData<Resource<StudentInfoGlance>> qcGetHomePageInfo(String staff_id,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetHomePageInfo(staff_id, params));
  }

  @Override public LiveData<Resource<List<StatDate>>> qcGetIncreaseStat(String staff_id,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetIncreaseStat(staff_id, params));
  }

  @Override public LiveData<Resource<StatDate>> qcGetFollowStat(HashMap<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return toLiveData(remoteService.qcGetFollowStat(loginStatus.staff_id(), params1));
  }

  @Override public LiveData<Resource<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
      Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return toLiveData(remoteService.qcGetTrackStudentFollow(loginStatus.staff_id(), params1));
  }

  @Override public void qcGetInactiveStat(MutableLiveData<InactiveStat> liveData,
      MutableLiveData<Resource<Object>> rst, int status) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("status", status);
    bindToLiveData(liveData, remoteService.qcGetInactiveStat(loginStatus.staff_id(), params), rst,
        "");
  }

  @Override
  public void qcGetSellerInactiveUsers(MutableLiveData<List<QcStudentBeanWithFollow>> liveData,
      MutableLiveData<Resource<Object>> rst, int status, int time_period_id, String seller_id) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("status", status);
    params.put("time_period_id", time_period_id);
    params.put("seller_id", seller_id);
    bindToLiveData(liveData, remoteService.qcGetSellerInactiveUsers(loginStatus.staff_id(), params)
        .map(response -> response.copyResponse(response.data.users)), rst, "");
  }

  @Override public LiveData<Resource<QcStudentBirthdayWrapper>> qcGetStudentBirthday(
      Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return toLiveData(remoteService.qcGetStudentBirthday(loginStatus.staff_id(), params1));
  }

  @Override
  public LiveData<Resource<StudentListWrapper>> qcGetAllStudents(Map<String, Object> params) {
    if (!params.containsKey("shop_id")) {
      params.putAll(gymWrapper.getParams());
    }
    return toLiveData(remoteService.qcGetAllStudents(loginStatus.staff_id(), params));
  }

  @Override public void qcGetTrackStatus(MutableLiveData<List<FollowRecordStatus>> liveData,
      MutableLiveData<Resource<Object>> result) {
    bindToLiveData(liveData, remoteService.qcGetTrackStatus()
        .flatMap(
            (Function<QcDataResponse<FollowRecordStatusListWrap>, Flowable<QcDataResponse<List<FollowRecordStatus>>>>) response -> Flowable
                .just(response.copyResponse(response.data.getStatuses()))), result, "");
  }

  @Override public void qcAddTrackStatus(HashMap<String, Object> params,
      MutableLiveData<Resource<Object>> rst) {
    bindToLiveData(null, remoteService.qcAddTrackStatus(params), rst, "add");
  }

  @Override public void qcEditTrackStatus(String status_id, HashMap<String, Object> params,
      MutableLiveData<Resource<Object>> rst) {
    bindToLiveData(null, remoteService.qcEditTrackStatus(status_id, params), rst, "edit");
  }

  @Override public void qcDelTrackStatus(String id, MutableLiveData<Resource<Object>> rst) {
    bindToLiveData(new MutableLiveData<>(), remoteService.qcDelTrackStatus(id), rst, "del");
  }

  @Override public void qcAddTrackRecord(String user_id, FollowRecordAdd body,
      MutableLiveData<Resource<Object>> rst) {
    bindToLiveData(null, remoteService.qcAddTrackRecord(user_id, body), rst, "add");
  }

  @Override public LiveData<Resource<CoachPtagQuestionnaire>> qcGetPtagQuestionnaire(String coachId,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetPtagQuestionnaire(coachId, params));
  }

  @Override public LiveData<Resource<CoachPtagQuestionnaire>> qcGetPtagAnswers(String coachId,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetPtagAnswers(coachId, params));
  }

  @Override
  public Flowable<QcDataResponse<CoachStudentOverview>> qcGetCoachStudentOverview(String coachId,
      HashMap<String, Object> params) {
    return remoteService.qcGetCoachStudentOverview(coachId, params);
  }

  @Override
  public LiveData<Resource<CoachPtagQuestionnaire>> qcGetTrainerFeedbackNaire(String naireId) {
    return toLiveData(remoteService.qcGetTrainerFeedbackNaire(naireId));
  }

  @Override public void qcModifyTrainerFeedbackNaire(String naireId, HashMap<String, Object> params,
      MutableLiveData<Resource<Object>> rst) {
    bindToLiveData(null, remoteService.qcModifyTrainerFeedbackNaire(naireId, params), rst,
        "modify");
  }

  @Override
  public void qcSubmitPtagAnswer(CoachPtagAnswerBody body, MutableLiveData<Resource<Object>> rst) {
    bindToLiveData(null, remoteService.qcSubmitPtagAnswer(body), rst, "submit");
  }

  @Override public LiveData<Resource<ClassRecords>> qcGetStudentClassRecords(String userID,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcGetStudentClassRecords(userID, params));
  }

  @Override public void qcGetTrackRecords(MutableLiveData<List<FollowRecord>> liveData,
      MutableLiveData<Resource<Object>> rst, String studentId, HashMap<String, Object> params) {
    bindToLiveData(liveData, remoteService.qcGetTrackRecords(studentId, params)
        .flatMap(
            (Function<QcDataResponse<FollowRecordListWrap>, Flowable<QcDataResponse<List<FollowRecord>>>>) followRecordListWrapQcDataResponse -> Flowable
                .just(followRecordListWrapQcDataResponse.copyResponse(
                    followRecordListWrapQcDataResponse.getData().getRecords()))), rst, "");
  }

  //@Override
  //public LiveData<StudentListWrapper> qcGetAllStudents(String id, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetAllStudents(id, params));
  //}
  //
  //@Override
  //public LiveData<List<FilterModel>> qcGetFilterModelFromLocal() {
  //    MutableLiveData<List<FilterModel>> filte = new MutableLiveData<>();
  //    filte.setValue(localRespository.getAssetsFilterModels());
  //    return filte;
  //}
  //
  //@Override
  //public LiveData<SalerUserListWrap> qcGetTrackStudentsRecommends(String id, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetTrackStudentsRecommends(id, params));
  //}
  //
  //@Override
  //public LiveData<SourceBeans> qcGetTrackStudentsOrigins(String id, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetTrackStudentsOrigins(id, params));
  //}
  //
  //@Override
  //public LiveData<SalerListWrap> qcGetTrackStudentsFilterSalers(String id, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetTrackStudentsFilterSalers(id,params));
  //}
}
