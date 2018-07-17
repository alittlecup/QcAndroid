package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.item.ChooseDetailItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class IncreaseMemberViewModel
    extends FlexibleViewModel<StudentListWrappeForFollow, ChooseDetailItem, Map<String, Object>> {

  @Inject StudentRepository repository;

  public Integer dataType = 1;//1- 已接洽，2- 会员

  private Map<String, Object> dates = new HashMap<>();

  @Inject IncreaseMemberViewModel() {

  }

  @NonNull @Override
  protected LiveData<StudentListWrappeForFollow> getSource(@NonNull Map<String, Object> map) {
    map.put("show_all", 1);

    return Transformations.map(repository.qcGetTrackStudentFollow(map), input -> {
      StudentListWrappeForFollow studentListWrappeForFollow = dealResource(input);
      if (studentListWrappeForFollow != null) {
        return studentListWrappeForFollow;
      } else {
        return null;
      }
    });
  }

  @Override
  protected boolean isSourceValid(@Nullable StudentListWrappeForFollow studentListWrappeForFollow) {
    return studentListWrappeForFollow != null && studentListWrappeForFollow.users != null;
  }

  @Override protected List<ChooseDetailItem> map(
      @NonNull StudentListWrappeForFollow studentListWrappeForFollow) {
    return FlexibleItemProvider.with(new FollowUpItemFactory(1))
        .from(studentListWrappeForFollow.users);
  }

  static class FollowUpItemFactory
      implements FlexibleItemProvider.Factory<QcStudentBeanWithFollow, ChooseDetailItem> {
    private Integer type;

    public FollowUpItemFactory(Integer type) {
      this.type = type;
    }

    @NonNull @Override public ChooseDetailItem create(QcStudentBeanWithFollow beanWithFollow) {
      return FlexibleFactory.create(ChooseDetailItem.class, beanWithFollow, 1);
    }
  }

  @Override public void loadSource(@NonNull Map<String, Object> stringObjectMap) {
   stringObjectMap.putAll(dates);
    identifier.setValue(new HashMap<>(stringObjectMap));
  }
  public void loadSourceByDate(Map<String,Object> map){
    dates.putAll(map);
    Map<String, Object> value = identifier.getValue();
    value.putAll(dates);
    identifier.setValue(value);
  }


}
