package cn.qingchengfit.saasbase.mvvm_student.view.followup;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import javax.inject.Inject;

public class IncreaseMemberSortViewModel extends BaseViewModel {

  public final ObservableBoolean appBarLayoutExpanded = new ObservableBoolean(true);
  public final MutableLiveData<Integer> filterIndex = new MutableLiveData<>();
  public final MutableLiveData<Boolean> filterVisible = new MutableLiveData<>();
  public final ActionLiveEvent filterAction = new ActionLiveEvent();

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


  public void onRightFilterClick() {
    filterAction.call();
  }

  @Inject public IncreaseMemberSortViewModel() {
    salerName.setValue("销售");
    followUpStatus.setValue("跟进状态");
    filterVisible.setValue(false);
  }
}
