package cn.qingchengfit.student.view.state;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.flexble.CommonItemFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.student.bean.MemberStat;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.item.SalerStudentInfoItem;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class StudentStateInfoViewModel extends
    FlexibleViewModel<List<QcStudentBeanWithFollow>, SalerStudentInfoItem, Map<String, Object>> {
  private final MutableLiveData<String> curType = new MutableLiveData<>();
  public final LiveData<MemberStat> statInfo;
  @Inject StudentRepository studentRepository;

  @Inject public StudentStateInfoViewModel() {
    statInfo = Transformations.switchMap(curType,
        type -> Transformations.map(loadStatInfo(type), input -> dealResource(input)));
  }

  @NonNull @Override protected LiveData<List<QcStudentBeanWithFollow>> getSource(
      @NonNull Map<String, Object> stringObjectMap) {

    return Transformations.map(
        studentRepository.qcGetMemberSeller(curType.getValue(), stringObjectMap),
        input -> dealResource(input));
  }

  @Override protected boolean isSourceValid(
      @Nullable List<QcStudentBeanWithFollow> qcStudentBeanWithFollows) {
    return qcStudentBeanWithFollows == null;
  }

  @Override protected List<SalerStudentInfoItem> map(
      @NonNull List<QcStudentBeanWithFollow> qcStudentBeanWithFollows) {
    List<SalerStudentInfoItem> from = FlexibleItemProvider.with(
        new CommonItemFactory<QcStudentBeanWithFollow, SalerStudentInfoItem>(
            SalerStudentInfoItem.class)).from(qcStudentBeanWithFollows);
    Collections.sort(from, (o1, o2) -> o1.getData().total_count-o2.getData().total_count);
    return from;
  }

  private LiveData<Resource<MemberStat>> loadStatInfo(String type) {
    return studentRepository.qcGetMemberStat(type);
  }

  public void setCurType(@IncreaseType String type) {
    curType.setValue(type);
  }
}
