package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;
import javax.inject.Inject;

public class StudentRecyclerSortViewModel extends BaseViewModel {
  public MutableLiveData<List<? extends AbstractFlexibleItem>> getStudentBeans() {
    return studentBeans;
  }

  private MutableLiveData<List<? extends AbstractFlexibleItem>> studentBeans=new MutableLiveData<>();

  @Inject public StudentRecyclerSortViewModel() {

  }
}
