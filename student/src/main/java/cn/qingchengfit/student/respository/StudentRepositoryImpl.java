package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.IntRange;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.HttpException;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.MemberStat;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.QcStudentBirthdayWrapper;
import cn.qingchengfit.student.bean.SalerTeachersListWrap;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.ShortMsgDetail;
import cn.qingchengfit.student.bean.ShortMsgList;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.bean.StudentInfoGlance;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.respository.local.LocalRespository;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

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
@Singleton public class StudentRepositoryImpl implements StudentRepository {

  @Inject IStudentModel remoteService;

  @Inject LocalRespository localRespository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public StudentRepositoryImpl() {
  }

  static <T> LiveData<Resource<T>> toLiveData(Flowable<QcDataResponse<T>> observable) {
    return LiveDataTransfer.fromPublisher(observable.compose(RxHelper.schedulersTransformerFlow()));
  }

  //
  //public LiveData<AttendanceCharDataBean> qcGetAttendanceChart(String id, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetAttendanceChart(id, params));
  //}
  //
  //public LiveData<AbsentceListWrap> qcGetUsersAbsences(String id, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetUsersAbsences(id, params));
  //}
  //
  //
  //public LiveData<AttendanceListWrap> qcGetUsersAttendances(String id, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetUsersAttendances(id, params));
  //
  //}
  //
  //
  //public LiveData<List<StudentWIthCount>> qcGetNotSignStudent(String staffId, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcGetNotSignStudent(staffId, params));
  //}
  //
  //
  //public LiveData<StudentTransferBean> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params) {
  //
  //    return toLiveData(remoteService.qcGetTrackStudentsConver(staff_id, params));
  //}
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

  //
  //public LiveData<Boolean> qcRemoveStaff(String staff_id, String type, HashMap<String, Object> params) {
  //    return toLiveData(remoteService.qcRemoveStaff(staff_id, type, params).map(qcResponse -> {
  //        QcDataResponse<Boolean> objectQcDataResponse = new QcDataResponse<>();
  //        objectQcDataResponse.setStatus(qcResponse.getStatus());
  //        if (qcResponse.status == 200) {
  //            objectQcDataResponse.setData(true);
  //        } else {
  //            throw new HttpException(qcResponse.getMsg(), 0);
  //        }
  //        return objectQcDataResponse;
  //    }));
  //
  //}
  //
  @Override public LiveData<Resource<SalerTeachersListWrap>> qcGetAllAllocateCoaches(
      String staff_id, HashMap<String, Object> params) {

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

  @Override public LiveData<Resource<MemberStat>> qcGetMemberStat( String type) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    switch (type) {
      case IncreaseType.INCREASE_FOLLOWUP:
        return toLiveData(remoteService.qcGetFollowingStat(loginStatus.staff_id(), params1));
      case IncreaseType.INCREASE_STUDENT:
        return toLiveData(remoteService.qcGetMemberStat(loginStatus.staff_id(), params1));
      case IncreaseType.INCREASE_MEMBER:
        return toLiveData(remoteService.qcGetRegisterStat(loginStatus.staff_id(), params1));
    }
    return null;
  }

  @Override
  public LiveData<Resource<List<QcStudentBeanWithFollow>>> qcGetMemberSeller(
      String type, Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    switch (type) {
      case IncreaseType.INCREASE_FOLLOWUP:
        return toLiveData(remoteService.qcGetFollowingSeller(loginStatus.staff_id(), params1));
      case IncreaseType.INCREASE_STUDENT:
        return toLiveData(remoteService.qcGetMemberSeller(loginStatus.staff_id(), params1));
      case IncreaseType.INCREASE_MEMBER:
        return toLiveData(remoteService.qcGetRegisterSeller(loginStatus.staff_id(), params1));
    }
    return null;
  }

  @Override public LiveData<Resource<QcStudentBirthdayWrapper>> qcGetStudentBirthday(
      Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return toLiveData(remoteService.qcGetStudentBirthday(loginStatus.staff_id(),params1));
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
