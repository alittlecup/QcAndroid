package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import com.amap.api.maps2d.MapView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class StudentAllViewModel
    extends FlexibleViewModel<List<QcStudentBeanWithFollow>, ChooseDetailItem, Map<String, Object>> {

  @Inject StudentRepository repository;

  public final MutableLiveData<Boolean> showLoading=new MutableLiveData<>();
  @Inject public StudentAllViewModel() {
  }

  @NonNull @Override
  protected LiveData<List<QcStudentBeanWithFollow>> getSource(@NonNull Map<String, Object> params) {
    params.put("show_all", 1);
    return Transformations.map(repository.qcGetAllStudents(params), input -> {
      StudentListWrapper studentListWrapper = dealResource(input);
      showLoading.setValue(false);
      if (studentListWrapper != null) {
        return studentListWrapper.users;
      } else {
        return null;
      }
    });
  }

  @Override public void loadSource(@NonNull Map<String, Object> stringObjectMap) {
    showLoading.setValue(true);
    this.identifier.setValue(stringObjectMap);
  }

  @Override protected boolean isSourceValid(@Nullable List<QcStudentBeanWithFollow> qcStudentBeans) {
    return qcStudentBeans != null && !qcStudentBeans.isEmpty();
  }

  @Override protected List<ChooseDetailItem> map(@NonNull List<QcStudentBeanWithFollow> qcStudentBeans) {
    return FlexibleItemProvider.with(new StudentItemFactory()).from(qcStudentBeans);
  }

  static class StudentItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, ChooseDetailItem> {

    @NonNull @Override public ChooseDetailItem create(QcStudentBeanWithFollow qcStudentBean) {
      return FlexibleFactory.create(ChooseDetailItem.class, qcStudentBean,-1);
    }
  }
}
