package cn.qingchengfit.student.view.state;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.flexble.CommonItemFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.bean.InactiveStat;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.item.SalerStudentInfoItem;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class StudentStateInfoViewModel extends BaseViewModel {
  public final MutableLiveData<InactiveStat> inactiveStat = new MutableLiveData<>();
  public final MediatorLiveData<String> totalCount = new MediatorLiveData<>();

  @Inject StudentRepository studentRepository;

  @Inject public StudentStateInfoViewModel() {
    totalCount.addSource(inactiveStat, inactiveStat1 -> {
      totalCount.setValue(StringUtils.formatePrice(String.valueOf(inactiveStat1.getUsers_count())));
    });
  }

  public void loadSource(int status) {
    studentRepository.qcGetInactiveStat(inactiveStat, defaultResult, status);
  }
}
