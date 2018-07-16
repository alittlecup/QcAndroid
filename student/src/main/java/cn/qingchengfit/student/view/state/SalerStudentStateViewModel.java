package cn.qingchengfit.student.view.state;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.item.ChooseDetailItem;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class SalerStudentStateViewModel extends FlexibleViewModel<List<QcStudentBeanWithFollow>,ChooseDetailItem,Map<String,Object>> {

  @Inject public SalerStudentStateViewModel() {

  }

  @NonNull @Override protected LiveData<List<QcStudentBeanWithFollow>> getSource(
      @NonNull Map<String, Object> stringObjectMap) {
    return null;
  }

  @Override protected boolean isSourceValid(
      @Nullable List<QcStudentBeanWithFollow> qcStudentBeanWithFollows) {
    return false;
  }

  @Override protected List<ChooseDetailItem> map(
      @NonNull List<QcStudentBeanWithFollow> qcStudentBeanWithFollows) {
    return null;
  }
}
