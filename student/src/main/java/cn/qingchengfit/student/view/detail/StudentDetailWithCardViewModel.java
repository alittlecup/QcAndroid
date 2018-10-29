package cn.qingchengfit.student.view.detail;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.StudentViewModel;
import cn.qingchengfit.student.respository.StudentRepository;
import javax.inject.Inject;

public class StudentDetailWithCardViewModel extends BaseViewModel {

  public MutableLiveData<QcStudentBean> student = new MutableLiveData<>();

  @Inject StudentDetailWithCardViewModel() {
  }

}
