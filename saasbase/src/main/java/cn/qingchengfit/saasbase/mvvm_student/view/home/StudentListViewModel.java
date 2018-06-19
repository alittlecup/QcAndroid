package cn.qingchengfit.saasbase.mvvm_student.view.home;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.saasbase.mvvm_student.items.ChooseStaffItem;
import java.util.List;
import java.util.Map;

public class StudentListViewModel extends FlexibleViewModel<List<QcStudentBean>, ChooseStaffItem, Map<String,?>> {
  @NonNull @Override
  protected LiveData<List<QcStudentBean>> getSource(@NonNull Map<String, ?> stringMap) {
    return null;
  }

  @Override protected boolean isSourceValid(@Nullable List<QcStudentBean> qcStudentBeans) {
    return false;
  }

  @Override protected List<ChooseStaffItem> map(@NonNull List<QcStudentBean> qcStudentBeans) {
    return null;
  }
}
