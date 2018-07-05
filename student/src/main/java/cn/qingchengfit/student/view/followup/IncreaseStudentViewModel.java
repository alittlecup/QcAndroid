package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.item.FollowUpItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class IncreaseStudentViewModel
    extends FlexibleViewModel<StudentListWrappeForFollow, FollowUpItem, Map<String, ?>> {

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StudentRepository repository;

  public Integer dataType = -1;


  @Inject IncreaseStudentViewModel() {

  }

  @NonNull @Override
  protected LiveData<StudentListWrappeForFollow> getSource(@NonNull Map<String, ?> map) {

    HashMap<String, Object> params = gymWrapper.getParams();
    if (!map.isEmpty()) {
      params.putAll(map);
    }

    String type = "";
    switch (dataType) {
      case 0:
        type = "create";
        break;
      case 1:
        type = "member";
        break;
    }
    //return repository.qcGetTrackStudents(loginStatus.staff_id(), type, params);
    return null;
  }

  @Override
  protected boolean isSourceValid(@Nullable StudentListWrappeForFollow studentListWrappeForFollow) {
    return studentListWrappeForFollow != null && studentListWrappeForFollow.users != null;
  }

  @Override
  protected List<FollowUpItem> map(@NonNull StudentListWrappeForFollow studentListWrappeForFollow) {
    return FlexibleItemProvider.with(new FollowUpItemFactory(dataType))
        .from(studentListWrappeForFollow.users);
  }

  static class FollowUpItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, FollowUpItem> {
    private Integer type;

    public FollowUpItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public FollowUpItem create(QcStudentBeanWithFollow beanWithFollow) {
      return FlexibleFactory.create(FollowUpItem.class, beanWithFollow, type);
    }
  }
}
