package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.item.ChooseDetailItem;
import io.reactivex.Flowable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

public class IncreaseMemberSortViewModel extends BaseViewModel {

  public final ObservableBoolean appBarLayoutExpanded = new ObservableBoolean(true);
  public final MutableLiveData<Integer> filterIndex = new MutableLiveData<>();
  public final MutableLiveData<Boolean> filterVisible = new MutableLiveData<>();
  public final MutableLiveData<Boolean> filterAction = new MutableLiveData<>();

  public final MutableLiveData<String> salerName = new MutableLiveData<>();
  public final MutableLiveData<String> followUpStatus = new MutableLiveData<>();

  public void onQcButtonFilterClick(boolean isChecked, int index) {
    if (isChecked) {
      appBarLayoutExpanded.set(false);
      filterIndex.setValue(index);
      filterVisible.setValue(true);
    } else {
      filterVisible.setValue(false);
    }
  }

  public final MutableLiveData<HashMap<String, Object>> params = new MutableLiveData<>();

  public void setSaller(Staff staff) {
    HashMap<String, Object> value = params.getValue();
    value.put("seller_id", staff == null ? null : staff.getId());
    params.setValue(value);
  }

  public void setStudentStatus(String status) {
    HashMap<String, Object> value = params.getValue();
    value.put("status_ids", status);
    params.setValue(value);
  }

  public List<ChooseDetailItem> sortFollowTime(List<ChooseDetailItem> items, boolean isUp) {
    if (isUp) {
      Collections.sort(items, (r1, r2) -> r2.getData().track_at.compareTo(r1.getData().track_at));
    } else {
      Collections.sort(items, (r1, r2) -> r1.getData().track_at.compareTo(r2.getData().track_at));
    }
    return items;
  }

  public void onRightFilterClick(boolean isChecked) {
    filterAction.setValue(isChecked);
  }

  @Inject public IncreaseMemberSortViewModel() {
    salerName.setValue("销售");
    followUpStatus.setValue("跟进状态");
    filterVisible.setValue(false);
    params.setValue(new HashMap<>());
  }
}
