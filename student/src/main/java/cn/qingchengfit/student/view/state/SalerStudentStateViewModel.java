package cn.qingchengfit.student.view.state;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.InactiveBean;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.view.followup.IncreaseStudentViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class SalerStudentStateViewModel
    extends FlexibleViewModel<List<QcStudentBeanWithFollow>, ChooseDetailItem, Integer> {
  public final MutableLiveData<String> filterContent = new MutableLiveData<>();
  public final MutableLiveData<Boolean> filterVisible = new MutableLiveData<>();
  @Inject StudentRepository studentRepository;

  private final MutableLiveData<List<QcStudentBeanWithFollow>> studentBeans =
      new MutableLiveData<>();
  public int type = -1;

  @Inject public SalerStudentStateViewModel() {

  }
  public void onQcButtonFilterClick(boolean isChecked, int index) {
    if (isChecked) {
      filterVisible.setValue(true);
    } else {
      filterVisible.setValue(false);
    }
  }

  @NonNull @Override
  protected LiveData<List<QcStudentBeanWithFollow>> getSource(@NonNull Integer params) {
    studentRepository.qcGetSellerInactiveUsers(studentBeans, defaultResult, type, params);
    return studentBeans;
  }

  @Override protected boolean isSourceValid(
      @Nullable List<QcStudentBeanWithFollow> qcStudentBeanWithFollows) {
    return qcStudentBeanWithFollows != null;
  }

  @Override protected List<ChooseDetailItem> map(
      @NonNull List<QcStudentBeanWithFollow> qcStudentBeanWithFollows) {
    return FlexibleItemProvider.with(new FollowUpItemFactory(type)).from(qcStudentBeanWithFollows);
  }

  static class FollowUpItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, ChooseDetailItem> {
    private Integer type;

    public FollowUpItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public ChooseDetailItem create(QcStudentBeanWithFollow beanWithFollow) {
      return FlexibleFactory.create(ChooseDetailItem.class, beanWithFollow, type);
    }
  }

  public void setCurAttack(InactiveBean inactiveBean) {
    filterContent.setValue(inactiveBean.getPeriod() + "未跟进");
    identifier.setValue(inactiveBean.getId());
  }
}
