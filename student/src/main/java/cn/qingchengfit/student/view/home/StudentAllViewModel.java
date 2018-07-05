package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class StudentAllViewModel
    extends FlexibleViewModel<List<QcStudentBean>, StudentItem, Map<String, ? extends Object>> {


  @Inject GymWrapper gymWrapper;
  @Inject StudentRepository repository;
  @Inject LoginStatus loginStatus;

  @Inject public StudentAllViewModel() {
  }

  @NonNull @Override
  protected LiveData<List<QcStudentBean>> getSource(@NonNull
      Map<String, ? extends Object> stringObjectMap) {
    HashMap<String, Object> params = gymWrapper.getParams();
    if (!stringObjectMap.isEmpty()) {
      params.putAll(stringObjectMap);
    }

    //return Transformations.map(repository.qcGetAllStudents(loginStatus.staff_id(), params),
    //    input -> input.users);
    return null;
  }

  @Override public void loadSource(@NonNull Map<String, ? extends Object> stringObjectMap) {
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
