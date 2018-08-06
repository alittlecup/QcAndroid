package cn.qingchengfit.student.view.followrecord;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.student.bean.Attach;
import cn.qingchengfit.student.bean.FollowRecordAdd;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

public class FollowRecordEditViewModel extends BaseViewModel {

  public MutableLiveData<String> content = new MutableLiveData<>();
  public MutableLiveData<Integer> followMethod = new MutableLiveData<>();
  public MutableLiveData<FollowRecordStatus> followStatus = new MutableLiveData<>();
  public MutableLiveData<List<User>> notiOthers = new MutableLiveData<>();
  public MutableLiveData<List<String>> userIds = new MutableLiveData<>();
  public MutableLiveData<String> nextFollowTime = new MutableLiveData<>();
  List<String> followRecordUrl = new ArrayList<>();

  MutableLiveData<List<FollowRecordStatus>> followRecordStatus = new MutableLiveData<>();

  public final MutableLiveData<Boolean> showLoading = new MutableLiveData<>();

  public LiveData<List<Staff>> getSalerStaffs() {
    return salerStaffs;
  }

  private MutableLiveData<List<Staff>> salerStaffs = new MutableLiveData<>();

  @Inject FollowRecordEditViewModel() {
  }

  @Inject StudentRepository studentRepository;
  @Inject StudentWrap studentWrap;

  public void loadFollowRecordStatus() {
    studentRepository.qcGetTrackStatus(followRecordStatus, defaultResult);
  }

  public void loadSellers() {
    studentRepository.qcGetSalers(salerStaffs, defaultResult);
  }

  public void addFollowRecord() {
    List<Attach> attaches = new ArrayList<>();
    for (String s : followRecordUrl) {
      attaches.add(new Attach(s));
    }
    String value = nextFollowTime.getValue();
    if ("不设定时间".equals(value)) value = null;
    studentRepository.qcAddTrackRecord(studentWrap.getStudentBean().id,
        new FollowRecordAdd.Builder().content(content.getValue())
            .next_track_time(value)
            .notice_users(userIds.getValue())
            .track_type_id(followMethod.getValue() == null ? null : (followMethod.getValue() + ""))
            .track_status_id(
                followStatus.getValue() == null ? null : followStatus.getValue().getId())
            .attachments(followRecordUrl)
            .build(), defaultResult);
  }
}
