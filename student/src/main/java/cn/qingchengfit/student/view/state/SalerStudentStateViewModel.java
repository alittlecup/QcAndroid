package cn.qingchengfit.student.view.state;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.MemberStat;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class SalerStudentStateViewModel extends
    FlexibleViewModel<List<QcStudentBeanWithFollow>, ChooseDetailItem, Map<String, Object>> {
  public final MutableLiveData<String> filterContent = new MutableLiveData<>();
  public final MutableLiveData<Boolean> filterVisible = new MutableLiveData<>();
  @Inject StudentRepository studentRepository;

  @Inject public SalerStudentStateViewModel() {

  }

  @NonNull @Override protected LiveData<List<QcStudentBeanWithFollow>> getSource(
      @NonNull Map<String, Object> stringObjectMap) {

    return null;
  }

  @Override protected boolean isSourceValid(
      @Nullable List<QcStudentBeanWithFollow> qcStudentBeanWithFollows) {
    return qcStudentBeanWithFollows != null;
  }

  @Override protected List<ChooseDetailItem> map(
      @NonNull List<QcStudentBeanWithFollow> qcStudentBeanWithFollows) {
    return null;
  }

  public void setCurAttack(MemberStat.UnAttacked attack) {
    filterContent.setValue(attack.getDesc() + "未跟进");
    Map<String, Object> params = new HashMap<>();
    params.put("time_period_id", attack.getId());
    identifier.setValue(params);
  }
}
