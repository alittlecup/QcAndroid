package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class StudentAllViewModel
    extends FlexibleViewModel<List<QcStudentBean>, StudentItem, Map<String, Object>> {

  @Inject StudentRepository repository;

  @Inject public StudentAllViewModel() {
  }

  @NonNull @Override
  protected LiveData<List<QcStudentBean>> getSource(@NonNull Map<String, Object> params) {
    params.put("show_all", 1);
    return Transformations.map(repository.qcGetAllStudents(params), input -> {
      StudentListWrapper studentListWrapper = dealResource(input);
      if (studentListWrapper != null) {
        return studentListWrapper.users;
      } else {
        return null;
      }
    });
  }

  @Override public void loadSource(@NonNull Map<String, Object> stringObjectMap) {
    this.identifier.setValue(stringObjectMap);
  }

  @Override protected boolean isSourceValid(@Nullable List<QcStudentBean> qcStudentBeans) {
    return qcStudentBeans != null && !qcStudentBeans.isEmpty();
  }

  @Override protected List<StudentItem> map(@NonNull List<QcStudentBean> qcStudentBeans) {
    return FlexibleItemProvider.with(new StudentItemFactory()).from(qcStudentBeans);
  }

  static class StudentItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBean, StudentItem> {

    @NonNull @Override public StudentItem create(QcStudentBean qcStudentBean) {
      return FlexibleFactory.create(StudentItem.class, qcStudentBean);
    }
  }
}
