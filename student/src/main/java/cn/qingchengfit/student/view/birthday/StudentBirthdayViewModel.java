package cn.qingchengfit.student.view.birthday;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.flexble.CommonItemFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.QcStudentBirthdayWrapper;
import cn.qingchengfit.student.bean.QcStudentWithUsers;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class StudentBirthdayViewModel
    extends FlexibleViewModel<QcStudentBirthdayWrapper, ChooseDetailItem, Map<String, Object>> {

  @Inject StudentRepository studentRepository;

  @Inject public StudentBirthdayViewModel() {

  }

  @NonNull @Override protected LiveData<QcStudentBirthdayWrapper> getSource(
      @NonNull Map<String, Object> params) {

    return Transformations.map(studentRepository.qcGetStudentBirthday(params), this::dealResource);
  }

  @Override
  protected boolean isSourceValid(@Nullable QcStudentBirthdayWrapper qcStudentBirthdayWrapper) {
    return qcStudentBirthdayWrapper != null;
  }

  @Override
  protected List<ChooseDetailItem> map(@NonNull QcStudentBirthdayWrapper qcStudentBirthdayWrapper) {
    CommonItemFactory<QcStudentBeanWithFollow, ChooseDetailItem> itemFactory =
        new CommonItemFactory<>(ChooseDetailItem.class);
    List<ChooseDetailItem> items = new ArrayList<>();
    for (QcStudentWithUsers users : qcStudentBirthdayWrapper.getBirthday()) {
      List<ChooseDetailItem> from = FlexibleItemProvider.with(itemFactory).from(users.getUsers());
      items.addAll(from);
    }
    return items;
  }
}
