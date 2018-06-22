package cn.qingchengfit.saasbase.mvvm_student.view.home;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.common.mvvm.BaseViewModel;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class StudentListViewModel extends BaseViewModel {
  public ObservableInt mBottomSelectedCount = new ObservableInt(0);

  public MutableLiveData<List<QcStudentBean>> getSelectedDatas() {
    return selectedDatas;
  }

  public MutableLiveData<Integer> getRemoveSelectPos() {
    return removeSelectPos;
  }

  private MutableLiveData<Integer> removeSelectPos = new MutableLiveData<>();

  private MutableLiveData<List<QcStudentBean>> selectedDatas = new MutableLiveData<>();

  public ObservableField<List<StudentItem>> items = new ObservableField<>();

  public void setStudentItems(List<? extends StudentItem> items) {
    this.items.set(new ArrayList<>(items));
  }

  @Inject public StudentListViewModel() {

  }
}
