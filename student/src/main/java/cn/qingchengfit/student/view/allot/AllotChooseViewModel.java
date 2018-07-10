package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.bean.SalerTeachersListWrap;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/1.
 */

public class AllotChooseViewModel extends BaseViewModel {

  private MutableLiveData<List<String>> staffIds = new MutableLiveData<>();
  private MutableLiveData<List<String>> salserStaffids = new MutableLiveData<>();

  private final LiveData<List<Staff>> staffs;
  private final LiveData<Boolean> responseSuccess;

  public LiveData<List<Staff>> getStaffs() {
    return staffs;
  }

  public LiveData<Boolean> getResponseSuccess() {
    return responseSuccess;
  }

  public LiveData<List<Staff>> getSalerStaffs() {
    return salerStaffs;
  }

  public LiveData<Boolean> getSalerResponse() {
    return salerResponse;
  }

  private final LiveData<List<Staff>> salerStaffs;
  private final LiveData<Boolean> salerResponse;

  LoginStatus loginStatus;
  GymWrapper gymWrapper;
  StudentRepository studentRepository;

  List<String> students;

  public void setCurId(String curId) {
    this.curId = curId;
  }

  String curId;

  public void setStudents(List<String> students) {
    this.students = students;
  }

  @Inject public AllotChooseViewModel(LoginStatus loginStatus, GymWrapper gymWrapper,
      StudentRepository studentRepository) {
    this.loginStatus = loginStatus;
    this.gymWrapper = gymWrapper;
    this.studentRepository = studentRepository;

    staffs = Transformations.map(loadData(), input -> {
      SalerTeachersListWrap salerTeachersListWrap = dealResource(input);
      if (salerTeachersListWrap != null) {
        return salerTeachersListWrap.teachers;
      } else {
        return null;
      }
    });
    responseSuccess = Transformations.switchMap(staffIds, this::allotCoachRemote);

    salerStaffs = Transformations.map(loadSalerData(), input -> {
      SalerUserListWrap salerUserListWrap = dealResource(input);
      if (salerUserListWrap != null) {
        return salerUserListWrap.users;
      } else {
        return null;
      }
    });
    salerResponse = Transformations.switchMap(salserStaffids, this::allotSalerRemote);
  }

  // TODO: 2018/7/5
  public LiveData<Resource<SalerTeachersListWrap>> loadData() {
    return studentRepository.qcGetAllAllocateCoaches(loginStatus.staff_id(),
        gymWrapper.getParams());
  }

  public LiveData<Resource<SalerUserListWrap>> loadSalerData() {
    return studentRepository.qcGetSalers(loginStatus.staff_id(), null, null, gymWrapper.id(),
        gymWrapper.model());
  }

  public void allotCoach(List<String> strings) {
    this.staffIds.setValue(strings);
  }

  public void allotSaler(List<String> strings) {
    this.salserStaffids.setValue(strings);
  }

  private LiveData<Boolean> allotCoachRemote(List<String> ids) {
    HashMap<String, Object> body = gymWrapper.getParams();
    body.put("user_ids", StringUtils.List2Str(students));
    body.put("coach_ids", StringUtils.List2Str(ids));
    return Transformations.map(studentRepository.qcAllocateCoach(loginStatus.staff_id(), body),input -> {
      Boolean aBoolean = dealResource(input);
      if (aBoolean == null) {
        return null;
      } else {
        return aBoolean;
      }
    });
  }

  private LiveData<Boolean> allotSalerRemote(List<String> ids) {
    HashMap<String, Object> body = new HashMap<>();
    body.put("user_ids", StringUtils.List2Str(students));
    body.put("seller_ids", StringUtils.List2Str(ids));
    body.put("seller_id", curId);
    return Transformations.map(
        studentRepository.qcModifySellers(loginStatus.staff_id(), gymWrapper.getParams(), body),
        input -> {
          Boolean aBoolean = dealResource(input);
          if (aBoolean == null) {
            return null;
          } else {
            return aBoolean;
          }
        });
  }
}
