package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.text.TextUtils;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.HashMap;
import javax.inject.Inject;

public class IncreaseStudentSortViewModel extends BaseViewModel {

  public final ObservableBoolean appBarLayoutExpanded = new ObservableBoolean(true);
  public final MutableLiveData<Integer> filterIndex = new MutableLiveData<>();
  public final MutableLiveData<Boolean> filterVisible = new MutableLiveData<>();
  public final ActionLiveEvent filterAction = new ActionLiveEvent();

  public final MutableLiveData<String> salerName = new MutableLiveData<>();
  public final MutableLiveData<String> studentStatus = new MutableLiveData<>();
  public final MutableLiveData<String> gender = new MutableLiveData<>();

  public final MutableLiveData<HashMap<String, Object>> params = new MutableLiveData<>();

  public void setSaller(Staff staff) {
    HashMap<String, Object> value = params.getValue();
    if (staff==null) {
      value.remove("seller_id");
    } else {
      value.put("seller_id", staff.getId());
    }
    params.setValue(value);
  }

  public void setStudentStatus(String status) {
    HashMap<String, Object> value = params.getValue();
    if (TextUtils.isEmpty(status)) {
      value.remove("status");
    } else {
      value.put("status", status);
    }
    params.setValue(value);
  }

  public void setStudentGender(String gender) {
    HashMap<String, Object> value = params.getValue();
    if (TextUtils.isEmpty(gender)) {
      value.remove("gender");
    } else {
      value.put("gender", gender);
    }
    params.setValue(value);
  }

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

  @Inject public IncreaseStudentSortViewModel() {
    salerName.setValue("全部销售");
    studentStatus.setValue("会员状态");
    gender.setValue("性别");
    params.setValue(new HashMap<>());
  }
}
