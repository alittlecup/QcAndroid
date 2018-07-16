package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.item.FollowUpItem;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.utils.Hash;
import java.io.PipedReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class IncreaseStudentViewModel
    extends FlexibleViewModel<StudentListWrappeForFollow, ChooseDetailItem, Map<String, ?>> {

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StudentRepository repository;

  private final MutableLiveData<HashMap<String,Object>> params=new MutableLiveData<>();
  public Integer dataType = -1;//0- 新注册，2-会员

  @Inject IncreaseStudentViewModel() {
    params.setValue(new HashMap<>());
    identifier.addSource(params,params->{
      identifier.setValue(params);
    });
  }

  @NonNull @Override
  protected LiveData<StudentListWrappeForFollow> getSource(@NonNull Map<String, ?> map) {

    HashMap<String, Object> params = gymWrapper.getParams();
    if (!map.isEmpty()) {
      params.putAll(map);
    }
    params.put("show_all", 1);

    String type = "";
    switch (dataType) {
      case 0:
        type = "create";
        break;
      case 2:
        type = "member";
        break;
    }
    return Transformations.map(repository.qcGetTrackStudents(loginStatus.staff_id(), type, params),
        this::dealResource);
  }

  @Override
  protected boolean isSourceValid(@Nullable StudentListWrappeForFollow studentListWrappeForFollow) {
    return studentListWrappeForFollow != null && studentListWrappeForFollow.users != null;
  }

  @Override
  protected List<ChooseDetailItem> map(@NonNull StudentListWrappeForFollow studentListWrappeForFollow) {
    return FlexibleItemProvider.with(new FollowUpItemFactory(dataType))
        .from(studentListWrappeForFollow.users);
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

  public void loadSources(@NonNull HashMap<String,Object> map) {
    HashMap<String, Object> value = params.getValue();
    value.putAll(map);
    params.setValue(value);
  }
}
