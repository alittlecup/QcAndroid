package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.respository.StudentRepository;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
  public final MutableLiveData<Boolean> removeResult = new MutableLiveData<>();
  public ObservableField<List<AbstractFlexibleItem>> items = new ObservableField<>();

  public void setStudentItems(List<? extends StudentItem> items) {
    this.items.set(new ArrayList<>(items));
  }

  @Inject StudentRepository studentRepository;

  @Inject public StudentListViewModel() {

  }

  void removeStaffStudents(@StudentListView.AllotType String type, Map<String, Object> params) {
    String path = "";
    switch (type) {
      case StudentListView.SELLER_TYPE:
        path = "seller";
        break;
      case StudentListView.TRAINER_TYPE:
        path = "trainer";
        break;
    }
    studentRepository.qcRemoveStaff(removeResult,defaultResult,path, params);
  }
}
